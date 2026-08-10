package com.eams.service;

import com.eams.dto.AssetDTO;
import com.eams.dto.AssetPageQueryDTO;
import com.eams.result.PageResult;
import com.eams.vo.AssetVO;

public interface AssetService {

    /**
     * 新增资产
     */
    void add(AssetDTO assetDTO);

    /**
     * 资产分页查询
     */
    PageResult pageQuery(AssetPageQueryDTO assetPageQueryDTO);

    /**
     * 根据 id 查询资产详情（含分类名称）
     */
    AssetVO getById(Long id);

    /**
     * 修改资产信息
     */
    void update(AssetDTO assetDTO);

    /**
     * 根据 id 删除资产（仅允许删除在库资产）
     */
    void deleteById(Long id);

    /**
     * 更新资产状态（在库/报废等）
     */
    void updateStatus(Long id, Integer status);
}