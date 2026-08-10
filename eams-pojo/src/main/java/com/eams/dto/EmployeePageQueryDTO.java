package com.eams.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeePageQueryDTO implements Serializable {

    /** 员工姓名 */
    private String name;

    /** 部门 */
    private String department;

    /** 页码 */
    private int page;

    /** 每页显示记录数 */
    private int pageSize;
}