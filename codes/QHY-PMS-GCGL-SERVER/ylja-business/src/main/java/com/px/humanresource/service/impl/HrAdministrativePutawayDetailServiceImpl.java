package com.px.humanresource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.humanresource.entity.HrAdministrativePutawayDetail;
import com.px.humanresource.mapper.HrAdministrativePutawayDetailMapper;
import com.px.humanresource.service.HrAdministrativePutawayDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * HrAdministrativePutawayDetailServiceImpl 服务实现类
 *
 * @author 品讯科技
 * @since 2023-12-12
 */
@Service
public class HrAdministrativePutawayDetailServiceImpl extends ServiceImpl<HrAdministrativePutawayDetailMapper, HrAdministrativePutawayDetail> implements HrAdministrativePutawayDetailService {

    @Override
    public List<HrAdministrativePutawayDetail> listByPutawayCode(String code) {
        return this.list(new LambdaQueryWrapper<HrAdministrativePutawayDetail>().eq(HrAdministrativePutawayDetail::getPutawayCode, code));
    }
}

