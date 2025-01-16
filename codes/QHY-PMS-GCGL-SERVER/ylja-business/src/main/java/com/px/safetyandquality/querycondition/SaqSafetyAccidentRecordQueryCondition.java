package com.px.safetyandquality.querycondition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.common.util.CollUtils;
import com.px.safetyandquality.bean.dto.SaqSafetyAccidentRecordQueryDto;
import com.px.safetyandquality.entity.SaqSafetyAccidentRecord;
import org.springframework.util.StringUtils;

/**
 * SaqSafetyAccidentRecord 查询
 *
 * @author 品讯科技
 * @since 2023-12-24
 */
public class SaqSafetyAccidentRecordQueryCondition {

    /**
     * SaqSafetyAccidentRecord使用lambda构建查询对象
     *
     * @param dto
     * @return
     */
    public static LambdaQueryWrapper<SaqSafetyAccidentRecord> build(SaqSafetyAccidentRecordQueryDto dto) {
        LambdaQueryWrapper<SaqSafetyAccidentRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // id : 
        if (!StringUtils.isEmpty(dto.getId())) {
            lambdaQueryWrapper.eq(SaqSafetyAccidentRecord::getId, dto.getId());
        }
        if (!StringUtils.isEmpty(dto.getDataType())) {
            lambdaQueryWrapper.eq(SaqSafetyAccidentRecord::getDataType, dto.getDataType());
        }
        // sarCode : 安全事故汇报编号
        if (!StringUtils.isEmpty(dto.getSarCode())) {
            lambdaQueryWrapper.eq(SaqSafetyAccidentRecord::getSarCode, dto.getSarCode());
        }
        // depCode : 部门编号
        if (CollUtils.isNotEmpty(dto.getDepCodeList())) {
            lambdaQueryWrapper.in(SaqSafetyAccidentRecord::getDepCode, dto.getDepCodeList());
        }
        // projectName : 项目名称
        if (!StringUtils.isEmpty(dto.getProjectName())) {
            lambdaQueryWrapper.like(SaqSafetyAccidentRecord::getProjectName, dto.getProjectName());
        }
        // projectCode : 项目编号
        if (!StringUtils.isEmpty(dto.getProjectCode())) {
            lambdaQueryWrapper.eq(SaqSafetyAccidentRecord::getProjectCode, dto.getProjectCode());
        }
        // accidentName : 事故名称
        if (!StringUtils.isEmpty(dto.getAccidentName())) {
            lambdaQueryWrapper.eq(SaqSafetyAccidentRecord::getAccidentName, dto.getAccidentName());
        }
        // accidentDate : 事故日期
        if (!StringUtils.isEmpty(dto.getAccidentDateStart()) && !StringUtils.isEmpty(dto.getAccidentDateEnd())) {
            lambdaQueryWrapper.ge(SaqSafetyAccidentRecord::getAccidentDate, dto.getAccidentDateStart());
            lambdaQueryWrapper.le(SaqSafetyAccidentRecord::getAccidentDate, dto.getAccidentDateEnd());
        }
        if (!StringUtils.isEmpty(dto.getCreateTimeStart()) && !StringUtils.isEmpty(dto.getCreateTimeEnd())) {
            lambdaQueryWrapper.ge(SaqSafetyAccidentRecord::getAccidentDate, dto.getCreateTimeStart());
            lambdaQueryWrapper.le(SaqSafetyAccidentRecord::getAccidentDate, dto.getCreateTimeEnd());
        }
        // accidentLevel : 事故等级
        if (!StringUtils.isEmpty(dto.getAccidentLevel())) {
            lambdaQueryWrapper.eq(SaqSafetyAccidentRecord::getAccidentLevel, dto.getAccidentLevel());
        }
        // accidentAddress : 事故地点
        if (!StringUtils.isEmpty(dto.getAccidentAddress())) {
            lambdaQueryWrapper.eq(SaqSafetyAccidentRecord::getAccidentAddress, dto.getAccidentAddress());
        }
        // accidentSceneCondition : 事故现场情况
        if (!StringUtils.isEmpty(dto.getAccidentSceneCondition())) {
            lambdaQueryWrapper.eq(SaqSafetyAccidentRecord::getAccidentSceneCondition, dto.getAccidentSceneCondition());
        }
        // accidentDesc : 事故简要经过
        if (!StringUtils.isEmpty(dto.getAccidentDesc())) {
            lambdaQueryWrapper.eq(SaqSafetyAccidentRecord::getAccidentDesc, dto.getAccidentDesc());
        }
        // accidentCasualties : 事故伤亡情况
        if (!StringUtils.isEmpty(dto.getAccidentCasualties())) {
            lambdaQueryWrapper.eq(SaqSafetyAccidentRecord::getAccidentCasualties, dto.getAccidentCasualties());
        }
        // tentativeFinancialLoss : 初步经济损失
        if (!StringUtils.isEmpty(dto.getTentativeFinancialLoss())) {
            lambdaQueryWrapper.eq(SaqSafetyAccidentRecord::getTentativeFinancialLoss, dto.getTentativeFinancialLoss());
        }
        // accidentMeasure : 措施
        if (!StringUtils.isEmpty(dto.getAccidentMeasure())) {
            lambdaQueryWrapper.eq(SaqSafetyAccidentRecord::getAccidentMeasure, dto.getAccidentMeasure());
        }
        // createTime : 创建时间
        if (!StringUtils.isEmpty(dto.getCreateTime())) {
            lambdaQueryWrapper.eq(SaqSafetyAccidentRecord::getCreateTime, dto.getCreateTime());
        }
        // createBy : 创建人
        if (!StringUtils.isEmpty(dto.getCreateBy())) {
            lambdaQueryWrapper.like(SaqSafetyAccidentRecord::getCreateBy, dto.getCreateBy());
        }
        // updateTime : 修改时间
        if (!StringUtils.isEmpty(dto.getUpdateTime())) {
            lambdaQueryWrapper.eq(SaqSafetyAccidentRecord::getUpdateTime, dto.getUpdateTime());
        }
        // updateBy : 修改人
        if (!StringUtils.isEmpty(dto.getUpdateBy())) {
            lambdaQueryWrapper.like(SaqSafetyAccidentRecord::getUpdateBy, dto.getUpdateBy());
        }
        lambdaQueryWrapper.orderByDesc(SaqSafetyAccidentRecord::getCreateTime);
        lambdaQueryWrapper.eq(SaqSafetyAccidentRecord::getIsDel, 0);
        return lambdaQueryWrapper;
    }

}

