package com.eams.service;

import com.eams.dto.AssetCategoryDTO;
import com.eams.dto.AssetCategoryPageQueryDTO;
import com.eams.entity.AssetCategory;
import com.eams.result.PageResult;

import java.util.List;

public interface AssetCategoryService {

    /**
     * 新增分类
     */
    void add(AssetCategoryDTO assetCategoryDTO);

    /**
     * 分类分页查询
     */
    PageResult pageQuery(AssetCategoryPageQueryDTO assetCategoryPageQueryDTO);

    /**
     * 根据 id 删除分类
     */
    void deleteById(Long id);

    /**
     * 修改分类
     */
    void update(AssetCategoryDTO assetCategoryDTO);

    /**
     * 启用/禁用分类状态
     */
    void startOrStop(Integer status, Long id);

    /**
     * 查询启用状态的全部分类（走 Redis 缓存，1 小时过期）
     */
    List<AssetCategory> list();
}