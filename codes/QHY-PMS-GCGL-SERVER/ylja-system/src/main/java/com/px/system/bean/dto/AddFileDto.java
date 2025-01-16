package com.px.system.bean.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddFileDto {

    /**
     * 变更单id
     */
    private Long changeRecordId;

    private String dataCode;
    private Integer fileType;

    /**
     * 附件
     */
    private List<FileDataDto> fileDataDtos;

}
