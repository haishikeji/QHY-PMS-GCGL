package com.px.safetyandquality.bean.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.px.safetyandquality.entity.SaqSafetyInspectionRectification;
import lombok.Data;

import java.util.List;

/**
 * SaqSafetyInspectionRectification 入参
 *
 * @author 品讯科技
 * @since 2023-12-24
 */
@Data
public class SaqSafetyInspectionRectificationQueryDto extends SaqSafetyInspectionRectification {

    /**
     * pageNum 页数
     */
    private Integer pageNum;

    /**
     * pageSize 每页大小
     */
    private Integer pageSize;
    /**
     * 检查日期-开始
     */
    private String checkDateStart;
    /**
     * 检查日期-结束
     */
    private String checkDateEnd;

    /**
     * 整改完成日期-开始
     */
    private String rectificationFinishDateStart;
    /**
     * 整改完成日期-结束
     */
    private String rectificationFinishDateEnd;
    @JsonIgnore
    private List<String> depCodeList;
}

