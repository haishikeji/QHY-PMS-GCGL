package com.px.system.bean.vo;

import com.px.system.entity.SysCertificate;
import lombok.Data;

/**
 * SysCertificate 返参
 *
 * @author 品讯科技
 * @since 2023-12-11
 */
@Data
public class SysCertificateVo extends SysCertificate {

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 性别，1 男、2 女
     */
    private String sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 手机号
     */
    private String phone;

}

