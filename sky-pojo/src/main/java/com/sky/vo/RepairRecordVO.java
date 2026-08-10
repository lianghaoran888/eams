package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 报修记录视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairRecordVO implements Serializable {

    private Long id;

    /** 关联申请单 id */
    private Long applicationId;

    /** 申请单号 */
    private String applicationNo;

    /** 维修资产 id */
    private Long assetId;

    /** 资产编号 */
    private String assetCode;

    /** 资产名称 */
    private String assetName;

    /** 故障描述 */
    private String description;

    /** 维修公司 */
    private String repairCompany;

    /** 维修费用 */
    private BigDecimal fee;

    /** 维修结果 */
    private String result;

    /** 维修完成时间 */
    private LocalDateTime repairTime;

    private LocalDateTime createTime;
}