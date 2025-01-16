package com.px.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.system.bean.dto.SysEarlyWarningConfigQueryDto;
import com.px.system.bean.vo.SysEarlyWarningConfigVo;
import com.px.system.entity.SysEarlyWarningConfig;

import java.util.List;

/**
 * <p>
 * 预警中心配置 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-12-10
 */
public interface SysEarlyWarningConfigService extends IService<SysEarlyWarningConfig> {

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    Page<SysEarlyWarningConfigVo> getPage(SysEarlyWarningConfigQueryDto dto);

    List<SysEarlyWarningConfigVo> buildSysEarlyWarningConfigVoList(List<SysEarlyWarningConfig> list);

    void scanningEarlyWarningData();
    void scanningEarlyWarningDataV2();
}

