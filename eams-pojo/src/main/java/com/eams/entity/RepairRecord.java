package com.eams.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 报修记录（关联资产申请单）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

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

    /** 维修完成时间 */
    private LocalDateTime repairTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

}