package com.px.marketmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * mm_earnest_money表对应的实体类
 *
 * @author 品讯科技
 * @since 2023-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MmEarnestMoney implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 保证金申请编号
     */
    private String earnestMoneyCode;

    /**
     * 投标项目编号
     */
    private String projectEstablishmentCode;

    /**
     * 客户编号
     */
    private String customerCode;

    /**
     * 申请人编号
     */
    private String applyUserCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 保证金支付方式：1.现金，2.保函
     */
    private Integer earnestMoneyPaymentType;

    /**
     * 保证金/保函金额
     */
    private Double earnestMoney;

    /**
     * 保函服务费
     */
    private Double backletterServiceMoney;

    /**
     * 付款截至时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lastPaymentDate;

    /**
     * 付款方式
     */
    private String paymentType;

    /**
     * 户名
     */
    private String username;

    /**
     * 账户
     */
    private String account;

    /**
     * 开户行
     */
    private String openingBank;

    /**
     * 预计收回时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date predictRetrieveDate;

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
     * 付款状态：0.未付款，1.已付款
     */
    private Integer payStatus;

    /**
     * 收回确认状态：1.是，0.否
     */
    private Integer confirmRetrieveStatus;

    /**
     * 部门编号
     */
    private String depCode;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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


}
