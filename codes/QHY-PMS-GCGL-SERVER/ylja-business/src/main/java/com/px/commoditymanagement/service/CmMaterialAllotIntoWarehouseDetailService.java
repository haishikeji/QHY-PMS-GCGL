package com.px.commoditymanagement.service;

import com.px.commoditymanagement.entity.CmMaterialAllotIntoWarehouseDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.commoditymanagement.bean.dto.CmMaterialAllotIntoWarehouseDetailQueryDto;
import com.px.commoditymanagement.bean.vo.CmMaterialAllotIntoWarehouseDetailVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 调拨入库详情 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-12-28
 */
public interface CmMaterialAllotIntoWarehouseDetailService extends IService<CmMaterialAllotIntoWarehouseDetail> {

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    Page<CmMaterialAllotIntoWarehouseDetailVo> getPage(CmMaterialAllotIntoWarehouseDetailQueryDto dto);

    List<CmMaterialAllotIntoWarehouseDetailVo> buildCmMaterialAllotIntoWarehouseDetailVoList(List<CmMaterialAllotIntoWarehouseDetail> list);

    List<CmMaterialAllotIntoWarehouseDetail> listByAllotIntoWarehouseCode(String code);
}

