package com.px.system.querycondition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.util.StringUtils;
import com.px.system.bean.dto.SysBusinessKeyQueryDto;
import com.px.system.entity.SysBusinessKey;

/**
* SysBusinessKey 查询
*
* @author 品讯科技
* @since 2024-01-18
*/
public class SysBusinessKeyQueryCondition {

/**
* SysBusinessKey使用lambda构建查询对象
* @param dto
* @return
*/
public static LambdaQueryWrapper<SysBusinessKey> build(SysBusinessKeyQueryDto dto){
        LambdaQueryWrapper<SysBusinessKey> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // id : 
        if(!StringUtils.isEmpty(dto.getId())){
            lambdaQueryWrapper.eq(SysBusinessKey::getId,dto.getId());
        }
        // businessKey : 业务标识
        if(!StringUtils.isEmpty(dto.getBusinessKey())){
            lambdaQueryWrapper.eq(SysBusinessKey::getBusinessKey,dto.getBusinessKey());
        }
        // businessName : 业务名称
        if(!StringUtils.isEmpty(dto.getBusinessName())){
            lambdaQueryWrapper.eq(SysBusinessKey::getBusinessName,dto.getBusinessName());
        }
        lambdaQueryWrapper.eq(SysBusinessKey::getIsDel,0);
        return lambdaQueryWrapper;
}

}

