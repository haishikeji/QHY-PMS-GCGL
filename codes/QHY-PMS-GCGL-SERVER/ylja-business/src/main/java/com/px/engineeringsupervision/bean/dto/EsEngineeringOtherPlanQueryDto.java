package com.px.engineeringsupervision.bean.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.px.engineeringsupervision.entity.EsEngineeringOtherPlan;
import lombok.Data;

import java.util.List;

/**
 * EsEngineeringOtherPlan 入参
 *
 * @author 品讯科技
 * @since 2023-12-18
 */
@Data
public class EsEngineeringOtherPlanQueryDto extends EsEngineeringOtherPlan {

    /**
     * pageNum 页数
     */
    private Integer pageNum;

    /**
     * pageSize 每页大小
     */
    private Integer pageSize;
    /**
     * 创建日期-开始
     */
    private String createTimeStart;

    /**
     * 创建日期-结束
     */
    private String createTimeEnd;
    @JsonIgnore
    private List<String> depCodeList;
}

