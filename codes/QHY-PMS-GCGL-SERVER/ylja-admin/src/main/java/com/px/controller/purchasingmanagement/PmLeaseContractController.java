package com.px.controller.purchasingmanagement;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.bpm.bean.dto.StartProcessDto;
import com.px.bpm.service.BusinessBpmService;
import com.px.common.bean.vo.CommonExportVo;
import com.px.common.consts.FlowAuditStatusConst;
import com.px.common.consts.FlowBusinessKeyConst;
import com.px.common.consts.SysFileConst;
import com.px.common.result.Result;
import com.px.common.result.ResultCode;
import com.px.common.service.FlowBusinessExecuteService;
import com.px.common.util.CollUtils;
import com.px.common.util.ExcelUtil;
import com.px.common.util.IpUtil;
import com.px.common.util.MoneyUtil;
import com.px.common.util.security.SecurityUtils;
import com.px.engineeringsupervision.entity.EsConstructionBudgetDetail;
import com.px.engineeringsupervision.service.EsConstructionBudgetDetailService;
import com.px.engineeringsupervision.service.EsConstructionBudgetService;
import com.px.purchasingmanagement.bean.dto.*;
import com.px.purchasingmanagement.bean.vo.PmLeaseContractVo;
import com.px.purchasingmanagement.entity.PmLeaseContract;
import com.px.purchasingmanagement.entity.PmPurchaseSettle;
import com.px.purchasingmanagement.querycondition.PmLeaseContractQueryCondition;
import com.px.purchasingmanagement.service.PmLeaseContractService;
import com.px.purchasingmanagement.service.PmPurchaseSettleService;
import com.px.purchasingmanagement.service.SysChangeRecordService;
import com.px.resourcecenter.service.RcProjectService;
import com.px.resourcecenter.service.RcSupplierService;
import com.px.service.others.ChangeRecordAuditService;
import com.px.system.service.SysDepartmentService;
import com.px.system.service.SysFileService;
import com.px.system.service.SysOperationLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;

/**
 * 租赁合同管理
 *
 * @author 品讯科技
 * @since 2023-12-19
 */
@RestController
@RequestMapping("/pm/leaseContract")
public class PmLeaseContractController {

    @Autowired
    private PmLeaseContractService pmLeaseContractService;
    @Autowired
    private SysFileService fileService;
    @Autowired
    private PmPurchaseSettleService purchaseSettleService;
    @Autowired
    private SysChangeRecordService changeRecordService;
    @Autowired
    private ChangeRecordAuditService changeRecordAuditService;
    @Autowired
    private BusinessBpmService businessBpmService;
    @Autowired
    private EsConstructionBudgetService constructionBudgetService;
    @Autowired
    private RcSupplierService supplierService;
    @Autowired
    private EsConstructionBudgetDetailService constructionBudgetDetailService;
    @Autowired
    private SysDepartmentService departmentService;
    @Autowired
    private SysOperationLogService operationLogService;
    @Autowired
    private RcProjectService projectService;

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    @GetMapping("/getPage")
    public Result<Page<PmLeaseContractVo>> getPage(PmLeaseContractQueryDto dto) {
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        Page<PmLeaseContractVo> voPage = pmLeaseContractService.getPage(dto);
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
    public Result<List<PmLeaseContractVo>> getList(PmLeaseContractQueryDto dto) {
        dto.setFlowStatus(4);
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        return new Result(pmLeaseContractService.buildPmLeaseContractVoList(pmLeaseContractService.list(PmLeaseContractQueryCondition.build(dto))));
    }

    /***
     * 新增/修改(id为空:新增，不为空:修改)
     * @param dto
     * @return
     */
//    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/addOrUpdate")
    public Result addOrUpdate(@RequestBody PmLeaseContractUpdateDto dto, HttpServletRequest request) {

        String lastJson = JSON.toJSONStringWithDateFormat(this.getInfo(dto.getId()), "yyyy-MM-dd");
        LambdaQueryWrapper<PmLeaseContract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmLeaseContract::getIsDel, 0)
                .eq(PmLeaseContract::getLeaseContractCode, dto.getLeaseContractCode());
        if (dto.getId() != null) {
            queryWrapper.ne(PmLeaseContract::getId, dto.getId());
        }
        if (pmLeaseContractService.count(queryWrapper) > 0) {
            return Result.fail(ResultCode.CODE_IS_REPETITION);
        }
        PmLeaseContract pmLeaseContract = new PmLeaseContract();
        BeanUtils.copyProperties(dto, pmLeaseContract, "createBy", "createTime", "updateBy", "updateTime", "flowStatus", "flowCode", "originalMoney");
//        pmLeaseContract.setFlowStatus(null);
        EsConstructionBudgetDetail constructionBudgetDetail = constructionBudgetDetailService.getByProjectCodeAndCostProject(dto.getProjectCode(), "机械费");
        pmLeaseContract.setConstructionBudgetDetailId(constructionBudgetDetail != null ? constructionBudgetDetail.getId() : null);
        if (pmLeaseContract.getId() == null) {
            pmLeaseContract.setCreateBy(SecurityUtils.getUserCodeAndName());
            pmLeaseContract.setCreateTime(new Date());
            pmLeaseContract.setOriginalMoney(pmLeaseContract.getContractMoneyBeforeTaxes());
            pmLeaseContractService.save(pmLeaseContract);
        } else {
            pmLeaseContract.setUpdateBy(SecurityUtils.getUserCodeAndName());
            pmLeaseContract.setUpdateTime(new Date());
            if (dto.getChangeFlag() == null || dto.getChangeFlag() == 0) {
                pmLeaseContract.setOriginalMoney(pmLeaseContract.getContractMoneyBeforeTaxes());
                pmLeaseContractService.updateById(pmLeaseContract);
            }
        }
        if (dto.getChangeFlag() == null || dto.getChangeFlag() == 0) {
            fileService.updateFileDataDtoList(pmLeaseContract.getLeaseContractCode(), SysFileConst.PM_LEASE_CONTRACT_PAGE_FILE, dto.getFileDataDtos());
            operationLogService.saveOperatorLog(IpUtil.getIpAddr(request),
                    dto.getId(), "租赁合同",
                    JSON.toJSONString(dto));
        }
        if (dto.getChangeFlag() != null && dto.getChangeFlag() == 1 && dto.getFlowStatus() == 4) {
            Integer num = changeRecordService.checkRepetition(dto.getLeaseContractCode());
            if (num > 0) {
                return Result.fail(ResultCode.AUDIT_STATUS_REPETITION);
            }
            SaveChangeRecordDto changeRecordDto = new SaveChangeRecordDto();
            changeRecordDto.setDepCode(dto.getDepCode());
            changeRecordDto.setType(9);
            changeRecordDto.setCode(dto.getLeaseContractCode());
            changeRecordDto.setRemark(dto.getRemark());
            changeRecordDto.setLastJson(lastJson);
            changeRecordDto.setNewJson(JSON.toJSONStringWithDateFormat(new Result<>(dto), "yyyy-MM-dd"));
            changeRecordDto.setProjectCode(dto.getProjectCode());
            changeRecordDto.setProjectName(dto.getProjectName());
            changeRecordDto.setChangeRecordId(dto.getChangeRecordId());
            Long id = changeRecordService.saveChangeRecord(changeRecordDto);
//            Long id = changeRecordService.saveChangeRecord(pmLeaseContract.getDepCode(), 9, pmLeaseContract.getLeaseContractCode(), pmLeaseContract.getRemark(), lastJson, JSON.toJSONStringWithDateFormat(new Result<>(dto), "yyyy-MM-dd"), dto.getProjectCode(), dto.getProjectName());
            if (dto.getCrSubmitAudit() != null && dto.getCrSubmitAudit() == 1) {
                changeRecordAuditService.submit(id, dto.getContractMoneyBeforeTaxes());
            }
            return new Result(id);
        }
        return new Result(pmLeaseContract.getId());
    }

    /***
     * 获取详情
     * @param id
     * @return
     */
    @GetMapping("/getInfo")
    public Result<PmLeaseContractUpdateDto> getInfo(Long id) {
        PmLeaseContractUpdateDto dto = null;
        PmLeaseContract pmLeaseContract = pmLeaseContractService.getById(id);
        if (pmLeaseContract != null) {
            dto = new PmLeaseContractUpdateDto();
            BeanUtils.copyProperties(pmLeaseContract, dto);
            dto.setProcurement(dto.getCreateBy());
            dto.setFileDataDtos(fileService.getFileDataDtoList(pmLeaseContract.getLeaseContractCode(), SysFileConst.PM_LEASE_CONTRACT_PAGE_FILE));
            businessBpmService.setTaskIds(Arrays.asList(dto));
            dto.setSupplierName(supplierService.getNameByCode(dto.getSupplierCode()));
            dto.setDepName(departmentService.getNameByCode(dto.getDepCode()));
            dto.setCustomerName(projectService.getCustomerNameByProjectCode(dto.getProjectCode()));
        }
        return new Result(dto);
    }

    /**
     * 删除
     *
     * @param id 数据id
     * @return
     */
    @PostMapping("/delete")
    public Result delete(Long id, HttpServletRequest request) {
        PmLeaseContract record = pmLeaseContractService.getById(id);
        if (record == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        Integer flowStatus = record.getFlowStatus();
        if (Arrays.asList(2, 4).contains(flowStatus)) {
            return Result.fail(3004, "当前审批状态下无法删除！");
        }
        record.setIsDel(1);
        pmLeaseContractService.updateById(record);
        operationLogService.saveDelOperatorLog(IpUtil.getIpAddr(request),
                "租赁合同" + "“" + record.getLeaseContractCode() + "”",
                JSON.toJSONString(id));
        return Result.ok();
    }

    /**
     * 结算申请
     *
     * @param id
     * @return
     */
    @PostMapping("/settleApply")
    public Result<PmPurchaseSettleUpdateDto> settleApply(Long id) {
        PmPurchaseSettleUpdateDto purchaseSettleUpdateDto = null;
        PmLeaseContract pmPurchaseContract = pmLeaseContractService.getById(id);
        if (pmPurchaseContract != null) {
            purchaseSettleUpdateDto = new PmPurchaseSettleUpdateDto();
            PmPurchaseSettle pmPurchaseSettle = purchaseSettleService.buildPmPurchaseSettle(pmPurchaseContract);
            BeanUtils.copyProperties(pmPurchaseSettle, purchaseSettleUpdateDto);
        }
        purchaseSettleUpdateDto.setDepName(departmentService.getNameByCode(purchaseSettleUpdateDto.getDepCode()));
        return new Result<>(purchaseSettleUpdateDto);
    }

    /**
     * 单个结算
     *
     * @param dto
     * @return
     */
    @PostMapping("/settleOne")
    public Result<PmPurchaseSettleUpdateDto> settleOne(@RequestBody SettleOneDto dto) {
        PmPurchaseSettleUpdateDto purchaseSettleUpdateDto = null;
        PmLeaseContract pmPurchaseContract = pmLeaseContractService.getById(dto.getId());
        if (pmPurchaseContract != null) {
            purchaseSettleUpdateDto = new PmPurchaseSettleUpdateDto();
            PmPurchaseSettle pmPurchaseSettle = purchaseSettleService.buildPmPurchaseSettle(pmPurchaseContract);
            BeanUtils.copyProperties(pmPurchaseSettle, purchaseSettleUpdateDto);
            purchaseSettleUpdateDto.setSupplierName(supplierService.getNameByCode(pmPurchaseContract.getSupplierCode()));
        }
        return new Result<>(purchaseSettleUpdateDto);
    }

    /**
     * 批量结算
     *
     * @param idList
     * @return
     */
    @PostMapping("/settleBatch")
    public Result<PmBatchSettleUpdateDto> settleBatch(@RequestBody List<Long> idList) {
        PmBatchSettleUpdateDto batchSettleUpdateDto = new PmBatchSettleUpdateDto();
        batchSettleUpdateDto.setBatchSettleCode("");
        batchSettleUpdateDto.setDataType(3);
        batchSettleUpdateDto.setCreateTime(new Date());
        batchSettleUpdateDto.setCreateBy(SecurityUtils.getUserCodeAndName());
        batchSettleUpdateDto.setUpdateTime(new Date());
        batchSettleUpdateDto.setUpdateBy(SecurityUtils.getUserCodeAndName());
        batchSettleUpdateDto.setIsDel(0);
        List<PmLeaseContract> purchaseContracts = pmLeaseContractService.listByIds(idList);
        if (CollUtils.isNotEmpty(purchaseContracts)) {
            Double totalPrice = 0.;
            Double totalReceiveMoney = 0.;
            String depCode = null;
            List<PurchaseSettleDto> purchaseSettleDtos = new ArrayList<>(purchaseContracts.size());
            Map<String, String> nameByCodes = supplierService.getNameByCodes(CollUtils.getDefaultStrVal(CollUtils.toList(purchaseContracts, x -> x.getSupplierCode())));
            Map<String, String> nameMaps = projectService.getCustomerNameByProjectCode(CollUtils.getDefaultStrVal(CollUtils.toList(purchaseContracts, x -> x.getProjectCode())));

            for (PmLeaseContract purchaseContract : purchaseContracts) {
                PmPurchaseSettle pmPurchaseSettle = purchaseSettleService.buildPmPurchaseSettle(purchaseContract);
                totalPrice = MoneyUtil.sum(totalPrice, pmPurchaseSettle.getOrderMoneyBeforeTaxes());
                totalReceiveMoney = MoneyUtil.sum(totalReceiveMoney, pmPurchaseSettle.getCurrentReceiveMoney());
                PurchaseSettleDto purchaseSettleDto = new PurchaseSettleDto();
                BeanUtils.copyProperties(pmPurchaseSettle, purchaseSettleDto);
                purchaseSettleDto.setSupplierName(nameByCodes.get(purchaseContract.getSupplierCode()));
                purchaseSettleDto.setCustomerName(nameMaps.get(purchaseContract.getProjectCode()));
                purchaseSettleDto.setConstructionBudgetDetailId(purchaseContract.getConstructionBudgetDetailId());
                EsConstructionBudgetDetail constructionBudgetDetail = constructionBudgetDetailService.getById(purchaseSettleDto.getConstructionBudgetDetailId());
                purchaseSettleDto.setConstructionBudgetDetailName(constructionBudgetDetail != null ? constructionBudgetDetail.getCostSubject() : null);
                purchaseSettleDto.setTaxesRatio(purchaseContract.getTaxesRatio());
                purchaseSettleDtos.add(purchaseSettleDto);
            }
            batchSettleUpdateDto.setDepCode(depCode);
            batchSettleUpdateDto.setTotalPrice(totalPrice);
            batchSettleUpdateDto.setTotalReceiveMoney(totalReceiveMoney);
            batchSettleUpdateDto.setPurchaseSettleDtos(purchaseSettleDtos);
        }
        return new Result<>(batchSettleUpdateDto);
    }

    /**
     * 提交审核
     *
     * @param id
     * @return
     */
    @PostMapping("/submitAudit")
    public Result submitAudit(Long id, Integer confirmFlag, String processDefId) {
        if (confirmFlag == null) {
            confirmFlag = 0;
        }
        PmLeaseContract record = pmLeaseContractService.getById(id);
        if (record == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        if (record.getFlowStatus() != null && Arrays.asList(2, 4).contains(record.getFlowStatus())) {
            return Result.fail(ResultCode.AUDIT_STATUS_CHANGED);
        }
        List<PmLeaseContract> purchaseOrderList = pmLeaseContractService.list(new LambdaQueryWrapper<PmLeaseContract>()
                .eq(PmLeaseContract::getProjectCode, record.getProjectCode())
                .in(PmLeaseContract::getFlowStatus, 2, 4)
                .eq(PmLeaseContract::getIsDel, 0));
        Double totalOrderMoneyBeforeTaxes = 0.;
        if (CollUtils.isNotEmpty(purchaseOrderList)) {
            totalOrderMoneyBeforeTaxes = purchaseOrderList.stream().map(x -> x.getContractMoneyBeforeTaxes())
                    .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
        }
        int resCode = constructionBudgetService.checkConstructionBudget(record.getConstructionBudgetDetailId(), MoneyUtil.sum(record.getContractMoneyBeforeTaxes(), totalOrderMoneyBeforeTaxes));
        if (resCode == 1 && confirmFlag == 0) {
            return Result.fail(ResultCode.EXCEED_EARLY_WARNING_MONEY.getCode(), constructionBudgetService.getReMindTitle(record.getConstructionBudgetDetailId(), 1));
        }
        if (resCode == 2) {
            return Result.fail(ResultCode.EXCEED_EARLY_HARD_CONTROL_MONEY.getCode(), constructionBudgetService.getReMindTitle(record.getConstructionBudgetDetailId(), 2));
        }
        StartProcessDto startProcessDto = new StartProcessDto();
        startProcessDto.setProjectCode(record.getProjectCode());
        startProcessDto.setProjectName(record.getProjectName());
        startProcessDto.setDeptId(record.getDepCode());
        startProcessDto.setBusinessType(FlowBusinessKeyConst.PM_LEASE_CONTRACT_KEY);
        startProcessDto.setBusinessId(record.getId());
        startProcessDto.setBusinessKey(record.getLeaseContractCode());
        startProcessDto.setProcessDefId(processDefId);
        startProcessDto.setAuditMoney(record.getContractMoneyBeforeTaxes());
        String processInstanceId = businessBpmService.startProcess(startProcessDto);
//        String processInstanceId = businessBpmService.startProcessV2(FlowBusinessKeyConst.PM_LEASE_CONTRACT_KEY, record.getDepCode(), record.getLeaseContractCode(), record.getId(),processDefId,record.getProjectCode(),record.getProjectName());
        if (StringUtils.isBlank(processInstanceId)) {
            return Result.fail(ResultCode.BUSINESS_NOT_RELEVANCE);
        }
        record.setFlowCode(processInstanceId);
        record.setFlowStatus(FlowAuditStatusConst.AUDITING);
        pmLeaseContractService.updateById(record);
        return Result.ok();
    }

    @Bean
    public FlowBusinessExecuteService getPmLeaseContractFlowBusinessExecuteService() {
        return new FlowBusinessExecuteService(FlowBusinessKeyConst.PM_LEASE_CONTRACT_KEY) {
            @Override
            @Async
            public void execute(String instanceId, Integer status) {
                pmLeaseContractService.updateByFlowCode(instanceId, status);
            }
        };
    }

    /**
     * 导出
     *
     * @param dto
     */
    @GetMapping("/export")
    public void export(PmLeaseContractQueryDto dto, HttpServletResponse response) {
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        List<PmLeaseContractVo> voList = pmLeaseContractService.buildPmLeaseContractVoList(pmLeaseContractService.list(PmLeaseContractQueryCondition.build(dto)));
        CommonExportVo<PmLeaseContractVo> exportVo = new CommonExportVo<>(voList);
        String templatePath = "/templates/export/租赁合同.xls";
        InputStream inputStream = this.getClass().getResourceAsStream(templatePath);
        ExcelUtil.exportResponse(exportVo, inputStream, response);
    }

}