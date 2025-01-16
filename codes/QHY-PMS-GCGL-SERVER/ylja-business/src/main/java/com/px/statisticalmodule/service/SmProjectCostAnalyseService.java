package com.px.statisticalmodule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.statisticalmodule.bean.dto.SmProjectCostAnalyseQueryDto;
import com.px.statisticalmodule.bean.vo.SmProjectCostAnalyseVo;
import com.px.statisticalmodule.entity.SmProjectCostAnalyse;

import java.util.List;

/**
 * <p>
 * 项目成本分析 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2024-04-11
 */
public interface SmProjectCostAnalyseService extends IService<SmProjectCostAnalyse> {

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    Page<SmProjectCostAnalyseVo> getPage(SmProjectCostAnalyseQueryDto dto);

    List<SmProjectCostAnalyseVo> buildSmProjectCostAnalyseVoList(List<SmProjectCostAnalyse> list);

//    void scanningData();
    void scanningDataV2();

}

