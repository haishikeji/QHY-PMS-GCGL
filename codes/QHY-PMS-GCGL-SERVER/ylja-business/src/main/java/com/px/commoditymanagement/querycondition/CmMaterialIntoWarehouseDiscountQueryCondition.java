package com.px.commoditymanagement.querycondition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.commoditymanagement.bean.dto.CmMaterialIntoWarehouseDiscountQueryDto;
import com.px.commoditymanagement.entity.CmMaterialIntoWarehouseDiscount;
import com.px.common.util.CollUtils;
import com.px.common.util.DateUtil;
import org.springframework.util.StringUtils;

/**
 * CmMaterialIntoWarehouseDiscount 查询
 *
 * @author 品讯科技
 * @since 2024-01-02
 */
public class CmMaterialIntoWarehouseDiscountQueryCondition {

    /**
     * CmMaterialIntoWarehouseDiscount使用lambda构建查询对象
     *
     * @param dto
     * @return
     */
    public static LambdaQueryWrapper<CmMaterialIntoWarehouseDiscount> build(CmMaterialIntoWarehouseDiscountQueryDto dto) {
        LambdaQueryWrapper<CmMaterialIntoWarehouseDiscount> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // id : 
        if (!StringUtils.isEmpty(dto.getId())) {
            lambdaQueryWrapper.eq(CmMaterialIntoWarehouseDiscount::getId, dto.getId());
        }
        // intoWarehouseDiscountCode : 入库折扣编号
        if (!StringUtils.isEmpty(dto.getIntoWarehouseDiscountCode())) {
            lambdaQueryWrapper.eq(CmMaterialIntoWarehouseDiscount::getIntoWarehouseDiscountCode, dto.getIntoWarehouseDiscountCode());
        }
        // depCode : 部门编号
        if (CollUtils.isNotEmpty(dto.getDepCodeList())) {
            lambdaQueryWrapper.in(CmMaterialIntoWarehouseDiscount::getDepCode, dto.getDepCodeList());
        }
        // purchaseCode : 采购合同/订单编号
        if (!StringUtils.isEmpty(dto.getPurchaseCode())) {
            lambdaQueryWrapper.eq(CmMaterialIntoWarehouseDiscount::getPurchaseCode, dto.getPurchaseCode());
        }
        // projectCode : 项目编号
        if (!StringUtils.isEmpty(dto.getProjectCode())) {
            lambdaQueryWrapper.eq(CmMaterialIntoWarehouseDiscount::getProjectCode, dto.getProjectCode());
        }
        // projectName : 项目名称
        if (!StringUtils.isEmpty(dto.getProjectName())) {
            lambdaQueryWrapper.eq(CmMaterialIntoWarehouseDiscount::getProjectName, dto.getProjectName());
        }
        // supplierCode : 供应商编号
        if (!StringUtils.isEmpty(dto.getSupplierCode())) {
            lambdaQueryWrapper.eq(CmMaterialIntoWarehouseDiscount::getSupplierCode, dto.getSupplierCode());
        }
        // supplierName : 供应商名称
        if (!StringUtils.isEmpty(dto.getSupplierName())) {
            lambdaQueryWrapper.eq(CmMaterialIntoWarehouseDiscount::getSupplierName, dto.getSupplierName());
        }
        // deliveryNumber : 送货单号
        if (!StringUtils.isEmpty(dto.getDeliveryNumber())) {
            lambdaQueryWrapper.eq(CmMaterialIntoWarehouseDiscount::getDeliveryNumber, dto.getDeliveryNumber());
        }
        // consigneeUserCode : 收货人编号
        if (!StringUtils.isEmpty(dto.getConsigneeUserCode())) {
            lambdaQueryWrapper.eq(CmMaterialIntoWarehouseDiscount::getConsigneeUserCode, dto.getConsigneeUserCode());
        }
        // intoWarehousingDate : 入库日期
        if (!StringUtils.isEmpty(dto.getIntoWarehousingDateStart()) && !StringUtils.isEmpty(dto.getIntoWarehousingDateEnd())) {
            lambdaQueryWrapper.ge(CmMaterialIntoWarehouseDiscount::getIntoWarehousingDate, DateUtil.getBeginOfDay(DateUtil.format(dto.getIntoWarehousingDateStart(), "yyyy-MM-dd")));
            lambdaQueryWrapper.le(CmMaterialIntoWarehouseDiscount::getIntoWarehousingDate, DateUtil.getEndOfDay(DateUtil.format(dto.getIntoWarehousingDateEnd(), "yyyy-MM-dd")));
        }
        // remark : 备注
        if (!StringUtils.isEmpty(dto.getRemark())) {
            lambdaQueryWrapper.eq(CmMaterialIntoWarehouseDiscount::getRemark, dto.getRemark());
        }
        // discountType : 折扣类型：1.折扣金额，2.折扣比例
        if (!StringUtils.isEmpty(dto.getDiscountType())) {
            lambdaQueryWrapper.eq(CmMaterialIntoWarehouseDiscount::getDiscountType, dto.getDiscountType());
        }
        // discountData : 折扣比例/折扣金额
        if (!StringUtils.isEmpty(dto.getDiscountData())) {
            lambdaQueryWrapper.eq(CmMaterialIntoWarehouseDiscount::getDiscountData, dto.getDiscountData());
        }
        // status : 生效状态：0.未生效，1.已生效
        if (!StringUtils.isEmpty(dto.getStatus())) {
            lambdaQueryWrapper.eq(CmMaterialIntoWarehouseDiscount::getStatus, dto.getStatus());
        }
        // flowStatus : 表单状态：1.已创建，2.审核中，3.已驳回（审核拒绝），4.审核通过，5.已弃审
        if (!StringUtils.isEmpty(dto.getFlowStatus())) {
            lambdaQueryWrapper.eq(CmMaterialIntoWarehouseDiscount::getFlowStatus, dto.getFlowStatus());
        }
        // flowCode : 流程编号
        if (!StringUtils.isEmpty(dto.getFlowCode())) {
            lambdaQueryWrapper.eq(CmMaterialIntoWarehouseDiscount::getFlowCode, dto.getFlowCode());
        }
        // createTime : 创建时间
        if (!StringUtils.isEmpty(dto.getCreateTime())) {
            lambdaQueryWrapper.eq(CmMaterialIntoWarehouseDiscount::getCreateTime, dto.getCreateTime());
        }
        // createBy : 创建人
        if (!StringUtils.isEmpty(dto.getCreateBy())) {
            lambdaQueryWrapper.like(CmMaterialIntoWarehouseDiscount::getCreateBy, dto.getCreateBy());
        }
        // updateTime : 修改时间
        if (!StringUtils.isEmpty(dto.getUpdateTime())) {
            lambdaQueryWrapper.eq(CmMaterialIntoWarehouseDiscount::getUpdateTime, dto.getUpdateTime());
        }
        // updateBy : 修改人
        if (!StringUtils.isEmpty(dto.getUpdateBy())) {
            lambdaQueryWrapper.like(CmMaterialIntoWarehouseDiscount::getUpdateBy, dto.getUpdateBy());
        }
        lambdaQueryWrapper.orderByDesc(CmMaterialIntoWarehouseDiscount::getCreateTime);
        lambdaQueryWrapper.eq(CmMaterialIntoWarehouseDiscount::getIsDel, 0);
        return lambdaQueryWrapper;
    }

}

