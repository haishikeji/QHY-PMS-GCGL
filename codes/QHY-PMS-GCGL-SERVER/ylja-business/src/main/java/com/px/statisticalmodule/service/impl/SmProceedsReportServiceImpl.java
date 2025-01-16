package com.px.statisticalmodule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.common.util.CollUtils;
import com.px.common.util.MoneyUtil;
import com.px.engineeringsupervision.entity.EsContractSupplementalAgreement;
import com.px.engineeringsupervision.entity.EsEngineeringContract;
import com.px.engineeringsupervision.service.EsContractSupplementalAgreementService;
import com.px.engineeringsupervision.service.EsEngineeringContractService;
import com.px.financialmanagement.entity.FmReceiptRegister;
import com.px.financialmanagement.service.FmReceiptRegisterService;
import com.px.resourcecenter.entity.RcProject;
import com.px.resourcecenter.service.RcCustomerService;
import com.px.resourcecenter.service.RcProjectService;
import com.px.statisticalmodule.bean.dto.SmProceedsReportQueryDto;
import com.px.statisticalmodule.bean.vo.SmProceedsReportVo;
import com.px.statisticalmodule.entity.SmProceedsReport;
import com.px.statisticalmodule.mapper.SmProceedsReportMapper;
import com.px.statisticalmodule.querycondition.SmProceedsReportQueryCondition;
import com.px.statisticalmodule.service.SmProceedsReportService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * SmProceedsReportServiceImpl 服务实现类
 *
 * @author 品讯科技
 * @since 2024-04-22
 */
@Service
public class SmProceedsReportServiceImpl extends ServiceImpl<SmProceedsReportMapper, SmProceedsReport> implements SmProceedsReportService {

    @Autowired
    private RcProjectService projectService;
    @Autowired
    private EsEngineeringContractService engineeringContractService;
    @Autowired
    private EsContractSupplementalAgreementService contractSupplementalAgreementService;
    @Autowired
    private FmReceiptRegisterService receiptRegisterService;
    @Autowired
    private RcCustomerService customerService;

    @Override
    public Page<SmProceedsReportVo> getPage(SmProceedsReportQueryDto dto) {
        LambdaQueryWrapper<SmProceedsReport> queryWrapper = SmProceedsReportQueryCondition.build(dto);
        Page<SmProceedsReport> page = this.page(new Page(dto.getPageNum(), dto.getPageSize()), queryWrapper);
        Page<SmProceedsReportVo> voPage = new Page<>();
        BeanUtils.copyProperties(page, voPage);
        List<SmProceedsReport> records = page.getRecords();
        List<SmProceedsReportVo> voList = this.buildSmProceedsReportVoList(records);
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<SmProceedsReportVo> buildSmProceedsReportVoList(List<SmProceedsReport> list) {
        List<SmProceedsReportVo> voList = null;
        if (!CollectionUtils.isEmpty(list)) {
            voList = new ArrayList<>(list.size());
            for (SmProceedsReport smProceedsReport : list) {
                SmProceedsReportVo vo = new SmProceedsReportVo();
                BeanUtils.copyProperties(smProceedsReport, vo);
                voList.add(vo);
            }
        }
        return voList;
    }

    @Override
    public void scanningData() {
        this.remove(new LambdaQueryWrapper<>());
        List<RcProject> rcProjectList = projectService.list(new LambdaQueryWrapper<RcProject>()
                .eq(RcProject::getIsDel, 0)
//                .eq(RcProject::getProjectCode, "GDBP-20230722-017")
                .eq(RcProject::getFlowStatus, 4)
                .orderByDesc(RcProject::getId));
        List<String> projectCodeList = CollUtils.toList(rcProjectList, x -> x.getProjectCode());
        List<EsEngineeringContract> engineeringContractList = engineeringContractService.list(new LambdaQueryWrapper<EsEngineeringContract>()
                .eq(EsEngineeringContract::getFlowStatus, 4)
                .in(EsEngineeringContract::getProjectCode, CollUtils.getDefaultStrVal(projectCodeList))
                .eq(EsEngineeringContract::getIsDel, 0));
        Map<String, EsEngineeringContract> engineeringContractMap = CollUtils.toMap(engineeringContractList, x -> x.getProjectCode(), Function.identity());
        List<EsContractSupplementalAgreement> esContractSupplementalAgreements = contractSupplementalAgreementService.list(new LambdaQueryWrapper<EsContractSupplementalAgreement>()
                .eq(EsContractSupplementalAgreement::getFlowStatus, 4)
                .in(EsContractSupplementalAgreement::getEngineeringContractCode, CollUtils.getDefaultStrVal(CollUtils.toList(engineeringContractList, x -> x.getEngineeringContractCode()))));
        Map<String, List<EsContractSupplementalAgreement>> esContractSupplementalAgreementsMap = CollUtils.groupByKey(esContractSupplementalAgreements, x -> x.getEngineeringContractCode());
        List<FmReceiptRegister> receiptRegisterList = receiptRegisterService.list(new LambdaQueryWrapper<FmReceiptRegister>()
                .eq(FmReceiptRegister::getIsDel, 0)
                .in(FmReceiptRegister::getProjectCode, CollUtils.getDefaultStrVal(projectCodeList))
                .eq(FmReceiptRegister::getFlowStatus, 4));
        Map<String, List<FmReceiptRegister>> receiptRegisterListMap = CollUtils.groupByKey(receiptRegisterList, x -> x.getProjectCode());
        List<SmProceedsReport> proceedsReports = new ArrayList<>();
        if (CollUtils.isNotEmpty(rcProjectList)) {
            Map<String, String> mapByCodes = customerService.mapByCodes(CollUtils.getDefaultStrVal(CollUtils.toList(rcProjectList, x -> x.getCustomerCode())));
            for (RcProject rcProject : rcProjectList) {
                SmProceedsReport report = new SmProceedsReport();
                report.setCustomerCode(rcProject.getCustomerCode());
                report.setCustomerName(mapByCodes.get(rcProject.getCustomerCode()));
                report.setProjectCode(rcProject.getProjectCode());
                report.setProjectName(rcProject.getProjectName());
                EsEngineeringContract engineeringContract = engineeringContractMap.get(rcProject.getProjectCode());
                Double contractMoney = 0.;
                if (engineeringContract != null) {
                    List<EsContractSupplementalAgreement> contractSupplementalAgreementList = esContractSupplementalAgreementsMap.get(engineeringContract.getEngineeringContractCode());
                    report.setContractCode(engineeringContract.getEngineeringContractCode());
                    contractMoney = engineeringContract.getContractMoneyBeforeTaxes();
                    if (CollUtils.isNotEmpty(contractSupplementalAgreementList)) {
                        contractMoney += contractSupplementalAgreementList.stream()
                                .map(x -> x.getAgreementMoneyBeforeTaxes())
                                .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                    }
                    report.setContractMoney(contractMoney);
                }
                List<FmReceiptRegister> fmReceiptRegisterList = receiptRegisterListMap.get(rcProject.getProjectCode());
                Double totalMoney = 0.;
                Double totalReceivePaymentMoney = 0.;
                Double totalForfeitMoney = 0.;
                Double totalServiceCharge = 0.;
                if (CollUtils.isNotEmpty(fmReceiptRegisterList)) {
                    totalMoney += fmReceiptRegisterList.stream()
                            .map(x -> x.getActualReceiveMoney())
                            .reduce(0., (x, y) -> MoneyUtil.sum(x, y));

                    totalForfeitMoney += fmReceiptRegisterList.stream()
                            .map(x -> x.getForfeitMoney())
                            .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                    totalServiceCharge += fmReceiptRegisterList.stream()
                            .map(x -> x.getServiceCharge())
                            .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                    totalReceivePaymentMoney += fmReceiptRegisterList.stream()
                            .map(x -> x.getReceivePaymentMoney())
                            .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
//                    Double reduce = fmReceiptRegisterList.stream()
//                            .filter(x -> StringUtils.isNotBlank(x.getCurrentPaymentRatio()))
//                            .map(x -> Double.parseDouble(x.getCurrentPaymentRatio().replace("%", "")))
//                            .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
//                    totalMoney = MoneyUtil.sum(totalMoney, -totalForfeitMoney);
//                    totalMoney = MoneyUtil.sum(totalMoney, totalServiceCharge);
//                    totalMoney = MoneyUtil.calculateByRatio(reduce / 100., contractMoney);
                }
                report.setTotalMoney(totalMoney);
                report.setLeftTotalMoney(MoneyUtil.sum(MoneyUtil.getDefault(report.getContractMoney()), -MoneyUtil.getDefault(totalReceivePaymentMoney)));

                Double totalMoneyRate = MoneyUtil.calculateProportionRatio(totalMoney, report.getContractMoney());
                report.setTotalMoneyRate(totalMoneyRate);
                Integer isFinish = 0;
                if (totalMoney >= contractMoney) {
                    isFinish = 1;
                }
                report.setIsFinish(isFinish);
                report.setCreateTime(new Date());
                proceedsReports.add(report);
            }
            if (CollUtils.isNotEmpty(proceedsReports)) {
                this.saveBatch(proceedsReports);
            }
        }
    }

}

