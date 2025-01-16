package com.px.bean.vo;

import lombok.Data;

import java.util.List;

@Data
public class PecuniaryConditionVo {

    /**
     * 已收款总金额
     */
    private Double totalReceiptMoney = 0.;

    /**
     * 未收款总金额
     */
    private Double totalNotReceiptMoney= 0.;

    /**
     * 日期
     */
    private List<String> dateList;
    /**
     * 数据
     */
    private List<Double> dataList;


}
