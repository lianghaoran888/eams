package com.eams.mapper;

import com.github.pagehelper.Page;
import com.eams.annotation.AutoFill;
import com.eams.dto.AssetPageQueryDTO;
import com.eams.entity.Asset;
import com.eams.enumeration.OperationType;
import com.eams.vo.AssetVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AssetMapper {

    /**
     * 根据分类 id 查询资产数量（删除分类前校验）
     */
    @Select("select count(*) from asset where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入资产
     */
    @AutoFill(OperationType.INSERT)
    void insert(Asset asset);

    /**
     * 资产分页条件查询（含分类名称）
     */
    Page<AssetVO> pageQuery(AssetPageQueryDTO assetPageQueryDTO);

    /**
     * 根据 id 查询资产
     */
    @Select("select * from asset where id = #{id}")
    Asset getById(Long id);

    /**
     * 动态修改资产基本信息
     */
    @AutoFill(OperationType.UPDATE)
    void update(Asset asset);

    /**
     * 根据 id 删除资产
     */
    @Delete("delete from asset where id = #{id}")
    void deleteById(Long id);

    /**
     * 更新资产状态
     */
    @Update("update asset set status = #{status} where id = #{id}")
    void updateStatus(Asset asset);

    /**
     * 资产总数（报表）
     */
    @Select("select count(*) from asset")
    Long countAll();

    /**
     * 按状态统计资产数量（报表）
     */
    @Select("select count(*) from asset where status = #{status}")
    Long countByStatus(Integer status);
}