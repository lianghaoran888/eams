package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    /**
     * 删除操作中的 :
     * 查询菜品是否关联了套餐
     */
    List<Long> selectByDishIds(List<Long> dishIds);

    /**
     * 向套餐菜品表中插入n条数据
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 批量删除套餐 :
     * 批量删除与套餐关联的菜品
     */
    void deleteWithDishBatch(List<Long> ids);

    /**
     * 修改套餐 : 查询回显
     * 根据id查询套餐关联的菜品数据
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> queryBySetmealId(Long setmealId);

    /**
     * 修改套餐 : 查询回显
     * 1.先删除原先的套餐菜品表
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void delWithDish(Long setmealId);
}
