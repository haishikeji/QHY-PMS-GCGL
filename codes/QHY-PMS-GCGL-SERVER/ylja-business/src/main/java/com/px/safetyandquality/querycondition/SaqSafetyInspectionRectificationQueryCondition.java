package com.px.safetyandquality.querycondition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.common.util.CollUtils;
import com.px.common.util.DateUtil;
import com.px.safetyandquality.bean.dto.SaqSafetyInspectionRectificationQueryDto;
import com.px.safetyandquality.entity.SaqSafetyInspectionRectification;
import org.springframework.util.StringUtils;

/**
 * SaqSafetyInspectionRectification 查询
 *
 * @author 品讯科技
 * @since 2023-12-24
 */
public class SaqSafetyInspectionRectificationQueryCondition {

    /**
     * SaqSafetyInspectionRectification使用lambda构建查询对象
     *
     * @param dto
     * @return
     */
    public static LambdaQueryWrapper<SaqSafetyInspectionRectification> build(SaqSafetyInspectionRectificationQueryDto dto) {
        LambdaQueryWrapper<SaqSafetyInspectionRectification> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // id : 
        if (!StringUtils.isEmpty(dto.getId())) {
            lambdaQueryWrapper.eq(SaqSafetyInspectionRectification::getId, dto.getId());
        }
        if (!StringUtils.isEmpty(dto.getDataType())) {
            lambdaQueryWrapper.eq(SaqSafetyInspectionRectification::getDataType, dto.getDataType());
        }
        // sirCode : 单据编号
        if (!StringUtils.isEmpty(dto.getSirCode())) {
            lambdaQueryWrapper.eq(SaqSafetyInspectionRectification::getSirCode, dto.getSirCode());
        }
        // depCode : 部门编号
        if (CollUtils.isNotEmpty(dto.getDepCodeList())) {
            lambdaQueryWrapper.in(SaqSafetyInspectionRectification::getDepCode, dto.getDepCodeList());
        }
        // projectName : 项目名称
        if (!StringUtils.isEmpty(dto.getProjectName())) {
            lambdaQueryWrapper.like(SaqSafetyInspectionRectification::getProjectName, dto.getProjectName());
        }
        // projectCode : 项目编号
        if (!StringUtils.isEmpty(dto.getProjectCode())) {
            lambdaQueryWrapper.eq(SaqSafetyInspectionRectification::getProjectCode, dto.getProjectCode());
        }
        // checkDate : 检查日期
        if (!StringUtils.isEmpty(dto.getCheckDateStart()) && !StringUtils.isEmpty(dto.getCheckDateEnd())) {
            lambdaQueryWrapper.ge(SaqSafetyInspectionRectification::getCheckDate, DateUtil.getBeginOfDay(DateUtil.format(dto.getCheckDateStart(), "yyyy-MM-dd")));
            lambdaQueryWrapper.le(SaqSafetyInspectionRectification::getCheckDate, DateUtil.getEndOfDay(DateUtil.format(dto.getCheckDateEnd(), "yyyy-MM-dd")));
        }
        // checkPrincipal : 质检负责人
        if (!StringUtils.isEmpty(dto.getCheckPrincipal())) {
            lambdaQueryWrapper.like(SaqSafetyInspectionRectification::getCheckPrincipal, dto.getCheckPrincipal());
        }
        // checkAcceptType : 验收种类
        if (!StringUtils.isEmpty(dto.getCheckAcceptType())) {
            lambdaQueryWrapper.eq(SaqSafetyInspectionRectification::getCheckAcceptType, dto.getCheckAcceptType());
        }
        // hiddenDangerName : 隐患名称
        if (!StringUtils.isEmpty(dto.getHiddenDangerName())) {
            lambdaQueryWrapper.eq(SaqSafetyInspectionRectification::getHiddenDangerName, dto.getHiddenDangerName());
        }
        // hiddenDangerDesc : 隐患描述
        if (!StringUtils.isEmpty(dto.getHiddenDangerDesc())) {
            lambdaQueryWrapper.eq(SaqSafetyInspectionRectification::getHiddenDangerDesc, dto.getHiddenDangerDesc());
        }
        // hiddenDangerLevel : 隐患等级
        if (!StringUtils.isEmpty(dto.getHiddenDangerLevel())) {
            lambdaQueryWrapper.eq(SaqSafetyInspectionRectification::getHiddenDangerLevel, dto.getHiddenDangerLevel());
        }
        // planRectificationFinishDate : 计划整改完成日期
        if (!StringUtils.isEmpty(dto.getPlanRectificationFinishDate())) {
            lambdaQueryWrapper.eq(SaqSafetyInspectionRectification::getPlanRectificationFinishDate, dto.getPlanRectificationFinishDate());
        }
        // rectificationPrincipal : 负责人
        if (!StringUtils.isEmpty(dto.getRectificationPrincipal())) {
            lambdaQueryWrapper.like(SaqSafetyInspectionRectification::getRectificationPrincipal, dto.getRectificationPrincipal());
        }
        // rectificationFinishDate : 整改完成日期
        if (!StringUtils.isEmpty(dto.getRectificationFinishDateStart()) && !StringUtils.isEmpty(dto.getRectificationFinishDateEnd())) {
            lambdaQueryWrapper.ge(SaqSafetyInspectionRectification::getRectificationFinishDate, DateUtil.getBeginOfDay(DateUtil.format(dto.getRectificationFinishDateStart(), "yyyy-MM-dd")));
            lambdaQueryWrapper.le(SaqSafetyInspectionRectification::getRectificationFinishDate, DateUtil.getEndOfDay(DateUtil.format(dto.getRectificationFinishDateEnd(), "yyyy-MM-dd")));
        }
        // rectificationCheckPrincipal : 验收人
        if (!StringUtils.isEmpty(dto.getRectificationCheckPrincipal())) {
            lambdaQueryWrapper.like(SaqSafetyInspectionRectification::getRectificationCheckPrincipal, dto.getRectificationCheckPrincipal());
        }
        // rectificationFinishCondition : 整改完成情况
        if (!StringUtils.isEmpty(dto.getRectificationFinishCondition())) {
            lambdaQueryWrapper.eq(SaqSafetyInspectionRectification::getRectificationFinishCondition, dto.getRectificationFinishCondition());
        }
        // createTime : 创建时间
        if (!StringUtils.isEmpty(dto.getCreateTime())) {
            lambdaQueryWrapper.eq(SaqSafetyInspectionRectification::getCreateTime, dto.getCreateTime());
        }
        // createBy : 创建人
        if (!StringUtils.isEmpty(dto.getCreateBy())) {
            lambdaQueryWrapper.like(SaqSafetyInspectionRectification::getCreateBy, dto.getCreateBy());
        }
        // updateTime : 修改时间
        if (!StringUtils.isEmpty(dto.getUpdateTime())) {
            lambdaQueryWrapper.eq(SaqSafetyInspectionRectification::getUpdateTime, dto.getUpdateTime());
        }
        // updateBy : 修改人
        if (!StringUtils.isEmpty(dto.getUpdateBy())) {
            lambdaQueryWrapper.eq(SaqSafetyInspectionRectification::getUpdateBy, dto.getUpdateBy());
        }
        lambdaQueryWrapper.orderByDesc(SaqSafetyInspectionRectification::getCreateTime);
        lambdaQueryWrapper.eq(SaqSafetyInspectionRectification::getIsDel, 0);
        return lambdaQueryWrapper;
    }

}

