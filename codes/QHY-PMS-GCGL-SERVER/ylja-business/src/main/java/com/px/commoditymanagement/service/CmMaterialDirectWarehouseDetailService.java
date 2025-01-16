package com.px.commoditymanagement.service;

import com.px.commoditymanagement.entity.CmMaterialDirectWarehouseDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.commoditymanagement.bean.dto.CmMaterialDirectWarehouseDetailQueryDto;
import com.px.commoditymanagement.bean.vo.CmMaterialDirectWarehouseDetailVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 直入直出详情 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-12-28
 */
public interface CmMaterialDirectWarehouseDetailService extends IService<CmMaterialDirectWarehouseDetail> {

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    Page<CmMaterialDirectWarehouseDetailVo> getPage(CmMaterialDirectWarehouseDetailQueryDto dto);

    List<CmMaterialDirectWarehouseDetailVo> buildCmMaterialDirectWarehouseDetailVoList(List<CmMaterialDirectWarehouseDetail> list);

    List<CmMaterialDirectWarehouseDetail> listByMaterialDirectWarehouseCode(String code);
}

