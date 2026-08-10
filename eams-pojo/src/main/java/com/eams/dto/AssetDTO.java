package com.eams.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AssetDTO implements Serializable {

    private Long id;

    /** 资产分类 id */
    private Long categoryId;

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
}