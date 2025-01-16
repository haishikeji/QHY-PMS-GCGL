package com.px.bean.dto;

import com.px.system.bean.dto.FileDataDto;
import lombok.Data;

import java.util.List;

@Data
public class UploadInvoiceDto {

    /**
     * 开票申请编号
     */
    private String invoiceApplyCode;

    /**
     * 文件信息
     */
    private List<FileDataDto> fileDataDtos;

}
