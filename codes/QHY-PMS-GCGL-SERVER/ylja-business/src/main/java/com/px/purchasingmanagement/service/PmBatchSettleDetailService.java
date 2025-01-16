package com.px.purchasingmanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.purchasingmanagement.bean.dto.PmBatchSettleDetailQueryDto;
import com.px.purchasingmanagement.bean.vo.PmBatchSettleDetailVo;
import com.px.purchasingmanagement.entity.PmBatchSettleDetail;

import java.util.List;

/**
 * <p>
 * 批量结算详情 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-12-19
 */
public interface PmBatchSettleDetailService extends IService<PmBatchSettleDetail> {

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    Page<PmBatchSettleDetailVo> getPage(PmBatchSettleDetailQueryDto dto);

    List<PmBatchSettleDetailVo> buildPmBatchSettleDetailVoList(List<PmBatchSettleDetail> list);

    List<PmBatchSettleDetail> listByBatchSettleCode(String batchSettleCode);
}

