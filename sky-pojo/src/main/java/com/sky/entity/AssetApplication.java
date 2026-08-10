package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资产申请单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetApplication implements Serializable {

    /** 待审 */
    public static final Integer PENDING = 1;
    /** 通过 */
    public static final Integer APPROVED = 2;
    /** 拒绝 */
    public static final Integer REJECTED = 3;
    /** 已完成 */
    public static final Integer COMPLETED = 4;

    private static final long serialVersionUID = 1L;

    private Long id;

    /** 申请单号 */
    private String applicationNo;

    /** 申请人（员工）id */
    private Long employeeId;

    /** 申请资产 id */
    private Long assetId;

    /** 申请数量 */
    private Integer quantity;

    /** 申请原因 */
    private String reason;

    /** 状态：1待审 2通过 3拒绝 4已完成 */
    private Integer status;

    /** 拒绝原因 */
    private String rejectReason;

    /** 申请时间 */
    private LocalDateTime applyTime;

    /** 审批时间 */
    private LocalDateTime approveTime;

    /** 审批人 id */
    private Long approverId;

    /** 完成时间 */
    private LocalDateTime completeTime;

    /** 备注 */
    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}