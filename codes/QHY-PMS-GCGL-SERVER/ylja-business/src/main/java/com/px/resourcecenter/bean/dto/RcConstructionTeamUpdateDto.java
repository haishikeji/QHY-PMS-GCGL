package com.px.resourcecenter.bean.dto;

import com.px.system.bean.dto.FileDataDto;
import com.px.resourcecenter.entity.RcConstructionTeam;
import lombok.Data;

import java.util.List;

/**
 * RcConstructionTeam 入参
 *
 * @author 品讯科技
 * @since 2023-12-10
 */
@Data
public class RcConstructionTeamUpdateDto extends RcConstructionTeam{
    private String taskId;
    private Integer startFlag = 0;
    private Integer createFlag = 0;
    /**
     * 附件信息
     */
    private List<FileDataDto> fileList;

}

