package com.px.bean.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProjectPeopleVo {

    /**
     * 日期
     */
    private List<String> dateList;

    /**
     * 数量
     */
    private List<Integer> dataList;

    /**
     * 总人数
     */
    private Integer empTotalNum;
    /**
     * 特殊作业人员
     */
    private Integer empSpecialNum;
    /**
     * 普通作业人员
     */
    private Integer empOrdinaryNum;

}
