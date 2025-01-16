package com.px.commoditymanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * cm_material_return_warehouse_detail表对应的实体类
 *
 * @author 品讯科技
 * @since 2023-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CmMaterialReturnWarehouseDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 领料退货编号
     */
    private String materialReturnWarehouseCode;

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
     * 领料数量
     */
    private Double takeNum;

    /**
     * 退库数量
     */
    private Double backNum;
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
     * 备注
     */
    private String remark;

    /**
     * 出库单编号
     */
    private String deliveryCode;

    private String warehouseName;
}
