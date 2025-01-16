package com.px.system.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.common.bean.vo.LoginUser;
import com.px.common.log.LogType;
import com.px.common.util.DateUtil;
import com.px.common.util.ExcelUtil;
import com.px.common.util.security.SecurityUtils;
import com.px.system.bean.dto.SysOperationLogQueryDto;
import com.px.system.bean.vo.OperationLogExportVo;
import com.px.system.bean.vo.SysOperationLogVo;
import com.px.system.entity.SysOperationLog;
import com.px.system.mapper.SysOperationLogMapper;
import com.px.system.querycondition.SysOperationLogQueryCondition;
import com.px.system.service.SysOperationLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * SysOperationLogServiceImpl 服务实现类
 *
 * @author 品讯科技
 * @since 2023-11-08
 */
@Service
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLog> implements SysOperationLogService {

    @Override
    public Page<SysOperationLogVo> getPage(SysOperationLogQueryDto dto) {
        LambdaQueryWrapper<SysOperationLog> queryWrapper = SysOperationLogQueryCondition.build(dto);
        Page<SysOperationLog> page = this.page(new Page(dto.getPageNum(), dto.getPageSize()), queryWrapper);
        Page<SysOperationLogVo> voPage = new Page<>();
        BeanUtils.copyProperties(page, voPage);
        List<SysOperationLog> records = page.getRecords();
        List<SysOperationLogVo> voList = this.buildSysOperationLogVoList(records);
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<SysOperationLogVo> buildSysOperationLogVoList(List<SysOperationLog> list) {
        List<SysOperationLogVo> voList = null;
        if (!CollectionUtils.isEmpty(list)) {
            voList = new ArrayList<>(list.size());
            for (SysOperationLog sysOperationLog : list) {
                SysOperationLogVo vo = new SysOperationLogVo();
                BeanUtils.copyProperties(sysOperationLog, vo);
                voList.add(vo);
            }
        }
        return voList;
    }

    @Override
    public void export(SysOperationLogQueryDto dto, HttpServletResponse response) {
        try {
            String fileName = "操作日志" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmss");
            ExcelUtil.setExcelResponseProp(response, fileName);
            List<OperationLogExportVo> voList = buildEmployeeExportVo(dto);
            EasyExcel.write(response.getOutputStream())
                    .head(OperationLogExportVo.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("操作日志")
                    .doWrite(voList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<OperationLogExportVo> buildEmployeeExportVo(SysOperationLogQueryDto dto) {
        LambdaQueryWrapper<SysOperationLog> queryWrapper = SysOperationLogQueryCondition.build(dto);
        List<SysOperationLog> list = this.list(queryWrapper);
        List<OperationLogExportVo> voList = null;
        if (!CollectionUtils.isEmpty(list)) {
            voList = new ArrayList<>(list.size());
            for (SysOperationLog data : list) {
                OperationLogExportVo vo = new OperationLogExportVo();
                BeanUtils.copyProperties(data, vo);
                Integer operationType = data.getOperationType();
                vo.setOperationType(getOperationType(operationType));
                vo.setCreateTime(DateUtil.dateToString(data.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                voList.add(vo);
            }
        }
        return voList;
    }

    private String getOperationType(Integer operationType) {
        //操作类型:1.修改，2.新增，3.删除，4.导入，5.导出
        String str = null;
        switch (operationType) {
            case 1:
                str = "修改";
                break;
            case 2:
                str = "新增";
                break;
            case 3:
                str = "删除";
                break;
            case 4:
                str = "导入";
                break;
            case 5:
                str = "导出";
                break;
        }
        return str;
    }

    @Override
    public void saveOperatorLog(String ip, String type, String remark, String operationData) {
        try {
            LoginUser loginUser = SecurityUtils.getUserDetails();
            SysOperationLog operationLog = new SysOperationLog();
            if (loginUser != null) {
                operationLog.setUserAccount(loginUser.getLoginName());
                operationLog.setUserCode(loginUser.getUserCode());
                operationLog.setUserName(loginUser.getName());
            }
            operationLog.setIpAddr(ip);
            operationLog.setCreateTime(new Date());
            operationLog.setOperationType(Integer.parseInt(type));
            operationLog.setOperationRemark(remark);
            operationLog.setOperationData(operationData);
            this.save(operationLog);
        } catch (Exception e) {
            log.error("保存用户操作日志出现异常：{}", e);
        }
    }

    @Override
    public void saveOperatorLog(String ip, Object obj, String title, String operationData) {
        this.saveOperatorLog(ip, Objects.isNull(obj) ? LogType.INSERT : LogType.UPDATE,
                (Objects.isNull(obj) ? "新增" : "更新") + title, operationData);
    }

    @Override
    public void saveDelOperatorLog(String ip, String title, String operationData) {
        this.saveOperatorLog(ip, LogType.DELETE,
                "删除" + title, operationData);
    }

}

