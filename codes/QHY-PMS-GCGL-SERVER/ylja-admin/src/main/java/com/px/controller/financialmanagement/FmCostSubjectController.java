package com.px.controller.financialmanagement;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.common.result.Result;
import com.px.common.result.ResultCode;
import com.px.common.util.security.SecurityUtils;
import com.px.financialmanagement.bean.dto.FmCostSubjectQueryDto;
import com.px.financialmanagement.bean.dto.FmCostSubjectUpdateDto;
import com.px.financialmanagement.bean.vo.FmCostSubjectVo;
import com.px.financialmanagement.entity.FmCostSubject;
import com.px.financialmanagement.service.FmCostSubjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 成本科目管理
 *
 * @author 品讯科技
 * @since 2023-12-26
 */
@RestController
@RequestMapping("/fm/costSubject")
public class FmCostSubjectController {

    @Autowired
    private FmCostSubjectService fmCostSubjectService;

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    @GetMapping("/getPage")
    public Result<Page<FmCostSubjectVo>> getPage(FmCostSubjectQueryDto dto) {
        Page<FmCostSubjectVo> voPage = fmCostSubjectService.getPage(dto);
        return new Result(voPage);
    }

    /***
     * 新增/修改(id为空:新增，不为空:修改)
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/addOrUpdate")
    public Result addOrUpdate(@RequestBody FmCostSubjectUpdateDto dto) {
        FmCostSubject fmCostSubject = new FmCostSubject();
        BeanUtils.copyProperties(dto, fmCostSubject,"createBy");
        if (fmCostSubject.getId() == null) {
            fmCostSubject.setCreateBy(SecurityUtils.getUserCodeAndName());
            fmCostSubject.setCreateTime(new Date());
            fmCostSubjectService.save(fmCostSubject);
        } else {
            fmCostSubject.setUpdateBy(SecurityUtils.getUserCodeAndName());
            fmCostSubject.setUpdateTime(new Date());
            fmCostSubjectService.updateById(fmCostSubject);
        }
        return Result.ok();
    }

    /***
     * 获取详情
     * @param id
     * @return
     */
    @GetMapping("/getInfo")
    public Result getInfo(Long id) {
        FmCostSubject fmCostSubject = fmCostSubjectService.getById(id);
        return new Result(fmCostSubject);
    }

    /**
     * 删除
     *
     * @param id 数据id
     * @return
     */
    @PostMapping("/delete")
    public Result delete(Long id) {
        FmCostSubject fmCostSubject = fmCostSubjectService.getById(id);
        if (fmCostSubject == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        fmCostSubject.setIsDel(1);
        fmCostSubjectService.updateById(fmCostSubject);
        return Result.ok();
    }

}