package com.eams.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 资产总览报表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetOverviewVO implements Serializable {

    /** 资产总数 */
    private Long total;

    /** 在库 */
    private Long inStock;

    /** 已领用 */
    private Long used;

    /** 维修中 */
    private Long repairing;

    /** 报废 */
    private Long scrapped;
}