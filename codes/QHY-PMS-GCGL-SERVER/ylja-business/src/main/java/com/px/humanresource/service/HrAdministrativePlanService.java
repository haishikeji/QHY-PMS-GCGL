package com.px.humanresource.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.humanresource.bean.dto.HrAdministrativePlanQueryDto;
import com.px.humanresource.bean.vo.HrAdministrativePlanVo;
import com.px.humanresource.entity.HrAdministrativePlan;

import java.util.List;

/**
 * <p>
 * 行政需用计划 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-12-12
 */
public interface HrAdministrativePlanService extends IService<HrAdministrativePlan> {

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    Page<HrAdministrativePlanVo> getPage(HrAdministrativePlanQueryDto dto);

    List<HrAdministrativePlanVo> buildHrAdministrativePlanVoList(List<HrAdministrativePlan> list);

    List<HrAdministrativePlan> listByCodes(List<String> codeList);

    void updateByFlowCode(String code, Integer status);
}

