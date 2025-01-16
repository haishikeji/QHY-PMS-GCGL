package com.px.financialmanagement.bean.vo;

import lombok.Data;

@Data
public class FmExpenseReimburseExport extends FmExpenseReimburseVo{

    /**
     * 费用大类
     */
    private String expenseType;

    /**
     * 内容
     */
    private String content;

    /**
     * 发票类型
     */
    private String invoiceType;

    /**
     * 发票数量
     */
    private Double invoiceNum;

    /**
     * 金额
     */
    private Double invoiceMoney;

    /**
     * 进项税额
     */
    private String taxAmounts;

    /**
     * 备注
     */
    private String remark;
    private String constructionBudgetName;
    private String hasFile;
}
