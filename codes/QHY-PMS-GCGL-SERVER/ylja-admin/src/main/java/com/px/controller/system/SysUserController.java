package com.px.controller.system;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.common.result.Result;
import com.px.common.result.ResultCode;
import com.px.system.bean.dto.SysUserQueryDto;
import com.px.system.bean.dto.SysUserUpdateDto;
import com.px.system.bean.vo.SysUserVo;
import com.px.system.entity.SysUser;
import com.px.system.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统用户管理
 *
 * @author 品讯科技
 * @since 2023-11-08
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 修改头像
     *
     * @param userCode 用户编号
     * @param headImg  头像
     * @return
     */
    @PostMapping("/updateHeadImg")
    public Result updateHeadImg(String userCode, String headImg) {
        SysUser user = sysUserService.getByCode(userCode);
        if (user == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        user.setHeadImg(headImg);
        sysUserService.updateById(user);
        return Result.ok();
    }

    /**
     * 修改密码
     *
     * @param userCode 用户编号
     * @param pwd      密码
     * @return
     */
    @PostMapping("/updatePwd")
    public Result updatePwd(String userCode, String pwd) {
        SysUser user = sysUserService.getByCode(userCode);
        if (user == null) {
            return Result.fail(ResultCode.QUERY_DATA_IS_NULL);
        }
        String sysNewPassword = passwordEncoder.encode(pwd);
        user.setPassword(sysNewPassword);
        sysUserService.updateById(user);
        return Result.ok();
    }

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    @GetMapping("/getPage")
    public Result<Page<SysUserVo>> getPage(SysUserQueryDto dto) {
        Page<SysUserVo> voPage = sysUserService.getPage(dto);
        return new Result(voPage);
    }

    /***
     * 新增/修改(id为空:新增，不为空:修改)
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/addOrUpdate")
    public Result addOrUpdate(@RequestBody SysUserUpdateDto dto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(dto, sysUser,"createBy");
        if (sysUser.getId() == null) {
            sysUserService.save(sysUser);
        } else {
            sysUserService.updateById(sysUser);
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
        SysUser sysUser = sysUserService.getById(id);
        return new Result(sysUser);
    }

    /**
     * 更新状态
     *
     * @param id     数据id
     * @param status 状态：1.是，0.否
     * @return
     */
    @PostMapping("/updateStatus")
    public Result updateStatus(Long id, Integer status) {
        SysUser sysUser = sysUserService.getById(id);
        if (sysUser == null) {
            return Result.fail(500, "更新的数据不存在！");
        }
        sysUser.setStatus(status);
        sysUserService.updateById(sysUser);
        return Result.ok();
    }

    /**
     * 删除
     *
     * @param idList 数据id
     * @return
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody List<Long> idList) {
        sysUserService.update(new LambdaUpdateWrapper<SysUser>().set(SysUser::getIsDel, 1).in(SysUser::getId, idList));
        return Result.ok();
    }


}