package com.px.engineeringsupervision.bean.vo;

import com.px.engineeringsupervision.entity.EsEngineeringPlanDetail;
import lombok.Data;

import java.util.Date;

@Data
public class EsEngineeringPlanDetailVo extends EsEngineeringPlanDetail {

    private String applyUserName;
    private String customerName;
    private Integer flowStatus;
    private Date createTime;
    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目部可用库存
     */
    private Double inventoryNum;

    /**
     * 采购数量
     */
    private Double purchaseNum;

    /**
     * 入库数量
     */
    private Double intoStorageNum;
    /**
     * 分类编号
     */
    private String categoryCode;
    /**
     * 分类名称
     */
    private String categoryName;
    private String depName;
    private Date approvaltime;
    private String detailRemark;
    private Date applyTime;

    /**
     * 审批人	评论
     */
    private String auditUserName1;
    private String auditUserRemark1;
    /**
     * 审批人	评论
     */
    private String auditUserName2;
    private String auditUserRemark2;
    /**
     * 审批人	评论
     */
    private String auditUserName3;
    private String auditUserRemark3;

    private Long fileNum;

}
