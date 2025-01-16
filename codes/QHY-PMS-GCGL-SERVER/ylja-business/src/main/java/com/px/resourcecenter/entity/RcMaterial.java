package com.px.resourcecenter.entity;

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
 * rc_material表对应的实体类
 *
 * @author 品讯科技
 * @since 2023-12-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RcMaterial implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 部门编号
     */
    private String depCode;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料类型：1.物资档案，2.行政物资档案
     */
    private Integer materialType;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料分类编号
     */
    private String categoryCode;

    /**
     * 物料规格
     */
    private String specification;

    /**
     * 物料型号
     */
    private String model;

    /**
     * 单位
     */
    private String units;

    /**
     * 物料图片
     */
    private String imgUrl;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 状态：1.启动，0.停用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

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


}
