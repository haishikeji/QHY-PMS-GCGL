package com.px.purchasingmanagement.bean.vo;

import com.px.purchasingmanagement.entity.PmBatchSettle;
import lombok.Data;

/**
 * PmBatchSettle 返参
 *
 * @author 品讯科技
 * @since 2023-12-19
 */
@Data
public class PmBatchSettleVo extends PmBatchSettle {

    /**
     * 创建人
     */
    private String createUserName;
    private String taskId;
    private Integer startFlag = 0;
    private Integer createFlag = 0;

    private Integer num;
    private String depName;
    private String supplierName;
}

