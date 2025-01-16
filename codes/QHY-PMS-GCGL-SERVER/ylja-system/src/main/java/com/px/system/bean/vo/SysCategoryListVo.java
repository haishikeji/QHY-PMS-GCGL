package com.px.system.bean.vo;

import com.px.system.entity.SysCategory;
import lombok.Data;

import java.util.List;

@Data
public class SysCategoryListVo extends SysCategory {

    /**
     * 下级分类
     */
    private List<SysCategoryListVo> nextCategoryList;

}
