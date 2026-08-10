package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AssetCategoryPageQueryDTO implements Serializable {

    /** 页码 */
    private int page;

    /** 每页记录数 */
    private int pageSize;

    /** 分类名称 */
    private String name;

    /** 状态：1启用 0禁用 */
    private Integer status;
}