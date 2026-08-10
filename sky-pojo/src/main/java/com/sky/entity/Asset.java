package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资产
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Asset implements Serializable {

    /** 在库 */
    public static final Integer IN_STOCK = 1;
    /** 已领用 */
    public static final Integer USED = 2;
    /** 维修中 */
    public static final Integer REPAIRING = 3;
    /** 报废 */
    public static final Integer SCRAPPED = 4;

    private static final long serialVersionUID = 1L;

    private Long id;

    /** 资产分类 id */
    private Long categoryId;

    /** 资产编号（唯一） */
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

    private Long createUser;

    private Long updateUser;

}