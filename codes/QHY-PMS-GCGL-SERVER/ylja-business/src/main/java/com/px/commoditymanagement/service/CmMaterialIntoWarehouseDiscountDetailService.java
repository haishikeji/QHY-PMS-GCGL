package com.px.commoditymanagement.service;

import com.px.commoditymanagement.entity.CmMaterialIntoWarehouseDiscountDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.commoditymanagement.bean.dto.CmMaterialIntoWarehouseDiscountDetailQueryDto;
import com.px.commoditymanagement.bean.vo.CmMaterialIntoWarehouseDiscountDetailVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 入库折扣详情 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2024-01-02
 */
public interface CmMaterialIntoWarehouseDiscountDetailService extends IService<CmMaterialIntoWarehouseDiscountDetail> {

   /**
   * 分页查询
   * @param dto
   * @return
   */
   Page<CmMaterialIntoWarehouseDiscountDetailVo> getPage(CmMaterialIntoWarehouseDiscountDetailQueryDto dto);

   List<CmMaterialIntoWarehouseDiscountDetailVo> buildCmMaterialIntoWarehouseDiscountDetailVoList(List<CmMaterialIntoWarehouseDiscountDetail> list);

}

