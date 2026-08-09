package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 插入口味
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 删除操作中的 :
     * 与菜品相关联的口味
     */
//    @Delete("delete from dish_flavor where dish_id = #{dishId}")
//    void deleteByDishId(Long dishId);

    /**
     * 删除操作中的 :
     * 批量删除与菜品相关联的口味
     */
    void deleteByDishIds(List<Long> dishIds);

    /**
     * 根据菜品id查询口味数据
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> queryByDishId(Long dishId);

    /**
     * 1. 先删除原先的口味数据
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void delByDishId(Long dishId);
}
