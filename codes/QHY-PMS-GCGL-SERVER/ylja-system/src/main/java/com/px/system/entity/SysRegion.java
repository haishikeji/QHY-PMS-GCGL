package com.px.system.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * sys_region表对应的实体类
 *
 * @author 品讯科技
 * @since 2023-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 父级id
     */
    private Integer pid;

    /**
     * 等级：1.省，2.市，3.区
     */
    private Integer level;


}
