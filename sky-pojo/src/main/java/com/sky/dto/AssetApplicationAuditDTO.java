package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 申请单审批
 */
@Data
public class AssetApplicationAuditDTO implements Serializable {

    /** 申请单 id */
    private Long id;

    /** 审批结果：2通过 3拒绝 */
    private Integer status;

    /** 拒绝原因（拒绝时必填） */
    private String rejectReason;
}