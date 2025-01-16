package com.px.resourcecenter.bean.dto;

import com.px.resourcecenter.entity.RcSupplier;
import lombok.Data;

/**
 * RcSupplier 入参
 *
 * @author 品讯科技
 * @since 2023-12-10
 */
@Data
public class RcSupplierQueryDto extends RcSupplier {

    /**
     * pageNum 页数
     */
    private Integer pageNum;

    /**
     * pageSize 每页大小
     */
    private Integer pageSize;

}

