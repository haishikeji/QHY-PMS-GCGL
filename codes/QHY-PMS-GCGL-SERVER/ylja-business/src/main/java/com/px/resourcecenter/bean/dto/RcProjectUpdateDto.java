package com.px.resourcecenter.bean.dto;

import com.px.resourcecenter.entity.RcProject;
import com.px.resourcecenter.entity.RcProjectPersonnel;
import lombok.Data;

import java.util.List;

/**
 * RcProject 入参
 *
 * @author 品讯科技
 * @since 2023-12-10
 */
@Data
public class RcProjectUpdateDto extends RcProject {
    private String taskId;
    private Integer startFlag = 0;
    private Integer createFlag = 0;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 项目人员安排
     */
    private List<RcProjectPersonnel> projectPersonnelList;

    /**
     * 施工项目部门名称
     */
    private String depName;

    private String principalName;

    /**
     * 是否有填写权限：1.是，0否
     */
    private Integer fillPermission = 0;
    private Integer fillManHourPermission = 0;
}

