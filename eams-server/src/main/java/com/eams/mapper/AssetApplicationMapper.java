package com.eams.mapper;

import com.github.pagehelper.Page;
import com.eams.dto.AssetApplicationPageQueryDTO;
import com.eams.entity.AssetApplication;
import com.eams.vo.AssetApplicationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AssetApplicationMapper {

    /**
     * 插入申请单
     */
    void insert(AssetApplication application);

    /**
     * 申请单分页条件查询（含申请人/资产信息）
     */
    Page<AssetApplicationVO> pageQuery(AssetApplicationPageQueryDTO assetApplicationPageQueryDTO);

    /**
     * 根据 id 查询申请单
     */
    @Select("select * from asset_application where id = #{id}")
    AssetApplication getById(Long id);

    /**
     * 修改申请单
     */
    void update(AssetApplication application);
}