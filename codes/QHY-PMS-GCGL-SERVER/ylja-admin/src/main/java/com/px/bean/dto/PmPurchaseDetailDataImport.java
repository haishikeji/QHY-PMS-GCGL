package com.px.bean.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class PmPurchaseDetailDataImport {

    /**
     * 物资编码
     */
    @ExcelProperty("物资编码")
    private String materialCode;
    @ExcelProperty("标准号")
    private String standardNumber;
    /**
     * 物资名称
     */
//    @ExcelProperty("物资名称")
//    private String materialName;
//    @ExcelProperty("规格")
//    private String specification;
//    @ExcelProperty("型号")
//    private String model;
//    @ExcelProperty("品牌")
//    private String brand;

    @ExcelProperty("计量单位")
    private String units;

    @ExcelProperty("数量")
    private Double num;

    /**
     * 单价（含税）
     */
    @ExcelProperty(value = "单价(含税)")
    private String unitPrice;

    /**
     * 税率
     */
    @ExcelProperty("税率")
    private String taxesRatio;

    /**
     * 总金额（含税）
     */
    @ExcelProperty(value = "总金额(含税)")
    private String totalPrice;

    /**
     * 需用到货时间
     */
    @ExcelProperty("需用到货时间")
    private String needArrivalDate;

    /**
     * 采购要求到货时间
     */
    @ExcelProperty("采购要求到货时间")
    private String requireArrivalDate;

    @ExcelProperty(value = "预算单价")
    private String budgetPrice;

    @ExcelProperty("预算说明")
    private String budgetRemark;

    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remark;

    /**
     * 钢材理论总重量
     */
    @ExcelProperty(value = "钢材理论总重量(kg)")
    private String theoryTotalWeight;

}
