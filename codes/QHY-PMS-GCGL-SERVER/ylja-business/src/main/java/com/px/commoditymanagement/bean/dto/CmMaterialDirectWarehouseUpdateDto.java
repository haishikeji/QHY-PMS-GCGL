package com.px.commoditymanagement.bean.dto;

import com.px.commoditymanagement.entity.CmMaterialDirectWarehouse;
import com.px.commoditymanagement.entity.CmMaterialDirectWarehouseDetail;
import com.px.system.bean.dto.FileDataDto;
import lombok.Data;

import java.util.List;

/**
 * CmMaterialDirectWarehouse 入参
 *
 * @author 品讯科技
 * @since 2023-12-28
 */
@Data
public class CmMaterialDirectWarehouseUpdateDto extends CmMaterialDirectWarehouse {
    private String taskId;
    private Integer startFlag = 0;
    private Integer createFlag = 0;

    /**
     * 明细
     */
    private List<CmMaterialDirectWarehouseDetail> details;

    /**
     * 附件
     */
    private List<FileDataDto> fileDataDtos;
    /**
     * 数量
     */
    private Double detailsNum;
}

