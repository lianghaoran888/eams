package com.eams.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 提交资产申请
 */
@Data
public class AssetApplicationDTO implements Serializable {

    /** 申请资产 id */
    private Long assetId;

    /** 申请数量 */
    private Integer quantity;

    /** 申请原因 */
    private String reason;

    /** 备注 */
    private String remark;
}