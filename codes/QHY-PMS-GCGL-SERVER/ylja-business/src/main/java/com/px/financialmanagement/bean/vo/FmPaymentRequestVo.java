package com.px.financialmanagement.bean.vo;

import com.px.financialmanagement.entity.FmPaymentRequest;
import com.px.financialmanagement.entity.FmPaymentRequestDetail;
import lombok.Data;

/**
 * FmPaymentRequest 返参
 *
 * @author 品讯科技
 * @since 2023-12-26
 */
@Data
public class FmPaymentRequestVo extends FmPaymentRequest{

    private String customerName;
    private String taskId;
    private Integer startFlag = 0;
    private Integer createFlag = 0;


    /**
     * 付款信息
     */
    private FmPaymentRequestDetail detail;
    /**
     * 是否有填写权限：1.是，0否
     */
    private Integer fillPermission = 0;

    /**
     * 已支付金额
     */
    private Double paidAmount;
}

