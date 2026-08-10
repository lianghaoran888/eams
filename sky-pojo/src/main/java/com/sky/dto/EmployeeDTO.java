package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeDTO implements Serializable {

    private Long id;

    private String idNumber;

    private String name;

    private String phone;

    private String sex;

    private String username;

    /** 角色：0员工 1管理员 */
    private Integer role;

    /** 部门 */
    private String department;
}