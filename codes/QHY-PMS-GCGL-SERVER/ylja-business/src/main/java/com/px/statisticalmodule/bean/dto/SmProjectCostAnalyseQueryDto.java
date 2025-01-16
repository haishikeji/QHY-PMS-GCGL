package com.px.statisticalmodule.bean.dto;

import com.px.statisticalmodule.entity.SmProjectCostAnalyse;
import lombok.Data;
/**
 * SmProjectCostAnalyse 入参
 *
 * @author 品讯科技
 * @since 2024-04-11
 */
@Data
public class SmProjectCostAnalyseQueryDto extends SmProjectCostAnalyse{

   /**
   * pageNum 页数
   */
   private Integer pageNum;

   /**
   * pageSize 每页大小
   */
   private Integer pageSize;

}

