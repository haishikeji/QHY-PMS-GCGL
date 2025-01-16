package com.px.system.bean.dto;

import com.px.system.entity.SysCodeGenerate;
import lombok.Data;
/**
 * SysCodeGenerate 入参
 *
 * @author 品讯科技
 * @since 2024-01-05
 */
@Data
public class SysCodeGenerateQueryDto extends SysCodeGenerate{

   /**
   * pageNum 页数
   */
   private Integer pageNum;

   /**
   * pageSize 每页大小
   */
   private Integer pageSize;

}

