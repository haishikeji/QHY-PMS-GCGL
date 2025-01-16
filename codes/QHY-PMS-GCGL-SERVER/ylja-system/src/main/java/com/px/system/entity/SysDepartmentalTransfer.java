package com.px.system.entity;

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
 * sys_departmental_transfer表对应的实体类
 *
 * @author 品讯科技
 * @since 2023-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDepartmentalTransfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 部门调动申请编号
     */
    private String depTransferCode;

    /**
     * 部门编号
     */
    private String depCode;

    /**
     * 申请人原部门编号
     */
    private String originalDepCode;

    /**
     * 申请人原部门名称
     */
    private String depName;

    /**
     * 项目编码
     */
    private String projectCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 员工编号
     */
    private String userCode;

    /**
     * 员工名称
     */
    private String userName;

    /**
     * 申请人调入部门编号
     */
    private String applyDepCode;

    /**
     * 申请人调入部门名称
     */
    private String applyDepName;

    /**
     * 调用原因
     */
    private String reason;

    /**
     * 备注
     */
    private String remark;

    /**
     * 表单状态：1.已创建，2.审核中，3.已驳回（审核拒绝），4.审核通过，5.已弃审
     */
    private Integer flowStatus;

    /**
     * 流程编号
     */
    private String flowCode;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

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
     * 删除状态：0否，1.是
     */
    @JsonIgnore
    private Integer isDel;


}
