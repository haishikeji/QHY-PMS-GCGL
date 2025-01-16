package com.px.engineeringsupervision.bean.dto;

import com.px.engineeringsupervision.entity.EsEngineeringOtherPlan;
import com.px.engineeringsupervision.entity.EsEngineeringOtherPlanDetail;
import com.px.system.bean.dto.FileDataDto;
import lombok.Data;

import java.util.List;

/**
 * EsEngineeringOtherPlan 入参
 *
 * @author 品讯科技
 * @since 2023-12-18
 */
@Data
public class EsEngineeringOtherPlanUpdateDto extends EsEngineeringOtherPlan {
    private String taskId;
    private Integer startFlag = 0;
    private Integer createFlag = 0;

    /**
     * 明细列表
     */
    private List<EsEngineeringOtherPlanDetail> details;

    /**
     * 附件信息
     */
    private List<FileDataDto> fileDataDtos;

    /**
     * 是否变更 :1是 ,0否
     */
    private Integer changeFlag = 0;
    /**
     * 部门
     */
    private String depName;
    private String engineeringPlanCode;
    /**
     * 下推状态：0未下推过，1.已经下推
     */
    private Integer pushDownStatus = 0;
}

