package com.px.purchasingmanagement.service;

import com.px.purchasingmanagement.entity.PmSubpackageContract;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.purchasingmanagement.bean.dto.PmSubpackageContractQueryDto;
import com.px.purchasingmanagement.bean.vo.PmSubpackageContractVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 分包合同 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-12-19
 */
public interface PmSubpackageContractService extends IService<PmSubpackageContract> {

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    Page<PmSubpackageContractVo> getPage(PmSubpackageContractQueryDto dto);

    List<PmSubpackageContractVo> buildPmSubpackageContractVoList(List<PmSubpackageContract> list);

    List<PmSubpackageContract> listByCodes(List<String> dataCodeList);

    PmSubpackageContract getByCode(String code);

    void updateByFlowCode(String instanceId, Integer status);

    Long getIdByCode(String dataCode);
}

