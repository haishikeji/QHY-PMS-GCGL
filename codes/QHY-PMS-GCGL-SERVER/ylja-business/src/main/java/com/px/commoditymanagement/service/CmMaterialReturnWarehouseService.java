package com.px.commoditymanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.commoditymanagement.bean.dto.CmMaterialReturnWarehouseQueryDto;
import com.px.commoditymanagement.bean.vo.CmMaterialReturnWarehouseVo;
import com.px.commoditymanagement.entity.CmMaterialReturnWarehouse;

import java.util.List;

/**
 * <p>
 * 领料退货 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-12-28
 */
public interface CmMaterialReturnWarehouseService extends IService<CmMaterialReturnWarehouse> {

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    Page<CmMaterialReturnWarehouseVo> getPage(CmMaterialReturnWarehouseQueryDto dto);

    List<CmMaterialReturnWarehouseVo> buildCmMaterialReturnWarehouseVoList(List<CmMaterialReturnWarehouse> list);

    void enteringWarehouse(String code);

    CmMaterialReturnWarehouse getByCode(String code);

    void updateByFlowCode(String instanceId, Integer status);
}

