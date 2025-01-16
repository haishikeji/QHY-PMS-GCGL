package com.px.bean.vo;

import lombok.Data;

@Data
public class ProjectInfoVo {

    /**
     * 项目编号
     */
    private String projectCode;
    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 合同编号
     */
    private String contractCode;

    /**
     * 施工单位
     */
    private String constructionOrganization;

    /**
     * 项目经理
     */
    private String projectManager;

    /**
     * 技术负责人
     */
    private String technicalDirector;
    /**
     * 安全员
     */
    private String safetyOfficer;
    /**
     * 资料员
     */
    private String documenter;

    /**
     * 项目进度
     */
    private Double projectProgress;

    /**
     * 人力成本
     */
    private Double humanCost;


}
