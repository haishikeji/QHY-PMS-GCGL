package com.px.controller.commoditymanagement;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.bean.dto.CmMaterialInventoryImport;
import com.px.bpm.service.BusinessBpmService;
import com.px.commoditymanagement.bean.dto.CmMaterialInventoryQueryDto;
import com.px.commoditymanagement.bean.dto.CmMaterialInventoryUpdateDto;
import com.px.commoditymanagement.bean.vo.CmMaterialInventoryVo;
import com.px.commoditymanagement.entity.CmMaterialInventory;
import com.px.commoditymanagement.entity.CmMaterialInventoryDetail;
import com.px.commoditymanagement.querycondition.CmMaterialInventoryQueryCondition;
import com.px.commoditymanagement.service.CmMaterialInventoryDetailService;
import com.px.commoditymanagement.service.CmMaterialInventoryService;
import com.px.commoditymanagement.service.CmWarehouseService;
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
import com.px.common.util.security.SecurityUtils;
import com.px.resourcecenter.entity.RcMaterial;
import com.px.resourcecenter.service.RcMaterialService;
import com.px.system.business.CodeBusiness;
import com.px.system.service.SysCategoryService;
import com.px.system.service.SysDepartmentService;
import com.px.system.service.SysFileService;
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
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 盘点管理管理
 *
 * @author 品讯科技
 * @since 2023-12-28
 */
@RestController
@RequestMapping("/cm/materialInventory")
public class CmMaterialInventoryController {

    @Autowired
    private CmMaterialInventoryService cmMaterialInventoryService;
    @Autowired
    private CmMaterialInventoryDetailService materialInventoryDetailService;
    @Autowired
    private SysFileService fileService;
    @Autowired
    private CodeBusiness codeBusiness;
    @Autowired
    private BusinessBpmService businessBpmService;
    @Autowired
    private CmWarehouseService warehouseService;
    @Autowired
    private RcMaterialService materialService;
    @Autowired
    private SysCategoryService categoryService;
    @Autowired
    private SysDepartmentService departmentService;
    @Autowired
    private SysOperationLogService operationLogService;

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    @GetMapping("/getPage")
    public Result<Page<CmMaterialInventoryVo>> getPage(CmMaterialInventoryQueryDto dto) {
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        Page<CmMaterialInventoryVo> voPage = cmMaterialInventoryService.getPage(dto);
        businessBpmService.setTaskIds(voPage.getRecords());
        return new Result(voPage);
    }

    /***
     * 新增/修改(id为空:新增，不为空:修改)
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/addOrUpdate")
    public Result addOrUpdate(@RequestBody CmMaterialInventoryUpdateDto dto, HttpServletRequest request) {
        dto.setFlowStatus(null);
        CmMaterialInventory cmMaterialInventory = new CmMaterialInventory();
        BeanUtils.copyProperties(dto, cmMaterialInventory, "createBy","createTime","updateBy","updateTime","flowStatus","flowCode");
        if (cmMaterialInventory.getId() == null) {
            cmMaterialInventory.setInventoryCode(codeBusiness.genCode(SysCodeConst.KCPD));
            cmMaterialInventory.setCreateBy(SecurityUtils.getUserCodeAndName());
            cmMaterialInventory.setCreateTime(new Date());
            cmMaterialInventoryService.save(cmMaterialInventory);
        } else {
            cmMaterialInventory.setUpdateBy(SecurityUtils.getUserCodeAndName());
            cmMaterialInventory.setUpdateTime(new Date());
            cmMaterialInventoryService.updateById(cmMaterialInventory);
        }
        materialInventoryDetailService.remove(new LambdaQueryWrapper<CmMaterialInventoryDetail>()
                .eq(CmMaterialInventoryDetail::getInventoryCode, cmMaterialInventory.getInventoryCode()));
        List<CmMaterialInventoryDetail> details = dto.getDetails();
        if (CollUtils.isNotEmpty(details)) {
            details.forEach(x -> x.setInventoryCode(cmMaterialInventory.getInventoryCode()));
            materialInventoryDetailService.saveBatch(details);
        }
        fileService.updateFileDataDtoList(cmMaterialInventory.getInventoryCode(), SysFileConst.CM_MATERIAL_INVENTORY_PAGE_FILE, dto.getFileDataDtos());
        operationLogService.saveOperatorLog(IpUtil.getIpAddr(request),
                dto.getId(), "盘点管理",
                JSON.toJSONString(dto));
        return new Result(cmMaterialInventory.getId());
    }

    /***
     * 获取详情
     * @param id
     * @return
     */
    @GetMapping("/getInfo")
    public Result<CmMaterialInventoryUpdateDto> getInfo(Long id) {
        CmMaterialInventoryUpdateDto dto = null;
        CmMaterialInventory cmMaterialRecord = cmMaterialInventoryService.getById(id);
        if (cmMaterialRecord != null) {
            dto = new CmMaterialInventoryUpdateDto();
            BeanUtils.copyProperties(cmMaterialRecord, dto);
            dto.setDetails(materialInventoryDetailService.list(new LambdaQueryWrapper<CmMaterialInventoryDetail>()
                    .eq(CmMaterialInventoryDetail::getInventoryCode, cmMaterialRecord.getInventoryCode())));
            dto.setFileDataDtos(fileService.getFileDataDtoList(cmMaterialRecord.getInventoryCode(), SysFileConst.CM_MATERIAL_INVENTORY_PAGE_FILE));
            businessBpmService.setTaskIds(Arrays.asList(dto));
            dto.setWarehouseName(warehouseService.getNameByCode(dto.getWarehouseCode()));
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
        CmMaterialInventory cmMaterialInventory = cmMaterialInventoryService.getById(id);
        if (cmMaterialInventory == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        cmMaterialInventory.setIsDel(1);
        cmMaterialInventoryService.updateById(cmMaterialInventory);
        operationLogService.saveDelOperatorLog(IpUtil.getIpAddr(request),
                "盘点管理" + "“" + cmMaterialInventory.getInventoryCode() + "”",
                JSON.toJSONString(id));
        return Result.ok();
    }

    /**
     * 提交审核
     *
     * @param id
     * @return
     */
    @PostMapping("/submitAudit")
    public Result submitAudit(Long id, String processDefId) {
        CmMaterialInventory record = cmMaterialInventoryService.getById(id);
        if (record == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        if (record.getFlowStatus() != null && Arrays.asList(2, 4).contains(record.getFlowStatus())) {
            return Result.fail(ResultCode.AUDIT_STATUS_CHANGED);
        }
        String processInstanceId = businessBpmService.startProcessV2(FlowBusinessKeyConst.CM_MATERIAL_INVENTORY_KEY, record.getDepCode(), record.getInventoryCode(), record.getId(),processDefId,record.getProjectCode(),record.getProjectName());
        if (StringUtils.isBlank(processInstanceId)) {
            return Result.fail(ResultCode.BUSINESS_NOT_RELEVANCE);
        }
        record.setFlowCode(processInstanceId);
        record.setFlowStatus(FlowAuditStatusConst.AUDITING);
        cmMaterialInventoryService.updateById(record);
        return Result.ok();
    }

    @Bean
    public FlowBusinessExecuteService getCmMaterialInventoryFlowBusinessExecuteService() {
        return new FlowBusinessExecuteService(FlowBusinessKeyConst.CM_MATERIAL_INVENTORY_KEY) {
            @Override
            @Async
            public void execute(String instanceId, Integer status) {
                cmMaterialInventoryService.updateByFlowCode(instanceId, status);
            }
        };
    }

    /**
     * 导出
     *
     * @param dto
     */
    @GetMapping("/export")
    public void export(CmMaterialInventoryQueryDto dto, HttpServletResponse response) {
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        List<CmMaterialInventoryVo> voList = cmMaterialInventoryService.buildCmMaterialInventoryVoList(cmMaterialInventoryService.list(CmMaterialInventoryQueryCondition.build(dto)));
        CommonExportVo<CmMaterialInventoryVo> exportVo = new CommonExportVo<>(voList);
        String templatePath = "/templates/export/盘点管理.xls";
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
    public Result<List<CmMaterialInventoryImport>> detailImport(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        List<CmMaterialInventoryImport> dataList = EasyExcel.read(inputStream)
                .head(CmMaterialInventoryImport.class)
                .sheet(0)
                .headRowNumber(1)
                .doReadSync();
        if (CollUtils.isNotEmpty(dataList)) {
            List<RcMaterial> materialList = materialService.list(new LambdaQueryWrapper<RcMaterial>()
                    .eq(RcMaterial::getIsDel, 0)
                    .in(RcMaterial::getMaterialCode, CollUtils.getDefaultStrVal(CollUtils.toList(dataList, x -> x.getMaterialCode()))));
            Map<String, String> materialListMap = CollUtils.toMap(materialList, x -> x.getMaterialCode(), y -> y.getCategoryCode());
            Map<String, String> nameByCodes = categoryService.getNameByCodes(CollUtils.getDefaultStrVal(CollUtils.toList(materialList, x -> x.getCategoryCode())));
            for (CmMaterialInventoryImport inventoryImport : dataList) {
                inventoryImport.setCategoryCode(materialListMap.get(inventoryImport.getMaterialCode()));
                inventoryImport.setCategoryName(nameByCodes.get(inventoryImport.getCategoryCode()));
            }
        }
        return new Result(dataList);
    }


}