package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RepairPageQueryDTO implements Serializable {

    /** 页码 */
    private int page;

    /** 每页记录数 */
    private int pageSize;

    /** 申请单号 */
    private String applicationNo;

    /** 申请人 id（员工端查询自己的报修） */
    private Long employeeId;
}