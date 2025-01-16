package com.px.commoditymanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * cm_warehouse_interspace表对应的实体类
 *
 * @author 品讯科技
 * @since 2023-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CmWarehouseInterspace implements Serializable {

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
     * 类型：1.行政物资，2.其他
     */
    private Integer dataType;

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
    private Double num;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

}
