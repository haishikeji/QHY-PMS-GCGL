package com.px.purchasingmanagement.service;

import com.px.purchasingmanagement.entity.PmPurchaseSupplierQuotation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.purchasingmanagement.bean.dto.PmPurchaseSupplierQuotationQueryDto;
import com.px.purchasingmanagement.bean.vo.PmPurchaseSupplierQuotationVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 采购供应商报价 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2024-01-09
 */
public interface PmPurchaseSupplierQuotationService extends IService<PmPurchaseSupplierQuotation> {

   /**
   * 分页查询
   * @param dto
   * @return
   */
   Page<PmPurchaseSupplierQuotationVo> getPage(PmPurchaseSupplierQuotationQueryDto dto);

   List<PmPurchaseSupplierQuotationVo> buildPmPurchaseSupplierQuotationVoList(List<PmPurchaseSupplierQuotation> list);

}

