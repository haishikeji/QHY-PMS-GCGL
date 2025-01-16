package com.px.commoditymanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * cm_material_into_warehouse_discount表对应的实体类
 *
 * @author 品讯科技
 * @since 2024-01-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CmMaterialIntoWarehouseDiscount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 入库折扣编号
     */
    private String intoWarehouseDiscountCode;

    /**
     * 收料入库编号
     */
    private String materialIntoWarehouseCode;

    /**
     * 部门编号
     */
    private String depCode;

    /**
     * 数据类型：1.采购订单，2.采购合同
     */
    private Integer purchaseType;

    /**
     *  采购订单/合同编号
     */
    private String purchaseCode;

    /**
     * 采购订单/合同结算编号
     */
    private String purchaseSettleCode;

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 供应商编号
     */
    private String supplierCode;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 送货单号
     */
    private String deliveryNumber;

    /**
     * 收货人编号
     */
    private String consigneeUserCode;

    /**
     * 入库日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date intoWarehousingDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 折扣类型：1.折扣金额，2.折扣比例
     */
    private Integer discountType;

    /**
     * 折扣比例/折扣金额
     */
    private String discountData;

    /**
     * 生效状态：0.未生效，1.已生效
     */
    private Integer status;

    /**
     * 表单状态：1.已创建，2.审核中，3.已驳回（审核拒绝），4.审核通过，5.已弃审
     */
    private Integer flowStatus;

    /**
     * 流程编号
     */
    private String flowCode;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 删除标识：0.否，1.是
     */
    @JsonIgnore
    private Integer isDel;


}
