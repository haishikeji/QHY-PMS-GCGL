package com.px.purchasingmanagement.bean.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.px.purchasingmanagement.entity.PmLeaseContract;
import lombok.Data;

import java.util.List;

/**
 * PmLeaseContract 入参
 *
 * @author 品讯科技
 * @since 2023-12-19
 */
@Data
public class PmLeaseContractQueryDto extends PmLeaseContract{

   /**
   * pageNum 页数
   */
   private Integer pageNum;

   /**
   * pageSize 每页大小
   */
   private Integer pageSize;
   @JsonIgnore
   private List<String> depCodeList;
}

