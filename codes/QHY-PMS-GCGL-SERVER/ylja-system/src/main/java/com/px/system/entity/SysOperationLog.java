package com.px.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * sys_operation_log表对应的实体类
 *
 * @author 品讯科技
 * @since 2023-11-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysOperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户编号
     */
    private String userCode;

    /**
     * 姓名
     */
    private String userName;

    /**
     * ip地址
     */
    private String ipAddr;

    /**
     * 操作类型:1.修改，2.新增，3.删除，4.导入，5.导出
     */
    private Integer operationType;

    /**
     * 操作描述
     */
    private String operationRemark;

    /**
     * 操作数据
     */
    private String operationData;

    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;


}
