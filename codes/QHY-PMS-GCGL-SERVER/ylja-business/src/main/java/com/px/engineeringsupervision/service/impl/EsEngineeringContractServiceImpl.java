package com.px.engineeringsupervision.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.common.helper.BpmStatusHelper;
import com.px.common.util.CollUtils;
import com.px.common.util.MoneyUtil;
import com.px.engineeringsupervision.bean.dto.EsEngineeringContractQueryDto;
import com.px.engineeringsupervision.bean.vo.EsEngineeringContractVo;
import com.px.engineeringsupervision.entity.EsContractSupplementalAgreement;
import com.px.engineeringsupervision.entity.EsEngineeringContract;
import com.px.engineeringsupervision.mapper.EsEngineeringContractMapper;
import com.px.engineeringsupervision.querycondition.EsEngineeringContractQueryCondition;
import com.px.engineeringsupervision.service.EsContractSupplementalAgreementService;
import com.px.engineeringsupervision.service.EsEngineeringContractService;
import com.px.resourcecenter.entity.RcProject;
import com.px.resourcecenter.service.RcProjectService;
import com.px.system.service.SysDepartmentService;
import com.px.system.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * EsEngineeringContractServiceImpl 服务实现类
 *
 * @author 品讯科技
 * @since 2023-12-18
 */
@Service
public class EsEngineeringContractServiceImpl extends ServiceImpl<EsEngineeringContractMapper, EsEngineeringContract> implements EsEngineeringContractService {

    @Autowired
    private RcProjectService projectService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private EsContractSupplementalAgreementService contractSupplementalAgreementService;
    @Autowired
    private SysDepartmentService departmentService;

    @Override
    public Page<EsEngineeringContractVo> getPage(EsEngineeringContractQueryDto dto) {
        LambdaQueryWrapper<EsEngineeringContract> queryWrapper = EsEngineeringContractQueryCondition.build(dto);
        Page<EsEngineeringContract> page = this.page(new Page(dto.getPageNum(), dto.getPageSize()), queryWrapper);
        Page<EsEngineeringContractVo> voPage = new Page<>();
        BeanUtils.copyProperties(page, voPage);
        List<EsEngineeringContract> records = page.getRecords();
        List<EsEngineeringContractVo> voList = this.buildEsEngineeringContractVoList(records);
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<EsEngineeringContractVo> buildEsEngineeringContractVoList(List<EsEngineeringContract> list) {
        List<EsEngineeringContractVo> voList = null;
        if (!CollectionUtils.isEmpty(list)) {
            voList = new ArrayList<>(list.size());
            Set<String> projectCodes = list.stream().map(x -> x.getProjectCode()).collect(Collectors.toSet());
            List<RcProject> projects = projectService.listByCodes(new ArrayList<>(projectCodes));
            Map<String, String> projectMap = null;
            if (CollUtils.isNotEmpty(projects)) {
                projectMap = projects.stream().filter(x -> StringUtils.isNotBlank(x.getAreaDetail())).collect(Collectors.toMap(x -> x.getProjectCode(), y -> y.getAreaDetail()));
            }
            Set<String> userCodeSet = CollUtils.toSet(list, x -> x.getCreateBy());
            Map<String, String> nameByCodeList = userService.getNameByCodeList(new ArrayList<>(userCodeSet));
            Set<String> engineeringContractCodeSet = CollUtils.toSet(list, x -> x.getEngineeringContractCode());
            List<EsContractSupplementalAgreement> supplementalAgreementList = contractSupplementalAgreementService.list(
                    new LambdaQueryWrapper<EsContractSupplementalAgreement>()
                            .eq(EsContractSupplementalAgreement::getFlowStatus, 4)
                            .in(EsContractSupplementalAgreement::getEngineeringContractCode, engineeringContractCodeSet));
            Map<String, List<EsContractSupplementalAgreement>> supplementalAgreementMap = CollUtils.groupByKey(supplementalAgreementList, x -> x.getEngineeringContractCode());
            Map<String, String> mapByCodes = departmentService.mapByCodes(CollUtils.getDefaultStrVal(CollUtils.toList(list, x -> x.getDepCode())));
            for (EsEngineeringContract esEngineeringContract : list) {
                EsEngineeringContractVo vo = new EsEngineeringContractVo();
                BeanUtils.copyProperties(esEngineeringContract, vo);
                if (CollUtils.isNotEmpty(projectMap)) {
                    vo.setProjectAddress(projectMap.get(esEngineeringContract.getProjectCode()));
                }
                vo.setCreateUserName(nameByCodeList.get(esEngineeringContract.getCreateBy()));
                List<EsContractSupplementalAgreement> contractSupplementalAgreementList = supplementalAgreementMap.get(esEngineeringContract.getEngineeringContractCode());
                Double supplementalAgreementPrice = 0.;
                if (CollUtils.isNotEmpty(contractSupplementalAgreementList)) {
                    supplementalAgreementPrice += contractSupplementalAgreementList.stream()
                            .map(x -> x.getAgreementMoneyBeforeTaxes())
                            .reduce(0., (x, y) -> MoneyUtil.sum(x, y));
                }
                vo.setSupplementalAgreementPrice(supplementalAgreementPrice);
                vo.setTotalContractMoneyBeforeTaxes(MoneyUtil.sum(vo.getContractMoneyBeforeTaxes(), vo.getSupplementalAgreementPrice()));
                vo.setDepName(mapByCodes.get(vo.getDepCode()));
                voList.add(vo);
            }
        }
        return voList;
    }

    @Override
    public List<EsEngineeringContract> listByCodes(List<String> codeList) {
        return this.list(new LambdaQueryWrapper<EsEngineeringContract>().in(EsEngineeringContract::getEngineeringContractCode, codeList).eq(EsEngineeringContract::getIsDel, 0));
    }

    @Override
    public void updateByFlowCode(String instanceId, Integer status) {
        EsEngineeringContract record = this.getOne(new LambdaQueryWrapper<EsEngineeringContract>().eq(EsEngineeringContract::getFlowCode, instanceId));
        if (record != null) {
            if (record.getFlowStatus() == 4 && BpmStatusHelper.getStatus(status) == 4) {
                return;
            }
            record.setFlowStatus(BpmStatusHelper.getStatus(status));
            this.updateById(record);
        }
    }

    @Override
    public EsEngineeringContract getByCode(String engineeringContractCode) {
        return this.getOne(new LambdaQueryWrapper<EsEngineeringContract>()
                .eq(EsEngineeringContract::getEngineeringContractCode, engineeringContractCode)
                .eq(EsEngineeringContract::getIsDel, 0));
    }

    @Override
    public Long getIdByCode(String dataCode) {
        EsEngineeringContract contract = this.getByCode(dataCode);
        return contract != null ? contract.getId() : null;
    }


}

