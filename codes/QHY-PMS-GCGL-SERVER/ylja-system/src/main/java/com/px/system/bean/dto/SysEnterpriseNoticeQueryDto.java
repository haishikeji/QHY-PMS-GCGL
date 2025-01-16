package com.px.system.bean.dto;

import com.px.system.entity.SysEnterpriseNotice;
import lombok.Data;
/**
 * SysEnterpriseNotice 入参
 *
 * @author 品讯科技
 * @since 2023-12-10
 */
@Data
public class SysEnterpriseNoticeQueryDto extends SysEnterpriseNotice{

   /**
   * pageNum 页数
   */
   private Integer pageNum;

   /**
   * pageSize 每页大小
   */
   private Integer pageSize;

}

