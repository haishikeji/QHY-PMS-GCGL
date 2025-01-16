package com.px.purchasingmanagement.querycondition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.common.util.CollUtils;
import com.px.purchasingmanagement.bean.dto.PmSubpackageContractQueryDto;
import com.px.purchasingmanagement.entity.PmSubpackageContract;
import org.springframework.util.StringUtils;

/**
 * PmSubpackageContract 查询
 *
 * @author 品讯科技
 * @since 2023-12-19
 */
public class PmSubpackageContractQueryCondition {

    /**
     * PmSubpackageContract使用lambda构建查询对象
     *
     * @param dto
     * @return
     */
    public static LambdaQueryWrapper<PmSubpackageContract> build(PmSubpackageContractQueryDto dto) {
        LambdaQueryWrapper<PmSubpackageContract> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // id : 主键
        if (!StringUtils.isEmpty(dto.getId())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getId, dto.getId());
        }
        // subpackageContractCode : 分包合同编号
        if (!StringUtils.isEmpty(dto.getSubpackageContractCode())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getSubpackageContractCode, dto.getSubpackageContractCode());
        }
        // depCode : 部门编号
        if (CollUtils.isNotEmpty(dto.getDepCodeList())) {
            lambdaQueryWrapper.in(PmSubpackageContract::getDepCode, dto.getDepCodeList());
        }
        // projectCode : 项目编号
        if (!StringUtils.isEmpty(dto.getProjectCode())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getProjectCode, dto.getProjectCode());
        }
        // projectName : 项目名称
        if (!StringUtils.isEmpty(dto.getProjectName())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getProjectName, dto.getProjectName());
        }
        // engineeringPlanCode : 需用计划编号
        if (!StringUtils.isEmpty(dto.getEngineeringPlanCode())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getEngineeringPlanCode, dto.getEngineeringPlanCode());
        }
        // supplierCode : 供应商编号
        if (!StringUtils.isEmpty(dto.getSupplierCode())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getSupplierCode, dto.getSupplierCode());
        }
        // constructionBudgetCode : 施工预算编号(预算成本科目编号)
        if (!StringUtils.isEmpty(dto.getConstructionBudgetDetailId())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getConstructionBudgetDetailId, dto.getConstructionBudgetDetailId());
        }
        // signDate : 签约时间
        if (!StringUtils.isEmpty(dto.getSignDate())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getSignDate, dto.getSignDate());
        }
        // signdSecurityProtocol : 是否签订安全协议：1.是，0.否
        if (!StringUtils.isEmpty(dto.getSigndSecurityProtocol())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getSigndSecurityProtocol, dto.getSigndSecurityProtocol());
        }
        // paymentClause : 付款条款
        if (!StringUtils.isEmpty(dto.getPaymentClause())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getPaymentClause, dto.getPaymentClause());
        }
        // remark : 备注
        if (!StringUtils.isEmpty(dto.getRemark())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getRemark, dto.getRemark());
        }
        // subpackageMoneyBeforeTaxes : 分包金额（含税）
        if (!StringUtils.isEmpty(dto.getSubpackageMoneyBeforeTaxes())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getSubpackageMoneyBeforeTaxes, dto.getSubpackageMoneyBeforeTaxes());
        }
        // subpackageMoneyAfterTaxes : 分包金额（不含税）
        if (!StringUtils.isEmpty(dto.getSubpackageMoneyAfterTaxes())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getSubpackageMoneyAfterTaxes, dto.getSubpackageMoneyAfterTaxes());
        }
        // taxesRatio : 税率
        if (!StringUtils.isEmpty(dto.getTaxesRatio())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getTaxesRatio, dto.getTaxesRatio());
        }
        // taxesMoney : 税金
        if (!StringUtils.isEmpty(dto.getTaxesMoney())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getTaxesMoney, dto.getTaxesMoney());
        }
        // invoiceType : 发票类型
        if (!StringUtils.isEmpty(dto.getInvoiceType())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getInvoiceType, dto.getInvoiceType());
        }
        // peggedPrice : 控制价（不含税）
        if (!StringUtils.isEmpty(dto.getPeggedPrice())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getPeggedPrice, dto.getPeggedPrice());
        }
        // status : 生效状态：0.未生效，1.已生效
        if (!StringUtils.isEmpty(dto.getStatus())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getStatus, dto.getStatus());
        }
        // flowStatus : 表单状态：1.已创建，2.审核中，3.已驳回（审核拒绝），4.审核通过，5.已弃审
        if (!StringUtils.isEmpty(dto.getFlowStatus())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getFlowStatus, dto.getFlowStatus());
        }
        // flowCode : 流程编号
        if (!StringUtils.isEmpty(dto.getFlowCode())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getFlowCode, dto.getFlowCode());
        }
        // createTime : 创建时间
        if (!StringUtils.isEmpty(dto.getCreateTime())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getCreateTime, dto.getCreateTime());
        }
        // createBy : 创建人
        if (!StringUtils.isEmpty(dto.getCreateBy())) {
            lambdaQueryWrapper.like(PmSubpackageContract::getCreateBy, dto.getCreateBy());
        }
        // updateTime : 修改时间
        if (!StringUtils.isEmpty(dto.getUpdateTime())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getUpdateTime, dto.getUpdateTime());
        }
        // updateBy : 修改人
        if (!StringUtils.isEmpty(dto.getUpdateBy())) {
            lambdaQueryWrapper.eq(PmSubpackageContract::getUpdateBy, dto.getUpdateBy());
        }
        lambdaQueryWrapper.orderByDesc(PmSubpackageContract::getCreateTime);
        lambdaQueryWrapper.eq(PmSubpackageContract::getIsDel, 0);
        return lambdaQueryWrapper;
    }

}

