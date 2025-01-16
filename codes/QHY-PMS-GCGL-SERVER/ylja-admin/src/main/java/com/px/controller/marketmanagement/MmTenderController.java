package com.px.controller.marketmanagement;

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
import com.px.common.util.CollUtils;
import com.px.common.util.ExcelUtil;
import com.px.common.util.IpUtil;
import com.px.common.util.security.SecurityUtils;
import com.px.marketmanagement.bean.dto.MmTenderQueryDto;
import com.px.marketmanagement.bean.dto.MmTenderUpdateDto;
import com.px.marketmanagement.bean.vo.MmTenderVo;
import com.px.marketmanagement.entity.MmTender;
import com.px.marketmanagement.entity.MmTenderPrincipal;
import com.px.marketmanagement.querycondition.MmTenderQueryCondition;
import com.px.marketmanagement.service.MmTenderPrincipalService;
import com.px.marketmanagement.service.MmTenderService;
import com.px.resourcecenter.service.RcCustomerService;
import com.px.system.business.CodeBusiness;
import com.px.system.service.SysDepartmentService;
import com.px.system.service.SysFileService;
import com.px.system.service.SysOperationLogService;
import com.px.system.service.SysUserService;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 标书评审管理
 *
 * @author 品讯科技
 * @since 2023-12-14
 */
@RestController
@RequestMapping("/mm/tender")
public class MmTenderController {

    @Autowired
    private MmTenderService mmTenderService;
    @Autowired
    private MmTenderPrincipalService tenderPrincipalService;
    @Autowired
    private SysFileService fileService;
    @Autowired
    private BusinessBpmService businessBpmService;
    @Autowired
    private CodeBusiness codeBusiness;
    @Autowired
    private SysUserService userService;
    @Autowired
    private RcCustomerService customerService;
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
    public Result<Page<MmTenderVo>> getPage(MmTenderQueryDto dto) {
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        Page<MmTenderVo> voPage = mmTenderService.getPage(dto);
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
    public Result addOrUpdate(@RequestBody MmTenderUpdateDto dto, HttpServletRequest request) {
        dto.setFlowStatus(null);
        MmTender mmTender = new MmTender();
        BeanUtils.copyProperties(dto, mmTender, "createBy","createTime","updateBy","updateTime","flowStatus","flowCode");
        LambdaQueryWrapper<MmTender> wrapper = new LambdaQueryWrapper<MmTender>()
                .eq(MmTender::getIsDel, 0)
                .eq(MmTender::getProjectEstablishmentCode, dto.getProjectEstablishmentCode());
        if (dto.getId() != null) {
            wrapper.ne(MmTender::getId, dto.getId());
        }
        int count = mmTenderService.count(wrapper);
        if (count > 0) {
            return Result.fail(4005, "该投标立项已存在标书评审");
        }
        if (mmTender.getId() == null) {
            mmTender.setTenderCode(codeBusiness.genCode(SysCodeConst.BSPS));
            mmTender.setCreateBy(SecurityUtils.getUserCodeAndName());
            mmTender.setCreateTime(new Date());
            mmTenderService.save(mmTender);
        } else {
            mmTender.setUpdateBy(SecurityUtils.getUserCodeAndName());
            mmTender.setUpdateTime(new Date());
            mmTenderService.updateById(mmTender);
        }
        tenderPrincipalService.remove(new LambdaQueryWrapper<MmTenderPrincipal>().eq(MmTenderPrincipal::getTenderCode, mmTender.getTenderCode()));
        List<MmTenderPrincipal> tenderPrincipals = dto.getTenderPrincipals();
        if (CollUtils.isNotEmpty(tenderPrincipals)) {
            tenderPrincipals.forEach(x -> x.setTenderCode(mmTender.getTenderCode()));
            tenderPrincipalService.saveBatch(tenderPrincipals);
        }
        fileService.updateFileDataDtoList(mmTender.getTenderCode(), SysFileConst.MM_TENDER_PAGE_FILE, dto.getFileDataDtos());
        operationLogService.saveOperatorLog(IpUtil.getIpAddr(request),
                dto.getId(), "标书评审",
                JSON.toJSONString(dto));
        return new Result(mmTender.getId());
    }

    /***
     * 获取详情
     * @param id
     * @return
     */
    @GetMapping("/getInfo")
    public Result<MmTenderUpdateDto> getInfo(Long id) {
        MmTenderUpdateDto dto = null;
        MmTender mmTender = mmTenderService.getById(id);
        if (mmTender != null) {
            dto = new MmTenderUpdateDto();
            BeanUtils.copyProperties(mmTender, dto);
            dto.setTenderPrincipals(tenderPrincipalService.list(new LambdaQueryWrapper<MmTenderPrincipal>()
                    .eq(MmTenderPrincipal::getTenderCode, mmTender.getTenderCode())));
            dto.setFileDataDtos(fileService.getFileDataDtoList(mmTender.getTenderCode(), SysFileConst.MM_TENDER_PAGE_FILE));
            businessBpmService.setTaskIds(Arrays.asList(dto));
            dto.setCustomerName(customerService.getNameByCode(dto.getCustomerCode()));
            dto.setBidPrincipalUserName(userService.getNameByCode(dto.getBidPrincipalUserCode()));
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
        MmTender mmTender = mmTenderService.getById(id);
        if (mmTender == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        mmTender.setIsDel(1);
        mmTenderService.updateById(mmTender);
        operationLogService.saveDelOperatorLog(IpUtil.getIpAddr(request),
                "标书评审" + "“" + mmTender.getTenderCode() + "”",
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
        MmTender record = mmTenderService.getById(id);
        if (record == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        if (record.getFlowStatus() != null && Arrays.asList(2, 4).contains(record.getFlowStatus())) {
            return Result.fail(ResultCode.AUDIT_STATUS_CHANGED);
        }
        String processInstanceId = businessBpmService.startProcessV2(FlowBusinessKeyConst.MM_TENDER_KEY, record.getDepCode(), record.getTenderCode(), record.getId(),processDefId,record.getProjectEstablishmentCode(),record.getProjectEstablishmentName());
        if (StringUtils.isBlank(processInstanceId)) {
            return Result.fail(ResultCode.BUSINESS_NOT_RELEVANCE);
        }
        record.setFlowCode(processInstanceId);
        record.setFlowStatus(FlowAuditStatusConst.AUDITING);
        mmTenderService.updateById(record);
        return Result.ok();
    }

    @Bean
    public FlowBusinessExecuteService getMmTenderFlowBusinessExecuteService() {
        return new FlowBusinessExecuteService(FlowBusinessKeyConst.MM_TENDER_KEY) {
            @Override
            @Async
            public void execute(String instanceId, Integer status) {
                mmTenderService.updateByFlowCode(instanceId, status);
            }
        };
    }

    /**
     * 导出
     *
     * @param dto
     */
    @GetMapping("/export")
    public void export(MmTenderQueryDto dto, HttpServletResponse response) {
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        List<MmTenderVo> voList = mmTenderService.buildMmTenderVoList(mmTenderService.list(MmTenderQueryCondition.build(dto)));
        CommonExportVo<MmTenderVo> exportVo = new CommonExportVo<>(voList);
        String templatePath = "/templates/export/标书评审.xls";
        InputStream inputStream = this.getClass().getResourceAsStream(templatePath);
        ExcelUtil.exportResponse(exportVo, inputStream, response);
    }

}