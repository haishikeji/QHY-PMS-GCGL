package com.px.safetyandquality.querycondition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.common.util.CollUtils;
import com.px.safetyandquality.bean.dto.SaqSafetyTrainingQueryDto;
import com.px.safetyandquality.entity.SaqSafetyTraining;
import org.springframework.util.StringUtils;

/**
 * SaqSafetyTraining 查询
 *
 * @author 品讯科技
 * @since 2023-12-24
 */
public class SaqSafetyTrainingQueryCondition {

    /**
     * SaqSafetyTraining使用lambda构建查询对象
     *
     * @param dto
     * @return
     */
    public static LambdaQueryWrapper<SaqSafetyTraining> build(SaqSafetyTrainingQueryDto dto) {
        LambdaQueryWrapper<SaqSafetyTraining> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // id : 
        if (!StringUtils.isEmpty(dto.getId())) {
            lambdaQueryWrapper.eq(SaqSafetyTraining::getId, dto.getId());
        }
        if (!StringUtils.isEmpty(dto.getDataType())) {
            lambdaQueryWrapper.eq(SaqSafetyTraining::getDataType, dto.getDataType());
        }
        // safetyTrainingCode : 单据编号
        if (!StringUtils.isEmpty(dto.getSafetyTrainingCode())) {
            lambdaQueryWrapper.eq(SaqSafetyTraining::getSafetyTrainingCode, dto.getSafetyTrainingCode());
        }
        // depCode : 部门编号
        if (CollUtils.isNotEmpty(dto.getDepCodeList())) {
            lambdaQueryWrapper.in(SaqSafetyTraining::getDepCode, dto.getDepCodeList());
        }
        // projectName : 项目名称
        if (!StringUtils.isEmpty(dto.getProjectName())) {
            lambdaQueryWrapper.like(SaqSafetyTraining::getProjectName, dto.getProjectName());
        }
        // projectCode : 项目编号
        if (!StringUtils.isEmpty(dto.getProjectCode())) {
            lambdaQueryWrapper.eq(SaqSafetyTraining::getProjectCode, dto.getProjectCode());
        }
        // keynoteSpeaker : 主讲人
        if (!StringUtils.isEmpty(dto.getKeynoteSpeaker())) {
            lambdaQueryWrapper.eq(SaqSafetyTraining::getKeynoteSpeaker, dto.getKeynoteSpeaker());
        }
        // trainingType : 培训类型：1.三级安全教育，2.入场安全培训，3.专项安全培训
        if (!StringUtils.isEmpty(dto.getTrainingType())) {
            lambdaQueryWrapper.eq(SaqSafetyTraining::getTrainingType, dto.getTrainingType());
        }
        // trainingContent : 培训内容
        if (!StringUtils.isEmpty(dto.getTrainingContent())) {
            lambdaQueryWrapper.eq(SaqSafetyTraining::getTrainingContent, dto.getTrainingContent());
        }
        // trainingAddress : 培训地点
        if (!StringUtils.isEmpty(dto.getTrainingAddress())) {
            lambdaQueryWrapper.eq(SaqSafetyTraining::getTrainingAddress, dto.getTrainingAddress());
        }
        // trainingDate : 培训时间
        if (!StringUtils.isEmpty(dto.getTrainingDate())) {
            lambdaQueryWrapper.eq(SaqSafetyTraining::getTrainingDate, dto.getTrainingDate());
        }
        // checkType : 考核方式：1.口试，2.笔试，3.现场演练，4.现场操作
        if (!StringUtils.isEmpty(dto.getCheckType())) {
            lambdaQueryWrapper.eq(SaqSafetyTraining::getCheckType, dto.getCheckType());
        }
        // remark : 备注
        if (!StringUtils.isEmpty(dto.getRemark())) {
            lambdaQueryWrapper.eq(SaqSafetyTraining::getRemark, dto.getRemark());
        }
        // createTime : 创建时间
        if (!StringUtils.isEmpty(dto.getCreateTime())) {
            lambdaQueryWrapper.eq(SaqSafetyTraining::getCreateTime, dto.getCreateTime());
        }
        // createBy : 创建人
        if (!StringUtils.isEmpty(dto.getCreateBy())) {
            lambdaQueryWrapper.like(SaqSafetyTraining::getCreateBy, dto.getCreateBy());
        }
        // updateTime : 修改时间
        if (!StringUtils.isEmpty(dto.getUpdateTime())) {
            lambdaQueryWrapper.eq(SaqSafetyTraining::getUpdateTime, dto.getUpdateTime());
        }
        // updateBy : 修改人
        if (!StringUtils.isEmpty(dto.getUpdateBy())) {
            lambdaQueryWrapper.like(SaqSafetyTraining::getUpdateBy, dto.getUpdateBy());
        }
        lambdaQueryWrapper.orderByDesc(SaqSafetyTraining::getCreateTime);
        lambdaQueryWrapper.eq(SaqSafetyTraining::getIsDel, 0);
        return lambdaQueryWrapper;
    }

}

