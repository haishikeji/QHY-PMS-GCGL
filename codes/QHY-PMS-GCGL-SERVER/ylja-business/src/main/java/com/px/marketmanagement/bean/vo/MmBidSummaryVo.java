package com.px.marketmanagement.bean.vo;

import com.px.marketmanagement.entity.MmBidSummary;
import lombok.Data;

/**
 * MmBidSummary 返参
 *
 * @author 品讯科技
 * @since 2023-12-14
 */
@Data
public class MmBidSummaryVo extends MmBidSummary{

    /**
     * 负责人名称
     */
    private String followUpUserCodeName;

    /**
     * 录入人名称
     */
    private String createUserName;
    private String taskId;
    private Integer startFlag = 0;
    private Integer createFlag = 0;

}

