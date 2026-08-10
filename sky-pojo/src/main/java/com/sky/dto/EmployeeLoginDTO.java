package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 员工登录参数：支持手机号+密码（H5端）或用户名+密码（管理端）
 */
@Data
public class EmployeeLoginDTO implements Serializable {

    /** 用户名（管理端登录使用） */
    private String username;

    /** 手机号（员工 H5 端登录使用） */
    private String phone;

    /** 密码 */
    private String password;
}