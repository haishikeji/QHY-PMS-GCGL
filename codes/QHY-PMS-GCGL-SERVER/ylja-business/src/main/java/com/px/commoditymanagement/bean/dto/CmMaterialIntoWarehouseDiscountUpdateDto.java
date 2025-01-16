package com.px.commoditymanagement.bean.dto;

import com.px.commoditymanagement.entity.CmMaterialIntoWarehouseDiscount;
import com.px.commoditymanagement.entity.CmMaterialIntoWarehouseDiscountDetail;
import com.px.system.bean.dto.FileDataDto;
import lombok.Data;

import java.util.List;

/**
 * CmMaterialIntoWarehouseDiscount 入参
 *
 * @author 品讯科技
 * @since 2024-01-02
 */
@Data
public class CmMaterialIntoWarehouseDiscountUpdateDto extends CmMaterialIntoWarehouseDiscount {
    private String taskId;
    private Integer startFlag = 0;
    private Integer createFlag = 0;

    /**
     * 明细
     */
    private List<CmMaterialIntoWarehouseDiscountDetail> details;

    /**
     * 附件
     */
    private List<FileDataDto> fileDataDtos;

    /**
     * 折扣后金额
     */
    private Double discountTotalPrice;

    /**
     * 折扣前金额
     */
    private Double totalPrice;

}

