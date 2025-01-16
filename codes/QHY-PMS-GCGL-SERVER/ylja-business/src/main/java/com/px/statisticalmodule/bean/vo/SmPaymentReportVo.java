package com.px.statisticalmodule.bean.vo;

import com.px.statisticalmodule.entity.SmPaymentReport;
import lombok.Data;

import java.util.List;

/**
 * SmPaymentReport 返参
 *
 * @author 品讯科技
 * @since 2024-04-23
 */
@Data
public class SmPaymentReportVo extends SmPaymentReport {

    /**
     * 付款记录数据
     */
    private List<PaymentRequestVo> paymentRequestVos;

}

