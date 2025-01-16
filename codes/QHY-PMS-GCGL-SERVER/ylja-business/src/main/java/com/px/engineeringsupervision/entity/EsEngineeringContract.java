package com.px.engineeringsupervision.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * es_engineering_contract表对应的实体类
 *
 * @author 品讯科技
 * @since 2023-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EsEngineeringContract implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 合同编号
     */
    private String engineeringContractCode;

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 部门编号
     */
    private String depCode;
    private String depName;

    /**
     * 合同金额（含税）
     */
    private Double contractMoneyBeforeTaxes;

    /**
     * 合同金额（不含税）
     */
    private Double contractMoneyAfterTaxes;

    /**
     * 税率
     */
    private String taxesRatio;

    /**
     * 税金
     */
    private Double taxesMoney;

    /**
     * 客户编号
     */
    private String customerCode;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 签约日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date signDate;

    /**
     * 开工日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date startWorkDate;

    /**
     * 竣工日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date finishWorkDate;

    /**
     * 合同工期
     */
    private Integer contractTotalDay;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否存在补充协议 : 1.是，0.否
     */
    private Integer existFlag;

    /**
     * 表单状态：1.已创建，2.审核中，3.已驳回（审核拒绝），4.审核通过，5.已弃审
     */
    private Integer flowStatus;

    /**
     * 流程编号
     */
    private String flowCode;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 删除标记 0-否,1-是
     */
    @JsonIgnore
    private Integer isDel;
    private String changeRemark;

}
