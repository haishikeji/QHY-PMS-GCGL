package com.px.system.bean.vo;

import lombok.Data;

import java.util.List;

@Data
public class RegionVo {

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 下级
     */
    private List<RegionVo> childList;

}
