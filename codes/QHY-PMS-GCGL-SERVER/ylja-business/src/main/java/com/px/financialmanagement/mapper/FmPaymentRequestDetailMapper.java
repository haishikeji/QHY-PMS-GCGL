package com.px.financialmanagement.mapper;

import com.px.financialmanagement.entity.FmPaymentRequestDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 付款结算信息 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2023-12-26
 */
public interface FmPaymentRequestDetailMapper extends BaseMapper<FmPaymentRequestDetail> {

    List<FmPaymentRequestDetail> listBySettleCodes(@Param("codes") List<String> codes);
}
