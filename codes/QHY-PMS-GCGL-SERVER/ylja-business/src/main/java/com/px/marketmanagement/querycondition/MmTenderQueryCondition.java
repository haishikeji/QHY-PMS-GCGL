package com.px.marketmanagement.querycondition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.common.util.CollUtils;
import com.px.marketmanagement.bean.dto.MmTenderQueryDto;
import com.px.marketmanagement.entity.MmTender;
import org.springframework.util.StringUtils;

/**
 * MmTender 查询
 *
 * @author 品讯科技
 * @since 2023-12-14
 */
public class MmTenderQueryCondition {

    /**
     * MmTender使用lambda构建查询对象
     *
     * @param dto
     * @return
     */
    public static LambdaQueryWrapper<MmTender> build(MmTenderQueryDto dto) {
        LambdaQueryWrapper<MmTender> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // id : 主键
        if (!StringUtils.isEmpty(dto.getId())) {
            lambdaQueryWrapper.eq(MmTender::getId, dto.getId());
        }
        // deptCode : 部门编号
        if (CollUtils.isNotEmpty(dto.getDepCodeList())) {
            lambdaQueryWrapper.in(MmTender::getDepCode, dto.getDepCodeList());
        }
        // tenderCode : 标识评审编号
        if (!StringUtils.isEmpty(dto.getTenderCode())) {
            lambdaQueryWrapper.eq(MmTender::getTenderCode, dto.getTenderCode());
        }
        // projectEstablishmentCode : 投标项目编号
        if (!StringUtils.isEmpty(dto.getProjectEstablishmentCode())) {
            lambdaQueryWrapper.eq(MmTender::getProjectEstablishmentCode, dto.getProjectEstablishmentCode());
        }
        // projectEstablishmentName : 投标项目名称
        if (!StringUtils.isEmpty(dto.getProjectEstablishmentName())) {
            lambdaQueryWrapper.eq(MmTender::getProjectEstablishmentName, dto.getProjectEstablishmentName());
        }
        // customerCode : 客户编号
        if (!StringUtils.isEmpty(dto.getCustomerCode())) {
            lambdaQueryWrapper.eq(MmTender::getCustomerCode, dto.getCustomerCode());
        }
        // bidPrincipalUserCode : 投标负责人编号
//        if (!StringUtils.isEmpty(dto.getBidPrincipalUserCode())) {
//            lambdaQueryWrapper.eq(MmTender::getBidPrincipalUserCode, dto.getBidPrincipalUserCode());
//        }
        // tenderQuotation : 投标报价
        if (!StringUtils.isEmpty(dto.getTenderQuotation())) {
            lambdaQueryWrapper.eq(MmTender::getTenderQuotation, dto.getTenderQuotation());
        }
        // costBudgeting : 成本预算
        if (!StringUtils.isEmpty(dto.getCostBudgeting())) {
            lambdaQueryWrapper.eq(MmTender::getCostBudgeting, dto.getCostBudgeting());
        }
        // anticipatedProfitMoney : 预计利润金额
        if (!StringUtils.isEmpty(dto.getAnticipatedProfitMoney())) {
            lambdaQueryWrapper.eq(MmTender::getAnticipatedProfitMoney, dto.getAnticipatedProfitMoney());
        }
        // anticipatedProfitRatio : 预计利润百分比
        if (!StringUtils.isEmpty(dto.getAnticipatedProfitRatio())) {
            lambdaQueryWrapper.eq(MmTender::getAnticipatedProfitRatio, dto.getAnticipatedProfitRatio());
        }
        // status : 生效状态：0.未生效，1.已生效
        if (!StringUtils.isEmpty(dto.getStatus())) {
            lambdaQueryWrapper.eq(MmTender::getStatus, dto.getStatus());
        }
        // flowStatus : 表单状态：1.已创建，2.审核中，3.已驳回（审核拒绝），4.审核通过，5.已弃审
        if (!StringUtils.isEmpty(dto.getFlowStatus())) {
            lambdaQueryWrapper.eq(MmTender::getFlowStatus, dto.getFlowStatus());
        }
        // flowCode : 流程编号
        if (!StringUtils.isEmpty(dto.getFlowCode())) {
            lambdaQueryWrapper.eq(MmTender::getFlowCode, dto.getFlowCode());
        }
        // createTime : 创建时间
        if (!StringUtils.isEmpty(dto.getCreateTime())) {
            lambdaQueryWrapper.eq(MmTender::getCreateTime, dto.getCreateTime());
        }
        // createBy : 创建人
        if (!StringUtils.isEmpty(dto.getCreateBy())) {
            lambdaQueryWrapper.like(MmTender::getCreateBy, dto.getCreateBy());
        }
        // updateTime : 修改时间
        if (!StringUtils.isEmpty(dto.getUpdateTime())) {
            lambdaQueryWrapper.eq(MmTender::getUpdateTime, dto.getUpdateTime());
        }
        // updateBy : 修改人
        if (!StringUtils.isEmpty(dto.getUpdateBy())) {
            lambdaQueryWrapper.like(MmTender::getUpdateBy, dto.getUpdateBy());
        }
        lambdaQueryWrapper.orderByDesc(MmTender::getCreateTime);
        lambdaQueryWrapper.eq(MmTender::getIsDel, 0);
        return lambdaQueryWrapper;
    }

}

