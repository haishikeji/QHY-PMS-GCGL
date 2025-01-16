package com.px.commoditymanagement.bean.dto;

import com.px.commoditymanagement.entity.CmMaterialOutWarehouse;
import com.px.commoditymanagement.entity.CmMaterialOutWarehouseDetail;
import com.px.system.bean.dto.FileDataDto;
import lombok.Data;

import java.util.List;

/**
 * CmMaterialOutWarehouse 入参
 *
 * @author 品讯科技
 * @since 2023-12-28
 */
@Data
public class CmMaterialOutWarehouseUpdateDto extends CmMaterialOutWarehouse {
    private String taskId;
    private Integer startFlag = 0;
    private Integer createFlag = 0;

    /**
     * 明细
     */
    private List<CmMaterialOutWarehouseDetail> details;

    /**
     * 附件
     */
    private List<FileDataDto> fileDataDtos;
    /**
     * 数量
     */
    private Double detailsNum;
}

