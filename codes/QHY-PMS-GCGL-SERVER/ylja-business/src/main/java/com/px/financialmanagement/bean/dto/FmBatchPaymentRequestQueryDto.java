package com.px.financialmanagement.bean.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.px.financialmanagement.entity.FmBatchPaymentRequest;
import lombok.Data;

import java.util.List;

/**
 * FmBatchPaymentRequest 入参
 *
 * @author 品讯科技
 * @since 2024-01-02
 */
@Data
public class FmBatchPaymentRequestQueryDto extends FmBatchPaymentRequest {

    /**
     * pageNum 页数
     */
    private Integer pageNum;

    /**
     * pageSize 每页大小
     */
    private Integer pageSize;

    /**
     * 创建时间-开始
     */
    private String createTimeStart;

    /**
     * 创建时间-结束
     */
    private String createTimeEnd;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 供应商编号
     */
    private String supplierCode;

    /**
     * 供应商名称
     */
    private String supplierName;
    @JsonIgnore
    private List<String> depCodeList;

}

