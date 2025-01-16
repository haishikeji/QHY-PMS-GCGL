package com.px.engineeringsupervision.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.commoditymanagement.entity.CmMaterialDirectWarehouse;
import com.px.commoditymanagement.entity.CmMaterialDirectWarehouseDetail;
import com.px.commoditymanagement.entity.CmMaterialIntoWarehouse;
import com.px.commoditymanagement.entity.CmMaterialIntoWarehouseDetail;
import com.px.commoditymanagement.service.*;
import com.px.common.util.CollUtils;
import com.px.common.util.MoneyUtil;
import com.px.engineeringsupervision.bean.vo.EsEngineeringPlanDetailVo;
import com.px.engineeringsupervision.entity.EsEngineeringPlan;
import com.px.engineeringsupervision.entity.EsEngineeringPlanDetail;
import com.px.engineeringsupervision.mapper.EsEngineeringPlanDetailMapper;
import com.px.engineeringsupervision.service.EsEngineeringPlanDetailService;
import com.px.engineeringsupervision.service.EsEngineeringPlanService;
import com.px.purchasingmanagement.entity.PmPurchaseContract;
import com.px.purchasingmanagement.entity.PmPurchaseContractDetail;
import com.px.purchasingmanagement.entity.PmPurchaseOrder;
import com.px.purchasingmanagement.entity.PmPurchaseOrderDetail;
import com.px.purchasingmanagement.service.PmPurchaseContractDetailService;
import com.px.purchasingmanagement.service.PmPurchaseContractService;
import com.px.purchasingmanagement.service.PmPurchaseOrderDetailService;
import com.px.purchasingmanagement.service.PmPurchaseOrderService;
import com.px.resourcecenter.entity.RcMaterial;
import com.px.resourcecenter.service.RcMaterialService;
import com.px.system.service.SysCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * EsEngineeringPlanDetailServiceImpl 服务实现类
 *
 * @author 品讯科技
 * @since 2023-12-18
 */
@Service
public class EsEngineeringPlanDetailServiceImpl extends ServiceImpl<EsEngineeringPlanDetailMapper, EsEngineeringPlanDetail> implements EsEngineeringPlanDetailService {

    @Autowired
    private EsEngineeringPlanService engineeringPlanService;
    @Autowired
    private PmPurchaseContractService purchaseContractService;
    @Autowired
    private PmPurchaseContractDetailService purchaseContractDetailService;
    @Autowired
    private PmPurchaseOrderService purchaseOrderService;
    @Autowired
    private PmPurchaseOrderDetailService purchaseOrderDetailService;
    @Autowired
    private CmMaterialIntoWarehouseService materialIntoWarehouseService;
    @Autowired
    private CmMaterialIntoWarehouseDetailService materialIntoWarehouseDetailService;
    @Autowired
    private CmMaterialDirectWarehouseService materialDirectWarehouseService;
    @Autowired
    private CmMaterialDirectWarehouseDetailService materialDirectWarehouseDetailService;
    @Autowired
    private CmWarehouseInterspaceService warehouseInterspaceService;
    @Autowired
    private RcMaterialService materialService;
    @Autowired
    private SysCategoryService categoryService;
    @Autowired
    private CmWarehouseService warehouseService;

    @Override
    public List<EsEngineeringPlanDetailVo> buildDetails(String engineeringPlanCode, List<EsEngineeringPlanDetail> planDetailList) {
        List<EsEngineeringPlanDetailVo> voList = new ArrayList<>();
        if (!CollUtils.isNotEmpty(planDetailList)) {
            return voList;
        }
        List<String> engineeringPlanCodeList = new ArrayList<>();
        engineeringPlanCodeList.add(engineeringPlanCode);
        Map<String, Map<String, Double>> wareHouseNumByMaterialCodes = warehouseInterspaceService.countWareHouseNumByMaterialCodes(CollUtils.getDefaultStrVal(CollUtils.toList(planDetailList, x -> x.getMaterialCode())));

        Map<String, String> purchaseDataMap = new HashMap<>();
        List<PmPurchaseContract> pmPurchaseContracts = purchaseContractService.list(new LambdaQueryWrapper<PmPurchaseContract>()
                .eq(PmPurchaseContract::getIsDel, 0)
//                    .eq(PmPurchaseContract::getFlowStatus, 4)
                .in(PmPurchaseContract::getEngineeringPlanCode, engineeringPlanCodeList));
        ArrayList<String> purchaseCodes = new ArrayList<>();
        Map<String, List<PmPurchaseContract>> pmPurchaseContractsListMap = CollUtils.groupByKey(pmPurchaseContracts, x -> x.getEngineeringPlanCode());
        Map<String, List<PmPurchaseContractDetail>> purchaseContractDetailsListMap = null;
        if (CollUtils.isNotEmpty(pmPurchaseContracts)) {
            List<String> codeList = CollUtils.toList(pmPurchaseContracts, x -> x.getPurchaseContractCode());
            purchaseCodes.addAll(codeList);
            List<PmPurchaseContractDetail> purchaseContractDetails = purchaseContractDetailService.listByPurchaseContractCodes(codeList);
            purchaseContractDetailsListMap = CollUtils.groupByKey(purchaseContractDetails, x -> x.getPurchaseContractCode());
            purchaseDataMap.putAll(CollUtils.toMap(pmPurchaseContracts, x -> x.getEngineeringPlanCode(), y -> y.getPurchaseContractCode()));
        }
        List<PmPurchaseOrder> pmPurchaseOrders = purchaseOrderService.list(new LambdaQueryWrapper<PmPurchaseOrder>()
                .eq(PmPurchaseOrder::getIsDel, 0)
//                    .eq(PmPurchaseOrder::getFlowStatus, 4)
                .in(PmPurchaseOrder::getEngineeringPlanCode, engineeringPlanCodeList));
        Map<String, List<PmPurchaseOrderDetail>> purchaseOrderDetailsListMap = null;
        Map<String, List<PmPurchaseOrder>> pmPurchaseOrdersListMap = CollUtils.groupByKey(pmPurchaseOrders, x -> x.getEngineeringPlanCode());
        if (CollUtils.isNotEmpty(pmPurchaseOrders)) {
            List<String> codeList = CollUtils.toList(pmPurchaseOrders, x -> x.getPurchaseOrderCode());
            purchaseCodes.addAll(codeList);
            List<PmPurchaseOrderDetail> purchaseOrderDetails = purchaseOrderDetailService.listByPurchaseOrderCodes(codeList);
            purchaseOrderDetailsListMap = CollUtils.groupByKey(purchaseOrderDetails, x -> x.getPurchaseOrderCode());
            purchaseDataMap.putAll(CollUtils.toMap(pmPurchaseOrders, x -> x.getEngineeringPlanCode(), y -> y.getPurchaseOrderCode()));
        }
        List<CmMaterialIntoWarehouse> materialIntoWarehouseList = materialIntoWarehouseService.list(new LambdaQueryWrapper<CmMaterialIntoWarehouse>()
                .eq(CmMaterialIntoWarehouse::getIsDel, 0)
//                    .eq(CmMaterialIntoWarehouse::getFlowStatus, 4)
                .in(CmMaterialIntoWarehouse::getEngineeringPlanCode, engineeringPlanCodeList));
        Map<String, List<CmMaterialIntoWarehouseDetail>> materialIntoWarehouseDetailListMap = null;
        if (CollUtils.isNotEmpty(materialIntoWarehouseList)) {
            List<String> codeList = CollUtils.toList(materialIntoWarehouseList, x -> x.getMaterialIntoWarehouseCode());
            List<CmMaterialIntoWarehouseDetail> materialIntoWarehouseDetails = materialIntoWarehouseDetailService.list(new LambdaQueryWrapper<CmMaterialIntoWarehouseDetail>()
                    .in(CmMaterialIntoWarehouseDetail::getMaterialIntoWarehouseCode, codeList));
            materialIntoWarehouseDetailListMap = CollUtils.groupByKey(materialIntoWarehouseDetails, x -> x.getMaterialIntoWarehouseCode());
        }
        Map<String, List<CmMaterialIntoWarehouse>> materialIntoWarehouseListMap = CollUtils.groupByKey(materialIntoWarehouseList, x -> x.getEngineeringPlanCode());
        List<CmMaterialDirectWarehouse> materialDirectWarehouseList = materialDirectWarehouseService.list(new LambdaQueryWrapper<CmMaterialDirectWarehouse>()
                .eq(CmMaterialDirectWarehouse::getIsDel, 0)
//                    .eq(CmMaterialDirectWarehouse::getFlowStatus, 4)
                .in(CmMaterialDirectWarehouse::getPurchaseCode, CollUtils.getDefaultStrVal(purchaseCodes)));
        Map<String, List<CmMaterialDirectWarehouse>> materialDirectWarehouseListMap = CollUtils.groupByKey(materialDirectWarehouseList, x -> x.getPurchaseCode());
        Map<String, List<CmMaterialDirectWarehouseDetail>> materialDirectWarehouseDetailListMap = null;
        if (CollUtils.isNotEmpty(materialDirectWarehouseList)) {
            List<CmMaterialDirectWarehouseDetail> materialDirectWarehouseDetailList = materialDirectWarehouseDetailService.list(new LambdaQueryWrapper<CmMaterialDirectWarehouseDetail>()
                    .in(CmMaterialDirectWarehouseDetail::getMaterialDirectWarehouseCode, CollUtils.getDefaultStrVal(CollUtils.toList(materialDirectWarehouseList, x -> x.getMaterialDirectWarehouseCode()))));
            materialDirectWarehouseDetailListMap = CollUtils.groupByKey(materialDirectWarehouseDetailList, x -> x.getMaterialDirectWarehouseCode());
        }


        EsEngineeringPlan esEngineeringPlan = engineeringPlanService.getByCode(engineeringPlanCode);
        List<RcMaterial> rcMaterialList = materialService.list(new LambdaQueryWrapper<RcMaterial>()
                .in(RcMaterial::getMaterialCode, CollUtils.getDefaultStrVal(CollUtils.toList(planDetailList, x -> x.getMaterialCode())))
                .eq(RcMaterial::getIsDel, 0));
        Map<String, String> getMaterialCodeMap = CollUtils.toMap(rcMaterialList, x -> x.getMaterialCode(), y -> y.getCategoryCode());
        Map<String, String> nameByCodes = categoryService.getNameByCodes(CollUtils.getDefaultStrVal(CollUtils.toList(rcMaterialList, x -> x.getCategoryCode())));

        for (EsEngineeringPlanDetail engineeringPlanDetail : planDetailList) {
            EsEngineeringPlanDetailVo detailVo = new EsEngineeringPlanDetailVo();
            BeanUtils.copyProperties(engineeringPlanDetail, detailVo);
            if (esEngineeringPlan != null) {
                detailVo.setProjectName(esEngineeringPlan.getProjectName());
                detailVo.setProjectCode(esEngineeringPlan.getProjectCode());
            }
            Date needTime = detailVo.getNeedTime();
            Map<String, Double> materialMap = wareHouseNumByMaterialCodes.get(warehouseService.getWarehouseCodeByProjectCode(detailVo.getProjectCode()));
            Double inventoryNum = 0.;
            if (CollUtils.isNotEmpty(materialMap)) {
                inventoryNum = materialMap.get(engineeringPlanDetail.getMaterialCode());
            }
            detailVo.setInventoryNum(inventoryNum);
            Double passPurchaseNum = 0.;
            Double allPurchaseNum = 0.;
            Double allPutwayNum = 0.;
            Double purchaseNum = 0.;
            List<PmPurchaseContract> pmPurchaseContractList = pmPurchaseContractsListMap.get(engineeringPlanDetail.getEngineeringPlanCode());
            if (CollUtils.isNotEmpty(pmPurchaseContractList)) {
                Map<Boolean, List<PmPurchaseContract>> collect = pmPurchaseContractList.stream()
                        .collect(Collectors.partitioningBy(x -> x.getFlowStatus() == 4));
                List<PmPurchaseContract> pmPurchaseContracts1 = collect.get(true);
                if (CollUtils.isNotEmpty(pmPurchaseContracts1)) {
                    for (PmPurchaseContract pmPurchaseContract : pmPurchaseContracts1) {
                        Double detailNum = 0.;
                        if (CollUtils.isNotEmpty(purchaseContractDetailsListMap)) {
                            List<PmPurchaseContractDetail> purchaseContractDetails = purchaseContractDetailsListMap.get(pmPurchaseContract.getPurchaseContractCode());
                            if (CollUtils.isNotEmpty(purchaseContractDetails)) {
                                detailNum += purchaseContractDetails.stream()
                                        .filter(x -> x.getNum() != null)
                                        .filter(x -> x.getMaterialCode() != null && engineeringPlanDetail.getMaterialCode().equals(x.getMaterialCode()))
                                        .map(x -> x.getNum())
                                        .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                                purchaseNum += detailNum;
                                passPurchaseNum += detailNum;
                                allPurchaseNum += detailNum;
                            }
                        }
                    }
                }

                List<PmPurchaseContract> pmPurchaseContracts2 = collect.get(false);
                if (CollUtils.isNotEmpty(pmPurchaseContracts2)) {
                    for (PmPurchaseContract pmPurchaseContract : pmPurchaseContracts2) {
                        Double detailNum = 0.;
                        if (CollUtils.isNotEmpty(purchaseContractDetailsListMap)) {
                            List<PmPurchaseContractDetail> purchaseContractDetails = purchaseContractDetailsListMap.get(pmPurchaseContract.getPurchaseContractCode());
                            if (CollUtils.isNotEmpty(purchaseContractDetails)) {
                                detailNum += purchaseContractDetails.stream()
                                        .filter(x -> x.getNum() != null)
                                        .filter(x -> x.getMaterialCode() != null && engineeringPlanDetail.getMaterialCode().equals(x.getMaterialCode()))
                                        .map(x -> x.getNum())
                                        .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                            }
                        }
                        if (pmPurchaseContract.getFlowStatus() == 2) {
                            passPurchaseNum += detailNum;
                        }
                        allPurchaseNum += detailNum;
                    }
                }
            }

            List<PmPurchaseOrder> pmPurchaseOrderList = pmPurchaseOrdersListMap.get(engineeringPlanDetail.getEngineeringPlanCode());
            if (CollUtils.isNotEmpty(pmPurchaseOrderList)) {
                Map<Boolean, List<PmPurchaseOrder>> collect = pmPurchaseOrderList.stream()
                        .collect(Collectors.partitioningBy(x -> x.getFlowStatus() == 4));
                List<PmPurchaseOrder> pmPurchaseOrders1 = collect.get(true);
                if (CollUtils.isNotEmpty(pmPurchaseOrders1)) {
                    for (PmPurchaseOrder purchaseOrder : pmPurchaseOrders1) {
                        Double detailNum = 0.;
                        if (CollUtils.isNotEmpty(purchaseOrderDetailsListMap)) {
                            List<PmPurchaseOrderDetail> purchaseOrderDetails = purchaseOrderDetailsListMap.get(purchaseOrder.getPurchaseOrderCode());
                            if (CollUtils.isNotEmpty(purchaseOrderDetails)) {
                                detailNum += purchaseOrderDetails.stream()
                                        .filter(x -> x.getNum() != null)
                                        .filter(x -> x.getMaterialCode() != null && engineeringPlanDetail.getMaterialCode().equals(x.getMaterialCode()))
                                        .map(x -> x.getNum())
                                        .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                                purchaseNum += detailNum;
                                passPurchaseNum += detailNum;
                                allPurchaseNum += detailNum;
                            }
                        }
                    }
                }

                List<PmPurchaseOrder> pmPurchaseOrders2 = collect.get(false);
                if (CollUtils.isNotEmpty(pmPurchaseOrders2)) {
                    for (PmPurchaseOrder purchaseOrder : pmPurchaseOrders2) {
                        Double detailNum = 0.;
                        if (CollUtils.isNotEmpty(purchaseOrderDetailsListMap)) {
                            List<PmPurchaseOrderDetail> purchaseOrderDetails = purchaseOrderDetailsListMap.get(purchaseOrder.getPurchaseOrderCode());
                            if (CollUtils.isNotEmpty(purchaseOrderDetails)) {
                                detailNum += purchaseOrderDetails.stream()
                                        .filter(x -> x.getNum() != null)
                                        .filter(x -> x.getMaterialCode() != null && engineeringPlanDetail.getMaterialCode().equals(x.getMaterialCode()))
                                        .map(x -> x.getNum())
                                        .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                            }
                        }
                        if (purchaseOrder.getFlowStatus() == 2) {
                            passPurchaseNum += detailNum;
                        }
                        allPurchaseNum += detailNum;
                    }
                }
            }

            Double storageNum = 0.;
            List<CmMaterialIntoWarehouse> materialIntoWarehouses = materialIntoWarehouseListMap.get(engineeringPlanDetail.getEngineeringPlanCode());
            if (CollUtils.isNotEmpty(materialIntoWarehouses)) {
                List<CmMaterialIntoWarehouse> cmMaterialIntoWarehouses1 = materialIntoWarehouses.stream()
                        .filter(x -> Arrays.asList(2, 4).contains(x.getFlowStatus()))
                        .collect(Collectors.toList());
                if (CollUtils.isNotEmpty(cmMaterialIntoWarehouses1)) {
                    for (CmMaterialIntoWarehouse materialIntoWarehouse : cmMaterialIntoWarehouses1) {
                        Double detailNum = 0.;
                        if (CollUtils.isNotEmpty(materialIntoWarehouseDetailListMap)) {
                            List<CmMaterialIntoWarehouseDetail> materialIntoWarehouseDetails = materialIntoWarehouseDetailListMap.get(materialIntoWarehouse.getMaterialIntoWarehouseCode());
                            if (CollUtils.isNotEmpty(materialIntoWarehouseDetails)) {
                                detailNum += materialIntoWarehouseDetails.stream()
                                        .filter(x -> x.getNum() != null)
                                        .filter(x -> x.getMaterialCode() != null && engineeringPlanDetail.getMaterialCode().equals(x.getMaterialCode()))
                                        .map(x -> x.getNum())
                                        .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                            }
                        }
                        if (materialIntoWarehouse.getFlowStatus() == 4) {
                            storageNum += detailNum;
                        }
                        allPutwayNum += detailNum;
                    }
                }
            }

            List<CmMaterialDirectWarehouse> cmMaterialDirectWarehouses = materialDirectWarehouseListMap.get(purchaseDataMap.get(engineeringPlanDetail.getEngineeringPlanCode()));
            if (CollUtils.isNotEmpty(cmMaterialDirectWarehouses)) {
                List<CmMaterialDirectWarehouse> cmMaterialDirectWarehouses1 = cmMaterialDirectWarehouses.stream()
                        .filter(x -> Arrays.asList(2, 4).contains(x.getFlowStatus()))
                        .collect(Collectors.toList());
                if (CollUtils.isNotEmpty(cmMaterialDirectWarehouses1)) {
                    for (CmMaterialDirectWarehouse cmMaterialDirectWarehouse : cmMaterialDirectWarehouses1) {
                        Double detailNum = 0.;
                        if (CollUtils.isNotEmpty(materialDirectWarehouseDetailListMap)) {
                            List<CmMaterialDirectWarehouseDetail> materialDirectWarehouseDetailList = materialDirectWarehouseDetailListMap.get(cmMaterialDirectWarehouse.getMaterialDirectWarehouseCode());
                            if (CollUtils.isNotEmpty(materialDirectWarehouseDetailList)) {
                                detailNum += materialDirectWarehouseDetailList.stream()
                                        .filter(x -> x.getNum() != null)
                                        .filter(x -> x.getMaterialCode() != null && engineeringPlanDetail.getMaterialCode().equals(x.getMaterialCode()))
                                        .map(x -> x.getNum())
                                        .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                            }
                        }
                        if (cmMaterialDirectWarehouse.getFlowStatus() == 4) {
                            storageNum += detailNum;
                        }
                        allPutwayNum += detailNum;
                    }
                }
            }
            detailVo.setPurchaseNum(purchaseNum);
            detailVo.setAllPurchaseNum(allPurchaseNum);
            Double leftPurchaseNum = MoneyUtil.sum(detailVo.getNum(), -allPurchaseNum);
            detailVo.setLeftPurchaseNum(leftPurchaseNum < 0 ? 0 : leftPurchaseNum);
            detailVo.setCategoryCode(getMaterialCodeMap.get(detailVo.getMaterialCode()));
            detailVo.setCategoryName(nameByCodes.get(detailVo.getCategoryCode()));
            detailVo.setPassPurchaseNum(passPurchaseNum);
            detailVo.setIntoStorageNum(storageNum);
            detailVo.setAllIntoStorageNum(allPutwayNum);

            engineeringPlanDetail.setAllPurchaseNum(allPurchaseNum);
            engineeringPlanDetail.setLeftPurchaseNum(leftPurchaseNum < 0 ? 0 : leftPurchaseNum);
            engineeringPlanDetail.setPassPurchaseNum(purchaseNum);
            engineeringPlanDetail.setAllIntoStorageNum(allPutwayNum);
            voList.add(detailVo);
        }
        return voList;
    }
}

