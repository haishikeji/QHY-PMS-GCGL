package com.px.bean.vo;

import com.px.humanresource.entity.HrAdministrativePlanDetail;
import lombok.Data;

import java.util.List;

@Data
public class HrAdministrativeDeliveryImportVo {

    /**
     * 需用计划编号
     */
    private String planCode;

    /**
     * 物资数据
     */
    private List<HrAdministrativePlanDetail> detailList;

}
