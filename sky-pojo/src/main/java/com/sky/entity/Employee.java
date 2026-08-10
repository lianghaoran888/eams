package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String idNumber;

    private String name;

    /** 手机号（员工 H5 端登录账号，唯一） */
    private String phone;

    private String sex;

    private String username;

    private String password;

    /** 状态：1启用 0禁用 */
    private Integer status;

    /** 角色：0员工 1管理员 */
    private Integer role;

    /** 部门 */
    private String department;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}