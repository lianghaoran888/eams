package com.eams.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AssetCategoryDTO implements Serializable {

    /** 主键 */
    private Long id;

    /** 分类名称 */
    private String name;

    /** 排序 */
    private Integer sort;

    /** 状态：1启用 0禁用 */
    private Integer status;
}