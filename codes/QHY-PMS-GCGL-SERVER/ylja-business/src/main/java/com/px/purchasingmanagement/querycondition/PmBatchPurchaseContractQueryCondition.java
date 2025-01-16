package com.px.purchasingmanagement.querycondition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.common.util.CollUtils;
import com.px.common.util.DateUtil;
import com.px.purchasingmanagement.bean.dto.PmBatchPurchaseContractQueryDto;
import com.px.purchasingmanagement.entity.PmBatchPurchaseContract;
import org.springframework.util.StringUtils;

/**
 * PmBatchPurchaseContract 查询
 *
 * @author 品讯科技
 * @since 2024-02-21
 */
public class PmBatchPurchaseContractQueryCondition {

    /**
     * PmBatchPurchaseContract使用lambda构建查询对象
     *
     * @param dto
     * @return
     */
    public static LambdaQueryWrapper<PmBatchPurchaseContract> build(PmBatchPurchaseContractQueryDto dto) {
        LambdaQueryWrapper<PmBatchPurchaseContract> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // id : 主键
        if (!StringUtils.isEmpty(dto.getId())) {
            lambdaQueryWrapper.eq(PmBatchPurchaseContract::getId, dto.getId());
        }
        // batchPurchaseContractCode : 集采合同编号
        if (!StringUtils.isEmpty(dto.getBatchPurchaseContractCode())) {
            lambdaQueryWrapper.eq(PmBatchPurchaseContract::getBatchPurchaseContractCode, dto.getBatchPurchaseContractCode());
        }
        if (CollUtils.isNotEmpty(dto.getBatchPurchaseContractCodeList())) {
            lambdaQueryWrapper.in(PmBatchPurchaseContract::getBatchPurchaseContractCode, dto.getBatchPurchaseContractCodeList());
        }
        // depCode : 部门编号
        if (CollUtils.isNotEmpty(dto.getDepCodeList())) {
            lambdaQueryWrapper.in(PmBatchPurchaseContract::getDepCode, dto.getDepCodeList());
        }
        // paymentClause : 付款条款
        if (!StringUtils.isEmpty(dto.getPaymentClause())) {
            lambdaQueryWrapper.eq(PmBatchPurchaseContract::getPaymentClause, dto.getPaymentClause());
        }
        // supplierCode : 供应商编号
        if (!StringUtils.isEmpty(dto.getSupplierCode())) {
            lambdaQueryWrapper.eq(PmBatchPurchaseContract::getSupplierCode, dto.getSupplierCode());
        }
        // contractType : 合同类型1.单次合同，2.框架合同
        if (!StringUtils.isEmpty(dto.getContractType())) {
            lambdaQueryWrapper.eq(PmBatchPurchaseContract::getContractType, dto.getContractType());
        }
        // orderTime : 下单时间
        if (!StringUtils.isEmpty(dto.getOrderTime())) {
            lambdaQueryWrapper.eq(PmBatchPurchaseContract::getOrderTime, dto.getOrderTime());
        }
        // remark : 备注
        if (!StringUtils.isEmpty(dto.getRemark())) {
            lambdaQueryWrapper.eq(PmBatchPurchaseContract::getRemark, dto.getRemark());
        }
        // invoiceType : 发票类型
        if (!StringUtils.isEmpty(dto.getInvoiceType())) {
            lambdaQueryWrapper.eq(PmBatchPurchaseContract::getInvoiceType, dto.getInvoiceType());
        }
        // orderMoneyBeforeTaxes : 订单金额（含税）
        if (!StringUtils.isEmpty(dto.getOrderMoneyBeforeTaxes())) {
            lambdaQueryWrapper.eq(PmBatchPurchaseContract::getOrderMoneyBeforeTaxes, dto.getOrderMoneyBeforeTaxes());
        }
        // orderMoneyAfterTaxes : 订单金额（不含税）
        if (!StringUtils.isEmpty(dto.getOrderMoneyAfterTaxes())) {
            lambdaQueryWrapper.eq(PmBatchPurchaseContract::getOrderMoneyAfterTaxes, dto.getOrderMoneyAfterTaxes());
        }
        // taxesRatio : 税率
        if (!StringUtils.isEmpty(dto.getTaxesRatio())) {
            lambdaQueryWrapper.eq(PmBatchPurchaseContract::getTaxesRatio, dto.getTaxesRatio());
        }
        // taxesMoney : 税金
        if (!StringUtils.isEmpty(dto.getTaxesMoney())) {
            lambdaQueryWrapper.eq(PmBatchPurchaseContract::getTaxesMoney, dto.getTaxesMoney());
        }
        // status : 生效状态：0.未生效，1.已生效
        if (!StringUtils.isEmpty(dto.getStatus())) {
            lambdaQueryWrapper.eq(PmBatchPurchaseContract::getStatus, dto.getStatus());
        }
        // flowStatus : 状态：0.待审批，1.审批通过，2.审批未通过
        if (!StringUtils.isEmpty(dto.getFlowStatus())) {
            lambdaQueryWrapper.eq(PmBatchPurchaseContract::getFlowStatus, dto.getFlowStatus());
        }
        // flowCode : 流程编号
        if (!StringUtils.isEmpty(dto.getFlowCode())) {
            lambdaQueryWrapper.eq(PmBatchPurchaseContract::getFlowCode, dto.getFlowCode());
        }
        // createTime : 创建时间
        if (!StringUtils.isEmpty(dto.getCreateTimeStart()) && !StringUtils.isEmpty(dto.getCreateTimeEnd())) {
            lambdaQueryWrapper.ge(PmBatchPurchaseContract::getCreateTime, DateUtil.getBeginOfDay(DateUtil.format(dto.getCreateTimeStart(),"yyyy-MM-dd")));
            lambdaQueryWrapper.le(PmBatchPurchaseContract::getCreateTime, DateUtil.getEndOfDay(DateUtil.format(dto.getCreateTimeEnd(),"yyyy-MM-dd")));
        }
        // createBy : 创建人
        if (!StringUtils.isEmpty(dto.getCreateBy())) {
            lambdaQueryWrapper.like(PmBatchPurchaseContract::getCreateBy, dto.getCreateBy());
        }
        // updateTime : 修改时间
        if (!StringUtils.isEmpty(dto.getUpdateTime())) {
            lambdaQueryWrapper.eq(PmBatchPurchaseContract::getUpdateTime, dto.getUpdateTime());
        }
        // updateBy : 修改人
        if (!StringUtils.isEmpty(dto.getUpdateBy())) {
            lambdaQueryWrapper.like(PmBatchPurchaseContract::getUpdateBy, dto.getUpdateBy());
        }
        lambdaQueryWrapper.orderByDesc(PmBatchPurchaseContract::getCreateTime);
        lambdaQueryWrapper.eq(PmBatchPurchaseContract::getIsDel, 0);
        return lambdaQueryWrapper;
    }

}

