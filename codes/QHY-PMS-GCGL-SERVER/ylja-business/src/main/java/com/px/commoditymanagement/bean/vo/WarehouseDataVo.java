package com.px.commoditymanagement.bean.vo;

import lombok.Data;

import java.util.List;

@Data
public class WarehouseDataVo {

    /**
     * 业务编号/名称
     */
    private String businessCode;

    /**
     * 明细数据
     */
    List<CmWarehouseInterspaceVo> voList;
}
