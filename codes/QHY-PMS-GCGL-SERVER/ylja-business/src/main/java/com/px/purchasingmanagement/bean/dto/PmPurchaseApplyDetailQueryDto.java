package com.px.purchasingmanagement.bean.dto;

import com.px.purchasingmanagement.entity.PmPurchaseApplyDetail;
import lombok.Data;
/**
 * PmPurchaseApplyDetail 入参
 *
 * @author 品讯科技
 * @since 2024-01-09
 */
@Data
public class PmPurchaseApplyDetailQueryDto extends PmPurchaseApplyDetail{

   /**
   * pageNum 页数
   */
   private Integer pageNum;

   /**
   * pageSize 每页大小
   */
   private Integer pageSize;

}

