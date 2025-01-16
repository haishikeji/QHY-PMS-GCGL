package com.px.resourcecenter.querycondition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.resourcecenter.bean.dto.RcProjectProgressQueryDto;
import com.px.resourcecenter.entity.RcProjectProgress;
import org.springframework.util.StringUtils;

/**
* RcProjectProgress 查询
*
* @author 品讯科技
* @since 2024-01-22
*/
public class RcProjectProgressQueryCondition {

/**
* RcProjectProgress使用lambda构建查询对象
* @param dto
* @return
*/
public static LambdaQueryWrapper<RcProjectProgress> build(RcProjectProgressQueryDto dto){
        LambdaQueryWrapper<RcProjectProgress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // id : 主键
        if(!StringUtils.isEmpty(dto.getId())){
            lambdaQueryWrapper.eq(RcProjectProgress::getId,dto.getId());
        }
        // projectCode : 项目编号
        if(!StringUtils.isEmpty(dto.getProjectCode())){
            lambdaQueryWrapper.eq(RcProjectProgress::getProjectCode,dto.getProjectCode());
        }
        // startTime : 开始时间
        if(!StringUtils.isEmpty(dto.getStartTime())){
            lambdaQueryWrapper.eq(RcProjectProgress::getStartTime,dto.getStartTime());
        }
        // endTime : 结束时间
        if(!StringUtils.isEmpty(dto.getEndTime())){
            lambdaQueryWrapper.eq(RcProjectProgress::getEndTime,dto.getEndTime());
        }
        // planProgress : 计划进度
        if(!StringUtils.isEmpty(dto.getPlanProgress())){
            lambdaQueryWrapper.eq(RcProjectProgress::getPlanProgress,dto.getPlanProgress());
        }
        // actualProgress : 实际进度
        if(!StringUtils.isEmpty(dto.getActualProgress())){
            lambdaQueryWrapper.eq(RcProjectProgress::getActualProgress,dto.getActualProgress());
        }
        // remark : 说明
        if(!StringUtils.isEmpty(dto.getRemark())){
            lambdaQueryWrapper.eq(RcProjectProgress::getRemark,dto.getRemark());
        }
        // createTime : 创建时间
        if(!StringUtils.isEmpty(dto.getCreateTime())){
            lambdaQueryWrapper.eq(RcProjectProgress::getCreateTime,dto.getCreateTime());
        }
        // createBy : 创建人
        if(!StringUtils.isEmpty(dto.getCreateBy())){
            lambdaQueryWrapper.eq(RcProjectProgress::getCreateBy,dto.getCreateBy());
        }
        // updateTime : 修改时间
        if(!StringUtils.isEmpty(dto.getUpdateTime())){
            lambdaQueryWrapper.eq(RcProjectProgress::getUpdateTime,dto.getUpdateTime());
        }
        // updateBy : 修改人
        if(!StringUtils.isEmpty(dto.getUpdateBy())){
            lambdaQueryWrapper.eq(RcProjectProgress::getUpdateBy,dto.getUpdateBy());
        }
        lambdaQueryWrapper.orderByDesc(RcProjectProgress::getCreateTime);
        lambdaQueryWrapper.eq(RcProjectProgress::getIsDel,0);
        return lambdaQueryWrapper;
}

}

