package com.px.purchasingmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.commoditymanagement.entity.CmMaterialDirectWarehouse;
import com.px.commoditymanagement.entity.CmMaterialDirectWarehouseDetail;
import com.px.commoditymanagement.entity.CmMaterialIntoWarehouse;
import com.px.commoditymanagement.entity.CmMaterialIntoWarehouseDetail;
import com.px.commoditymanagement.service.CmMaterialDirectWarehouseDetailService;
import com.px.commoditymanagement.service.CmMaterialDirectWarehouseService;
import com.px.commoditymanagement.service.CmMaterialIntoWarehouseDetailService;
import com.px.commoditymanagement.service.CmMaterialIntoWarehouseService;
import com.px.common.helper.BpmStatusHelper;
import com.px.common.util.CollUtils;
import com.px.common.util.MoneyUtil;
import com.px.engineeringsupervision.entity.EsConstructionBudgetDetail;
import com.px.engineeringsupervision.service.EsConstructionBudgetDetailService;
import com.px.engineeringsupervision.service.EsEngineeringPlanService;
import com.px.financialmanagement.entity.FmPaymentRequestDetail;
import com.px.financialmanagement.service.FmPaymentRequestDetailService;
import com.px.financialmanagement.service.FmPaymentRequestService;
import com.px.purchasingmanagement.bean.dto.PmPurchaseOrderQueryDto;
import com.px.purchasingmanagement.bean.vo.PmPurchaseOrderDetailVo;
import com.px.purchasingmanagement.bean.vo.PmPurchaseOrderVo;
import com.px.purchasingmanagement.entity.*;
import com.px.purchasingmanagement.mapper.PmPurchaseOrderMapper;
import com.px.purchasingmanagement.querycondition.PmPurchaseOrderQueryCondition;
import com.px.purchasingmanagement.service.*;
import com.px.resourcecenter.service.RcProjectService;
import com.px.resourcecenter.service.RcSupplierService;
import com.px.system.service.SysDepartmentService;
import com.px.system.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * PmPurchaseOrderServiceImpl 服务实现类
 *
 * @author 品讯科技
 * @since 2023-12-19
 */
@Service
public class PmPurchaseOrderServiceImpl extends ServiceImpl<PmPurchaseOrderMapper, PmPurchaseOrder> implements PmPurchaseOrderService {

    @Autowired
    private SysUserService userService;
    @Autowired
    private RcSupplierService supplierService;
    @Autowired
    private PmPurchaseOrderDetailService purchaseOrderDetailService;
    @Autowired
    private CmMaterialIntoWarehouseService materialIntoWarehouseService;
    @Autowired
    private CmMaterialIntoWarehouseDetailService materialIntoWarehouseDetailService;
    @Autowired
    private PmPurchaseSettleService purchaseSettleService;
    @Autowired
    private PmPurchaseSettleDetailService purchaseSettleDetailService;
    @Autowired
    private EsConstructionBudgetDetailService constructionBudgetDetailService;
    @Autowired
    private CmMaterialDirectWarehouseService materialDirectWarehouseService;
    @Autowired
    private CmMaterialDirectWarehouseDetailService materialDirectWarehouseDetailService;
    @Autowired
    private SysDepartmentService departmentService;
    @Autowired
    private FmPaymentRequestService paymentRequestService;
    @Autowired
    private FmPaymentRequestDetailService paymentRequestDetailService;
    @Autowired
    private RcProjectService projectService;
    @Autowired
    private SysChangeRecordService changeRecordService;
    @Autowired
    private EsEngineeringPlanService esEngineeringPlanService;

    @Override
    public Page<PmPurchaseOrderVo> getPage(PmPurchaseOrderQueryDto dto) {
        LambdaQueryWrapper<PmPurchaseOrder> queryWrapper = PmPurchaseOrderQueryCondition.build(dto);
        Page<PmPurchaseOrder> page = this.page(new Page(dto.getPageNum(), dto.getPageSize()), queryWrapper);
        Page<PmPurchaseOrderVo> voPage = new Page<>();
        BeanUtils.copyProperties(page, voPage);
        List<PmPurchaseOrder> records = page.getRecords();
        List<PmPurchaseOrderVo> voList = this.buildPmPurchaseOrderVoList(records);
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<PmPurchaseOrderVo> buildPmPurchaseOrderVoList(List<PmPurchaseOrder> list) {
        List<PmPurchaseOrderVo> voList = null;
        if (!CollectionUtils.isEmpty(list)) {
            voList = new ArrayList<>(list.size());
            List<String> purchaseOrderCodeList = CollUtils.toList(list, x -> x.getPurchaseOrderCode());
            List<PmPurchaseOrderDetail> purchaseOrderDetailList = purchaseOrderDetailService.list(new LambdaQueryWrapper<PmPurchaseOrderDetail>()
                    .in(PmPurchaseOrderDetail::getPurchaseOrderCode, purchaseOrderCodeList));
            Map<String, List<PmPurchaseOrderDetail>> purchaseOrderDetailListMap = CollUtils.groupByKey(purchaseOrderDetailList, x -> x.getPurchaseOrderCode());
            Set<String> supplierCodeSet = CollUtils.toSet(list, x -> x.getSupplierCode());
            Map<String, String> supplierNameMap = supplierService.getNameByCodes(new ArrayList<>(supplierCodeSet));
            Set<String> userCodeSet = CollUtils.toSet(list, x -> x.getCreateBy());
            Map<String, String> userNameMap = userService.getNameByCodeList(new ArrayList<>(userCodeSet));
            List<PmPurchaseSettle> purchaseSettleList = purchaseSettleService.list(new LambdaQueryWrapper<PmPurchaseSettle>()
                    .eq(PmPurchaseSettle::getIsDel, 0)
//                    .eq(PmPurchaseSettle::getFlowStatus, 4)
                    .in(PmPurchaseSettle::getDataCode, purchaseOrderCodeList));
            Map<String, List<PmPurchaseSettleDetail>> purchaseSettleDetailListMap = null;
//            Map<String, List<PmPurchaseSettle>> purchaseSettleListMapData = null;
            List<FmPaymentRequestDetail> paymentRequestDetailList = paymentRequestDetailService.listBySettleCodes(CollUtils.getDefaultStrVal(CollUtils.toList(purchaseSettleList, x -> x.getSettleCode())));
            Map<String, List<FmPaymentRequestDetail>> paymentRequestDetailListMap = CollUtils.groupByKey(paymentRequestDetailList, x -> x.getSettleCode());
            if (CollUtils.isNotEmpty(purchaseSettleList)) {
                List<String> codeList = CollUtils.toList(purchaseSettleList, x -> x.getSettleCode());
                List<PmPurchaseSettleDetail> purchaseSettleDetails = purchaseSettleDetailService.list(new LambdaQueryWrapper<PmPurchaseSettleDetail>()
                        .in(PmPurchaseSettleDetail::getSettleCode, codeList));
                purchaseSettleDetailListMap = CollUtils.groupByKey(purchaseSettleDetails, x -> x.getSettleCode());
//                purchaseSettleListMapData = purchaseSettleList.stream().filter(x -> x.getFlowStatus() == 4).collect(Collectors.groupingBy(x -> x.getDataCode()));
            }
            Map<String, List<PmPurchaseSettle>> purchaseSettleMap = CollUtils.groupByKey(purchaseSettleList, x -> x.getDataCode());
            List<CmMaterialIntoWarehouse> materialIntoWarehouseList = materialIntoWarehouseService.list(new LambdaQueryWrapper<CmMaterialIntoWarehouse>()
                    .eq(CmMaterialIntoWarehouse::getIsDel, 0)
//                    .eq(CmMaterialIntoWarehouse::getFlowStatus, 4)
                    .in(CmMaterialIntoWarehouse::getRelatedDataCode, purchaseOrderCodeList));
            Map<String, List<CmMaterialIntoWarehouseDetail>> materialIntoWarehouseDetailListMap = null;
            if (CollUtils.isNotEmpty(materialIntoWarehouseList)) {
                List<String> codeList = CollUtils.toList(materialIntoWarehouseList, x -> x.getMaterialIntoWarehouseCode());
                List<CmMaterialIntoWarehouseDetail> materialIntoWarehouseDetails = materialIntoWarehouseDetailService.list(new LambdaQueryWrapper<CmMaterialIntoWarehouseDetail>()
                        .in(CmMaterialIntoWarehouseDetail::getMaterialIntoWarehouseCode, codeList));
                materialIntoWarehouseDetailListMap = CollUtils.groupByKey(materialIntoWarehouseDetails, x -> x.getMaterialIntoWarehouseCode());
            }
            Map<String, List<CmMaterialIntoWarehouse>> materialIntoWarehouseListMap = CollUtils.groupByKey(materialIntoWarehouseList, x -> x.getRelatedDataCode());
            List<CmMaterialDirectWarehouse> materialDirectWarehouseList = materialDirectWarehouseService.list(new LambdaQueryWrapper<CmMaterialDirectWarehouse>()
                    .eq(CmMaterialDirectWarehouse::getIsDel, 0)
//                    .eq(CmMaterialDirectWarehouse::getFlowStatus, 4)
                    .in(CmMaterialDirectWarehouse::getPurchaseCode, CollUtils.getDefaultStrVal(purchaseOrderCodeList)));
            Map<String, List<CmMaterialDirectWarehouse>> materialDirectWarehouseListMap = CollUtils.groupByKey(materialDirectWarehouseList, x -> x.getPurchaseCode());
            Map<String, List<CmMaterialDirectWarehouseDetail>> materialDirectWarehouseDetailListMap = null;
            if (CollUtils.isNotEmpty(materialDirectWarehouseList)) {
                List<CmMaterialDirectWarehouseDetail> materialDirectWarehouseDetailList = materialDirectWarehouseDetailService.list(new LambdaQueryWrapper<CmMaterialDirectWarehouseDetail>()
                        .in(CmMaterialDirectWarehouseDetail::getMaterialDirectWarehouseCode, CollUtils.getDefaultStrVal(CollUtils.toList(materialDirectWarehouseList, x -> x.getMaterialDirectWarehouseCode()))));
                materialDirectWarehouseDetailListMap = CollUtils.groupByKey(materialDirectWarehouseDetailList, x -> x.getMaterialDirectWarehouseCode());
            }
            Set<Long> idList = CollUtils.toSet(list, x -> x.getConstructionBudgetDetailId());
            Map<Long, EsConstructionBudgetDetail> budgetMap = null;
            if (CollUtils.isNotEmpty(idList)) {
                List<EsConstructionBudgetDetail> budgetDetailList = constructionBudgetDetailService.listByIds(idList);
                budgetMap = CollUtils.toMap(budgetDetailList, x -> x.getId(), Function.identity());
            }
            Map<String, List<PmPurchaseSettle>> purchaseSettleListMap = CollUtils.groupByKey(purchaseSettleList, x -> x.getDataCode());
            Map<String, String> mapByCodes = departmentService.mapByCodes(CollUtils.getDefaultStrVal(CollUtils.toList(list, x -> x.getDepCode())));
            Map<String, String> nameMaps = projectService.getCustomerNameByProjectCode(CollUtils.getDefaultStrVal(CollUtils.toList(list, x -> x.getProjectCode())));
            Map<String, SysChangeRecord> lastChangeRecordMap = changeRecordService.getLastChangeRecordMap(purchaseOrderCodeList);
            for (PmPurchaseOrder pmPurchaseOrder : list) {
                PmPurchaseOrderVo vo = new PmPurchaseOrderVo();
                BeanUtils.copyProperties(pmPurchaseOrder, vo);
                vo.setChangedMoney(MoneyUtil.sum(vo.getOrderMoneyBeforeTaxes(),-MoneyUtil.getDefault(vo.getOriginalMoney())));
                vo.setDepName(mapByCodes.get(vo.getDepCode()));
                vo.setSupplierName(supplierNameMap.get(vo.getSupplierCode()));
                vo.setCreateUserName(userNameMap.get(vo.getCreateBy()));
                vo.setCustomerName(nameMaps.get(vo.getProjectCode()));
                vo.setContractMoney(vo.getOrderMoneyBeforeTaxes());
                Double totalNum = 0.;
                Double totalSettleNum = 0.;
                Double totalStorageNum = 0.;
                Double totalAllSettleNum = 0.;
                Double totalAllStorageNum = 0.;
                if (CollUtils.isNotEmpty(purchaseOrderDetailListMap)) {
                    List<PmPurchaseOrderDetail> details = purchaseOrderDetailListMap.get(vo.getPurchaseOrderCode());
                    List<PmPurchaseOrderDetailVo> detailVos = new ArrayList<>();
                    List<CmMaterialIntoWarehouse> materialIntoWarehouses = materialIntoWarehouseListMap.get(pmPurchaseOrder.getPurchaseOrderCode());
                    List<CmMaterialDirectWarehouse> cmMaterialDirectWarehouses = materialDirectWarehouseListMap.get(pmPurchaseOrder.getPurchaseOrderCode());
                    List<PmPurchaseSettle> purchaseSettles = purchaseSettleMap.get(pmPurchaseOrder.getPurchaseOrderCode());
                    if (CollUtils.isNotEmpty(details)) {
                        for (PmPurchaseOrderDetail detail : details) {
                            PmPurchaseOrderDetailVo detailVo = new PmPurchaseOrderDetailVo();
                            BeanUtils.copyProperties(detail, detailVo);
                            Double storageNum = 0.;
                            Double allStorageNum = 0.;
                            if (CollUtils.isNotEmpty(materialIntoWarehouses)) {
                                List<CmMaterialIntoWarehouse> cmMaterialIntoWarehouses1 = materialIntoWarehouses.stream()
                                        .filter(x -> Arrays.asList(2, 4).contains(x.getFlowStatus()))
                                        .collect(Collectors.toList());
                                for (CmMaterialIntoWarehouse materialIntoWarehouse : cmMaterialIntoWarehouses1) {
                                    Double detailNum = 0.;
                                    if (CollUtils.isNotEmpty(materialIntoWarehouseDetailListMap)) {
                                        List<CmMaterialIntoWarehouseDetail> materialIntoWarehouseDetails = materialIntoWarehouseDetailListMap.get(materialIntoWarehouse.getMaterialIntoWarehouseCode());
                                        if (CollUtils.isNotEmpty(materialIntoWarehouseDetails)) {
                                            detailNum += materialIntoWarehouseDetails.stream()
                                                    .filter(x -> x.getNum() != null)
                                                    .filter(x -> x.getMaterialCode() != null && detail.getMaterialCode().equals(x.getMaterialCode()))
                                                    .map(x -> x.getNum())
                                                    .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                                        }
                                    }
                                    if (materialIntoWarehouse.getFlowStatus() == 4) {
                                        storageNum += detailNum;
                                    }
                                    allStorageNum += detailNum;
                                }
                            }
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
                                                        .filter(x -> x.getMaterialCode() != null && detail.getMaterialCode().equals(x.getMaterialCode()))
                                                        .map(x -> x.getNum())
                                                        .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                                            }
                                        }
                                        if (cmMaterialDirectWarehouse.getFlowStatus() == 4) {
                                            storageNum += detailNum;
                                        }
                                        allStorageNum += detailNum;
                                    }
                                }
                            }
                            detailVo.setStorageNum(storageNum);
                            detailVo.setAllStorageNum(allStorageNum);
                            Double leftStorageNum = MoneyUtil.sum(detailVo.getNum(), -allStorageNum);
                            detailVo.setLeftStorageNum(leftStorageNum < 0 ? 0 : leftStorageNum);
                            totalAllStorageNum += allStorageNum;

                            Double settleNum = 0.;
                            Double allSettleNum = 0.;
                            if (CollUtils.isNotEmpty(purchaseSettles)) {
                                List<PmPurchaseSettle> purchaseSettleList1 = purchaseSettles.stream()
                                        .filter(x -> Arrays.asList(2, 4).contains(x.getFlowStatus()))
                                        .collect(Collectors.toList());
                                for (PmPurchaseSettle purchaseSettle : purchaseSettleList1) {
                                    Double detailNum = 0.;
                                    String settleCode = purchaseSettle.getSettleCode();
                                    if (CollUtils.isNotEmpty(purchaseSettleDetailListMap)) {
                                        List<PmPurchaseSettleDetail> purchaseSettleDetails = purchaseSettleDetailListMap.get(settleCode);
                                        if (CollUtils.isNotEmpty(purchaseSettleDetails)) {
                                            detailNum += purchaseSettleDetails.stream()
                                                    .filter(x -> x.getSettleNum() != null)
                                                    .filter(x -> x.getMaterialCode() != null && detail.getMaterialCode().equals(x.getMaterialCode()))
                                                    .map(x -> x.getSettleNum())
                                                    .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                                        }
                                    }
                                    if (purchaseSettle.getFlowStatus() == 4) {
                                        settleNum += detailNum;
                                    }
                                    allSettleNum += detailNum;
                                }
                            }
                            totalSettleNum += settleNum;
                            totalAllSettleNum += allSettleNum;
                            totalStorageNum += storageNum;
                            detailVo.setSettleNum(settleNum);
                            detailVo.setAllSettleNum(allSettleNum);
                            detailVos.add(detailVo);
                            totalNum += MoneyUtil.getDefault(detail.getNum());
                        }
                    }
                    vo.setDetailVos(detailVos);
                }
                vo.setTotalNum(totalNum);
                vo.setSettleNum(totalSettleNum);
                vo.setStorageNum(totalStorageNum);
                vo.setTotalAllStorageNum(totalAllStorageNum);
                vo.setTotalAllSettleNum(totalAllSettleNum);
                if (CollUtils.isNotEmpty(budgetMap)) {
                    EsConstructionBudgetDetail budgetDetail = budgetMap.get(vo.getConstructionBudgetDetailId());
                    if (budgetDetail != null) {
                        vo.setConstructionBudgetVal(budgetDetail.getTotalPrice());
                        vo.setCostSubject(budgetDetail.getCostSubject());
                    }
                }
//                if (CollUtils.isNotEmpty(purchaseSettleListMapData)) {
//                    vo.setSettleCodeList(purchaseSettleListMapData.get(vo.getPurchaseOrderCode()));
//                }
                List<PmPurchaseSettle> pmPurchaseSettles = purchaseSettleListMap.get(vo.getPurchaseOrderCode());
                Double totalAllSettleMoney = 0.;
                if (CollUtils.isNotEmpty(pmPurchaseSettles)) {
                    for (PmPurchaseSettle pmPurchaseSettle : pmPurchaseSettles) {
                        List<FmPaymentRequestDetail> fmPaymentRequestDetails = paymentRequestDetailListMap.get(pmPurchaseSettle.getSettleCode());
                        Double getPaymentMoney = 0.;
                        if (CollUtils.isNotEmpty(fmPaymentRequestDetails)) {
                            getPaymentMoney = fmPaymentRequestDetails.stream().map(x -> x.getPaymentMoney())
                                    .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                        }
                        totalAllSettleMoney = MoneyUtil.sum(totalAllSettleMoney, MoneyUtil.getDefault(pmPurchaseSettle.getSettleMoneyBeforeTaxes()));
                        pmPurchaseSettle.setPaymentSettleMoney(MoneyUtil.sum(pmPurchaseSettle.getSettleMoneyBeforeTaxes(), -getPaymentMoney));
                    }
                }
                vo.setTotalAllSettleMoney(MoneyUtil.getRoundVal(totalAllSettleMoney));
                vo.setSettleCodeList(pmPurchaseSettles);
                voList.add(vo);
            }
        }
        return voList;
    }

    @Override
    public List<PmPurchaseOrder> listByCodes(List<String> codeList) {
        return this.list(new LambdaQueryWrapper<PmPurchaseOrder>()
                .in(PmPurchaseOrder::getPurchaseOrderCode, codeList)
                .eq(PmPurchaseOrder::getIsDel, 0));
    }

    @Override
    public PmPurchaseOrder getByCode(String code) {
        return this.getOne(new LambdaQueryWrapper<PmPurchaseOrder>()
                .eq(PmPurchaseOrder::getPurchaseOrderCode, code)
                .eq(PmPurchaseOrder::getIsDel, 0));
    }

    @Override
    public void updateByFlowCode(String instanceId, Integer status) {
        PmPurchaseOrder record = this.getOne(new LambdaQueryWrapper<PmPurchaseOrder>().eq(PmPurchaseOrder::getFlowCode, instanceId));
        if (record != null) {
            esEngineeringPlanService.asyncUpdatePurchaseStatus(record.getEngineeringPlanCode());
            if (record.getFlowStatus() == 4 && BpmStatusHelper.getStatus(status) == 4) {
                return;
            }
            record.setFlowStatus(BpmStatusHelper.getStatus(status));
            this.updateById(record);
        }
    }

    @Override
    public Long getIdByCode(String dataCode) {
        PmPurchaseOrder order = this.getByCode(dataCode);
        return order != null ? order.getId() : null;
    }

//    private void setData(PmPurchaseOrderVo purchaseOrderVo,Map<String, SysChangeRecord> lastChangeRecordMap){
//        SysChangeRecord sysChangeRecord = lastChangeRecordMap.get(purchaseOrderVo.getPurchaseOrderCode());
//        if (sysChangeRecord != null) {
//            purchaseOrderVo.setOriginalMoney();
//            purchaseOrderVo.setChangeMoney();
//        }
//    }


}

