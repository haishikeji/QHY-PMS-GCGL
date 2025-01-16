package com.px.financialmanagement.bean.dto;

import com.px.financialmanagement.entity.FmReceiptRegister;
import com.px.system.bean.dto.FileDataDto;
import lombok.Data;

import java.util.List;

/**
 * FmReceiptRegister 入参
 *
 * @author 品讯科技
 * @since 2023-12-26
 */
@Data
public class FmReceiptRegisterUpdateDto extends FmReceiptRegister {
    private String taskId;
    private Integer startFlag = 0;
    private Integer createFlag = 0;

    /**
     * 附件
     */
    private List<FileDataDto> fileDataDtos;
    /**
     * 客户
     */
    private String customerName;
}

