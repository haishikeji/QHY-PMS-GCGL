package com.px.controller.financialmanagement;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.bean.dto.UploadInvoiceDto;
import com.px.bpm.service.BusinessBpmService;
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
import com.px.engineeringsupervision.entity.EsEngineeringContract;
import com.px.engineeringsupervision.service.EsEngineeringContractService;
import com.px.financialmanagement.bean.dto.FmInvoiceApplyQueryDto;
import com.px.financialmanagement.bean.dto.FmInvoiceApplyUpdateDto;
import com.px.financialmanagement.bean.vo.FmInvoiceApplyVo;
import com.px.financialmanagement.entity.FmInvoiceApply;
import com.px.financialmanagement.querycondition.FmInvoiceApplyQueryCondition;
import com.px.financialmanagement.service.FmInvoiceApplyService;
import com.px.resourcecenter.bean.dto.RcProjectQueryDto;
import com.px.resourcecenter.bean.vo.CommonDoubleVo;
import com.px.resourcecenter.bean.vo.ProjectVo;
import com.px.resourcecenter.entity.RcProject;
import com.px.resourcecenter.querycondition.RcProjectQueryCondition;
import com.px.resourcecenter.service.RcProjectService;
import com.px.system.bean.dto.FileDataDto;
import com.px.system.business.CodeBusiness;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;

/**
 * 开票申请管理
 *
 * @author 品讯科技
 * @since 2024-01-02
 */
@RestController
@RequestMapping("/fm/invoiceApply")
public class FmInvoiceApplyController {

    @Autowired
    private FmInvoiceApplyService fmInvoiceApplyService;
    @Autowired
    private SysFileService fileService;
    @Autowired
    private CodeBusiness codeBusiness;
    @Autowired
    private BusinessBpmService businessBpmService;
    @Autowired
    private RcProjectService projectService;
    @Autowired
    private EsEngineeringContractService esEngineeringContractService;
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
    public Result<Page<FmInvoiceApplyVo>> getPage(FmInvoiceApplyQueryDto dto) {
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        Page<FmInvoiceApplyVo> voPage = fmInvoiceApplyService.getPage(dto);
        businessBpmService.setTaskIds(voPage.getRecords());
        return new Result(voPage);
    }

    @GetMapping("/getList")
    public Result<List<FmInvoiceApplyVo>> getList(FmInvoiceApplyQueryDto dto) {
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        dto.setFlowStatus(4);
        return new Result(fmInvoiceApplyService.list(FmInvoiceApplyQueryCondition.build(dto)));
    }

    /**
     * 获取项目列表
     *
     * @param dto
     * @return
     */
    @GetMapping("/getProjectList")
    public Result<List<ProjectVo>> getProjectList(RcProjectQueryDto dto) {
        List<ProjectVo> voList = new ArrayList<>();
//        dto.setFlowStatus(4);
        List<RcProject> projectList = projectService.list(RcProjectQueryCondition.build(dto));
        if (CollUtils.isNotEmpty(projectList)) {
            List<String> projectCodeList = CollUtils.toList(projectList, x -> x.getProjectCode());
            List<EsEngineeringContract> engineeringContractList = esEngineeringContractService.list(new LambdaQueryWrapper<EsEngineeringContract>()
                    .in(EsEngineeringContract::getProjectCode, projectCodeList).eq(EsEngineeringContract::getIsDel, 0));
            Map<String, EsEngineeringContract> engineeringContractMap = CollUtils.toMap(engineeringContractList, x -> x.getProjectCode(), Function.identity());
            List<CommonDoubleVo> commonDoubleVos = fmInvoiceApplyService.countData(projectCodeList);
            Map<String, Double> dataMap = CollUtils.toMap(commonDoubleVos, x -> x.getCode(), y -> y.getVal());
            for (RcProject rcProject : projectList) {
                ProjectVo vo = new ProjectVo();
                vo.setProjectCode(rcProject.getProjectCode());
                vo.setProjectName(rcProject.getProjectName());
                vo.setAreaAddress(rcProject.getAreaDetail());
                EsEngineeringContract engineeringContract = engineeringContractMap.get(rcProject.getProjectCode());
                if (engineeringContract != null) {
                    vo.setContractCode(engineeringContract.getEngineeringContractCode());
                    vo.setContractMoney(engineeringContract.getContractMoneyBeforeTaxes());
                }
                Double historyInvoiceMoney = 0.;
                if (CollUtils.isNotEmpty(dataMap)) {
                    historyInvoiceMoney = dataMap.get(rcProject.getProjectCode());
                }
                vo.setHistoryInvoiceMoney(historyInvoiceMoney);
                voList.add(vo);
            }
        }
        return new Result(voList);
    }

    /***
     * 新增/修改(id为空:新增，不为空:修改)
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/addOrUpdate")
    public Result addOrUpdate(@RequestBody FmInvoiceApplyUpdateDto dto, HttpServletRequest request) {
        dto.setFlowStatus(null);
        FmInvoiceApply fmInvoiceApply = new FmInvoiceApply();
        BeanUtils.copyProperties(dto, fmInvoiceApply, "createBy","createTime","updateBy","updateTime","flowStatus","flowCode");
        List<FmInvoiceApply> fmInvoiceApplyList = fmInvoiceApplyService.list(new LambdaQueryWrapper<FmInvoiceApply>()
//                .eq(FmInvoiceApply::getFlowStatus, 4)
                .eq(FmInvoiceApply::getIsDel, 0)
                .eq(FmInvoiceApply::getProjectCode, dto.getProjectCode()));
        Double lastInvoiceMoney = 0.;
        if (CollUtils.isNotEmpty(fmInvoiceApplyList)) {
            lastInvoiceMoney = fmInvoiceApplyList.stream().filter(x -> x.getCurrentInvoiceMoney() != null)
                    .map(x -> x.getCurrentInvoiceMoney()).reduce(0., (x, y) ->MoneyUtil.sum(x,y));
        }
        fmInvoiceApply.setHistoryInvoiceMoney(lastInvoiceMoney);
        fmInvoiceApply.setTotalInvoiceMoney(MoneyUtil.sum(lastInvoiceMoney, dto.getCurrentInvoiceMoney()));
        if (fmInvoiceApply.getId() == null) {
            fmInvoiceApply.setInvoiceApplyCode(codeBusiness.genCode(SysCodeConst.KP));
            fmInvoiceApply.setCreateBy(SecurityUtils.getUserCodeAndName());
            fmInvoiceApply.setCreateTime(new Date());
            fmInvoiceApplyService.save(fmInvoiceApply);
        } else {
            fmInvoiceApply.setUpdateBy(SecurityUtils.getUserCodeAndName());
            fmInvoiceApply.setUpdateTime(new Date());
            fmInvoiceApplyService.updateById(fmInvoiceApply);
        }
        fileService.updateFileDataDtoList(fmInvoiceApply.getInvoiceApplyCode(), SysFileConst.FM_INVOICE_APPLY_PAGE_FILE, dto.getFileDataDtos());
        operationLogService.saveOperatorLog(IpUtil.getIpAddr(request),
                dto.getId(), "开票申请",
                JSON.toJSONString(dto));
        return new Result(fmInvoiceApply.getId());
    }

    /***
     * 获取详情
     * @param id
     * @return
     */
    @GetMapping("/getInfo")
    public Result<FmInvoiceApplyUpdateDto> getInfo(Long id) {
        FmInvoiceApplyUpdateDto dto = null;
        FmInvoiceApply fmInvoiceApply = fmInvoiceApplyService.getById(id);
        if (fmInvoiceApply != null) {
            dto = new FmInvoiceApplyUpdateDto();
            BeanUtils.copyProperties(fmInvoiceApply, dto);
            dto.setFileDataDtos(fileService.getFileDataDtoList(fmInvoiceApply.getInvoiceApplyCode(), SysFileConst.FM_INVOICE_APPLY_PAGE_FILE));
            businessBpmService.setTaskIds(Arrays.asList(dto));
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
        FmInvoiceApply fmInvoiceApply = fmInvoiceApplyService.getById(id);
        if (fmInvoiceApply == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        fmInvoiceApply.setIsDel(1);
        fmInvoiceApplyService.updateById(fmInvoiceApply);
        operationLogService.saveDelOperatorLog(IpUtil.getIpAddr(request),
                "开票申请" + "“" + fmInvoiceApply.getInvoiceApplyCode() + "”",
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
        FmInvoiceApply record = fmInvoiceApplyService.getById(id);
        if (record == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        if (record.getFlowStatus() != null && Arrays.asList(2, 4).contains(record.getFlowStatus())) {
            return Result.fail(ResultCode.AUDIT_STATUS_CHANGED);
        }
        String processInstanceId = businessBpmService.startProcessV2(FlowBusinessKeyConst.FM_INVOICE_APPLY_KEY, record.getDepCode(), record.getInvoiceApplyCode(), record.getId(),processDefId,record.getProjectCode(),record.getProjectName());
        if (StringUtils.isBlank(processInstanceId)) {
            return Result.fail(ResultCode.BUSINESS_NOT_RELEVANCE);
        }
        record.setFlowCode(processInstanceId);
        record.setFlowStatus(FlowAuditStatusConst.AUDITING);
        fmInvoiceApplyService.updateById(record);
        return Result.ok();
    }

    @Bean
    public FlowBusinessExecuteService getFmInvoiceApplyFlowBusinessExecuteService() {
        return new FlowBusinessExecuteService(FlowBusinessKeyConst.FM_INVOICE_APPLY_KEY) {
            @Override
            @Async
            public void execute(String instanceId, Integer status) {
                fmInvoiceApplyService.updateByFlowCode(instanceId, status);
            }
        };
    }

    /**
     * 导出
     *
     * @param dto
     */
    @GetMapping("/export")
    public void export(FmInvoiceApplyQueryDto dto, HttpServletResponse response) {
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        List<FmInvoiceApplyVo> voList = fmInvoiceApplyService.buildFmInvoiceApplyVoList(fmInvoiceApplyService.list(FmInvoiceApplyQueryCondition.build(dto)), 0);
        CommonExportVo<FmInvoiceApplyVo> exportVo = new CommonExportVo<>(voList);
        String templatePath = "/templates/export/开票管理.xls";
        InputStream inputStream = this.getClass().getResourceAsStream(templatePath);
        ExcelUtil.exportResponse(exportVo, inputStream, response);
    }

    /**
     * 上传发票
     *
     * @param dto
     * @return
     */
    @PostMapping("/uploadInvoice")
    public Result uploadInvoice(@RequestBody UploadInvoiceDto dto) {
        String invoiceApplyCode = dto.getInvoiceApplyCode();
        FmInvoiceApply fmInvoiceApply = fmInvoiceApplyService.getOne(new LambdaQueryWrapper<FmInvoiceApply>()
                .eq(FmInvoiceApply::getInvoiceApplyCode, invoiceApplyCode));
        if (fmInvoiceApply != null) {
            fmInvoiceApply.setInvoiceStatus(2);
            fmInvoiceApplyService.updateById(fmInvoiceApply);
        }
        fileService.updateFileDataDtoList(dto.getInvoiceApplyCode(), SysFileConst.FM_INVOICE_APPLY_PAGE_FILE, dto.getFileDataDtos());
        return Result.ok();
    }

    /**
     * 获取发票列表
     *
     * @param invoiceApplyCode
     * @return
     */
    @GetMapping("/getInvoiceFiles")
    public Result<List<FileDataDto>> getInvoiceFiles(String invoiceApplyCode) {
        return new Result<>(fileService.getFileDataDtoList(invoiceApplyCode, SysFileConst.FM_INVOICE_APPLY_PAGE_FILE));
    }

}