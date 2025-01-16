package com.px.safetyandquality.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.common.util.CollUtils;
import com.px.safetyandquality.bean.dto.SaqSafetyTrainingQueryDto;
import com.px.safetyandquality.bean.vo.SaqSafetyTrainingVo;
import com.px.safetyandquality.entity.SaqSafetyTraining;
import com.px.safetyandquality.mapper.SaqSafetyTrainingMapper;
import com.px.safetyandquality.querycondition.SaqSafetyTrainingQueryCondition;
import com.px.safetyandquality.service.SaqSafetyTrainingService;
import com.px.system.service.SysDepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SaqSafetyTrainingServiceImpl 服务实现类
 *
 * @author 品讯科技
 * @since 2023-12-24
 */
@Service
public class SaqSafetyTrainingServiceImpl extends ServiceImpl<SaqSafetyTrainingMapper, SaqSafetyTraining> implements SaqSafetyTrainingService {

    @Autowired
    private SysDepartmentService departmentService;

    @Override
    public Page<SaqSafetyTrainingVo> getPage(SaqSafetyTrainingQueryDto dto) {
        LambdaQueryWrapper<SaqSafetyTraining> queryWrapper = SaqSafetyTrainingQueryCondition.build(dto);
        Page<SaqSafetyTraining> page = this.page(new Page(dto.getPageNum(), dto.getPageSize()), queryWrapper);
        Page<SaqSafetyTrainingVo> voPage = new Page<>();
        BeanUtils.copyProperties(page, voPage);
        List<SaqSafetyTraining> records = page.getRecords();
        List<SaqSafetyTrainingVo> voList = this.buildSaqSafetyTrainingVoList(records);
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<SaqSafetyTrainingVo> buildSaqSafetyTrainingVoList(List<SaqSafetyTraining> list) {
        List<SaqSafetyTrainingVo> voList = null;
        if (!CollectionUtils.isEmpty(list)) {
            voList = new ArrayList<>(list.size());
            Set<String> depSet = CollUtils.toSet(list, x -> x.getDepCode());
            Map<String, String> mapByCodes = departmentService.mapByCodes(new ArrayList<>(depSet));
            for (SaqSafetyTraining saqSafetyTraining : list) {
                SaqSafetyTrainingVo vo = new SaqSafetyTrainingVo();
                BeanUtils.copyProperties(saqSafetyTraining, vo);
                vo.setDepName(mapByCodes.get(vo.getDepCode()));
                voList.add(vo);
            }
        }
        return voList;
    }


}

