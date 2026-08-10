package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资产申请单视图对象（含申请人/资产信息）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetApplicationVO implements Serializable {

    private Long id;

    /** 申请单号 */
    private String applicationNo;

    /** 申请人 id */
    private Long employeeId;

    /** 申请人姓名 */
    private String employeeName;

    /** 申请人部门 */
    private String employeeDepartment;

    /** 申请资产 id */
    private Long assetId;

    /** 资产编号 */
    private String assetCode;

    /** 资产名称 */
    private String assetName;

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
}