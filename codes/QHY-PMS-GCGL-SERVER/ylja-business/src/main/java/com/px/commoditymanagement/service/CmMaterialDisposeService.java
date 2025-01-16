package com.px.commoditymanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.commoditymanagement.bean.dto.CmMaterialDisposeQueryDto;
import com.px.commoditymanagement.bean.vo.CmMaterialDisposeVo;
import com.px.commoditymanagement.entity.CmMaterialDispose;

import java.util.List;

/**
 * <p>
 * 呆滞物料处理 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-12-28
 */
public interface CmMaterialDisposeService extends IService<CmMaterialDispose> {

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    Page<CmMaterialDisposeVo> getPage(CmMaterialDisposeQueryDto dto);

    List<CmMaterialDisposeVo> buildCmMaterialDisposeVoList(List<CmMaterialDispose> list);

    void updateByFlowCode(String instanceId, Integer status);
}

