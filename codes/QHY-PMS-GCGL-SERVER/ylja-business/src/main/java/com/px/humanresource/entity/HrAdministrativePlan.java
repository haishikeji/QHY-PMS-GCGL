package com.px.humanresource.entity;

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
 * hr_administrative_plan表对应的实体类
 *
 * @author 品讯科技
 * @since 2023-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class HrAdministrativePlan implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 行政需用计划编号
     */
    private String planCode;

    /**
     * 部门编号
     */
    private String depCode;

    /**
     * 申请人编号
     */
    private String applyUserCode;

    /**
     * 需要计划名称
     */
    private String name;

    /**
     * 用途
     */
    private String purpose;

    /**
     * 备注
     */
    private String remark;

    /**
     * 生效状态：0.未生效，1.已生效
     */
    private Integer status;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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
