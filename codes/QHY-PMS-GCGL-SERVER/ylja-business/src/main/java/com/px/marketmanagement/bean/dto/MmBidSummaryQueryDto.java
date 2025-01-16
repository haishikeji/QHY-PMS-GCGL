package com.px.marketmanagement.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.px.marketmanagement.entity.MmBidSummary;
import lombok.Data;

import java.util.List;

/**
 * MmBidSummary 入参
 *
 * @author 品讯科技
 * @since 2023-12-14
 */
@Data
public class MmBidSummaryQueryDto extends MmBidSummary {

    /**
     * pageNum 页数
     */
    private Integer pageNum;

    /**
     * pageSize 每页大小
     */
    private Integer pageSize;

    /**
     * 投标项目编号
     */
    @JsonFormat
    private List<String> projectEstablishmentCodeList;

    private String bidPrincipalUserCode;
    @JsonIgnore
    private List<String> depCodeList;
}

