package com.px.controller.commoditymanagement;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.commoditymanagement.bean.dto.CmWarehouseInterspaceQueryDto;
import com.px.commoditymanagement.bean.vo.CmWarehouseInterspaceVo;
import com.px.commoditymanagement.service.CmWarehouseInterspaceService;
import com.px.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仓库空间管理
 *
 * @author 品讯科技
 * @since 2023-12-28
 */
@RestController
@RequestMapping("/cm/warehouseInterspace")
public class CmWarehouseInterspaceController {

    @Autowired
    private CmWarehouseInterspaceService cmWarehouseInterspaceService;

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    @GetMapping("/getPage")
    public Result<Page<CmWarehouseInterspaceVo>> getPage(CmWarehouseInterspaceQueryDto dto) {
        Page<CmWarehouseInterspaceVo> voPage = cmWarehouseInterspaceService.getPage(dto);
        return new Result(voPage);
    }

}