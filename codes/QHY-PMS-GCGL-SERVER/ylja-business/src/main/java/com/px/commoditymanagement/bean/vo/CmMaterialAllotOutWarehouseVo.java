package com.px.commoditymanagement.bean.vo;

import com.px.commoditymanagement.entity.CmMaterialAllotOutWarehouse;
import lombok.Data;

/**
 * CmMaterialAllotOutWarehouse 返参
 *
 * @author 品讯科技
 * @since 2023-12-28
 */
@Data
public class CmMaterialAllotOutWarehouseVo extends CmMaterialAllotOutWarehouse {

    /**
     * 调拨出库数量
     */
    private Double totalNum;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 更新人名称
     */
    private String updateUserName;

    /**
     * 调出仓库名称
     */
    private String outWarehouseName;

    /**
     * 调入仓库名称
     */
    private String intoWarehouseName;

    private String taskId;
    private Integer startFlag = 0;
    private Integer createFlag = 0;

}

