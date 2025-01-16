package com.px.controller.engineeringsupervision;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.bpm.service.BusinessBpmService;
import com.px.common.bean.vo.CommonExportVo;
import com.px.common.consts.FlowAuditStatusConst;
import com.px.common.consts.FlowBusinessKeyConst;
import com.px.common.consts.SysFileConst;
import com.px.common.log.LogType;
import com.px.common.result.Result;
import com.px.common.result.ResultCode;
import com.px.common.service.FlowBusinessExecuteService;
import com.px.common.util.ExcelUtil;
import com.px.common.util.IpUtil;
import com.px.common.util.security.SecurityUtils;
import com.px.engineeringsupervision.bean.dto.EsEngineeringContractQueryDto;
import com.px.engineeringsupervision.bean.dto.EsEngineeringContractUpdateDto;
import com.px.engineeringsupervision.bean.vo.EsEngineeringContractVo;
import com.px.engineeringsupervision.entity.EsEngineeringContract;
import com.px.engineeringsupervision.entity.EsEngineeringContractClause;
import com.px.engineeringsupervision.querycondition.EsEngineeringContractQueryCondition;
import com.px.engineeringsupervision.service.EsEngineeringContractClauseService;
import com.px.engineeringsupervision.service.EsEngineeringContractService;
import com.px.purchasingmanagement.bean.dto.ChangeRecordStartProcessDto;
import com.px.purchasingmanagement.bean.dto.SaveChangeRecordDto;
import com.px.purchasingmanagement.bean.dto.SysChangeRecordQueryDto;
import com.px.purchasingmanagement.bean.vo.SysChangeRecordVo;
import com.px.purchasingmanagement.service.SysChangeRecordService;
import com.px.service.others.ChangeRecordAuditService;
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
 * 合同登记管理
 *
 * @author 品讯科技
 * @since 2023-12-18
 */
@RestController
@RequestMapping("/es/engineeringContract")
public class EsEngineeringContractController {

    @Autowired
    private EsEngineeringContractService esEngineeringContractService;
    @Autowired
    private EsEngineeringContractClauseService engineeringContractClauseService;
    @Autowired
    private SysFileService fileService;
    @Autowired
    private SysChangeRecordService changeRecordService;
    @Autowired
    private ChangeRecordAuditService changeRecordAuditService;
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
    public Result<Page<EsEngineeringContractVo>> getPage(EsEngineeringContractQueryDto dto) {
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        Page<EsEngineeringContractVo> voPage = esEngineeringContractService.getPage(dto);
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
    public Result<List<EsEngineeringContractVo>> getList(EsEngineeringContractQueryDto dto) {
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        dto.setFlowStatus(4);
        return new Result(esEngineeringContractService.buildEsEngineeringContractVoList(esEngineeringContractService.list(EsEngineeringContractQueryCondition.build(dto))));
    }

    /***
     * 新增/修改(id为空:新增，不为空:修改)
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/addOrUpdate")
    public Result addOrUpdate(@RequestBody EsEngineeringContractUpdateDto dto, HttpServletRequest request) {
        LambdaQueryWrapper<EsEngineeringContract> lambdaQueryWrapper = new LambdaQueryWrapper<EsEngineeringContract>()
                .eq(EsEngineeringContract::getProjectCode, dto.getProjectCode())
                .eq(EsEngineeringContract::getIsDel, 0);
        if (dto.getId() != null) {
            lambdaQueryWrapper.ne(EsEngineeringContract::getId, dto.getId());
        }
        int count = esEngineeringContractService.count(lambdaQueryWrapper);
        if (count > 0) {
            return Result.fail(4005, "该项目已存在施工合同");
        }

        String lastJson = JSON.toJSONStringWithDateFormat(this.getInfo(dto.getId()), "yyyy-MM-dd");
        EsEngineeringContract esEngineeringContract = new EsEngineeringContract();
        BeanUtils.copyProperties(dto, esEngineeringContract, "createBy", "createTime", "updateBy", "updateTime", "flowStatus", "flowCode");
        LambdaQueryWrapper<EsEngineeringContract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EsEngineeringContract::getIsDel, 0)
                .eq(EsEngineeringContract::getEngineeringContractCode, esEngineeringContract.getEngineeringContractCode());
        if (dto.getId() != null) {
            queryWrapper.ne(EsEngineeringContract::getId, dto.getId());
        }
        if (esEngineeringContractService.count(queryWrapper) > 0) {
            return Result.fail(ResultCode.CODE_IS_REPETITION);
        }
//        esEngineeringContract.setFlowStatus(null);
        if (esEngineeringContract.getId() == null) {
            esEngineeringContract.setCreateBy(SecurityUtils.getUserCodeAndName());
            esEngineeringContract.setCreateTime(new Date());
            esEngineeringContractService.save(esEngineeringContract);
        } else {
            esEngineeringContract.setUpdateBy(SecurityUtils.getUserCodeAndName());
            esEngineeringContract.setUpdateTime(new Date());
            if (dto.getChangeFlag() == null || dto.getChangeFlag() == 0) {
                esEngineeringContractService.updateById(esEngineeringContract);
            }
        }
        if (dto.getChangeFlag() == null || dto.getChangeFlag() == 0) {
            List<EsEngineeringContractClause> contractClauses = dto.getContractClauses();
            engineeringContractClauseService.remove(new LambdaQueryWrapper<EsEngineeringContractClause>()
                    .eq(EsEngineeringContractClause::getEngineeringContractCode, dto.getEngineeringContractCode()));
            if (!CollectionUtils.isEmpty(contractClauses)) {
                contractClauses.forEach(x -> x.setEngineeringContractCode(esEngineeringContract.getEngineeringContractCode()));
                engineeringContractClauseService.saveBatch(contractClauses);
            }
            fileService.updateFileDataDtoList(esEngineeringContract.getEngineeringContractCode(), SysFileConst.ENGINEERING_CONTRACT_PAGE_FILE, dto.getFileDataDtos());
            operationLogService.saveOperatorLog(IpUtil.getIpAddr(request),
                    dto.getId() == null ? LogType.INSERT : LogType.UPDATE,
                    (dto.getId() == null ? "新增" : "更新") + "合同登记",
                    JSON.toJSONString(dto));
        }
        if (dto.getChangeFlag() != null && dto.getChangeFlag() == 1 && dto.getFlowStatus() == 4) {
            Integer num = changeRecordService.checkRepetition(dto.getEngineeringContractCode());
            if (num > 0) {
                return Result.fail(ResultCode.AUDIT_STATUS_REPETITION);
            }
            SaveChangeRecordDto changeRecordDto = new SaveChangeRecordDto();
            changeRecordDto.setDepCode(dto.getDepCode());
            changeRecordDto.setType(2);
            changeRecordDto.setCode(dto.getEngineeringContractCode());
            changeRecordDto.setRemark(dto.getRemark());
            changeRecordDto.setLastJson(lastJson);
            changeRecordDto.setNewJson(JSON.toJSONStringWithDateFormat(new Result<>(dto), "yyyy-MM-dd"));
            changeRecordDto.setProjectCode(dto.getProjectCode());
            changeRecordDto.setProjectName(dto.getProjectName());
            changeRecordDto.setChangeRecordId(dto.getChangeRecordId());
            Long id = changeRecordService.saveChangeRecord(changeRecordDto);
            if (dto.getCrSubmitAudit() != null && dto.getCrSubmitAudit() == 1) {
                ChangeRecordStartProcessDto startProcessDto = new ChangeRecordStartProcessDto();
                startProcessDto.setId(id);
                startProcessDto.setAuditMoney(dto.getContractMoneyBeforeTaxes());
                changeRecordAuditService.submit(startProcessDto);
            }
            return new Result(id);
        }
        return new Result(esEngineeringContract.getId());
    }

    /***
     * 获取详情
     * @param id
     * @return
     */
    @GetMapping("/getInfo")
    public Result<EsEngineeringContractUpdateDto> getInfo(Long id) {
        EsEngineeringContractUpdateDto dto = null;
        EsEngineeringContract esEngineeringContract = esEngineeringContractService.getById(id);
        if (esEngineeringContract != null) {
            dto = new EsEngineeringContractUpdateDto();
            BeanUtils.copyProperties(esEngineeringContract, dto);
            dto.setContractClauses(engineeringContractClauseService.list(new LambdaQueryWrapper<EsEngineeringContractClause>()
                    .eq(EsEngineeringContractClause::getEngineeringContractCode, esEngineeringContract.getEngineeringContractCode())));
            dto.setFileDataDtos(fileService.getFileDataDtoList(esEngineeringContract.getEngineeringContractCode(), SysFileConst.ENGINEERING_CONTRACT_PAGE_FILE));
            businessBpmService.setTaskIds(Arrays.asList(dto));

        }
        return new Result(dto);
    }


    /**
     * 查询合同变更记录
     *
     * @param pageNum                 页数
     * @param pageSize                每页大小
     * @param engineeringContractCode 合同编号
     * @return
     */
    @GetMapping("/getChangeRecordPage")
    public Result<Page<SysChangeRecordVo>> getChangeRecordPage(Integer pageNum,
                                                               Integer pageSize,
                                                               String engineeringContractCode) {
        SysChangeRecordQueryDto dto = new SysChangeRecordQueryDto();
        dto.setPageNum(pageNum);
        dto.setPageSize(pageSize);
        dto.setDataCode(engineeringContractCode);
        Page<SysChangeRecordVo> page = changeRecordService.getPage(dto);
        return new Result<>(page);
    }

    /**
     * 提交审核
     *
     * @param id
     * @return
     */
    @PostMapping("/submitAudit")
    public Result submitAudit(Long id, String processDefId) {
        EsEngineeringContract record = esEngineeringContractService.getById(id);
        if (record == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        if (record.getFlowStatus() != null && Arrays.asList(2, 4).contains(record.getFlowStatus())) {
            return Result.fail(ResultCode.AUDIT_STATUS_CHANGED);
        }
        String processInstanceId = businessBpmService.startProcessV2(FlowBusinessKeyConst.ES_ENGINEERING_CONTRACT_KEY, record.getDepCode(), record.getEngineeringContractCode(), record.getId(), processDefId, record.getProjectCode(), record.getProjectName());
        if (StringUtils.isBlank(processInstanceId)) {
            return Result.fail(ResultCode.BUSINESS_NOT_RELEVANCE);
        }
        record.setFlowCode(processInstanceId);
        record.setFlowStatus(FlowAuditStatusConst.AUDITING);
        esEngineeringContractService.updateById(record);
        return Result.ok();
    }

    @Bean
    public FlowBusinessExecuteService getEsEngineeringContractFlowBusinessExecuteService() {
        return new FlowBusinessExecuteService(FlowBusinessKeyConst.ES_ENGINEERING_CONTRACT_KEY) {
            @Override
            @Async
            public void execute(String instanceId, Integer status) {
                esEngineeringContractService.updateByFlowCode(instanceId, status);
            }
        };
    }

    /**
     * 导出
     *
     * @param dto
     */
    @GetMapping("/export")
    public void export(EsEngineeringContractQueryDto dto, HttpServletResponse response) {
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            dto.setDepCodeList(departmentService.getNextLevel(dto.getDepCode()));
        }
        List<EsEngineeringContractVo> voList = esEngineeringContractService.buildEsEngineeringContractVoList(esEngineeringContractService.list(EsEngineeringContractQueryCondition.build(dto)));
        CommonExportVo<EsEngineeringContractVo> exportVo = new CommonExportVo<>(voList);
        String templatePath = "/templates/export/合同登记.xls";
        InputStream inputStream = this.getClass().getResourceAsStream(templatePath);
        ExcelUtil.exportResponse(exportVo, inputStream, response);
    }

    /**
     * 删除
     *
     * @param id 数据id
     * @return
     */
    @PostMapping("/delete")
    public Result delete(Long id, HttpServletRequest request) {
        EsEngineeringContract record = esEngineeringContractService.getById(id);
        if (record == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        Integer flowStatus = record.getFlowStatus();
        if (Arrays.asList(2, 4).contains(flowStatus)) {
            return Result.fail(3004, "当前审批状态下无法删除！");
        }
        record.setIsDel(1);
        esEngineeringContractService.updateById(record);
        operationLogService.saveDelOperatorLog(IpUtil.getIpAddr(request),
                "合同登记" + "“" + record.getEngineeringContractCode() + "”",
                JSON.toJSONString(id));
        return Result.ok();
    }

}