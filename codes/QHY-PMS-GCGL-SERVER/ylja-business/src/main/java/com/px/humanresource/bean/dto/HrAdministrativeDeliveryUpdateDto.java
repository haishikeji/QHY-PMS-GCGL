package com.px.humanresource.bean.dto;

import com.px.humanresource.entity.HrAdministrativeDelivery;
import com.px.humanresource.entity.HrAdministrativeDeliveryDetail;
import com.px.system.bean.dto.FileDataDto;
import lombok.Data;

import java.util.List;

/**
 * HrAdministrativeDelivery 入参
 *
 * @author 品讯科技
 * @since 2023-12-12
 */
@Data
public class HrAdministrativeDeliveryUpdateDto extends HrAdministrativeDelivery {
    private String taskId;
    private Integer startFlag = 0;
    private Integer createFlag = 0;

    /**
     * 明细信息
     */
    private List<HrAdministrativeDeliveryDetail> deliveryDetails;

    /**
     * 附件信息
     */
    private List<FileDataDto> fileDataDtos;
    /**
     * 数量
     */
    private Double detailsNum;
    /**
     * 部门
     */
    private String depName;

}

