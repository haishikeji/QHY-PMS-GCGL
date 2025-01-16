package com.px.bean.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class CategoryImportDto {


    @ExcelProperty("编码")
    private String categoryCode;

    @ExcelProperty("父类编号")
    private String parentCode;

    @ExcelProperty("物料分类")
    private String name;
}
