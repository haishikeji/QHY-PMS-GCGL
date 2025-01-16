package com.px.purchasingmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.commoditymanagement.entity.CmMaterialIntoWarehouseDiscount;
import com.px.commoditymanagement.entity.CmMaterialRefundWarehouse;
import com.px.commoditymanagement.entity.CmMaterialRefundWarehouseDetail;
import com.px.commoditymanagement.service.CmMaterialIntoWarehouseDiscountService;
import com.px.commoditymanagement.service.CmMaterialRefundWarehouseDetailService;
import com.px.commoditymanagement.service.CmMaterialRefundWarehouseService;
import com.px.common.consts.SysCodeConst;
import com.px.common.consts.SysFileConst;
import com.px.common.helper.BpmStatusHelper;
import com.px.common.util.CollUtils;
import com.px.common.util.MoneyUtil;
import com.px.common.util.security.SecurityUtils;
import com.px.engineeringsupervision.entity.EsConstructionBudgetDetail;
import com.px.engineeringsupervision.service.EsConstructionBudgetDetailService;
import com.px.engineeringsupervision.service.EsEngineeringPlanDetailService;
import com.px.financialmanagement.bean.dto.FmPaymentRequestAndSettleUpdateDto;
import com.px.financialmanagement.bean.dto.FmPaymentRequestUpdateDto;
import com.px.financialmanagement.entity.FmInvoiceRegister;
import com.px.financialmanagement.entity.FmPaymentRequest;
import com.px.financialmanagement.entity.FmPaymentRequestDetail;
import com.px.financialmanagement.service.FmInvoiceRegisterService;
import com.px.financialmanagement.service.FmPaymentRecordService;
import com.px.financialmanagement.service.FmPaymentRequestDetailService;
import com.px.financialmanagement.service.FmPaymentRequestService;
import com.px.purchasingmanagement.bean.dto.PmPurchaseSettleQueryDto;
import com.px.purchasingmanagement.bean.dto.PmPurchaseSettleUpdateDto;
import com.px.purchasingmanagement.bean.vo.PmPurchaseSettleVo;
import com.px.purchasingmanagement.bean.vo.PurchaseSettleCountDataVo;
import com.px.purchasingmanagement.entity.*;
import com.px.purchasingmanagement.mapper.PmPurchaseSettleMapper;
import com.px.purchasingmanagement.querycondition.PmPurchaseSettleQueryCondition;
import com.px.purchasingmanagement.service.*;
import com.px.resourcecenter.service.RcProjectService;
import com.px.resourcecenter.service.RcSupplierService;
import com.px.system.business.CodeBusiness;
import com.px.system.service.SysFileService;
import com.px.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;

/**
 * PmPurchaseSettleServiceImpl 服务实现类
 *
 * @author 品讯科技
 * @since 2023-12-19
 */
@Slf4j
@Service
public class PmPurchaseSettleServiceImpl extends ServiceImpl<PmPurchaseSettleMapper, PmPurchaseSettle> implements PmPurchaseSettleService {

    @Autowired
    private PmPurchaseSettleDetailService purchaseSettleDetailService;
    @Autowired
    private SysFileService fileService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private RcSupplierService supplierService;
    @Autowired
    private PmPurchaseOrderService purchaseOrderService;
    @Autowired
    private PmPurchaseContractService purchaseContractService;
    @Autowired
    private PmLeaseContractService leaseContractService;
    @Autowired
    private PmSubpackageContractService subpackageContractService;
    @Autowired
    private FmPaymentRequestService paymentRequestService;
    @Autowired
    private FmPaymentRecordService paymentRecordService;
    @Autowired
    private CmMaterialRefundWarehouseService materialRefundWarehouseService;
    @Autowired
    private CmMaterialRefundWarehouseDetailService materialRefundWarehouseDetailService;
    @Autowired
    private FmInvoiceRegisterService invoiceRegisterService;
    @Autowired
    private CodeBusiness codeBusiness;
    @Autowired
    private EsConstructionBudgetDetailService constructionBudgetDetailService;
    @Autowired
    private CmMaterialIntoWarehouseDiscountService materialIntoWarehouseDiscountService;
    @Autowired
    private FmPaymentRequestDetailService paymentRequestDetailService;
    @Autowired
    private EsEngineeringPlanDetailService engineeringPlanDetailService;
    @Autowired
    private PmBatchPurchaseContractSettleService batchPurchaseContractSettleService;
    @Autowired
    private PmBatchPurchaseContractSettleDetailService batchPurchaseContractSettleDetailService;
    @Autowired
    private RcProjectService projectService;

    @Override
    public Page<PmPurchaseSettleVo> getPage(PmPurchaseSettleQueryDto dto) {
        LambdaQueryWrapper<PmPurchaseSettle> queryWrapper = PmPurchaseSettleQueryCondition.build(dto);
        if (StringUtils.isNotBlank(dto.getBatchPurchaseContractSettleCode())) {
            List<String> codeList = new ArrayList<>();
            codeList.add("-1");
            PmBatchPurchaseContractSettle pmBatchPurchaseContractSettle = batchPurchaseContractSettleService.getOne(new LambdaQueryWrapper<PmBatchPurchaseContractSettle>()
                    .eq(PmBatchPurchaseContractSettle::getBatchPurchaseContractSettleCode, dto.getBatchPurchaseContractSettleCode())
                    .eq(PmBatchPurchaseContractSettle::getIsDel, 0));
            if (pmBatchPurchaseContractSettle != null) {
                List<PmBatchPurchaseContractSettleDetail> purchaseContractSettleDetails = batchPurchaseContractSettleDetailService.list(new LambdaQueryWrapper<PmBatchPurchaseContractSettleDetail>()
                        .eq(PmBatchPurchaseContractSettleDetail::getBatchPurchaseContractSettleCode, dto.getBatchPurchaseContractSettleCode()));
                codeList = CollUtils.getDefaultStrVal(CollUtils.toList(purchaseContractSettleDetails, x -> x.getSettleCode()));
            }
            queryWrapper.in(PmPurchaseSettle::getSettleCode, codeList);
        }
        Page<PmPurchaseSettle> page = this.page(new Page(dto.getPageNum(), dto.getPageSize()), queryWrapper);
        Page<PmPurchaseSettleVo> voPage = new Page<>();
        BeanUtils.copyProperties(page, voPage);
        List<PmPurchaseSettle> records = page.getRecords();
        List<PmPurchaseSettleVo> voList = this.buildPmPurchaseSettleVoList(records);
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<PmPurchaseSettleVo> buildPmPurchaseSettleVoList(List<PmPurchaseSettle> list) {
        List<PmPurchaseSettleVo> voList = null;
        if (!CollectionUtils.isEmpty(list)) {
            voList = new ArrayList<>(list.size());
            Set<String> supplierCodeSet = CollUtils.toSet(list, x -> x.getSupplierCode());
            Map<String, String> supplierNameMap = supplierService.getNameByCodes(new ArrayList<>(supplierCodeSet));
            Set<String> userCodeSet = CollUtils.toSet(list, x -> x.getCreateBy());
            Map<String, String> userNameMap = userService.getNameByCodeList(new ArrayList<>(userCodeSet));
            Set<Long> idList = CollUtils.toSet(list, x -> x.getConstructionBudgetDetailId());
            Map<Long, EsConstructionBudgetDetail> budgetMap = null;
            if (CollUtils.isNotEmpty(idList)) {
                List<EsConstructionBudgetDetail> budgetDetailList = constructionBudgetDetailService.listByIds(idList);
                budgetMap = CollUtils.toMap(budgetDetailList, x -> x.getId(), Function.identity());
            }
            List<FmInvoiceRegister> invoiceRegisterList = invoiceRegisterService.list(new LambdaQueryWrapper<FmInvoiceRegister>()
                    .in(FmInvoiceRegister::getSettleCode, CollUtils.getDefaultStrVal(CollUtils.toList(list, x -> x.getSettleCode())))
                    .eq(FmInvoiceRegister::getIsDel, 0));
            Map<String, List<FmInvoiceRegister>> dataMap = CollUtils.groupByKey(invoiceRegisterList, x -> x.getSettleCode());
            List<FmPaymentRequest> fmPaymentRequestList = paymentRequestService.list(new LambdaQueryWrapper<FmPaymentRequest>()
                            .in(FmPaymentRequest::getDataCode, CollUtils.getDefaultStrVal(CollUtils.toList(list, x -> x.getDataCode())))
//                    .eq(FmPaymentRequest::getFlowStatus, 4)
            );
            Map<String, List<FmPaymentRequest>> fmPaymentRequestListMap = CollUtils.groupByKey(fmPaymentRequestList, x -> x.getDataCode());
            Map<String, String> nameMaps = projectService.getCustomerNameByProjectCode(CollUtils.getDefaultStrVal(CollUtils.toList(list, x -> x.getProjectCode())));

            for (PmPurchaseSettle pmPurchaseSettle : list) {
                PmPurchaseSettleVo vo = new PmPurchaseSettleVo();
                BeanUtils.copyProperties(pmPurchaseSettle, vo);
                vo.setProcurement(vo.getCreateBy());
                vo.setSupplierName(supplierNameMap.get(vo.getSupplierCode()));
                vo.setCreateUserName(userNameMap.get(vo.getCreateBy()));
                vo.setCustomerName(nameMaps.get(vo.getProjectCode()));
                if (CollUtils.isNotEmpty(budgetMap)) {
                    EsConstructionBudgetDetail budgetDetail = budgetMap.get(vo.getConstructionBudgetDetailId());
                    if (budgetDetail != null) {
                        vo.setConstructionBudgetVal(budgetDetail.getTotalPrice());
                        vo.setCostSubject(budgetDetail.getCostSubject());
                    }
                }
                List<FmInvoiceRegister> fmInvoiceRegisters = dataMap.get(vo.getSettleCode());
                Integer invoiceFlag = 0;
                Double invoiceMoneyBeforeTaxes = 0.;
                if (CollUtils.isNotEmpty(fmInvoiceRegisters)) {
                    invoiceFlag = 1;
                    invoiceMoneyBeforeTaxes += fmInvoiceRegisters.stream().map(x -> x.getInvoiceMoney()).reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                }
                vo.setInvoiceFlag(invoiceFlag);
                vo.setInvoiceMoneyBeforeTaxes(invoiceMoneyBeforeTaxes);
                Double totalPaymentMoney = 0.;
                Double totalPaymentRatio = 0.;
                if (CollUtils.isNotEmpty(fmPaymentRequestListMap)) {
                    List<FmPaymentRequest> fmPaymentRequests = fmPaymentRequestListMap.get(pmPurchaseSettle.getDataCode());
                    List<String> paymentRequestCodeList = CollUtils.toList(fmPaymentRequests, x -> x.getPaymentRequestCode());
                    List<FmPaymentRequestDetail> paymentRequestDetails = paymentRequestDetailService.listByPaymentRequestCodes(CollUtils.getDefaultStrVal(paymentRequestCodeList));
                    if (CollUtils.isNotEmpty(paymentRequestDetails)) {
                        totalPaymentMoney = paymentRequestDetails.stream()
                                .filter(x -> x.getPaymentMoney() != null)
                                .map(x -> x.getPaymentMoney())
                                .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                        totalPaymentRatio = paymentRequestDetails.stream()
                                .filter(x -> StringUtils.isNotBlank(x.getPaymentRatio()))
                                .map(x -> Double.parseDouble(x.getPaymentRatio().replace("%", "")))
                                .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                    }
                }
                vo.setTotalFmPaymentRequestMoney(totalPaymentMoney);
                vo.setTotalFmPaymentRequestRatio(totalPaymentRatio + "%");
                vo.setInvoiceMoney(vo.getSettleMoneyBeforeTaxes());
                voList.add(vo);
            }
        }
        return voList;
    }

    @Override
    public PmPurchaseSettle buildPmPurchaseSettle(PmPurchaseOrder purchaseOrder) {
        PmPurchaseSettle pmPurchaseSettle = new PmPurchaseSettle();
        pmPurchaseSettle.setSettleCode(codeBusiness.genCode(SysCodeConst.CGDDJS));
        pmPurchaseSettle.setDepCode(purchaseOrder.getDepCode());
        pmPurchaseSettle.setProjectCode(purchaseOrder.getProjectCode());
        pmPurchaseSettle.setProjectName(purchaseOrder.getProjectName());
        pmPurchaseSettle.setDataCode(purchaseOrder.getPurchaseOrderCode());
        pmPurchaseSettle.setDataType(1);
        pmPurchaseSettle.setSupplierCode(purchaseOrder.getSupplierCode());
        pmPurchaseSettle.setSettleType(2);
        pmPurchaseSettle.setConstructionBudgetDetailId(purchaseOrder.getConstructionBudgetDetailId());

        pmPurchaseSettle.setOrderMoneyBeforeTaxes(purchaseOrder.getOrderMoneyBeforeTaxes());
        pmPurchaseSettle.setOrderMoneyAfterTaxes(purchaseOrder.getOrderMoneyAfterTaxes());
        pmPurchaseSettle.setTaxesMoney(purchaseOrder.getTaxesMoney());

        pmPurchaseSettle.setSettleMoneyBeforeTaxes(0.0D);
        pmPurchaseSettle.setSettleMoneyAfterTaxes(0.0D);
        pmPurchaseSettle.setWithholdType("");
        pmPurchaseSettle.setWithholdRemark("");
        pmPurchaseSettle.setCurrentWithholdMoney(0.0D);
        pmPurchaseSettle.setInvoiceFlag(0);
        pmPurchaseSettle.setInvoiceType("");
        pmPurchaseSettle.setTaxesRatio("");
        pmPurchaseSettle.setInvoiceCode("");
        pmPurchaseSettle.setInvoiceMoneyBeforeTaxes(0.0D);
        pmPurchaseSettle.setInvoiceMoneyAfterTaxes(0.0D);
        pmPurchaseSettle.setRemark("");
        pmPurchaseSettle.setStatus(0);
        setPurchaseSettleData(pmPurchaseSettle, purchaseOrder.getPurchaseOrderCode());
        //累计折扣金额
        pmPurchaseSettle.setTotalDiscountMoney(countDiscountMoney(pmPurchaseSettle.getDataCode()));
        return pmPurchaseSettle;
    }

    private void setPurchaseSettleData(PmPurchaseSettle pmPurchaseSettle, String dataCode) {
        pmPurchaseSettle.setFlowStatus(1);
        pmPurchaseSettle.setBatchFlag(1);
        pmPurchaseSettle.setCreateTime(new Date());
        pmPurchaseSettle.setCreateBy(SecurityUtils.getUserCodeAndName());
        //累计结算金额-根据采购单号查询已经结算的数据，查询结算表
        pmPurchaseSettle.setTotalSettleMoney(countTotalSettleMoney(dataCode));
        //累计已付款金额-根据采购单号查询已经付款的数据，查询财务管理-付款申请
        pmPurchaseSettle.setTotalPaymentMoney(countTotalPaymentMoney(dataCode));
        pmPurchaseSettle.setTotalWithholdMoney(countTotalWithholdMoney(dataCode));
        //累计退货金额，查询退货单的金额
        pmPurchaseSettle.setTotalRefundMoney(countTotalRefundMoney(dataCode));
        //累计已收票金额，查询财务管理-收票管理
        pmPurchaseSettle.setTotalReceiveMoney(countTotalReceiveMoney(dataCode));
        //本次已收票金额-就是对应的发票金额
        pmPurchaseSettle.setCurrentReceiveMoney(0.0D);
    }

    private Double countDiscountMoney(String dataCode) {
        Double result = 0.;
        List<CmMaterialIntoWarehouseDiscount> list = materialIntoWarehouseDiscountService.list(new LambdaQueryWrapper<CmMaterialIntoWarehouseDiscount>()
                .eq(CmMaterialIntoWarehouseDiscount::getPurchaseCode, dataCode)
                .eq(CmMaterialIntoWarehouseDiscount::getFlowStatus, 4)
                .eq(CmMaterialIntoWarehouseDiscount::getIsDel, 0));
        if (CollUtils.isNotEmpty(list)) {
            if (CollUtils.isNotEmpty(list)) {
                result += list.stream()
                        .filter(x -> StringUtils.isNotBlank(x.getDiscountData()))
                        .map(x -> Double.parseDouble(x.getDiscountData()))
                        .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
            }
        }
        return result;
    }


//    private Double countDiscountMoney(String dataCode, Integer type) {
//        Double result = 0.;
//        if (type == 1) {
//            PmPurchaseOrder purchaseOrder = purchaseOrderService.getById(dataCode);
//            List<CmMaterialIntoWarehouseDiscount> list = materialIntoWarehouseDiscountService.list(new LambdaQueryWrapper<CmMaterialIntoWarehouseDiscount>()
//                    .eq(CmMaterialIntoWarehouseDiscount::getPurchaseCode, dataCode)
//                    .eq(CmMaterialIntoWarehouseDiscount::getFlowStatus, 4)
//                    .eq(CmMaterialIntoWarehouseDiscount::getIsDel, 0));
//            if (purchaseOrder != null && CollUtils.isNotEmpty(list)) {
//                Map<Integer, List<CmMaterialIntoWarehouseDiscount>> integerListMap = CollUtils.groupByKey(list, x -> x.getDiscountType());
//                List<CmMaterialIntoWarehouseDiscount> cmMaterialIntoWarehouseDiscounts1 = integerListMap.get(1);
//                if (CollUtils.isNotEmpty(cmMaterialIntoWarehouseDiscounts1)) {
//                    result += cmMaterialIntoWarehouseDiscounts1.stream()
//                            .filter(x -> StringUtils.isNotBlank(x.getDiscountData()))
//                            .map(x -> Double.parseDouble(x.getDiscountData()))
//                            .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
//                }
//                List<CmMaterialIntoWarehouseDiscount> cmMaterialIntoWarehouseDiscounts2 = integerListMap.get(2);
//                if (CollUtils.isNotEmpty(cmMaterialIntoWarehouseDiscounts2)) {
//                    for (CmMaterialIntoWarehouseDiscount discount : cmMaterialIntoWarehouseDiscounts2) {
//                        String discountData = discount.getDiscountData();
//                        if (StringUtils.isNotBlank(discountData)) {
//                            result += new BigDecimal(purchaseOrder.getOrderMoneyBeforeTaxes())
//                                    .multiply(new BigDecimal(discountData.replace("%", "")))
//                                    .multiply(new BigDecimal("0.01")).doubleValue();
//                        }
//                    }
//                }
//            }
//        } else {
//            PmPurchaseContract purchaseContract = purchaseContractService.getByCode(dataCode);
//            List<CmMaterialIntoWarehouseDiscount> list = materialIntoWarehouseDiscountService.list(new LambdaQueryWrapper<CmMaterialIntoWarehouseDiscount>()
//                    .eq(CmMaterialIntoWarehouseDiscount::getPurchaseCode, dataCode)
//                    .eq(CmMaterialIntoWarehouseDiscount::getFlowStatus, 4)
//                    .eq(CmMaterialIntoWarehouseDiscount::getIsDel, 0));
//            if (purchaseContract != null && CollUtils.isNotEmpty(list)) {
//                Map<Integer, List<CmMaterialIntoWarehouseDiscount>> integerListMap = CollUtils.groupByKey(list, x -> x.getDiscountType());
//                List<CmMaterialIntoWarehouseDiscount> cmMaterialIntoWarehouseDiscounts1 = integerListMap.get(1);
//                if (CollUtils.isNotEmpty(cmMaterialIntoWarehouseDiscounts1)) {
//                    result += cmMaterialIntoWarehouseDiscounts1.stream()
//                            .filter(x -> StringUtils.isNotBlank(x.getDiscountData()))
//                            .map(x -> Double.parseDouble(x.getDiscountData()))
//                            .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
//                }
//                List<CmMaterialIntoWarehouseDiscount> cmMaterialIntoWarehouseDiscounts2 = integerListMap.get(2);
//                if (CollUtils.isNotEmpty(cmMaterialIntoWarehouseDiscounts2)) {
//                    for (CmMaterialIntoWarehouseDiscount discount : cmMaterialIntoWarehouseDiscounts2) {
//                        String discountData = discount.getDiscountData();
//                        if (StringUtils.isNotBlank(discountData)) {
//                            result += new BigDecimal(purchaseContract.getOrderMoneyBeforeTaxes())
//                                    .multiply(new BigDecimal(discountData.replace("%", "")))
//                                    .multiply(new BigDecimal("0.01")).doubleValue();
//                        }
//                    }
//                }
//            }
//        }
//        return result;
//    }

    private Double countTotalReceiveMoney(String dataCode) {
        List<FmInvoiceRegister> list = invoiceRegisterService.list(new LambdaQueryWrapper<FmInvoiceRegister>()
                .eq(FmInvoiceRegister::getDataCode, dataCode).eq(FmInvoiceRegister::getIsDel, 0));
        if (CollUtils.isNotEmpty(list)) {
            return list.stream().filter(x -> x.getInvoiceMoney() != null)
                    .map(x -> x.getInvoiceMoney())
                    .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
        }
        return 0.;
    }

    private Double countTotalRefundMoney(String dataCode) {
        List<CmMaterialRefundWarehouse> list = materialRefundWarehouseService.list(new LambdaQueryWrapper<CmMaterialRefundWarehouse>()
                .eq(CmMaterialRefundWarehouse::getDataCode, dataCode)
                .eq(CmMaterialRefundWarehouse::getFlowStatus, 4)
                .eq(CmMaterialRefundWarehouse::getIsDel, 0));
        if (CollUtils.isNotEmpty(list)) {
            List<String> codeList = CollUtils.toList(list, x -> x.getMaterialRefundWarehouseCode());
            List<CmMaterialRefundWarehouseDetail> detailList = materialRefundWarehouseDetailService.list(new LambdaQueryWrapper<CmMaterialRefundWarehouseDetail>()
                    .in(CmMaterialRefundWarehouseDetail::getMaterialRefundWarehouseCode, codeList));
            if (CollUtils.isNotEmpty(detailList)) {
                return detailList.stream()
                        .filter(x -> x.getTotalPriceBeforeTaxes() != null)
                        .map(x -> x.getTotalPriceBeforeTaxes())
                        .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
            }
        }
        return 0.;
    }

    private Double countTotalPaymentMoney(String dataCode) {
        List<FmPaymentRequest> paymentRequests = paymentRequestService.list(new LambdaQueryWrapper<FmPaymentRequest>()
                .eq(FmPaymentRequest::getDataCode, dataCode)
                .eq(FmPaymentRequest::getFlowStatus, 4));
        List<FmPaymentRequestDetail> paymentRequestDetails = paymentRequestDetailService.listByPaymentRequestCodes(CollUtils.getDefaultStrVal(CollUtils.toList(paymentRequests, x -> x.getPaymentRequestCode())));
//        List<FmPaymentRecord> paymentRecordList = paymentRecordService.list(new LambdaQueryWrapper<FmPaymentRecord>()
//                .in(FmPaymentRecord::getDataCode, CollUtils.getDefaultStrVal(CollUtils.toList(paymentRequests, x -> x.getPaymentRequestCode()))));
        if (CollUtils.isNotEmpty(paymentRequestDetails)) {
            return paymentRequestDetails.stream()
                    .map(x -> x.getPaymentMoney())
                    .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
        }
        return 0.;
    }

    private Double countTotalSettleMoney(String dataCode) {
        List<PmPurchaseSettle> purchaseSettleList = this.list(new LambdaQueryWrapper<PmPurchaseSettle>()
                .eq(PmPurchaseSettle::getDataCode, dataCode)
                .eq(PmPurchaseSettle::getFlowStatus, 4)
                .eq(PmPurchaseSettle::getIsDel, 0));
        if (CollUtils.isNotEmpty(purchaseSettleList)) {
            return purchaseSettleList.stream()
                    .filter(x -> x.getSettleMoneyBeforeTaxes() != null)
                    .map(x -> x.getSettleMoneyBeforeTaxes())
                    .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
        }
        return 0.;
    }

    @Override
    public PmPurchaseSettle buildPmPurchaseSettle(PmPurchaseContract pmPurchaseContract) {
        PmPurchaseSettle pmPurchaseSettle = new PmPurchaseSettle();
        pmPurchaseSettle.setSettleCode(codeBusiness.genCode(SysCodeConst.CGHTJS));
        pmPurchaseSettle.setDepCode(pmPurchaseContract.getDepCode());
        pmPurchaseSettle.setProjectCode(pmPurchaseContract.getProjectCode());
        pmPurchaseSettle.setProjectName(pmPurchaseContract.getProjectName());
        pmPurchaseSettle.setDataCode(pmPurchaseContract.getPurchaseContractCode());
        pmPurchaseSettle.setDataType(2);
        pmPurchaseSettle.setSupplierCode(pmPurchaseContract.getSupplierCode());
        pmPurchaseSettle.setSettleType(2);
        pmPurchaseSettle.setConstructionBudgetDetailId(pmPurchaseContract.getConstructionBudgetDetailId());

        pmPurchaseSettle.setOrderMoneyBeforeTaxes(pmPurchaseContract.getOrderMoneyBeforeTaxes());
        pmPurchaseSettle.setOrderMoneyAfterTaxes(pmPurchaseContract.getOrderMoneyAfterTaxes());
        pmPurchaseSettle.setTaxesMoney(pmPurchaseContract.getTaxesMoney());
        pmPurchaseSettle.setWithholdType("");
        pmPurchaseSettle.setWithholdRemark("");
        pmPurchaseSettle.setCurrentWithholdMoney(0.0D);
        pmPurchaseSettle.setSettleMoneyBeforeTaxes(0.0D);
        pmPurchaseSettle.setSettleMoneyAfterTaxes(0.0D);

        pmPurchaseSettle.setInvoiceFlag(0);
        pmPurchaseSettle.setInvoiceType("");
        pmPurchaseSettle.setTaxesRatio("");
        pmPurchaseSettle.setInvoiceCode("");
        pmPurchaseSettle.setInvoiceMoneyBeforeTaxes(0.0D);
        pmPurchaseSettle.setInvoiceMoneyAfterTaxes(0.0D);

        pmPurchaseSettle.setRemark("");
        pmPurchaseSettle.setStatus(0);
        setPurchaseSettleData(pmPurchaseSettle, pmPurchaseContract.getPurchaseContractCode());
        //累计折扣金额
        pmPurchaseSettle.setTotalDiscountMoney(countDiscountMoney(pmPurchaseSettle.getDataCode()));
        return pmPurchaseSettle;
    }

    @Override
    public PmPurchaseSettle buildPmPurchaseSettle(PmLeaseContract pmLeaseContract) {
        PmPurchaseSettle pmPurchaseSettle = new PmPurchaseSettle();
        pmPurchaseSettle.setSettleCode(codeBusiness.genCode(SysCodeConst.ZLHTJS));
        pmPurchaseSettle.setDepCode(pmLeaseContract.getDepCode());
        pmPurchaseSettle.setProjectCode(pmLeaseContract.getProjectCode());
        pmPurchaseSettle.setProjectName(pmLeaseContract.getProjectName());
        pmPurchaseSettle.setDataCode(pmLeaseContract.getLeaseContractCode());
        pmPurchaseSettle.setDataType(3);
        pmPurchaseSettle.setSupplierCode(pmLeaseContract.getSupplierCode());
        pmPurchaseSettle.setSettleType(2);
        pmPurchaseSettle.setConstructionBudgetDetailId(pmLeaseContract.getConstructionBudgetDetailId());

        pmPurchaseSettle.setSettleMoneyBeforeTaxes(0.0D);
        pmPurchaseSettle.setSettleMoneyAfterTaxes(0.0D);

        pmPurchaseSettle.setOrderMoneyBeforeTaxes(pmLeaseContract.getContractMoneyBeforeTaxes());
        pmPurchaseSettle.setOrderMoneyAfterTaxes(pmLeaseContract.getContractMoneyAfterTaxes());
        pmPurchaseSettle.setTaxesMoney(pmLeaseContract.getTaxesMoney());

        pmPurchaseSettle.setWithholdType("");
        pmPurchaseSettle.setWithholdRemark("");
        pmPurchaseSettle.setCurrentWithholdMoney(0.0D);
        pmPurchaseSettle.setInvoiceFlag(0);
        pmPurchaseSettle.setInvoiceType("");
        pmPurchaseSettle.setTaxesRatio("");
        pmPurchaseSettle.setInvoiceCode("");
        pmPurchaseSettle.setInvoiceMoneyBeforeTaxes(0.0D);
        pmPurchaseSettle.setInvoiceMoneyAfterTaxes(0.0D);

        pmPurchaseSettle.setRemark("");
        pmPurchaseSettle.setStatus(0);
        setPurchaseSettleData(pmPurchaseSettle, pmLeaseContract.getLeaseContractCode());
        return pmPurchaseSettle;
    }

    @Override
    public PmPurchaseSettle buildPmPurchaseSettle(PmSubpackageContract pmSubpackageContract) {
        PmPurchaseSettle pmPurchaseSettle = new PmPurchaseSettle();
        pmPurchaseSettle.setSettleCode(codeBusiness.genCode(SysCodeConst.FBHTJS));
        pmPurchaseSettle.setDepCode(pmSubpackageContract.getDepCode());
        pmPurchaseSettle.setProjectCode(pmSubpackageContract.getProjectCode());
        pmPurchaseSettle.setProjectName(pmSubpackageContract.getProjectName());
        pmPurchaseSettle.setDataCode(pmSubpackageContract.getSubpackageContractCode());
        pmPurchaseSettle.setDataType(4);
        pmPurchaseSettle.setSupplierCode(pmSubpackageContract.getSupplierCode());
        pmPurchaseSettle.setSettleType(2);
        pmPurchaseSettle.setConstructionBudgetDetailId(pmSubpackageContract.getConstructionBudgetDetailId());

        pmPurchaseSettle.setSettleMoneyBeforeTaxes(0.0D);
        pmPurchaseSettle.setSettleMoneyAfterTaxes(0.0D);

        pmPurchaseSettle.setOrderMoneyBeforeTaxes(pmSubpackageContract.getSubpackageMoneyBeforeTaxes());
        pmPurchaseSettle.setOrderMoneyAfterTaxes(pmSubpackageContract.getSubpackageMoneyAfterTaxes());
        pmPurchaseSettle.setTaxesMoney(pmSubpackageContract.getTaxesMoney());

        pmPurchaseSettle.setWithholdType("");
        pmPurchaseSettle.setWithholdRemark("");
        pmPurchaseSettle.setCurrentWithholdMoney(0.0D);
        pmPurchaseSettle.setInvoiceFlag(0);
        pmPurchaseSettle.setInvoiceType("");
        pmPurchaseSettle.setTaxesRatio("");
        pmPurchaseSettle.setInvoiceCode("");
        pmPurchaseSettle.setInvoiceMoneyBeforeTaxes(0.0D);
        pmPurchaseSettle.setInvoiceMoneyAfterTaxes(0.0D);

        pmPurchaseSettle.setRemark("");
        pmPurchaseSettle.setStatus(0);
        setPurchaseSettleData(pmPurchaseSettle, pmSubpackageContract.getSubpackageContractCode());
        return pmPurchaseSettle;
    }

    @Override
    public List<PmPurchaseSettle> listByCodes(List<String> codeList) {
        return this.list(new LambdaQueryWrapper<PmPurchaseSettle>()
                .in(PmPurchaseSettle::getSettleCode, codeList).eq(PmPurchaseSettle::getIsDel, 0));
    }

    @Override
    public Double countTotalWithholdMoney(String dataCode) {
        List<PmPurchaseSettle> purchaseSettleList = this.list(new LambdaQueryWrapper<PmPurchaseSettle>()
                .eq(PmPurchaseSettle::getDataCode, dataCode));
        Double money = 0.;
        if (CollUtils.isNotEmpty(purchaseSettleList)) {
            money = purchaseSettleList.stream().map(x -> x.getCurrentWithholdMoney())
                    .reduce((x, y) -> (MoneyUtil.sum(x, y))).get();
        }
        return money;
    }

    @Override
    public FmPaymentRequestUpdateDto buildFmPaymentRequestUpdateDto(PmPurchaseSettle pmPurchaseSettle) {
        FmPaymentRequestUpdateDto dto = new FmPaymentRequestUpdateDto();
        dto.setPaymentRequestCode(codeBusiness.genCode(SysCodeConst.FKSQ));
        dto.setDepCode(pmPurchaseSettle.getDepCode());
        dto.setProjectName(pmPurchaseSettle.getProjectName());
        dto.setProjectCode(pmPurchaseSettle.getProjectCode());
        //1.采购订单，2.采购合同，3.租赁合同，4.分包合同
        Integer dataType = pmPurchaseSettle.getDataType();
        String dataCode = pmPurchaseSettle.getDataCode();
        Double contractMoney = 0.;
        if (dataType != null && StringUtils.isNotBlank(dataCode)) {
            if (dataType == 1) {
                PmPurchaseOrder record = purchaseOrderService.getByCode(dataCode);
                if (record == null) {
                    contractMoney = record.getOrderMoneyBeforeTaxes();
                }
            } else if (dataType == 2) {
                PmPurchaseContract record = purchaseContractService.getByCode(dataCode);
                if (record == null) {
                    contractMoney = record.getOrderMoneyBeforeTaxes();
                }
            } else if (dataType == 3) {
                PmLeaseContract record = leaseContractService.getByCode(dataCode);
                if (record == null) {
                    contractMoney = record.getContractMoneyBeforeTaxes();
                }
            } else {
                PmSubpackageContract record = subpackageContractService.getByCode(dataCode);
                if (record == null) {
                    contractMoney = record.getSubpackageMoneyBeforeTaxes();
                }
            }
        }
        dto.setSettleCode(pmPurchaseSettle.getSettleCode());
        dto.setDataType(dataType);
        dto.setDataCode(dataCode);
        dto.setContractMoney(contractMoney);
        dto.setSupplierCode(pmPurchaseSettle.getSupplierCode());
        dto.setSupplierName(pmPurchaseSettle.getSupplierName());
        dto.setCreateTime(new Date());
        dto.setCreateBy(SecurityUtils.getUserCodeAndName());
        return dto;
    }

    @Override
    public void updateByFlowCode(String instanceId, Integer status) {
        PmPurchaseSettle record = this.getOne(new LambdaQueryWrapper<PmPurchaseSettle>().eq(PmPurchaseSettle::getFlowCode, instanceId));
        if (record != null) {
            if (record.getFlowStatus() == 4 && BpmStatusHelper.getStatus(status) == 4) {
                return;
            }
            record.setFlowStatus(BpmStatusHelper.getStatus(status));
            this.updateById(record);
        }
    }

    @Override
    public PurchaseSettleCountDataVo getCountData(String dataCode, Integer type) {
        PurchaseSettleCountDataVo dataVo = new PurchaseSettleCountDataVo();
        //累计结算金额-根据采购单号查询已经结算的数据，查询结算表
        dataVo.setTotalSettleMoney(countTotalSettleMoney(dataCode));
        //累计已付款金额-根据采购单号查询已经付款的数据，查询财务管理-付款申请
        dataVo.setTotalPaymentMoney(countTotalPaymentMoney(dataCode));
        dataVo.setTotalWithholdMoney(countTotalWithholdMoney(dataCode));
        //累计退货金额，查询退货单的金额
        dataVo.setTotalRefundMoney(countTotalRefundMoney(dataCode));
        //累计已收票金额，查询财务管理-收票管理
        dataVo.setTotalReceiveMoney(countTotalReceiveMoney(dataCode));
        //累计折扣金额
        dataVo.setTotalDiscountMoney(countDiscountMoney(dataCode));
        return dataVo;
    }

    @Override
    public Map<String, Double> countSettleDataByPurchaseCode(String code) {
        List<PmPurchaseSettle> purchaseSettles = this.list(new LambdaQueryWrapper<PmPurchaseSettle>().eq(PmPurchaseSettle::getIsDel, 0)
                .eq(PmPurchaseSettle::getDataCode, code));
        List<PmPurchaseSettleDetail> pmPurchaseSettleDetails = purchaseSettleDetailService.listBySettleCodes(CollUtils.getDefaultStrVal(CollUtils.toList(purchaseSettles, x -> x.getSettleCode())));
        Map<String, List<PmPurchaseSettleDetail>> pmPurchaseSettleDetailsMap = CollUtils.groupByKey(pmPurchaseSettleDetails, x -> x.getMaterialCode());
        HashMap<String, Double> dataMap = new HashMap<>();
        for (Map.Entry<String, List<PmPurchaseSettleDetail>> entry : pmPurchaseSettleDetailsMap.entrySet()) {
            List<PmPurchaseSettleDetail> value = entry.getValue();
            Double num = 0.;
            if (CollUtils.isNotEmpty(value)) {
                num = value.stream().map(x -> x.getSettleNum()).reduce(0., (x, y) -> MoneyUtil.sum(x, y));
            }
            dataMap.put(entry.getKey(), num);
        }
        return dataMap;
    }

//    @Override
//    public Map<String, Map<String, Double>> countSettleDataByPurchaseCodes(List<String> codeList) {
//        List<PmPurchaseSettle> purchaseSettles = this.list(new LambdaQueryWrapper<PmPurchaseSettle>().eq(PmPurchaseSettle::getIsDel, 0)
//                .in(PmPurchaseSettle::getDataCode, codeList));
//        Map<String, String> purchaseSettlesMap = CollUtils.toMap(purchaseSettles, x -> x.getSettleCode(), y -> y.getDataCode());
//        List<PmPurchaseSettleDetail> pmPurchaseSettleDetails = purchaseSettleDetailService.listBySettleCodes(CollUtils.getDefaultStrVal(CollUtils.toList(purchaseSettles, x -> x.getSettleCode())));
//        Map<String, List<PmPurchaseSettleDetail>> listMap = CollUtils.groupByKey(pmPurchaseSettleDetails, x -> x.getSettleCode());
//        Map<String, Map<String, Double>> dataMap = new HashMap<>();
//        for (Map.Entry<String, List<PmPurchaseSettleDetail>> entry : listMap.entrySet()) {
//            List<PmPurchaseSettleDetail> value = entry.getValue();
//            Map<String, Double> dataNumMap = new HashMap<>();
//            if (CollUtils.isNotEmpty(value)) {
//                Map<String, List<PmPurchaseSettleDetail>> pmPurchaseSettleDetailsMap = CollUtils.groupByKey(value, x -> x.getMaterialCode());
//                for (Map.Entry<String, List<PmPurchaseSettleDetail>> entry1 : pmPurchaseSettleDetailsMap.entrySet()) {
//                    List<PmPurchaseSettleDetail> value1 = entry1.getValue();
//                    Double num = 0.;
//                    if (CollUtils.isNotEmpty(value1)) {
//                        num = value1.stream().map(x -> x.getSettleNum()).reduce(0., (x, y) -> MoneyUtil.sum(x, y));
//                    }
//                    dataNumMap.put(entry1.getKey(), num);
//                }
//            }
//            dataMap.put(purchaseSettlesMap.get(entry.getKey()), dataNumMap);
//        }
//        return dataMap;
//    }

    @Override
    public Map<String, Map<String, Double>> countSettleDataByPurchaseCodes(List<String> codeList) {
        List<PmPurchaseSettle> purchaseSettles = this.list(new LambdaQueryWrapper<PmPurchaseSettle>().eq(PmPurchaseSettle::getIsDel, 0)
                .in(PmPurchaseSettle::getDataCode, codeList));
        Map<String, List<PmPurchaseSettle>> purchaseSettlesMap = CollUtils.groupByKey(purchaseSettles, x -> x.getDataCode());
        Map<String, Map<String, Double>> dataMap = new HashMap<>();
        for (Map.Entry<String, List<PmPurchaseSettle>> entry : purchaseSettlesMap.entrySet()) {
            List<PmPurchaseSettle> value = entry.getValue();
            Map<String, Double> dataNumMap = new HashMap<>();
            if (CollUtils.isNotEmpty(value)) {
                List<PmPurchaseSettleDetail> pmPurchaseSettleDetails = purchaseSettleDetailService.listBySettleCodes(CollUtils.getDefaultStrVal(CollUtils.toList(value, x -> x.getSettleCode())));
                if (CollUtils.isNotEmpty(pmPurchaseSettleDetails)) {
                    if (CollUtils.isNotEmpty(pmPurchaseSettleDetails)) {
                        Map<String, List<PmPurchaseSettleDetail>> pmPurchaseSettleDetailsMap = CollUtils.groupByKey(pmPurchaseSettleDetails, x -> x.getMaterialCode());
                        for (Map.Entry<String, List<PmPurchaseSettleDetail>> entry1 : pmPurchaseSettleDetailsMap.entrySet()) {
                            List<PmPurchaseSettleDetail> value1 = entry1.getValue();
                            Double num = 0.;
                            if (CollUtils.isNotEmpty(value1)) {
                                num = value1.stream().map(x -> x.getSettleNum()).reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                            }
                            dataNumMap.put(entry1.getKey(), num);
                        }
                    }
                }
            }
            dataMap.put(entry.getKey(), dataNumMap);
        }
        return dataMap;
    }

    @Override
    public Map<String, Map<String, Double>> countSettleDataByPurchaseCodesV2(List<String> codeList) {
        List<PmPurchaseSettle> purchaseSettles = this.list(new LambdaQueryWrapper<PmPurchaseSettle>().eq(PmPurchaseSettle::getIsDel, 0)
                .in(PmPurchaseSettle::getDataCode, codeList));
        Map<String, List<PmPurchaseSettle>> purchaseSettlesMap = CollUtils.groupByKey(purchaseSettles, x -> x.getDataCode());
        Map<String, Map<String, Double>> dataMap = new HashMap<>();
        for (Map.Entry<String, List<PmPurchaseSettle>> entry : purchaseSettlesMap.entrySet()) {
            List<PmPurchaseSettle> value = entry.getValue();
            Map<String, Double> dataNumMap = new HashMap<>();
            if (CollUtils.isNotEmpty(value)) {
                List<PmPurchaseSettleDetail> pmPurchaseSettleDetails = purchaseSettleDetailService.list(new LambdaQueryWrapper<PmPurchaseSettleDetail>()
                        .in(PmPurchaseSettleDetail::getSettleCode, CollUtils.getDefaultStrVal(CollUtils.toList(value, x -> x.getSettleCode())))
                        .eq(PmPurchaseSettleDetail::getRefundFlag, 1));
                if (CollUtils.isNotEmpty(pmPurchaseSettleDetails)) {
                    if (CollUtils.isNotEmpty(pmPurchaseSettleDetails)) {
                        Map<String, List<PmPurchaseSettleDetail>> pmPurchaseSettleDetailsMap = CollUtils.groupByKey(pmPurchaseSettleDetails, x -> x.getMaterialCode());
                        for (Map.Entry<String, List<PmPurchaseSettleDetail>> entry1 : pmPurchaseSettleDetailsMap.entrySet()) {
                            List<PmPurchaseSettleDetail> value1 = entry1.getValue();
                            Double num = 0.;
                            if (CollUtils.isNotEmpty(value1)) {
                                num = value1.stream().map(x -> x.getSettleNum()).reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                            }
                            dataNumMap.put(entry1.getKey(), num);
                        }
                    }
                }
            }
            dataMap.put(entry.getKey(), dataNumMap);
        }
        return dataMap;
    }


    @Override
    public PmPurchaseSettle getByCode(String settleCode) {
        return this.getOne(new LambdaQueryWrapper<PmPurchaseSettle>().eq(PmPurchaseSettle::getSettleCode, settleCode)
                .eq(PmPurchaseSettle::getIsDel, 0));
    }

    @Override
    public PmPurchaseSettleUpdateDto getInfo(Long id) {
        PmPurchaseSettleUpdateDto dto = null;
        PmPurchaseSettle pmPurchaseSettle = this.getById(id);
        if (pmPurchaseSettle != null) {
            dto = new PmPurchaseSettleUpdateDto();
            BeanUtils.copyProperties(pmPurchaseSettle, dto);
            dto.setProcurement(dto.getCreateBy());
            dto.setPmPurchaseSettleDetails(purchaseSettleDetailService.list(new LambdaQueryWrapper<PmPurchaseSettleDetail>()
                    .eq(PmPurchaseSettleDetail::getSettleCode, pmPurchaseSettle.getSettleCode())));
            dto.setFileDataDtos(fileService.getFileDataDtoList(pmPurchaseSettle.getSettleCode(), SysFileConst.PURCHASE_SETTLE_PAGE_FILE));
            EsConstructionBudgetDetail budgetDetail = constructionBudgetDetailService.getById(pmPurchaseSettle.getConstructionBudgetDetailId());
            dto.setCostSubject(budgetDetail != null ? budgetDetail.getCostSubject() : null);
            dto.setInvoiceMoney(dto.getSettleMoneyBeforeTaxes());
        }
        return dto;
    }

    @Override
    public Long getIdByCode(String dataCode) {
        PmPurchaseSettle byCode = this.getByCode(dataCode);
        return byCode != null ? byCode.getId() : null;
    }

    @Override
    public void updateByPaymentRequest(FmPaymentRequestAndSettleUpdateDto dto) {
        try {
            Integer settleType = dto.getSettleType();
            if (settleType == null) {
                return;
            }
            this.remove(new LambdaQueryWrapper<PmPurchaseSettle>()
                    .eq(PmPurchaseSettle::getPaymentRequestCode, dto.getPaymentRequestCode()));
            PmPurchaseSettle pmPurchaseSettle = new PmPurchaseSettle();
            String settleCode = null;
            //1.材料，2.租赁，3.分包
            if (dto.getPrType() == 2) {
                settleCode = codeBusiness.genCode(SysCodeConst.CGHTJS);
            } else if (dto.getPrType() == 3) {
                settleCode = codeBusiness.genCode(SysCodeConst.FBHTJS);
            }
            if (StringUtils.isBlank(settleCode)) {
                return;
            }
            pmPurchaseSettle.setSettleCode(settleCode);
            pmPurchaseSettle.setDepCode(dto.getDepCode());
            pmPurchaseSettle.setDepName(dto.getDepName());
            pmPurchaseSettle.setProjectCode(dto.getProjectCode());
            pmPurchaseSettle.setProjectName(dto.getProjectName());
            pmPurchaseSettle.setDataCode(dto.getDataCode());
            pmPurchaseSettle.setDataType(dto.getDataType());
            pmPurchaseSettle.setSupplierCode(dto.getSupplierCode());
            pmPurchaseSettle.setSupplierName(dto.getSupplierName());
            pmPurchaseSettle.setSettleType(dto.getSettleType());
            pmPurchaseSettle.setConstructionBudgetDetailId(0L);
            pmPurchaseSettle.setSettleMoneyBeforeTaxes(dto.getSettleMoneyBeforeTaxes());
            pmPurchaseSettle.setSettleMoneyAfterTaxes(dto.getSettleMoneyAfterTaxes());
            pmPurchaseSettle.setOrderMoneyBeforeTaxes(dto.getOrderMoneyBeforeTaxes());
            pmPurchaseSettle.setOrderMoneyAfterTaxes(dto.getOrderMoneyAfterTaxes());
            pmPurchaseSettle.setTaxesMoney(dto.getTaxesMoney());
            Double totalSettleMoney = countTotalSettleMoney(dto.getDataCode());
            pmPurchaseSettle.setTotalSettleMoney(totalSettleMoney);
            pmPurchaseSettle.setTotalPaymentMoney(dto.getTotalPaymentMoney());
            pmPurchaseSettle.setTotalWithholdMoney(dto.getTotalWithholdMoney());
            pmPurchaseSettle.setCurrentWithholdMoney(dto.getCurrentWithholdMoney());
            pmPurchaseSettle.setWithholdType(dto.getWithholdType());
            pmPurchaseSettle.setWithholdRemark(dto.getWithholdRemark());
            pmPurchaseSettle.setTotalReceiveMoney(dto.getTotalReceiveMoney());

            pmPurchaseSettle.setTotalRefundMoney(0.0D);
            pmPurchaseSettle.setCurrentReceiveMoney(0.0D);
            pmPurchaseSettle.setInvoiceFlag(0);
            pmPurchaseSettle.setTotalDiscountMoney(0.0D);
            pmPurchaseSettle.setInvoiceType("");
            pmPurchaseSettle.setTaxesRatio(dto.getTaxesRatio());
            pmPurchaseSettle.setInvoiceCode("");
            pmPurchaseSettle.setInvoiceMoneyBeforeTaxes(0.0D);
            pmPurchaseSettle.setInvoiceMoneyAfterTaxes(0.0D);
            pmPurchaseSettle.setRemark("");
            pmPurchaseSettle.setStatus(0);
            pmPurchaseSettle.setFlowStatus(4);
            pmPurchaseSettle.setFlowCode("");
            pmPurchaseSettle.setBatchFlag(0);
            pmPurchaseSettle.setSettledFlag(0);

            pmPurchaseSettle.setCreateTime(new Date());
            pmPurchaseSettle.setCreateBy(SecurityUtils.getUserCodeAndName());
            pmPurchaseSettle.setIsDel(0);
            pmPurchaseSettle.setPaymentClause(dto.getPaymentClause());
            pmPurchaseSettle.setPaymentSettleMoney(0.0D);
            pmPurchaseSettle.setProcurement("");
            pmPurchaseSettle.setInvoiceNumber(dto.getInvoiceNumber());
            pmPurchaseSettle.setInvoiceValue(dto.getInvoiceValue());
            pmPurchaseSettle.setChangeRemark("");
            pmPurchaseSettle.setPaymentRequestCode(dto.getPaymentRequestCode());
            this.save(pmPurchaseSettle);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("创建结算单出现异常：{}", e.getMessage());
        }
    }

    @Override
    public PmPurchaseSettle getByPaymentRequestCode(String paymentRequestCode) {
        return this.getOne(new LambdaQueryWrapper<PmPurchaseSettle>()
                .eq(PmPurchaseSettle::getIsDel, 0)
                .eq(PmPurchaseSettle::getPaymentRequestCode, paymentRequestCode));
    }

}

