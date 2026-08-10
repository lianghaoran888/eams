package com.eams.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 提交报修记录
 */
@Data
public class RepairRecordDTO implements Serializable {

    /** 关联申请单 id */
    private Long applicationId;

    /** 维修资产 id */
    private Long assetId;

    /** 故障描述 */
    private String description;

    /** 维修公司 */
    private String repairCompany;

    /** 维修费用 */
    private BigDecimal fee;

    /** 维修结果 */
    private String result;
}