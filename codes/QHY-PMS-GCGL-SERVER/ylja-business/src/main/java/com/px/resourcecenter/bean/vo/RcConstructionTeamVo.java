package com.px.resourcecenter.bean.vo;

import com.px.resourcecenter.entity.RcConstructionTeam;
import lombok.Data;

/**
 * RcConstructionTeam 返参
 *
 * @author 品讯科技
 * @since 2023-12-10
 */
@Data
public class RcConstructionTeamVo extends RcConstructionTeam{

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 更新人名称
     */
    private String updateUserName;

}

