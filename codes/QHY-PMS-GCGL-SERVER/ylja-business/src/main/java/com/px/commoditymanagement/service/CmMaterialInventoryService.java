package com.px.commoditymanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.commoditymanagement.bean.dto.CmMaterialInventoryQueryDto;
import com.px.commoditymanagement.bean.vo.CmMaterialInventoryVo;
import com.px.commoditymanagement.entity.CmMaterialInventory;

import java.util.List;

/**
 * <p>
 * 盘点管理 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-12-28
 */
public interface CmMaterialInventoryService extends IService<CmMaterialInventory> {

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    Page<CmMaterialInventoryVo> getPage(CmMaterialInventoryQueryDto dto);

    List<CmMaterialInventoryVo> buildCmMaterialInventoryVoList(List<CmMaterialInventory> list);

    void updateByFlowCode(String instanceId, Integer status);
}

