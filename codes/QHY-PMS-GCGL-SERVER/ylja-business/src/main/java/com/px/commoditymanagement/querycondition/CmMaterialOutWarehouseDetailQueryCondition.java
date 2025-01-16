package com.px.commoditymanagement.querycondition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.util.StringUtils;
import com.px.commoditymanagement.bean.dto.CmMaterialOutWarehouseDetailQueryDto;
import com.px.commoditymanagement.entity.CmMaterialOutWarehouseDetail;

/**
* CmMaterialOutWarehouseDetail 查询
*
* @author 品讯科技
* @since 2023-12-28
*/
public class CmMaterialOutWarehouseDetailQueryCondition {

/**
* CmMaterialOutWarehouseDetail使用lambda构建查询对象
* @param dto
* @return
*/
public static LambdaQueryWrapper<CmMaterialOutWarehouseDetail> build(CmMaterialOutWarehouseDetailQueryDto dto){
        LambdaQueryWrapper<CmMaterialOutWarehouseDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // id : 
        if(!StringUtils.isEmpty(dto.getId())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getId,dto.getId());
        }
        // materialOutWarehouseCode : 领料出库编号
        if(!StringUtils.isEmpty(dto.getMaterialOutWarehouseCode())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getMaterialOutWarehouseCode,dto.getMaterialOutWarehouseCode());
        }
        // warehouseCode : 仓库编号
        if(!StringUtils.isEmpty(dto.getWarehouseCode())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getWarehouseCode,dto.getWarehouseCode());
        }
        // materialCode : 物资编码
        if(!StringUtils.isEmpty(dto.getMaterialCode())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getMaterialCode,dto.getMaterialCode());
        }
        // materialName : 物资名称
        if(!StringUtils.isEmpty(dto.getMaterialName())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getMaterialName,dto.getMaterialName());
        }
        // categoryCode : 物资分类编号
        if(!StringUtils.isEmpty(dto.getCategoryCode())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getCategoryCode,dto.getCategoryCode());
        }
        // categoryName : 物资分类
        if(!StringUtils.isEmpty(dto.getCategoryName())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getCategoryName,dto.getCategoryName());
        }
        // specification : 规格
        if(!StringUtils.isEmpty(dto.getSpecification())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getSpecification,dto.getSpecification());
        }
        // model : 型号
        if(!StringUtils.isEmpty(dto.getModel())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getModel,dto.getModel());
        }
        // brand : 品牌
        if(!StringUtils.isEmpty(dto.getBrand())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getBrand,dto.getBrand());
        }
        // units : 计量单位
        if(!StringUtils.isEmpty(dto.getUnits())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getUnits,dto.getUnits());
        }
        // num : 领料前库存数量
        if(!StringUtils.isEmpty(dto.getNum())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getNum,dto.getNum());
        }
        // takeNum : 领料数量
        if(!StringUtils.isEmpty(dto.getTakeNum())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getTakeNum,dto.getTakeNum());
        }
        // leftNum : 剩余库存数量
        if(!StringUtils.isEmpty(dto.getLeftNum())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getLeftNum,dto.getLeftNum());
        }
        // unitPriceAfterTaxes : 单价（不含税）
        if(!StringUtils.isEmpty(dto.getUnitPriceAfterTaxes())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getUnitPriceAfterTaxes,dto.getUnitPriceAfterTaxes());
        }
        // totalPriceBeforeTaxes : 总金额（含税）
        if(!StringUtils.isEmpty(dto.getTotalPriceBeforeTaxes())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getTotalPriceBeforeTaxes,dto.getTotalPriceBeforeTaxes());
        }
        // totalPriceAfterTaxes : 总金额（不含税）
        if(!StringUtils.isEmpty(dto.getTotalPriceAfterTaxes())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getTotalPriceAfterTaxes,dto.getTotalPriceAfterTaxes());
        }
        // purpose : 用途
        if(!StringUtils.isEmpty(dto.getPurpose())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getPurpose,dto.getPurpose());
        }
        // remark : 备注
        if(!StringUtils.isEmpty(dto.getRemark())){
            lambdaQueryWrapper.eq(CmMaterialOutWarehouseDetail::getRemark,dto.getRemark());
        }
        return lambdaQueryWrapper;
}

}

