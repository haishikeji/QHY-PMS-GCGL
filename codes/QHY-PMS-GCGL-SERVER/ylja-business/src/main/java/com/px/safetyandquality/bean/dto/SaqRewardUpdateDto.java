package com.px.safetyandquality.bean.dto;

import com.px.safetyandquality.entity.SaqReward;
import com.px.system.bean.dto.FileDataDto;
import lombok.Data;

import java.util.List;

/**
 * SaqReward 入参
 *
 * @author 品讯科技
 * @since 2023-12-24
 */
@Data
public class SaqRewardUpdateDto extends SaqReward {
    private String taskId;
    private Integer startFlag = 0;
    private Integer createFlag = 0;

    /**
     * 附件信息
     */
    private List<FileDataDto> fileList;
    private Integer changeFlag = 0;
    /**
     * 变更单id
     */
    private Long changeRecordId;
    /**
     * 保存并提审标识
     */
    private Integer crSubmitAudit = 0;
}

