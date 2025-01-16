package com.px.statisticalmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * sm_payment_report表对应的实体类
 *
 * @author 品讯科技
 * @since 2024-04-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SmPaymentReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户编号
     */
    private String customerCode;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 合同编号
     */
    private String contractCode;

    /**
     * 合同金额
     */
    private Double contractMoney;

    /**
     * 合同税率
     */
    private String contractRate;

    /**
     * 合同性质:1.材料,2.人工,3.机械
     */
    private Integer contractNature;

    /**
     * 付款金额合计
     */
    private Double totalMoney;

    /**
     * 累计开票金额合计
     */
    private Double totalInvoiceMoney;

    /**
     * 应付账款
     */
    private Double leftPaymentMoney;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


}
