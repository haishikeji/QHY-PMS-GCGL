package com.px.engineeringsupervision.querycondition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.util.StringUtils;
import com.px.engineeringsupervision.bean.dto.EsContractSupplementalAgreementQueryDto;
import com.px.engineeringsupervision.entity.EsContractSupplementalAgreement;

/**
* EsContractSupplementalAgreement 查询
*
* @author 品讯科技
* @since 2023-12-18
*/
public class EsContractSupplementalAgreementQueryCondition {

/**
* EsContractSupplementalAgreement使用lambda构建查询对象
* @param dto
* @return
*/
public static LambdaQueryWrapper<EsContractSupplementalAgreement> build(EsContractSupplementalAgreementQueryDto dto){
        LambdaQueryWrapper<EsContractSupplementalAgreement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // id : 主键
        if(!StringUtils.isEmpty(dto.getId())){
            lambdaQueryWrapper.eq(EsContractSupplementalAgreement::getId,dto.getId());
        }
        // engineeringContractCode : 工程合同编号
        if(!StringUtils.isEmpty(dto.getEngineeringContractCode())){
            lambdaQueryWrapper.eq(EsContractSupplementalAgreement::getEngineeringContractCode,dto.getEngineeringContractCode());
        }
        // customerCode : 客户编号
        if(!StringUtils.isEmpty(dto.getCustomerCode())){
            lambdaQueryWrapper.eq(EsContractSupplementalAgreement::getCustomerCode,dto.getCustomerCode());
        }
        // customerName : 客户名称
        if(!StringUtils.isEmpty(dto.getCustomerName())){
            lambdaQueryWrapper.eq(EsContractSupplementalAgreement::getCustomerName,dto.getCustomerName());
        }
        // supplementalAgreementCode : 补充协议编号
        if(!StringUtils.isEmpty(dto.getSupplementalAgreementCode())){
            lambdaQueryWrapper.eq(EsContractSupplementalAgreement::getSupplementalAgreementCode,dto.getSupplementalAgreementCode());
        }
        // supplementalAgreementName : 补充协议名称
        if(!StringUtils.isEmpty(dto.getSupplementalAgreementName())){
            lambdaQueryWrapper.eq(EsContractSupplementalAgreement::getSupplementalAgreementName,dto.getSupplementalAgreementName());
        }
        // responsibleUserCode : 经办人编号
        if(!StringUtils.isEmpty(dto.getResponsibleUserCode())){
            lambdaQueryWrapper.eq(EsContractSupplementalAgreement::getResponsibleUserCode,dto.getResponsibleUserCode());
        }
        // signDate : 签约日期
        if(!StringUtils.isEmpty(dto.getSignDate())){
            lambdaQueryWrapper.eq(EsContractSupplementalAgreement::getSignDate,dto.getSignDate());
        }
        // agreementMoneyBeforeTaxes : 协议金额（含税）
        if(!StringUtils.isEmpty(dto.getAgreementMoneyBeforeTaxes())){
            lambdaQueryWrapper.eq(EsContractSupplementalAgreement::getAgreementMoneyBeforeTaxes,dto.getAgreementMoneyBeforeTaxes());
        }
        // agreementMoneyAfterTaxes : 协议金额（不含税）
        if(!StringUtils.isEmpty(dto.getAgreementMoneyAfterTaxes())){
            lambdaQueryWrapper.eq(EsContractSupplementalAgreement::getAgreementMoneyAfterTaxes,dto.getAgreementMoneyAfterTaxes());
        }
        // taxesRatio : 税率
        if(!StringUtils.isEmpty(dto.getTaxesRatio())){
            lambdaQueryWrapper.eq(EsContractSupplementalAgreement::getTaxesRatio,dto.getTaxesRatio());
        }
        // taxesMoney : 税金
        if(!StringUtils.isEmpty(dto.getTaxesMoney())){
            lambdaQueryWrapper.eq(EsContractSupplementalAgreement::getTaxesMoney,dto.getTaxesMoney());
        }
        // remark : 备注
        if(!StringUtils.isEmpty(dto.getRemark())){
            lambdaQueryWrapper.eq(EsContractSupplementalAgreement::getRemark,dto.getRemark());
        }
        return lambdaQueryWrapper;
}

}

