package com.eams.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AssetApplicationPageQueryDTO implements Serializable {

    /** 页码 */
    private int page;

    /** 每页记录数 */
    private int pageSize;

    /** 申请单号 */
    private String applicationNo;

    /** 申请人 id */
    private Long employeeId;

    /** 状态：1待审 2通过 3拒绝 4已完成 */
    private Integer status;
}