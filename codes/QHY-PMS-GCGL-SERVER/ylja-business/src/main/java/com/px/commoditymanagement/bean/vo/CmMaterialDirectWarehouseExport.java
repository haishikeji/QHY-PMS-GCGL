package com.px.commoditymanagement.bean.vo;

import lombok.Data;

@Data
public class CmMaterialDirectWarehouseExport extends CmMaterialDirectWarehouseVo{

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


    private String detailPurpose;
    private String detailRemark;
    private String warehouseName;
    private String hasFile;
    private String constructionTeamName;

}
