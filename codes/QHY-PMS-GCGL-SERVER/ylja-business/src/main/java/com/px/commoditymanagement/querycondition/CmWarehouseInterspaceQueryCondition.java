package com.px.commoditymanagement.querycondition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.commoditymanagement.bean.dto.CmWarehouseInterspaceQueryDto;
import com.px.commoditymanagement.entity.CmWarehouseInterspace;
import com.px.common.util.CollUtils;
import com.px.common.util.DateUtil;
import org.springframework.util.StringUtils;

/**
 * CmWarehouseInterspace 查询
 *
 * @author 品讯科技
 * @since 2023-12-28
 */
public class CmWarehouseInterspaceQueryCondition {

    /**
     * CmWarehouseInterspace使用lambda构建查询对象
     *
     * @param dto
     * @return
     */
    public static LambdaQueryWrapper<CmWarehouseInterspace> build(CmWarehouseInterspaceQueryDto dto) {
        LambdaQueryWrapper<CmWarehouseInterspace> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // id : 
        if (!StringUtils.isEmpty(dto.getId())) {
            lambdaQueryWrapper.eq(CmWarehouseInterspace::getId, dto.getId());
        }
        // projectCode : 项目编号
        if (!StringUtils.isEmpty(dto.getProjectCode())) {
            lambdaQueryWrapper.eq(CmWarehouseInterspace::getProjectCode, dto.getProjectCode());
        }
        // projectName : 项目名称
        if (!StringUtils.isEmpty(dto.getProjectName())) {
            lambdaQueryWrapper.like(CmWarehouseInterspace::getProjectName, dto.getProjectName());
        }
        // warehouseCode : 仓库编号
        if (!StringUtils.isEmpty(dto.getWarehouseCode())) {
            lambdaQueryWrapper.eq(CmWarehouseInterspace::getWarehouseCode, dto.getWarehouseCode());
        }
        if (CollUtils.isNotEmpty(dto.getWarehouseCodeList())) {
            lambdaQueryWrapper.in(CmWarehouseInterspace::getWarehouseCode, dto.getWarehouseCodeList());
        }
        // materialCode : 物资编码
        if (!StringUtils.isEmpty(dto.getMaterialCode())) {
            lambdaQueryWrapper.eq(CmWarehouseInterspace::getMaterialCode, dto.getMaterialCode());
        }
        if (CollUtils.isNotEmpty(dto.getMaterialCodeList())) {
            lambdaQueryWrapper.in(CmWarehouseInterspace::getMaterialCode, dto.getMaterialCodeList());
        }
        // materialName : 物资名称
        if (!StringUtils.isEmpty(dto.getMaterialName())) {
            lambdaQueryWrapper.like(CmWarehouseInterspace::getMaterialName, dto.getMaterialName());
        }
        // categoryCode : 物资分类编号
        if (!StringUtils.isEmpty(dto.getCategoryCode())) {
            lambdaQueryWrapper.eq(CmWarehouseInterspace::getCategoryCode, dto.getCategoryCode());
        }
        // categoryName : 物资分类
        if (!StringUtils.isEmpty(dto.getCategoryName())) {
            lambdaQueryWrapper.like(CmWarehouseInterspace::getCategoryName, dto.getCategoryName());
        }
        // model : 型号
        if (!StringUtils.isEmpty(dto.getModel())) {
            lambdaQueryWrapper.like(CmWarehouseInterspace::getModel, dto.getModel());
        }
        // specification : 规格
        if (!StringUtils.isEmpty(dto.getSpecification())) {
            lambdaQueryWrapper.like(CmWarehouseInterspace::getSpecification, dto.getSpecification());
        }
        // units : 计量单位
        if (!StringUtils.isEmpty(dto.getUnits())) {
            lambdaQueryWrapper.eq(CmWarehouseInterspace::getUnits, dto.getUnits());
        }
        // brand : 品牌
        if (!StringUtils.isEmpty(dto.getBrand())) {
            lambdaQueryWrapper.like(CmWarehouseInterspace::getBrand, dto.getBrand());
        }
        // num : 数量
        if (!StringUtils.isEmpty(dto.getNum())) {
            lambdaQueryWrapper.eq(CmWarehouseInterspace::getNum, dto.getNum());
        }
        // unitPriceBeforeTaxes : 单价（含税）
        if (!StringUtils.isEmpty(dto.getUnitPriceBeforeTaxes())) {
            lambdaQueryWrapper.eq(CmWarehouseInterspace::getUnitPriceBeforeTaxes, dto.getUnitPriceBeforeTaxes());
        }
        // unitPriceAfterTaxes : 单价（不含税）
        if (!StringUtils.isEmpty(dto.getUnitPriceAfterTaxes())) {
            lambdaQueryWrapper.eq(CmWarehouseInterspace::getUnitPriceAfterTaxes, dto.getUnitPriceAfterTaxes());
        }
        // totalPriceBeforeTaxes : 总金额（含税）
        if (!StringUtils.isEmpty(dto.getTotalPriceBeforeTaxes())) {
            lambdaQueryWrapper.eq(CmWarehouseInterspace::getTotalPriceBeforeTaxes, dto.getTotalPriceBeforeTaxes());
        }
        // totalPriceAfterTaxes : 总金额（不含税）
        if (!StringUtils.isEmpty(dto.getTotalPriceAfterTaxes())) {
            lambdaQueryWrapper.eq(CmWarehouseInterspace::getTotalPriceAfterTaxes, dto.getTotalPriceAfterTaxes());
        }
        // taxesRatio : 税率
        if (!StringUtils.isEmpty(dto.getTaxesRatio())) {
            lambdaQueryWrapper.eq(CmWarehouseInterspace::getTaxesRatio, dto.getTaxesRatio());
        }
        // taxesMoney : 税金
        if (!StringUtils.isEmpty(dto.getTaxesMoney())) {
            lambdaQueryWrapper.eq(CmWarehouseInterspace::getTaxesMoney, dto.getTaxesMoney());
        }
        // remark : 备注
        if (!StringUtils.isEmpty(dto.getRemark())) {
            lambdaQueryWrapper.eq(CmWarehouseInterspace::getRemark, dto.getRemark());
        }
        if (!StringUtils.isEmpty(dto.getCreateTimeStart()) && !StringUtils.isEmpty(dto.getCreateTimeEnd())) {
            lambdaQueryWrapper.ge(CmWarehouseInterspace::getCreateTime, DateUtil.getBeginOfDay(DateUtil.format(dto.getCreateTimeStart(), "yyyy-MM-dd")));
            lambdaQueryWrapper.le(CmWarehouseInterspace::getCreateTime, DateUtil.getEndOfDay(DateUtil.format(dto.getCreateTimeEnd(), "yyyy-MM-dd")));
        }
        return lambdaQueryWrapper;
    }

}

