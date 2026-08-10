package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资产分类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /** 分类名称 */
    private String name;

    /** 排序 */
    private Integer sort;

    /** 状态：1启用 0禁用 */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}