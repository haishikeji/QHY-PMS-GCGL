package com.px.controller.commoditymanagement;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.bean.dto.WarehouseInitImport;
import com.px.bpm.service.BusinessBpmService;
import com.px.commoditymanagement.bean.dto.CmWarehouseInitializeQueryDto;
import com.px.commoditymanagement.bean.dto.CmWarehouseQueryDto;
import com.px.commoditymanagement.bean.dto.CmWarehouseUpdateDto;
import com.px.commoditymanagement.bean.dto.InitWarehouseDto;
import com.px.commoditymanagement.bean.vo.CmWarehouseInitializeVo;
import com.px.commoditymanagement.bean.vo.CmWarehouseVo;
import com.px.commoditymanagement.entity.CmWarehouse;
import com.px.commoditymanagement.entity.CmWarehouseInitialize;
import com.px.commoditymanagement.querycondition.CmWarehouseInitializeQueryCondition;
import com.px.commoditymanagement.querycondition.CmWarehouseQueryCondition;
import com.px.commoditymanagement.service.CmWarehouseInitializeService;
import com.px.commoditymanagement.service.CmWarehouseService;
import com.px.common.bean.vo.CommonExportVo;
import com.px.common.consts.FlowAuditStatusConst;
import com.px.common.consts.FlowBusinessKeyConst;
import com.px.common.consts.SysCodeConst;
import com.px.common.log.LogType;
import com.px.common.result.Result;
import com.px.common.result.ResultCode;
import com.px.common.service.FlowBusinessExecuteService;
import com.px.common.util.CollUtils;
import com.px.common.util.ExcelUtil;
import com.px.common.util.IpUtil;
import com.px.common.util.security.SecurityUtils;
import com.px.resourcecenter.entity.RcMaterial;
import com.px.resourcecenter.entity.RcProject;
import com.px.resourcecenter.service.RcMaterialService;
import com.px.resourcecenter.service.RcProjectService;
import com.px.system.business.CodeBusiness;
import com.px.system.entity.SysDepartment;
import com.px.system.service.SysCategoryService;
import com.px.system.service.SysDepartmentService;
import com.px.system.service.SysOperationLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 仓库管理管理
 *
 * @author 品讯科技
 * @since 2023-12-28
 */
@RestController
@RequestMapping("/cm/warehouse")
public class CmWarehouseController {

    @Autowired
    private CmWarehouseService cmWarehouseService;
    @Autowired
    private CodeBusiness codeBusiness;
    @Autowired
    private CmWarehouseInitializeService cmWarehouseInitializeService;
    @Autowired
    private BusinessBpmService businessBpmService;
    @Autowired
    private RcMaterialService materialService;
    @Autowired
    private SysCategoryService categoryService;
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
    public Result<Page<CmWarehouseVo>> getPage(CmWarehouseQueryDto dto) {
//        if (StringUtils.isNotBlank(dto.getDepCode())) {
//            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
//        }
        Page<CmWarehouseVo> voPage = cmWarehouseService.getPage(dto);
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
    public Result<List<CmWarehouseVo>> getList(CmWarehouseQueryDto dto) {
//        if (StringUtils.isNotBlank(dto.getDepCode())) {
//            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
//        }
        return new Result(cmWarehouseService.list(CmWarehouseQueryCondition.build(dto)));
    }

    /***
     * 新增/修改(id为空:新增，不为空:修改)
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/addOrUpdate")
    public Result addOrUpdate(@RequestBody CmWarehouseUpdateDto dto, HttpServletRequest request) {
        dto.setFlowStatus(null);
        CmWarehouse cmWarehouse = new CmWarehouse();
        BeanUtils.copyProperties(dto, cmWarehouse, "createBy","createTime","updateBy","updateTime","flowStatus","flowCode");
        Integer warehouseCategory = dto.getWarehouseCategory();
        String depCode = dto.getDepCode();
        SysDepartment department = departmentService.getByCode(depCode);
        if (warehouseCategory == 1) {//仓库分类：1.行政物资，2.工程物资
            if (!"ZZJG2404180001".equals(depCode)) {
                return Result.fail(10005, "总部部门才可创建行政仓库");
            }
        } else {
            Integer depType = department.getDepType();
            if (depType != 2) {
                return Result.fail(10005, "非基地类型的部门无法创建工程仓库");
            }
            int count = cmWarehouseService.count(new LambdaQueryWrapper<CmWarehouse>().eq(CmWarehouse::getIsDel, 0)
                    .eq(CmWarehouse::getDepCode, depCode));
            if (count > 0) {
                return Result.fail(10005, "当前基地已存在工程仓库");
            }
        }
        if (cmWarehouse.getId() == null) {
            cmWarehouse.setWarehouseCode(codeBusiness.genCode(SysCodeConst.CK));
            cmWarehouse.setCreateBy(SecurityUtils.getUserCodeAndName());
            cmWarehouse.setCreateTime(new Date());
            cmWarehouseService.save(cmWarehouse);
        } else {
            cmWarehouse.setUpdateBy(SecurityUtils.getUserCodeAndName());
            cmWarehouse.setUpdateTime(new Date());
            cmWarehouseService.updateById(cmWarehouse);
        }
        operationLogService.saveOperatorLog(IpUtil.getIpAddr(request),
                dto.getId() == null ? LogType.INSERT : LogType.UPDATE,
                (dto.getId() == null ? "新增" : "更新") + "仓库",
                JSON.toJSONString(dto));
        return new Result(cmWarehouse.getId());
    }

    /***
     * 获取详情
     * @param id
     * @return
     */
    @GetMapping("/getInfo")
    public Result<CmWarehouseVo> getInfo(Long id) {
        CmWarehouseVo vo = null;
        CmWarehouse cmWarehouse = cmWarehouseService.getById(id);
        if (cmWarehouse != null) {
            vo = new CmWarehouseVo();
            List<CmWarehouseVo> cmWarehouseVos = cmWarehouseService.buildCmWarehouseVoList(Arrays.asList(cmWarehouse));
            if (CollUtils.isNotEmpty(cmWarehouseVos)) {
                vo = cmWarehouseVos.get(0);
            }
        }
        businessBpmService.setTaskIds(Arrays.asList(vo));
        return new Result(vo);
    }

    /**
     * 根据项目查询仓库
     *
     * @param projectCode
     * @return
     */
    @GetMapping("/getCmWarehouseByProjectCode")
    public Result<CmWarehouse> getCmWarehouseByProjectCode(String projectCode) {
        RcProject project = projectService.getByCode(projectCode);
        if (project != null) {
            SysDepartment department = departmentService.getByCode(project.getDepCode());
            if (department != null) {
                //组织类型：1.普通部门、2.基地、3.项目部
                Integer depType = department.getDepType();
                String depCode = "-1";
                if (depType == 3) {
                    depCode = department.getParentCode();
                } else if (depType == 2) {
                    depCode = department.getDepCode();
                }
                CmWarehouse warehouse = cmWarehouseService.getOne(new LambdaQueryWrapper<CmWarehouse>()
                        .eq(CmWarehouse::getDepCode, depCode)
                        .eq(CmWarehouse::getIsDel, 0)
                        .orderByDesc(CmWarehouse::getId).last(" limit 1 "));
                return new Result<>(warehouse);
            }
        }
        return new Result<>();
    }

    /**
     * 更新状态
     *
     * @param id     数据id
     * @param status 状态：1.待启用，2.使用中，3.停用
     * @return
     */
    @PostMapping("/updateStatus")
    public Result updateStatus(Long id, Integer status) {
        CmWarehouse cmWarehouse = cmWarehouseService.getById(id);
        if (cmWarehouse == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        cmWarehouse.setStatus(status);
        cmWarehouseService.updateById(cmWarehouse);
        return Result.ok();
    }

    /**
     * 删除
     *
     * @param id 数据id
     * @return
     */
    @PostMapping("/delete")
    public Result delete(Long id, HttpServletRequest request) {
        CmWarehouse cmWarehouse = cmWarehouseService.getById(id);
        if (cmWarehouse == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        cmWarehouse.setIsDel(1);
        cmWarehouseService.updateById(cmWarehouse);
        operationLogService.saveDelOperatorLog(IpUtil.getIpAddr(request),
                "仓库" + "“" + cmWarehouse.getWarehouseName() + "”",
                JSON.toJSONString(id));
        return Result.ok();
    }

    /**
     * 仓库初始化
     *
     * @return
     */
    @PostMapping("/initWarehouse")
    @Transactional(rollbackFor = Exception.class)
    public Result initWarehouse(@RequestBody InitWarehouseDto dto) {
        String warehouseCode = dto.getWarehouseCode();
        List<CmWarehouseInitialize> warehouseInterspaceList = dto.getWarehouseInterspaceList();
        if (CollUtils.isNotEmpty(warehouseInterspaceList)) {
            warehouseInterspaceList.forEach(x -> x.setWarehouseCode(warehouseCode));
            cmWarehouseInitializeService.saveBatch(warehouseInterspaceList);
        }
        cmWarehouseService.saveDataToWarehouse(warehouseInterspaceList);
        CmWarehouse cmWarehouse = cmWarehouseService.getOne(new LambdaQueryWrapper<CmWarehouse>().eq(CmWarehouse::getWarehouseCode, warehouseCode));
        if (cmWarehouse != null) {
            cmWarehouse.setFlowStatus(1);
            cmWarehouse.setStatus(2);
            cmWarehouseService.updateById(cmWarehouse);
        }
        return Result.ok();
    }

    /**
     * 仓库初始化详情
     *
     * @param warehouseCode 仓库编号
     * @return
     */
    @GetMapping("/getInitWarehouseDetail")
    public Result<CmWarehouseInitializeVo> getInitWarehouseDetail(String warehouseCode, Integer pageNum, Integer pageSize) {
        CmWarehouseInitializeVo initializeVo = new CmWarehouseInitializeVo();
        CmWarehouse cmWarehouse = cmWarehouseService.getOne(new LambdaQueryWrapper<CmWarehouse>()
                .eq(CmWarehouse::getWarehouseCode, warehouseCode));
        if (cmWarehouse != null) {
            CmWarehouseVo warehouseVo = new CmWarehouseVo();
            BeanUtils.copyProperties(cmWarehouse, warehouseVo);
            businessBpmService.setTaskIds(Arrays.asList(warehouseVo));
            initializeVo.setWarehouse(warehouseVo);
            initializeVo.setTaskId(warehouseVo.getTaskId());
            initializeVo.setCreateFlag(warehouseVo.getCreateFlag());
            initializeVo.setStartFlag(warehouseVo.getStartFlag());
            CmWarehouseInitializeQueryDto queryDto = new CmWarehouseInitializeQueryDto();
            queryDto.setWarehouseCode(warehouseCode);
//        initializeVo.setWarehouseInterspaceList(cmWarehouseInitializeService.list(new LambdaQueryWrapper<CmWarehouseInitialize>()
//                .eq(CmWarehouseInitialize::getWarehouseCode, warehouseCode)));
            initializeVo.setVoPage(cmWarehouseInitializeService.page(new Page<>(pageNum, pageSize), CmWarehouseInitializeQueryCondition.build(queryDto)));
        }
        return new Result<>(initializeVo);
    }

    /**
     * 提交审核
     *
     * @param id
     * @return
     */
    @PostMapping("/submitAudit")
    public Result submitAudit(Long id, String processDefId) {
        CmWarehouse record = cmWarehouseService.getById(id);
        if (record == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        if (record.getFlowStatus() != null && Arrays.asList(2, 4).contains(record.getFlowStatus())) {
            return Result.fail(ResultCode.AUDIT_STATUS_CHANGED);
        }
        String processInstanceId = businessBpmService.startProcessV2(FlowBusinessKeyConst.CM_WAREHOUSE_INIT_KEY, record.getDepCode(), record.getWarehouseCode(), record.getId(),processDefId,null,null);
        if (StringUtils.isBlank(processInstanceId)) {
            return Result.fail(ResultCode.BUSINESS_NOT_RELEVANCE);
        }
        record.setFlowCode(processInstanceId);
        record.setFlowStatus(FlowAuditStatusConst.AUDITING);
        cmWarehouseService.updateById(record);
        return Result.ok();
    }

    @Bean
    public FlowBusinessExecuteService getCmWarehouseInitFlowBusinessExecuteService() {
        return new FlowBusinessExecuteService(FlowBusinessKeyConst.CM_WAREHOUSE_INIT_KEY) {
            @Override
            @Async
            public void execute(String instanceId, Integer status) {
                cmWarehouseService.updateByFlowCode(instanceId, status);
            }
        };
    }

    /**
     * 导出
     *
     * @param dto
     */
    @GetMapping("/export")
    public void export(CmWarehouseQueryDto dto, HttpServletResponse response) {
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        List<CmWarehouseVo> voList = cmWarehouseService.buildCmWarehouseVoList(cmWarehouseService.list(CmWarehouseQueryCondition.build(dto)));
        CommonExportVo<CmWarehouseVo> exportVo = new CommonExportVo<>(voList);
        String templatePath = "/templates/export/仓库管理.xls";
        InputStream inputStream = this.getClass().getResourceAsStream(templatePath);
        ExcelUtil.exportResponse(exportVo, inputStream, response);
    }

    /**
     * 详情页-导入
     *
     * @param file
     * @param warehouseCode 仓库编号
     * @return
     */
    @PostMapping("/detailImport")
    public Result<List<WarehouseInitImport>> detailImport(MultipartFile file, String warehouseCode) throws IOException {
        InputStream inputStream = file.getInputStream();
        List<WarehouseInitImport> projectImportDtos = EasyExcel.read(inputStream) //调用read方法
                .head(WarehouseInitImport.class)
                .sheet(1)
                .headRowNumber(1)
                .doReadSync();
        if (CollUtils.isNotEmpty(projectImportDtos)) {
            Set<String> codeSet = CollUtils.toSet(projectImportDtos, x -> x.getMaterialCode());
            List<RcMaterial> materialList = materialService.list(new LambdaQueryWrapper<RcMaterial>()
                    .eq(RcMaterial::getIsDel, 0)
                    .in(RcMaterial::getMaterialCode, CollUtils.getDefaultStrVal(new ArrayList<>(codeSet))));
            Map<String, String> materialListMap = CollUtils.toMap(materialList, x -> x.getMaterialCode(), y -> y.getCategoryCode());
            Map<String, String> nameByCodes = categoryService.getNameByCodes(CollUtils.getDefaultStrVal(new ArrayList<>(CollUtils.toSet(materialList, x -> x.getCategoryCode()))));
            for (WarehouseInitImport initImport : projectImportDtos) {
                String code = materialListMap.get(initImport.getMaterialCode());
                initImport.setCategoryCode(code);
                initImport.setCategoryName(nameByCodes.get(code));
                initImport.setWarehouseCode(warehouseCode);
            }
        }
        return new Result<>(projectImportDtos);
    }

}