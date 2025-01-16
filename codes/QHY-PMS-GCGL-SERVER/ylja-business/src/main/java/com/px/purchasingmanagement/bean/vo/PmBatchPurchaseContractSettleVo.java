package com.px.purchasingmanagement.bean.vo;

import com.px.purchasingmanagement.entity.PmBatchPurchaseContractSettle;
import lombok.Data;

/**
 * PmBatchPurchaseContractSettle 返参
 *
 * @author 品讯科技
 * @since 2024-03-08
 */
@Data
public class PmBatchPurchaseContractSettleVo extends PmBatchPurchaseContractSettle{

    private String taskId;
    private Integer startFlag = 0;
    private Integer createFlag = 0;
}

