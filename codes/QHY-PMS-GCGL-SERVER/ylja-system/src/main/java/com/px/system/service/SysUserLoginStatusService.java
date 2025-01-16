package com.px.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.system.entity.SysUserLoginStatus;

/**
 * <p>
 * 用户登录状态 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-11-08
 */
public interface SysUserLoginStatusService extends IService<SysUserLoginStatus> {

    SysUserLoginStatus getByUserCode(String userCode);

    void saveLoginStatus(String userCode, String token);

}

