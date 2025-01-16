package com.px.financialmanagement.querycondition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.common.util.CollUtils;
import com.px.common.util.DateUtil;
import com.px.financialmanagement.bean.dto.FmPaymentRequestQueryDto;
import com.px.financialmanagement.entity.FmPaymentRequest;
import org.springframework.util.StringUtils;

/**
 * FmPaymentRequest 查询
 *
 * @author 品讯科技
 * @since 2023-12-26
 */
public class FmPaymentRequestQueryCondition {

    /**
     * FmPaymentRequest使用lambda构建查询对象
     *
     * @param dto
     * @return
     */
    public static LambdaQueryWrapper<FmPaymentRequest> build(FmPaymentRequestQueryDto dto) {
        LambdaQueryWrapper<FmPaymentRequest> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // id : 
        if (!StringUtils.isEmpty(dto.getId())) {
            lambdaQueryWrapper.eq(FmPaymentRequest::getId, dto.getId());
        }
        // paymentRequestCode : 付款申请编号
        if (!StringUtils.isEmpty(dto.getPaymentRequestCode())) {
            lambdaQueryWrapper.eq(FmPaymentRequest::getPaymentRequestCode, dto.getPaymentRequestCode());
        }
        // depCode : 部门编号
        if (CollUtils.isNotEmpty(dto.getDepCodeList())) {
            lambdaQueryWrapper.in(FmPaymentRequest::getDepCode, dto.getDepCodeList());
        }
        // projectName : 项目名称
        if (!StringUtils.isEmpty(dto.getProjectName())) {
            lambdaQueryWrapper.eq(FmPaymentRequest::getProjectName, dto.getProjectName());
        }
        // projectCode : 项目编号
        if (!StringUtils.isEmpty(dto.getProjectCode())) {
            lambdaQueryWrapper.eq(FmPaymentRequest::getProjectCode, dto.getProjectCode());
        }
        // dataCode : 合同/订单编号
        if (!StringUtils.isEmpty(dto.getDataCode())) {
            lambdaQueryWrapper.eq(FmPaymentRequest::getDataCode, dto.getDataCode());
        }
        // contractMoney : 合同金额
        if (!StringUtils.isEmpty(dto.getContractMoney())) {
            lambdaQueryWrapper.eq(FmPaymentRequest::getContractMoney, dto.getContractMoney());
        }
        // supplierCode : 供应商编号
        if (!StringUtils.isEmpty(dto.getSupplierCode())) {
            lambdaQueryWrapper.eq(FmPaymentRequest::getSupplierCode, dto.getSupplierCode());
        }
        if (!StringUtils.isEmpty(dto.getSupplierName())) {
            lambdaQueryWrapper.like(FmPaymentRequest::getSupplierName, dto.getSupplierName());
        }
        // paymentType : 付款类型：1.预付款，2.进度款，3.结算款，4.质保金
        if (!StringUtils.isEmpty(dto.getPaymentType())) {
            lambdaQueryWrapper.eq(FmPaymentRequest::getPaymentType, dto.getPaymentType());
        }
        if (!StringUtils.isEmpty(dto.getContractType())) {
            lambdaQueryWrapper.eq(FmPaymentRequest::getContractType, dto.getContractType());
        }
        // status : 付款状态：0.未付款，1.已付款
        if (!StringUtils.isEmpty(dto.getStatus())) {
            lambdaQueryWrapper.eq(FmPaymentRequest::getStatus, dto.getStatus());
        }
        // flowStatus : 表单状态：1.已创建，2.审核中，3.已驳回（审核拒绝），4.审核通过，5.已弃审
        if (!StringUtils.isEmpty(dto.getFlowStatus())) {
            lambdaQueryWrapper.eq(FmPaymentRequest::getFlowStatus, dto.getFlowStatus());
        }
        // batchFlag : 批量处理标识：0.否，1.是
        if (!StringUtils.isEmpty(dto.getBatchFlag())) {
            lambdaQueryWrapper.eq(FmPaymentRequest::getBatchFlag, dto.getBatchFlag());
        }
        // flowCode : 流程编号
        if (!StringUtils.isEmpty(dto.getFlowCode())) {
            lambdaQueryWrapper.eq(FmPaymentRequest::getFlowCode, dto.getFlowCode());
        }
        // createTime : 创建时间
        if (!StringUtils.isEmpty(dto.getCreateTimeStart()) && !StringUtils.isEmpty(dto.getCreateTimeEnd())) {
            lambdaQueryWrapper.ge(FmPaymentRequest::getCreateTime, DateUtil.getBeginOfDay(DateUtil.format(dto.getCreateTimeStart(), "yyyy-MM-dd")));
            lambdaQueryWrapper.le(FmPaymentRequest::getCreateTime, DateUtil.getEndOfDay(DateUtil.format(dto.getCreateTimeEnd(), "yyyy-MM-dd")));
        }
        if (!StringUtils.isEmpty(dto.getPaidDateStart()) && !StringUtils.isEmpty(dto.getPaidDateEnd())) {
            lambdaQueryWrapper.ge(FmPaymentRequest::getPaidDate, DateUtil.getBeginOfDay(DateUtil.format(dto.getPaidDateStart(), "yyyy-MM-dd")));
            lambdaQueryWrapper.le(FmPaymentRequest::getPaidDate, DateUtil.getEndOfDay(DateUtil.format(dto.getPaidDateEnd(), "yyyy-MM-dd")));
        }
        // createBy : 创建人
        if (!StringUtils.isEmpty(dto.getCreateBy())) {
            lambdaQueryWrapper.like(FmPaymentRequest::getCreateBy, dto.getCreateBy());
        }
        // updateTime : 修改时间
        if (!StringUtils.isEmpty(dto.getUpdateTime())) {
            lambdaQueryWrapper.eq(FmPaymentRequest::getUpdateTime, dto.getUpdateTime());
        }
        // updateBy : 修改人
        if (!StringUtils.isEmpty(dto.getUpdateBy())) {
            lambdaQueryWrapper.like(FmPaymentRequest::getUpdateBy, dto.getUpdateBy());
        }
        lambdaQueryWrapper.orderByDesc(FmPaymentRequest::getCreateTime);
        lambdaQueryWrapper.eq(FmPaymentRequest::getIsDel, 0);
        return lambdaQueryWrapper;
    }

}

