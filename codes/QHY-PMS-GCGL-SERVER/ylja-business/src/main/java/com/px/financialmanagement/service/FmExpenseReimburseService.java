package com.px.financialmanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.financialmanagement.bean.dto.FmExpenseReimburseQueryDto;
import com.px.financialmanagement.bean.vo.FmExpenseReimburseExport;
import com.px.financialmanagement.bean.vo.FmExpenseReimburseVo;
import com.px.financialmanagement.entity.FmExpenseReimburse;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 费用报销 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-12-26
 */
public interface FmExpenseReimburseService extends IService<FmExpenseReimburse> {

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    Page<FmExpenseReimburseVo> getPage(FmExpenseReimburseQueryDto dto);

    List<FmExpenseReimburseVo> buildFmExpenseReimburseVoList(List<FmExpenseReimburse> list);
    List<FmExpenseReimburseVo> buildFmExpenseReimburseVoList(List<FmExpenseReimburse> list,Integer flag);

    void updateByFlowCode(String instanceId, Integer status);

    XSSFWorkbook export(List<FmExpenseReimburseExport> exportArrayList);

    Map<String,Double> getDataMapByProject(String projectCode,Integer paidFlag);

}

