package com.px.humanresource.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * hr_administrative_delivery_detail表对应的实体类
 *
 * @author 品讯科技
 * @since 2023-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class HrAdministrativeDeliveryDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 行政物资入库编号
     */
    private String deliveryCode;

    /**
     * 行政物资编号
     */
    private String materialCode;

    /**
     * 收货仓库编号
     */
    private String warehouseCode;

    /**
     * 行政材料分类编号
     */
    private String categoryCode;

    /**
     * 物资名称
     */
    private String materialName;

    /**
     * 规格
     */
    private String specification;

    /**
     * 型号
     */
    private String model;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 计量单位
     */
    private String units;

    /**
     * 数量
     */
    private Double num;

    /**
     * 领料数量
     */
    private Double usedNum;

    /**
     * 单价金额（不含税）
     */
    private Double unitPriceAfterTaxes;

    /**
     * 总价金额（不含税）
     */
    private Double totalPriceAfterTaxes;

    /**
     * 用途
     */
    private String purpose;

    /**
     * 备注
     */
    private String remark;

    /**
     * 行政物资入库编号
     */
    private String putawayCode;

    /**
     * 需用计划
     */
    private String planCode;

    private String warehouseName;
    private String categoryName;
}
