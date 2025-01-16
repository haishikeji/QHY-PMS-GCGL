package com.px.controller.humanresource;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.bean.dto.EsEngineeringPlanDetailImport;
import com.px.bpm.service.BusinessBpmService;
import com.px.commoditymanagement.bean.dto.CmWarehouseInterspaceQueryDto;
import com.px.commoditymanagement.bean.vo.CmWarehouseInterspaceVo;
import com.px.commoditymanagement.service.CmWarehouseInterspaceService;
import com.px.common.bean.vo.CommonExportVo;
import com.px.common.consts.FlowAuditStatusConst;
import com.px.common.consts.FlowBusinessKeyConst;
import com.px.common.consts.SysCodeConst;
import com.px.common.consts.SysFileConst;
import com.px.common.result.Result;
import com.px.common.result.ResultCode;
import com.px.common.service.FlowBusinessExecuteService;
import com.px.common.util.CollUtils;
import com.px.common.util.ExcelUtil;
import com.px.common.util.IpUtil;
import com.px.common.util.MoneyUtil;
import com.px.common.util.security.SecurityUtils;
import com.px.humanresource.bean.dto.HrAdministrativePlanQueryDto;
import com.px.humanresource.bean.dto.HrAdministrativePlanUpdateDto;
import com.px.humanresource.bean.vo.HrAdministrativePlanDetailVo;
import com.px.humanresource.bean.vo.HrAdministrativePlanVo;
import com.px.humanresource.entity.HrAdministrativeDeliveryDetail;
import com.px.humanresource.entity.HrAdministrativePlan;
import com.px.humanresource.entity.HrAdministrativePlanDetail;
import com.px.humanresource.mapper.HrAdministrativeDeliveryDetailMapper;
import com.px.humanresource.querycondition.HrAdministrativePlanQueryCondition;
import com.px.humanresource.service.HrAdministrativePlanDetailService;
import com.px.humanresource.service.HrAdministrativePlanService;
import com.px.purchasingmanagement.service.SysChangeRecordService;
import com.px.resourcecenter.entity.RcMaterial;
import com.px.resourcecenter.service.RcMaterialService;
import com.px.service.others.ChangeRecordAuditService;
import com.px.system.business.CodeBusiness;
import com.px.system.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;

/**
 * 行政需用计划管理
 *
 * @author 品讯科技
 * @since 2023-12-12
 */
@RestController
@RequestMapping("/hr/administrativePlan")
public class HrAdministrativePlanController {

    @Autowired
    private HrAdministrativePlanService hrAdministrativePlanService;
    @Autowired
    private HrAdministrativePlanDetailService planDetailService;
    @Autowired
    private SysFileService fileService;
    @Autowired
    private BusinessBpmService businessBpmService;
    @Autowired
    private CodeBusiness codeBusiness;
    @Autowired
    private SysChangeRecordService changeRecordService;
    @Autowired
    private ChangeRecordAuditService changeRecordAuditService;
    @Autowired
    private CmWarehouseInterspaceService warehouseInterspaceService;
    @Autowired
    private SysDepartmentService departmentService;
    @Autowired
    private HrAdministrativeDeliveryDetailMapper administrativeDeliveryDetailMapper;
    @Autowired
    private RcMaterialService materialService;
    @Autowired
    private SysCategoryService categoryService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysOperationLogService operationLogService;

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    @GetMapping("/getPage")
    public Result<Page<HrAdministrativePlanVo>> getPage(HrAdministrativePlanQueryDto dto) {
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        Page<HrAdministrativePlanVo> voPage = hrAdministrativePlanService.getPage(dto);
        businessBpmService.setTaskIds(voPage.getRecords());
        return new Result(voPage);
    }

    /**
     * 无分页查询
     *
     * @param dto
     * @return
     */
    @GetMapping("/getList")
    public Result<List<HrAdministrativePlan>> getList(HrAdministrativePlanQueryDto dto) {
        dto.setFlowStatus(4);
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        List<HrAdministrativePlan> list = hrAdministrativePlanService.list(HrAdministrativePlanQueryCondition.build(dto));
        return new Result(list);
    }

    /**
     * 分页查询明细列表
     *
     * @param pageNum  页数
     * @param pageSize 每页大小
     * @param planCode 行政需用计划编号
     * @return
     */
    @GetMapping("/getDetailPage")
    public Result<Page<HrAdministrativePlanDetailVo>> getDetailPage(Integer pageNum, Integer pageSize, String planCode) {
        Page<HrAdministrativePlanDetail> voPage = planDetailService.page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<HrAdministrativePlanDetail>().eq(HrAdministrativePlanDetail::getPlanCode, planCode));
        Page<HrAdministrativePlanDetailVo> planDetailVoPage = new Page<>();
        BeanUtils.copyProperties(voPage, planDetailVoPage);
        List<HrAdministrativePlanDetail> records = voPage.getRecords();
        ArrayList<HrAdministrativePlanDetailVo> voArrayList = new ArrayList<>();
        HashMap<String, Map<String, Double>> dataMap = new HashMap<>();
        if (CollUtils.isNotEmpty(records)) {
            List<String> getPlanCodeList = CollUtils.toList(records, x -> x.getPlanCode());
            List<HrAdministrativeDeliveryDetail> deliveryDetails = administrativeDeliveryDetailMapper.queryDataList(CollUtils.getDefaultStrVal(getPlanCodeList));
            Map<String, List<HrAdministrativeDeliveryDetail>> listMap = CollUtils.groupByKey(deliveryDetails, x -> x.getPlanCode());
            for (Map.Entry<String, List<HrAdministrativeDeliveryDetail>> entry : listMap.entrySet()) {
                String key = entry.getKey();
                List<HrAdministrativeDeliveryDetail> value = entry.getValue();
                if (CollUtils.isNotEmpty(value)) {
                    HashMap<String, Double> numMap = new HashMap<>();
                    Map<String, List<HrAdministrativeDeliveryDetail>> stringListMap = CollUtils.groupByKey(value, x -> x.getMaterialCode());
                    for (Map.Entry<String, List<HrAdministrativeDeliveryDetail>> listEntry : stringListMap.entrySet()) {
                        String key1 = listEntry.getKey();
                        List<HrAdministrativeDeliveryDetail> value1 = listEntry.getValue();
                        Double num = 0.;
                        if (CollUtils.isNotEmpty(value1)) {
                            num = value1.stream().map(x -> x.getUsedNum()).reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                        }
                        numMap.put(key1, num);
                    }
                    dataMap.put(key, numMap);
                }
            }
            List<String> getMaterialCodeList = CollUtils.toList(records, x -> x.getMaterialCode());
            Map<String, Double> warehouseInterspaceMap = warehouseInterspaceService.countHrWareHouseNumByMaterialCodes(CollUtils.getDefaultStrVal(getMaterialCodeList));
            List<RcMaterial> rcMaterialList = materialService.list(new LambdaQueryWrapper<RcMaterial>()
                    .in(RcMaterial::getMaterialCode, CollUtils.getDefaultStrVal(getMaterialCodeList))
                    .eq(RcMaterial::getIsDel, 0));
            Map<String, String> getMaterialCodeMap = CollUtils.toMap(rcMaterialList, x -> x.getMaterialCode(), y -> y.getCategoryCode());
            Map<String, String> nameByCodes = categoryService.getNameByCodes(CollUtils.getDefaultStrVal(CollUtils.toList(rcMaterialList, x -> x.getCategoryCode())));
            for (HrAdministrativePlanDetail record : records) {
                HrAdministrativePlanDetailVo vo = new HrAdministrativePlanDetailVo();
                BeanUtils.copyProperties(record, vo);
                Map<String, Double> integerMap = dataMap.get(record.getPlanCode());
                Double deliveryNum = 0.;
                if (CollUtils.isNotEmpty(integerMap)) {
                    deliveryNum = integerMap.get(record.getMaterialCode());
                }
                vo.setCategoryCode(getMaterialCodeMap.get(vo.getMaterialCode()));
                vo.setCategoryName(nameByCodes.get(vo.getCategoryCode()));
                vo.setDeliveryNum(deliveryNum);
                Double nums = warehouseInterspaceMap.get(record.getMaterialCode());
                vo.setWarehouseNum(nums == null ? 0. : nums);
                voArrayList.add(vo);
            }
        }
        planDetailVoPage.setRecords(voArrayList);
        return new Result(planDetailVoPage);
    }

    /**
     * 从需用计划获取仓库数据
     *
     * @return
     */
//    @GetMapping("/selectByWarehouse")
//    public Result<List<WarehouseDataVo>> selectByWarehouse(HrAdministrativePlanQueryDto dto) {
//        List<WarehouseDataVo> dataVos = null;
//        //dto.setFlowStatus(4);
//        Page<HrAdministrativePlan> planPage = hrAdministrativePlanService.page(new Page<>(dto.getPageNum(), dto.getPageSize()), HrAdministrativePlanQueryCondition.build(dto));
//        List<HrAdministrativePlan> list = planPage.getRecords();
//        if (CollUtils.isNotEmpty(list)) {
//            dataVos = new ArrayList<>();
//            List<String> codeList = CollUtils.toList(list, x -> x.getPlanCode());
//            List<HrAdministrativePlanDetail> planDetailList = planDetailService.list(new LambdaQueryWrapper<HrAdministrativePlanDetail>()
//                    .in(HrAdministrativePlanDetail::getPlanCode, codeList));
//            Map<String, List<HrAdministrativePlanDetail>> dataListMap = CollUtils.groupByKey(planDetailList, x -> x.getPlanCode());
//            for (HrAdministrativePlan hrAdministrativePlan : list) {
//                WarehouseDataVo warehouseDataVo = new WarehouseDataVo();
//                warehouseDataVo.setBusinessCode(hrAdministrativePlan.getPlanCode());
//                warehouseDataVo.setVoList(warehouseInterspaceService.getDataByMaterialCodes(CollUtils.toList(dataListMap.get(hrAdministrativePlan.getPlanCode()),
//                        x -> x.getMaterialCode()), 1, null));
//                dataVos.add(warehouseDataVo);
//            }
//        }
//        return new Result<>(dataVos);
//    }

    /**
     * 从需用计划获取仓库数据
     *
     * @param pageNum     页数
     * @param pageSize    每页大小
     * @param depCode     部门编号
     * @param projectCode 项目编号
     * @return
     */
    @GetMapping("/selectByWarehouse")
    public Result<Page<CmWarehouseInterspaceVo>> selectByWarehouse(Integer pageNum, Integer pageSize,
                                                                   String depCode,
                                                                   String planCode,
                                                                   String projectCode) {
        CmWarehouseInterspaceQueryDto dto = new CmWarehouseInterspaceQueryDto();
        dto.setPageNum(pageNum);
        dto.setPageSize(pageSize);
        List<HrAdministrativePlanDetail> planDetails = planDetailService.list(new LambdaQueryWrapper<HrAdministrativePlanDetail>()
                .eq(HrAdministrativePlanDetail::getPlanCode, planCode));
        List<String> codeList = CollUtils.toList(planDetails, x -> x.getMaterialCode());
        if (!CollUtils.isNotEmpty(codeList)) {
            codeList = new ArrayList<>();
            codeList.add("-1");
        }
        dto.setMaterialCodeList(codeList);
        dto.setProjectCode(projectCode);
        Page<CmWarehouseInterspaceVo> voPage = warehouseInterspaceService.getPage(dto);
        return new Result<>(voPage);
    }

    /***
     * 新增/修改(id为空:新增，不为空:修改)
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/addOrUpdate")
    public Result addOrUpdate(@RequestBody HrAdministrativePlanUpdateDto dto, HttpServletRequest request) {
        String lastJson = JSON.toJSONStringWithDateFormat(this.getInfo(dto.getId()), "yyyy-MM-dd");
        HrAdministrativePlan hrAdministrativePlan = new HrAdministrativePlan();
        BeanUtils.copyProperties(dto, hrAdministrativePlan, "createBy", "createTime", "updateBy", "updateTime", "flowStatus", "flowCode");
//        hrAdministrativePlan.setFlowStatus(null);
        if (hrAdministrativePlan.getId() == null) {
            hrAdministrativePlan.setPlanCode(codeBusiness.genCode(SysCodeConst.XZXYJH));
            hrAdministrativePlan.setCreateBy(SecurityUtils.getUserCodeAndName());
            hrAdministrativePlan.setCreateTime(new Date());
            hrAdministrativePlan.setFlowStatus(FlowAuditStatusConst.CREATED);
            hrAdministrativePlanService.save(hrAdministrativePlan);
        } else {
            hrAdministrativePlan.setUpdateBy(SecurityUtils.getUserCodeAndName());
            hrAdministrativePlan.setUpdateTime(new Date());
//            if (dto.getChangeFlag() == null || dto.getChangeFlag() == 0) {
                hrAdministrativePlanService.updateById(hrAdministrativePlan);
//            }
        }
//        if (dto.getChangeFlag() == null || dto.getChangeFlag() == 0) {
            List<HrAdministrativePlanDetail> planDetails = dto.getPlanDetails();
            planDetailService.remove(new LambdaQueryWrapper<HrAdministrativePlanDetail>()
                    .eq(HrAdministrativePlanDetail::getPlanCode, dto.getPlanCode()));
            if (!CollectionUtils.isEmpty(planDetails)) {
                planDetails.forEach(x -> x.setPlanCode(hrAdministrativePlan.getPlanCode()));
                planDetailService.saveBatch(planDetails);
            }
            fileService.updateFileDataDtoList(hrAdministrativePlan.getPlanCode(), SysFileConst.ADMINISTRATIVE_PLAN_PAGE_FILE, dto.getFileDataDtos());
            operationLogService.saveOperatorLog(IpUtil.getIpAddr(request),
                    dto.getId(), "行政领用申请",
                    JSON.toJSONString(dto));
//        }
        if (dto.getChangeFlag() != null && dto.getChangeFlag() == 1 && dto.getFlowStatus() == 4) {
            Long id = changeRecordService.saveChangeRecord(hrAdministrativePlan.getDepCode(), 1, hrAdministrativePlan.getPlanCode(), hrAdministrativePlan.getRemark(), lastJson, JSON.toJSONStringWithDateFormat(new Result<>(dto), "yyyy-MM-dd"), null, null);
//            changeRecordAuditService.submit(id);
        }
        return new Result(hrAdministrativePlan.getId());
    }

    /***
     * 获取详情
     * @param id
     * @return
     */
    @GetMapping("/getInfo")
    public Result<HrAdministrativePlanUpdateDto> getInfo(Long id) {
        HrAdministrativePlanUpdateDto dto = null;
        HrAdministrativePlan hrAdministrativePlan = hrAdministrativePlanService.getById(id);
        if (hrAdministrativePlan != null) {
            dto = new HrAdministrativePlanUpdateDto();
            BeanUtils.copyProperties(hrAdministrativePlan, dto);
            List<HrAdministrativePlanDetail> planDetailList = planDetailService.list(new LambdaQueryWrapper<HrAdministrativePlanDetail>()
                    .eq(HrAdministrativePlanDetail::getPlanCode, dto.getPlanCode()));
            if (CollUtils.isNotEmpty(planDetailList)) {
                List<RcMaterial> rcMaterialList = materialService.list(new LambdaQueryWrapper<RcMaterial>()
                        .in(RcMaterial::getMaterialCode, CollUtils.getDefaultStrVal(CollUtils.toList(planDetailList, x -> x.getMaterialCode())))
                        .eq(RcMaterial::getIsDel, 0));
                Map<String, String> getMaterialCodeMap = CollUtils.toMap(rcMaterialList, x -> x.getMaterialCode(), y -> y.getCategoryCode());
                Map<String, String> nameByCodes = categoryService.getNameByCodes(CollUtils.getDefaultStrVal(CollUtils.toList(rcMaterialList, x -> x.getCategoryCode())));
                Map<String, Double> wareHouseNumByMaterialCodes = warehouseInterspaceService.countHrWareHouseNumByMaterialCodes(CollUtils.getDefaultStrVal(CollUtils.toList(planDetailList, x -> x.getMaterialCode())));
                for (HrAdministrativePlanDetail planDetail : planDetailList) {
                    planDetail.setCategoryCode(getMaterialCodeMap.get(planDetail.getMaterialCode()));
                    planDetail.setCategoryName(nameByCodes.get(planDetail.getCategoryCode()));
                    planDetail.setWarehouseNum(wareHouseNumByMaterialCodes.get(planDetail.getMaterialCode()));
                }
            }
            dto.setPlanDetails(planDetailList);
            dto.setFileDataDtos(fileService.getFileDataDtoList(dto.getPlanCode(), SysFileConst.ADMINISTRATIVE_PLAN_PAGE_FILE));
            businessBpmService.setTaskIds(Arrays.asList(dto));
            Double num = 0.;
            if (CollUtils.isNotEmpty(dto.getPlanDetails())) {
                num = dto.getPlanDetails().stream().map(x -> x.getNum())
                        .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
            }
            dto.setApplyUserName(userService.getNameByCode(dto.getApplyUserCode()));
            dto.setDetailsNum(num);
            dto.setDepName(departmentService.getNameByCode(dto.getDepCode()));
        }
        return new Result(dto);
    }

    /**
     * 提交审核
     *
     * @param id
     * @return
     */
    @PostMapping("/submitAudit")
    public Result submitAudit(Long id, String processDefId) {
        HrAdministrativePlan record = hrAdministrativePlanService.getById(id);
        if (record == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        if (record.getFlowStatus() != null && Arrays.asList(2, 4).contains(record.getFlowStatus())) {
            return Result.fail(ResultCode.AUDIT_STATUS_CHANGED);
        }
        String processInstanceId = businessBpmService.startProcessV2(FlowBusinessKeyConst.ADMINISTRATIVE_PLAN_KEY, record.getDepCode(), record.getPlanCode(), record.getName(), record.getId(), processDefId, null, null);
        if (StringUtils.isBlank(processInstanceId)) {
            return Result.fail(ResultCode.BUSINESS_NOT_RELEVANCE);
        }
        record.setFlowCode(processInstanceId);
        record.setFlowStatus(FlowAuditStatusConst.AUDITING);
        hrAdministrativePlanService.updateById(record);
        return Result.ok();
    }

    @Bean
    public FlowBusinessExecuteService getHrAdministrativePlanFlowBusinessExecuteService() {
        return new FlowBusinessExecuteService(FlowBusinessKeyConst.ADMINISTRATIVE_PLAN_KEY) {
            @Override
            @Async
            public void execute(String instanceId, Integer status) {
                hrAdministrativePlanService.updateByFlowCode(instanceId, status);
            }
        };
    }

    /**
     * 导出
     *
     * @param dto
     */
    @GetMapping("/export")
    public void export(HrAdministrativePlanQueryDto dto, HttpServletResponse response) {
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        List<HrAdministrativePlanVo> voList = hrAdministrativePlanService.buildHrAdministrativePlanVoList(hrAdministrativePlanService.list(HrAdministrativePlanQueryCondition.build(dto)));
        CommonExportVo<HrAdministrativePlanVo> exportVo = new CommonExportVo<>(voList);
        String templatePath = "/templates/export/行政需用计划.xls";
        InputStream inputStream = this.getClass().getResourceAsStream(templatePath);
        ExcelUtil.exportResponse(exportVo, inputStream, response);
    }

    /**
     * 详情页-导入
     *
     * @param file
     * @return
     */
    @PostMapping("/detailImport")
    public Result<List<EsEngineeringPlanDetailImport>> detailImport(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        List<EsEngineeringPlanDetailImport> dataList = EasyExcel.read(inputStream)
                .head(EsEngineeringPlanDetailImport.class)
                .sheet(0)
                .headRowNumber(1)
                .doReadSync();
        return new Result(dataList);
    }


}