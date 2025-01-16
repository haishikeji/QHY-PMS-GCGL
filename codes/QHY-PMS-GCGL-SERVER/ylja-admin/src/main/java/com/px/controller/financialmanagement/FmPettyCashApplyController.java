package com.px.controller.financialmanagement;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.bpm.service.BusinessBpmService;
import com.px.common.bean.vo.CommonExportVo;
import com.px.common.consts.FlowAuditStatusConst;
import com.px.common.consts.FlowBusinessKeyConst;
import com.px.common.consts.SysCodeConst;
import com.px.common.consts.SysFileConst;
import com.px.common.result.Result;
import com.px.common.result.ResultCode;
import com.px.common.service.FlowBusinessExecuteService;
import com.px.common.util.ExcelUtil;
import com.px.common.util.IpUtil;
import com.px.common.util.security.SecurityUtils;
import com.px.financialmanagement.bean.dto.FmPettyCashApplyQueryDto;
import com.px.financialmanagement.bean.dto.FmPettyCashApplyUpdateDto;
import com.px.financialmanagement.bean.vo.FmPettyCashApplyVo;
import com.px.financialmanagement.entity.FmPettyCashApply;
import com.px.financialmanagement.querycondition.FmPettyCashApplyQueryCondition;
import com.px.financialmanagement.service.FmPettyCashApplyService;
import com.px.system.business.CodeBusiness;
import com.px.system.entity.SysBankAccount;
import com.px.system.service.SysBankAccountService;
import com.px.system.service.SysDepartmentService;
import com.px.system.service.SysFileService;
import com.px.system.service.SysOperationLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 备用金管理管理
 *
 * @author 品讯科技
 * @since 2023-12-26
 */
@RestController
@RequestMapping("/fm/pettyCashApply")
public class FmPettyCashApplyController {

    @Autowired
    private FmPettyCashApplyService fmPettyCashApplyService;
    @Autowired
    private SysFileService fileService;
    @Autowired
    private SysBankAccountService bankAccountService;
    @Autowired
    private CodeBusiness codeBusiness;
    @Autowired
    private BusinessBpmService businessBpmService;
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
    public Result<Page<FmPettyCashApplyVo>> getPage(FmPettyCashApplyQueryDto dto) {
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        Page<FmPettyCashApplyVo> voPage = fmPettyCashApplyService.getPage(dto);
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
    public Result<List<FmPettyCashApplyVo>> getList(FmPettyCashApplyQueryDto dto) {
        List<FmPettyCashApply> list = fmPettyCashApplyService.list(FmPettyCashApplyQueryCondition.build(dto));
        return new Result(list);
    }

    /***
     * 新增/修改(id为空:新增，不为空:修改)
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/addOrUpdate")
    public Result addOrUpdate(@RequestBody FmPettyCashApplyUpdateDto dto, HttpServletRequest request) {
        dto.setFlowStatus(null);
        FmPettyCashApply fmPettyCashApply = new FmPettyCashApply();
        BeanUtils.copyProperties(dto, fmPettyCashApply, "createBy","createTime","updateBy","updateTime","flowStatus","flowCode");
        if (fmPettyCashApply.getId() == null) {
            fmPettyCashApply.setPettyCashApplyCode(codeBusiness.genCode(SysCodeConst.BYJ));
            fmPettyCashApply.setCreateBy(SecurityUtils.getUserCodeAndName());
            fmPettyCashApply.setCreateTime(new Date());
            fmPettyCashApplyService.save(fmPettyCashApply);
        } else {
            fmPettyCashApply.setUpdateBy(SecurityUtils.getUserCodeAndName());
            fmPettyCashApply.setUpdateTime(new Date());
            fmPettyCashApplyService.updateById(fmPettyCashApply);
        }
        List<SysBankAccount> bankAccounts = dto.getBankAccounts();
        if (!CollectionUtils.isEmpty(bankAccounts)) {
            bankAccountService.remove(new LambdaQueryWrapper<SysBankAccount>()
                    .eq(SysBankAccount::getDataCode, fmPettyCashApply.getPettyCashApplyCode()));
            bankAccounts.forEach(x -> x.setDataCode(fmPettyCashApply.getPettyCashApplyCode()));
            bankAccountService.saveBatch(bankAccounts);
        }
        fileService.updateFileDataDtoList(fmPettyCashApply.getPettyCashApplyCode(), SysFileConst.FM_PETTY_CASH_APPLY_PAGE_FILE, dto.getFileDataDtos());
        operationLogService.saveOperatorLog(IpUtil.getIpAddr(request),
                dto.getId(), "备用金申请",
                JSON.toJSONString(dto));
        return new Result(fmPettyCashApply.getId());
    }

    /***
     * 获取详情
     * @param id
     * @return
     */
    @GetMapping("/getInfo")
    public Result<FmPettyCashApplyUpdateDto> getInfo(Long id) {
        FmPettyCashApplyUpdateDto dto = null;
        FmPettyCashApply fmPettyCashApply = fmPettyCashApplyService.getById(id);
        if (fmPettyCashApply != null) {
            dto = new FmPettyCashApplyUpdateDto();
            BeanUtils.copyProperties(fmPettyCashApply, dto);
            dto.setBankAccounts(bankAccountService.list(new LambdaQueryWrapper<SysBankAccount>()
                    .eq(SysBankAccount::getDataCode, fmPettyCashApply.getPettyCashApplyCode())));
            dto.setFileDataDtos(fileService.getFileDataDtoList(fmPettyCashApply.getPettyCashApplyCode(), SysFileConst.FM_PETTY_CASH_APPLY_PAGE_FILE));
            businessBpmService.setTaskIds(Arrays.asList(dto));
            dto.setDepName(departmentService.getNameByCode(dto.getDepCode()));
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
        FmPettyCashApply fmPettyCashApply = fmPettyCashApplyService.getById(id);
        if (fmPettyCashApply == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        fmPettyCashApply.setIsDel(1);
        fmPettyCashApplyService.updateById(fmPettyCashApply);
        operationLogService.saveDelOperatorLog(IpUtil.getIpAddr(request),
                "备用金申请" + "“" + fmPettyCashApply.getPettyCashApplyCode() + "”",
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
        FmPettyCashApply record = fmPettyCashApplyService.getById(id);
        if (record == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        if (record.getFlowStatus() != null && Arrays.asList(2, 4).contains(record.getFlowStatus())) {
            return Result.fail(ResultCode.AUDIT_STATUS_CHANGED);
        }
        String processInstanceId = businessBpmService.startProcessV2(FlowBusinessKeyConst.FM_PETTY_CASH_APPLY_KEY, record.getDepCode(), record.getPettyCashApplyCode(), record.getId(),processDefId,null,null);
        if (StringUtils.isBlank(processInstanceId)) {
            return Result.fail(ResultCode.BUSINESS_NOT_RELEVANCE);
        }
        record.setStatus(1);
        record.setFlowCode(processInstanceId);
        record.setFlowStatus(FlowAuditStatusConst.AUDITING);
        fmPettyCashApplyService.updateById(record);
        return Result.ok();
    }

    @Bean
    public FlowBusinessExecuteService getFmPettyCashApplyFlowBusinessExecuteService() {
        return new FlowBusinessExecuteService(FlowBusinessKeyConst.FM_PETTY_CASH_APPLY_KEY) {
            @Override
            @Async
            public void execute(String instanceId, Integer status) {
                fmPettyCashApplyService.updateByFlowCode(instanceId, status);
            }
        };
    }

    /**
     * 导出
     *
     * @param dto
     */
    @GetMapping("/export")
    public void export(FmPettyCashApplyQueryDto dto, HttpServletResponse response) {
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        List<FmPettyCashApplyVo> voList = fmPettyCashApplyService.buildFmPettyCashApplyVoList(fmPettyCashApplyService.list(FmPettyCashApplyQueryCondition.build(dto)));
        CommonExportVo<FmPettyCashApplyVo> exportVo = new CommonExportVo<>(voList);
        String templatePath = "/templates/export/备用金管理.xls";
        InputStream inputStream = this.getClass().getResourceAsStream(templatePath);
        ExcelUtil.exportResponse(exportVo, inputStream, response);
    }

}