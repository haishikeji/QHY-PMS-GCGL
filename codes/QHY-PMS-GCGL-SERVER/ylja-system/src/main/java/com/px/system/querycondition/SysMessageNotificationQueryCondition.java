package com.px.system.querycondition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.util.StringUtils;
import com.px.system.bean.dto.SysMessageNotificationQueryDto;
import com.px.system.entity.SysMessageNotification;

/**
* SysMessageNotification 查询
*
* @author 品讯科技
* @since 2023-12-10
*/
public class SysMessageNotificationQueryCondition {

/**
* SysMessageNotification使用lambda构建查询对象
* @param dto
* @return
*/
public static LambdaQueryWrapper<SysMessageNotification> build(SysMessageNotificationQueryDto dto){
        LambdaQueryWrapper<SysMessageNotification> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // id : ID
        if(!StringUtils.isEmpty(dto.getId())){
            lambdaQueryWrapper.eq(SysMessageNotification::getId,dto.getId());
        }
        // type : 消息类型：
        if(!StringUtils.isEmpty(dto.getType())){
            lambdaQueryWrapper.eq(SysMessageNotification::getType,dto.getType());
        }
        // title : 标题
        if(!StringUtils.isEmpty(dto.getTitle())){
            lambdaQueryWrapper.like(SysMessageNotification::getTitle,dto.getTitle());
        }
        // content : 内容
        if(!StringUtils.isEmpty(dto.getContent())){
            lambdaQueryWrapper.eq(SysMessageNotification::getContent,dto.getContent());
        }
        // skipUrl : 跳转链接
        if(!StringUtils.isEmpty(dto.getSkipUrl())){
            lambdaQueryWrapper.eq(SysMessageNotification::getSkipUrl,dto.getSkipUrl());
        }
        // dataId : 数据id
        if(!StringUtils.isEmpty(dto.getDataId())){
            lambdaQueryWrapper.eq(SysMessageNotification::getDataId,dto.getDataId());
        }
        // userId : 接收用户id
        if(!StringUtils.isEmpty(dto.getUserId())){
            lambdaQueryWrapper.eq(SysMessageNotification::getUserId,dto.getUserId());
        }
        // isRead : 已读标识：0.否，1.是
        if(!StringUtils.isEmpty(dto.getIsRead())){
            lambdaQueryWrapper.eq(SysMessageNotification::getIsRead,dto.getIsRead());
        }
        // createTime : 创建时间
        if(!StringUtils.isEmpty(dto.getCreateTime())){
            lambdaQueryWrapper.eq(SysMessageNotification::getCreateTime,dto.getCreateTime());
        }
        lambdaQueryWrapper.orderByDesc(SysMessageNotification::getCreateTime);
        return lambdaQueryWrapper;
}

}

