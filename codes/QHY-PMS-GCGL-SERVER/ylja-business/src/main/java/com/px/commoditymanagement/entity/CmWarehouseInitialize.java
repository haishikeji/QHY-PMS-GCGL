package com.px.commoditymanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * cm_warehouse_initialize表对应的实体类
 *
 * @author 品讯科技
 * @since 2024-01-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CmWarehouseInitialize implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 仓库编号
     */
    private String warehouseCode;

    /**
     * 物资编码
     */
    private String materialCode;

    /**
     * 物资名称
     */
    private String materialName;

    /**
     * 物资分类编号
     */
    private String categoryCode;

    /**
     * 物资分类
     */
    private String categoryName;

    /**
     * 型号
     */
    private String model;

    /**
     * 规格
     */
    private String specification;

    /**
     * 计量单位
     */
    private String units;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 单价（含税）
     */
    private Double unitPriceBeforeTaxes;

    /**
     * 单价（不含税）
     */
    private Double unitPriceAfterTaxes;

    /**
     * 总金额（含税）
     */
    private Double totalPriceBeforeTaxes;

    /**
     * 总金额（不含税）
     */
    private Double totalPriceAfterTaxes;

    /**
     * 税率
     */
    private String taxesRatio;

    /**
     * 税金
     */
    private Double taxesMoney;

    /**
     * 备注
     */
    private String remark;


}
