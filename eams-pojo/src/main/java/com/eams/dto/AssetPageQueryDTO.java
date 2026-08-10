package com.eams.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AssetPageQueryDTO implements Serializable {

    /** 页码 */
    private int page;

    /** 每页记录数 */
    private int pageSize;

    /** 资产名称 */
    private String name;

    /** 资产编号 */
    private String code;

    /** 资产分类 id */
    private Long categoryId;

    /** 状态 */
    private Integer status;
}