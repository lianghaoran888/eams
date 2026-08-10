package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资产视图对象（含分类名称）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetVO implements Serializable {

    private Long id;

    private Long categoryId;

    /** 分类名称 */
    private String categoryName;

    /** 资产编号 */
    private String code;

    /** 资产名称 */
    private String name;

    /** 规格型号 */
    private String spec;

    /** 单位 */
    private String unit;

    /** 资产价值 */
    private BigDecimal price;

    /** 资产图片 */
    private String image;

    /** 状态：1在库 2已领用 3维修中 4报废 */
    private Integer status;

    /** 资产描述 */
    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}