package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.AssetCategoryPageQueryDTO;
import com.sky.entity.AssetCategory;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AssetCategoryMapper {

    /**
     * 新增分类
     */
    @Insert("insert into asset_category(name, sort, status, create_time, update_time, create_user, update_user)" +
            " values (#{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(OperationType.INSERT)
    void insert(AssetCategory category);

    /**
     * 分页查询
     */
    Page<AssetCategory> pageQuery(AssetCategoryPageQueryDTO assetCategoryPageQueryDTO);

    /**
     * 根据 id 删除分类
     */
    @Delete("delete from asset_category where id = #{id}")
    void deleteById(Long id);

    /**
     * 启用/禁用分类状态 / 根据 id 修改分类
     */
    @AutoFill(OperationType.UPDATE)
    void update(AssetCategory category);

    /**
     * 查询启用状态的全部分类
     */
    List<AssetCategory> list();

    /**
     * 根据 id 查询分类
     */
    @Select("select * from asset_category where id = #{id}")
    AssetCategory getById(Long id);
}