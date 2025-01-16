package com.px.humanresource.bean.vo;

import com.px.humanresource.entity.HrAdministrativePlanDetail;
import lombok.Data;

@Data
public class HrAdministrativePlanDetailVo extends HrAdministrativePlanDetail {

    /**
     * 库存数量
     */
//    private Integer warehouseNum;

    /**
     * 出库数量
     */
    private Double deliveryNum;


}
