package com.px.purchasingmanagement.querycondition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.purchasingmanagement.bean.dto.PmPurchaseContractDetailQueryDto;
import com.px.purchasingmanagement.entity.PmPurchaseContractDetail;
import org.springframework.util.StringUtils;

/**
 * PmPurchaseContractDetail 查询
 *
 * @author 品讯科技
 * @since 2023-12-19
 */
public class PmPurchaseContractDetailQueryCondition {

    /**
     * PmPurchaseContractDetail使用lambda构建查询对象
     *
     * @param dto
     * @return
     */
    public static LambdaQueryWrapper<PmPurchaseContractDetail> build(PmPurchaseContractDetailQueryDto dto) {
        LambdaQueryWrapper<PmPurchaseContractDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // id : 主键
        if (!StringUtils.isEmpty(dto.getId())) {
            lambdaQueryWrapper.eq(PmPurchaseContractDetail::getId, dto.getId());
        }
        // purchaseContractCode : 采购合同编号
        if (!StringUtils.isEmpty(dto.getPurchaseContractCode())) {
            lambdaQueryWrapper.eq(PmPurchaseContractDetail::getPurchaseContractCode, dto.getPurchaseContractCode());
        }
        // materialCode : 物资编码
        if (!StringUtils.isEmpty(dto.getMaterialCode())) {
            lambdaQueryWrapper.eq(PmPurchaseContractDetail::getMaterialCode, dto.getMaterialCode());
        }
        // materialName : 物资名称
        if (!StringUtils.isEmpty(dto.getMaterialName())) {
            lambdaQueryWrapper.like(PmPurchaseContractDetail::getMaterialName, dto.getMaterialName());
        }
        // model : 型号
        if (!StringUtils.isEmpty(dto.getModel())) {
            lambdaQueryWrapper.like(PmPurchaseContractDetail::getModel, dto.getModel());
        }
        // specification : 规格
        if (!StringUtils.isEmpty(dto.getSpecification())) {
            lambdaQueryWrapper.like(PmPurchaseContractDetail::getSpecification, dto.getSpecification());
        }
        // units : 单位
        if (!StringUtils.isEmpty(dto.getUnits())) {
            lambdaQueryWrapper.eq(PmPurchaseContractDetail::getUnits, dto.getUnits());
        }
        // brand : 品牌
        if (!StringUtils.isEmpty(dto.getBrand())) {
            lambdaQueryWrapper.eq(PmPurchaseContractDetail::getBrand, dto.getBrand());
        }
        // num : 数量
        if (!StringUtils.isEmpty(dto.getNum())) {
            lambdaQueryWrapper.eq(PmPurchaseContractDetail::getNum, dto.getNum());
        }
        // unitPrice : 单价（含税）
        if (!StringUtils.isEmpty(dto.getUnitPrice())) {
            lambdaQueryWrapper.eq(PmPurchaseContractDetail::getUnitPrice, dto.getUnitPrice());
        }
        // taxesRatio : 税率
        if (!StringUtils.isEmpty(dto.getTaxesRatio())) {
            lambdaQueryWrapper.eq(PmPurchaseContractDetail::getTaxesRatio, dto.getTaxesRatio());
        }
        // totalPrice : 总金额（含税）
        if (!StringUtils.isEmpty(dto.getTotalPrice())) {
            lambdaQueryWrapper.eq(PmPurchaseContractDetail::getTotalPrice, dto.getTotalPrice());
        }
        // needArrivalDate : 需用到货时间
        if (!StringUtils.isEmpty(dto.getNeedArrivalDate())) {
            lambdaQueryWrapper.eq(PmPurchaseContractDetail::getNeedArrivalDate, dto.getNeedArrivalDate());
        }
        // requireArrivalDate : 采购要求到货时间
        if (!StringUtils.isEmpty(dto.getRequireArrivalDate())) {
            lambdaQueryWrapper.eq(PmPurchaseContractDetail::getRequireArrivalDate, dto.getRequireArrivalDate());
        }
        // remark : 备注
        if (!StringUtils.isEmpty(dto.getRemark())) {
            lambdaQueryWrapper.eq(PmPurchaseContractDetail::getRemark, dto.getRemark());
        }
        // theoryTotalWeight : 钢材理论总重量
        if (!StringUtils.isEmpty(dto.getTheoryTotalWeight())) {
            lambdaQueryWrapper.eq(PmPurchaseContractDetail::getTheoryTotalWeight, dto.getTheoryTotalWeight());
        }
        lambdaQueryWrapper.orderByAsc(PmPurchaseContractDetail::getId);
        return lambdaQueryWrapper;
    }

}

