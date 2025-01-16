package com.px.engineeringsupervision.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * es_subcontractor_review_result表对应的实体类
 *
 * @author 品讯科技
 * @since 2023-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EsSubcontractorReviewResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 单据编号
     */
    private String subcontractorExamineCode;

    /**
     * 分包商入场审查资料id
     */
    private Long reviewDataId;

    /**
     * 状态：1.符合，0.不符合
     */
    private Integer status;


}
