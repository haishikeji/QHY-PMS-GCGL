package com.px.financialmanagement.bean.dto;

import com.px.financialmanagement.entity.FmExpenseReimburseRecord;
import lombok.Data;
/**
 * FmExpenseReimburseRecord 入参
 *
 * @author 品讯科技
 * @since 2024-09-16
 */
@Data
public class FmExpenseReimburseRecordQueryDto extends FmExpenseReimburseRecord{

   /**
   * pageNum 页数
   */
   private Integer pageNum;

   /**
   * pageSize 每页大小
   */
   private Integer pageSize;

}

