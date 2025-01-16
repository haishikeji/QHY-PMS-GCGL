package com.px.commoditymanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.commoditymanagement.bean.dto.CmMaterialIntoWarehouseQueryDto;
import com.px.commoditymanagement.bean.vo.CmMaterialIntoWarehouseDetailVo;
import com.px.commoditymanagement.bean.vo.CmMaterialIntoWarehouseVo;
import com.px.commoditymanagement.entity.*;
import com.px.commoditymanagement.mapper.CmMaterialIntoWarehouseMapper;
import com.px.commoditymanagement.querycondition.CmMaterialIntoWarehouseQueryCondition;
import com.px.commoditymanagement.service.*;
import com.px.common.helper.BpmStatusHelper;
import com.px.common.util.CollUtils;
import com.px.common.util.MoneyUtil;
import com.px.purchasingmanagement.entity.PmPurchaseContractDetail;
import com.px.purchasingmanagement.entity.PmPurchaseOrderDetail;
import com.px.purchasingmanagement.service.PmPurchaseContractDetailService;
import com.px.purchasingmanagement.service.PmPurchaseOrderDetailService;
import com.px.system.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * CmMaterialIntoWarehouseServiceImpl 服务实现类
 *
 * @author 品讯科技
 * @since 2023-12-28
 */
@Service
public class CmMaterialIntoWarehouseServiceImpl extends ServiceImpl<CmMaterialIntoWarehouseMapper, CmMaterialIntoWarehouse> implements CmMaterialIntoWarehouseService {

    @Autowired
    private CmMaterialIntoWarehouseDetailService materialIntoWarehouseDetailService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private PmPurchaseContractDetailService purchaseContractDetailService;
    @Autowired
    private PmPurchaseOrderDetailService purchaseOrderDetailService;
    //    @Autowired
//    private RcSupplierService supplierService;
    @Autowired
    private CmWarehouseInterspaceService warehouseInterspaceService;
    @Autowired
    private CmMaterialIntoWarehouseDiscountService materialIntoWarehouseDiscountService;
    @Autowired
    private CmMaterialIntoWarehouseDiscountDetailService materialIntoWarehouseDiscountDetailService;
    @Autowired
    private CmWarehouseService warehouseService;
    @Autowired
    private CmMaterialIntoWarehouseMapper materialIntoWarehouseMapper;

    @Override
    public Page<CmMaterialIntoWarehouseVo> getPage(CmMaterialIntoWarehouseQueryDto dto) {
        LambdaQueryWrapper<CmMaterialIntoWarehouse> queryWrapper = CmMaterialIntoWarehouseQueryCondition.build(dto);
        String materialCode = dto.getMaterialCode();
        if (StringUtils.isNotBlank(materialCode)) {
            List<CmMaterialIntoWarehouseDetail> list = materialIntoWarehouseDetailService.list(
                    new LambdaQueryWrapper<CmMaterialIntoWarehouseDetail>()
                            .eq(CmMaterialIntoWarehouseDetail::getMaterialCode, materialCode));
            Set<String> codeSet = CollUtils.toSet(list, x -> x.getMaterialIntoWarehouseCode());
            List<String> codeList = new ArrayList<>();
            if (CollUtils.isNotEmpty(codeSet)) {
                codeList.addAll(codeSet);
            } else {
                codeList.add("-1");
            }
            if (CollUtils.isNotEmpty(codeList)) {
                queryWrapper.in(CmMaterialIntoWarehouse::getMaterialIntoWarehouseCode, codeList);
            }
        }
        String materialName = dto.getMaterialName();
        if (StringUtils.isNotBlank(materialName)) {
            List<CmMaterialIntoWarehouseDetail> list = materialIntoWarehouseDetailService.list(
                    new LambdaQueryWrapper<CmMaterialIntoWarehouseDetail>()
                            .like(CmMaterialIntoWarehouseDetail::getMaterialName, materialName));
            Set<String> codeSet = CollUtils.toSet(list, x -> x.getMaterialIntoWarehouseCode());
            List<String> codeList = new ArrayList<>();
            if (CollUtils.isNotEmpty(codeSet)) {
                codeList.addAll(codeSet);
            } else {
                codeList.add("-1");
            }
            if (CollUtils.isNotEmpty(codeList)) {
                queryWrapper.in(CmMaterialIntoWarehouse::getMaterialIntoWarehouseCode, codeList);
            }
        }
        if (StringUtils.isNotBlank(dto.getWarehouseCode())) {
            List<CmMaterialIntoWarehouseDetail> list = materialIntoWarehouseDetailService.list(
                    new LambdaQueryWrapper<CmMaterialIntoWarehouseDetail>()
                            .eq(CmMaterialIntoWarehouseDetail::getWarehouseCode, dto.getWarehouseCode()));
            Set<String> codeSet = CollUtils.toSet(list, x -> x.getMaterialIntoWarehouseCode());
            List<String> codeList = new ArrayList<>();
            if (CollUtils.isNotEmpty(codeSet)) {
                codeList.addAll(codeSet);
            } else {
                codeList.add("-1");
            }
            if (CollUtils.isNotEmpty(codeList)) {
                queryWrapper.in(CmMaterialIntoWarehouse::getMaterialIntoWarehouseCode, codeList);
            }
        }
        Page<CmMaterialIntoWarehouse> page = this.page(new Page(dto.getPageNum(), dto.getPageSize()), queryWrapper);
        Page<CmMaterialIntoWarehouseVo> voPage = new Page<>();
        BeanUtils.copyProperties(page, voPage);
        List<CmMaterialIntoWarehouse> records = page.getRecords();
        List<CmMaterialIntoWarehouseVo> voList = this.buildCmMaterialIntoWarehouseVoList(records);
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<CmMaterialIntoWarehouseVo> buildCmMaterialIntoWarehouseVoList(List<CmMaterialIntoWarehouse> list) {
        List<CmMaterialIntoWarehouseVo> voList = null;
        if (!CollectionUtils.isEmpty(list)) {
            voList = new ArrayList<>(list.size());
            HashSet<String> userCodeSet = new HashSet<>();
            userCodeSet.addAll(CollUtils.toSet(list, x -> x.getUpdateBy()));
            userCodeSet.addAll(CollUtils.toSet(list, x -> x.getCreateBy()));
            Map<String, String> nameByCodeList = userService.getNameByCodeList(new ArrayList<>(userCodeSet));
//            Set<String> supplierCodeSet = CollUtils.toSet(list, x -> x.getSupplierCode());
//            Map<String, String> nameByCodes = supplierService.getNameByCodes(new ArrayList<>(supplierCodeSet));
            List<String> codeList = CollUtils.toList(list, x -> x.getMaterialIntoWarehouseCode());
            List<CmMaterialIntoWarehouseDetail> materialIntoWarehouseDetailList = materialIntoWarehouseDetailService.list(new LambdaQueryWrapper<CmMaterialIntoWarehouseDetail>()
                    .in(CmMaterialIntoWarehouseDetail::getMaterialIntoWarehouseCode, codeList));
            Map<String, List<CmMaterialIntoWarehouseDetail>> listMap = CollUtils.groupByKey(materialIntoWarehouseDetailList, x -> x.getMaterialIntoWarehouseCode());
            List<String> collect1 = list.stream()
                    .filter(x -> x.getRelatedDataType() != null && x.getRelatedDataType().equals(1))
                    .map(x -> x.getRelatedDataCode()).collect(Collectors.toList());
            List<PmPurchaseOrderDetail> purchaseOrderDetails = purchaseOrderDetailService.listByPurchaseOrderCodes(collect1);
            Map<String, List<PmPurchaseOrderDetail>> purchaseOrderDetailMap = CollUtils.groupByKey(purchaseOrderDetails, x -> x.getPurchaseOrderCode());
            List<String> collect2 = list.stream()
                    .filter(x -> x.getRelatedDataType() != null && x.getRelatedDataType().equals(2))
                    .map(x -> x.getRelatedDataCode()).collect(Collectors.toList());
            List<PmPurchaseContractDetail> purchaseContractDetails = purchaseContractDetailService.listByPurchaseContractCodes(collect2);
            Map<String, List<PmPurchaseContractDetail>> purchaseContractDetailsMap = CollUtils.groupByKey(purchaseContractDetails, x -> x.getPurchaseContractCode());

            Set<String> relatedDataCodeSet = CollUtils.toSet(list, x -> x.getRelatedDataCode());
            List<CmMaterialIntoWarehouseDiscount> materialIntoWarehouseDiscounts = null;
            if (CollUtils.isNotEmpty(relatedDataCodeSet)) {
                materialIntoWarehouseDiscounts = materialIntoWarehouseDiscountService.list(new LambdaQueryWrapper<CmMaterialIntoWarehouseDiscount>()
                        .in(CmMaterialIntoWarehouseDiscount::getPurchaseCode, relatedDataCodeSet)
                        .eq(CmMaterialIntoWarehouseDiscount::getIsDel, 0));
            }
            Map<String, CmMaterialIntoWarehouseDiscount> materialIntoWarehouseDiscountMap = CollUtils.toMap(materialIntoWarehouseDiscounts, x -> x.getPurchaseCode(), Function.identity());
            for (CmMaterialIntoWarehouse cmMaterialIntoWarehouse : list) {
                CmMaterialIntoWarehouseVo vo = new CmMaterialIntoWarehouseVo();
                BeanUtils.copyProperties(cmMaterialIntoWarehouse, vo);
                vo.setUpdateUserName(nameByCodeList.get(vo.getUpdateBy()));
                vo.setCreateUserName(nameByCodeList.get(vo.getCreateBy()));
//                vo.setSupplierName(nameByCodes.get(vo.getSupplierCode()));
                List<CmMaterialIntoWarehouseDetail> cmMaterialIntoWarehouseDetails = listMap.get(vo.getMaterialIntoWarehouseCode());
                Double materialIntoNum = 0.;
                Double materialIntoMoney = 0.;
                if (CollUtils.isNotEmpty(cmMaterialIntoWarehouseDetails)) {
                    materialIntoNum = cmMaterialIntoWarehouseDetails
                            .stream().filter(x -> x.getNum() != null)
                            .map(x -> x.getNum()).reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                    materialIntoMoney = cmMaterialIntoWarehouseDetails
                            .stream().filter(x -> x.getTotalPriceBeforeTaxes() != null)
                            .map(x -> x.getTotalPriceBeforeTaxes()).reduce(0d, (x, y) -> MoneyUtil.sum(x, y));
                }
                vo.setMaterialIntoNum(materialIntoNum);
                vo.setMaterialIntoMoney(materialIntoMoney);
                Double contractMaterialNum = 0.;
                Integer relatedDataType = vo.getRelatedDataType();
                String relatedDataCode = vo.getRelatedDataCode();
                if (StringUtils.isNotBlank(relatedDataCode)) {
                    if (relatedDataType != null && relatedDataType.equals(1)) {
                        List<PmPurchaseOrderDetail> purchaseOrderDetails1 = purchaseOrderDetailMap.get(relatedDataCode);
                        if (CollUtils.isNotEmpty(purchaseOrderDetails1)) {
                            contractMaterialNum = purchaseOrderDetails1.stream()
                                    .filter(x -> x.getNum() != null)
                                    .map(x -> x.getNum())
                                    .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                        }
                    }
                    if (relatedDataType != null && relatedDataType.equals(2)) {
                        List<PmPurchaseContractDetail> purchaseContractDetails1 = purchaseContractDetailsMap.get(relatedDataCode);
                        if (CollUtils.isNotEmpty(purchaseContractDetails1)) {
                            contractMaterialNum = purchaseContractDetails1.stream()
                                    .filter(x -> x.getNum() != null)
                                    .map(x -> x.getNum())
                                    .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                        }
                    }
                }
                Double materialIntoDiscountMoney = 0.;
                CmMaterialIntoWarehouseDiscount materialIntoWarehouseDiscount = materialIntoWarehouseDiscountMap.get(vo.getRelatedDataCode());
                if (materialIntoWarehouseDiscount != null) {
//                    Integer discountType = materialIntoWarehouseDiscount.getDiscountType();
//                    String discountData = materialIntoWarehouseDiscount.getDiscountData();
//                    if (StringUtils.isNotBlank(discountData)) {
//                        if (discountType == 1) {
//                            materialIntoDiscountMoney = Double.parseDouble(discountData);
//                        } else {
//                            Double discountDataDouble = Double.parseDouble(discountData.replace("%", ""));
//                            materialIntoDiscountMoney = MoneyUtil.getRoundVal(MoneyUtil.calculateByRatio(discountDataDouble,));
//                        }
//                    }
                    List<CmMaterialIntoWarehouseDiscountDetail> discountDetailList = materialIntoWarehouseDiscountDetailService.list(new LambdaQueryWrapper<CmMaterialIntoWarehouseDiscountDetail>()
                            .in(CmMaterialIntoWarehouseDiscountDetail::getIntoWarehouseDiscountCode, materialIntoWarehouseDiscount.getIntoWarehouseDiscountCode()));
                    if (CollUtils.isNotEmpty(discountDetailList)) {
                        materialIntoDiscountMoney = discountDetailList.stream().filter(x -> x.getDiscountTotalPrice() != null)
                                .map(x -> x.getDiscountTotalPrice()).reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                    }
                }
                vo.setMaterialIntoDiscountMoney(materialIntoDiscountMoney);
                vo.setContractMaterialNum(contractMaterialNum);
                voList.add(vo);
            }
        }
        return voList;
    }

    @Override
    public CmMaterialIntoWarehouse getByCode(String code) {
        return this.getOne(new LambdaQueryWrapper<CmMaterialIntoWarehouse>()
                .eq(CmMaterialIntoWarehouse::getMaterialIntoWarehouseCode, code)
                .eq(CmMaterialIntoWarehouse::getIsDel, 0));
    }

    @Override
    public void enteringWarehouse(String code) {
        CmMaterialIntoWarehouse materialIntoWarehouse = this.getByCode(code);
        if (materialIntoWarehouse != null) {
            String projectCode = warehouseService.getWarehouseCodeByProjectCode(materialIntoWarehouse.getProjectCode());
//            String projectName = materialIntoWarehouse.getProjectName();
            List<CmMaterialIntoWarehouseDetail> details = materialIntoWarehouseDetailService.listByMaterialIntoWarehouseCode(code);
            if (CollUtils.isNotEmpty(details)) {
                List<CmWarehouseInterspace> warehouseInterspaces = new ArrayList<>();
                for (CmMaterialIntoWarehouseDetail materialIntoWarehouseDetail : details) {
                    CmWarehouseInterspace warehouseInterspace = new CmWarehouseInterspace();
                    BeanUtils.copyProperties(materialIntoWarehouseDetail, warehouseInterspace);
                    warehouseInterspace.setWarehouseCode(projectCode);
//                    warehouseInterspace.setProjectCode(projectCode);
//                    warehouseInterspace.setProjectName(projectName);
                    warehouseInterspaces.add(warehouseInterspace);
                }
                warehouseInterspaceService.enteringWarehouse(projectCode, warehouseInterspaces, true);
            }
        }
    }

    @Override
    public void updateByFlowCode(String instanceId, Integer status) {
        CmMaterialIntoWarehouse record = this.getOne(new LambdaQueryWrapper<CmMaterialIntoWarehouse>().eq(CmMaterialIntoWarehouse::getFlowCode, instanceId));
        if (record != null) {
            if (record.getFlowStatus() == 4 && BpmStatusHelper.getStatus(status) == 4) {
                return;
            }
            record.setFlowStatus(BpmStatusHelper.getStatus(status));
            this.updateById(record);
            if (record.getFlowStatus() == 4) {
                this.enteringWarehouse(record.getMaterialIntoWarehouseCode());
            }
        }
    }

    @Override
    public Map<String, Double> countMoneyByPurchaseCodes(List<String> list) {
        if (!CollUtils.isNotEmpty(list)) {
            return Collections.emptyMap();
        }
        Map<String, Double> dataMap = new HashMap<>();
        List<CmMaterialIntoWarehouseDetailVo> detailVos = materialIntoWarehouseMapper.countMoneyByPurchaseCodes(list);
        if (CollUtils.isNotEmpty(detailVos)) {
            Map<String, List<CmMaterialIntoWarehouseDetailVo>> listMap = CollUtils.groupByKey(detailVos, x -> x.getRelatedDataCode());
            if (CollUtils.isNotEmpty(listMap)) {
                for (Map.Entry<String, List<CmMaterialIntoWarehouseDetailVo>> entry : listMap.entrySet()) {
                    String key = entry.getKey();
                    Double money = 0.;
                    List<CmMaterialIntoWarehouseDetailVo> value = entry.getValue();
                    if (CollUtils.isNotEmpty(value)) {
                        money = value.stream().map(x -> x.getTotalPriceBeforeTaxes())
                                .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                    }
                    dataMap.put(key, money);
                }
            }
        }
        return dataMap;
    }

}

