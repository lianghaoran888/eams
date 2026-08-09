package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     */
    @Select("select count(*) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品
     */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分页条件查询
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 删除操作中的 :
     * 根据主键id查询菜品状态
     */
    @Select("select dish.status from dish where id = #{id}")
    Dish selectById(Long id);

    /**
     * 删除操作中的 :
     * 根据id删除菜品
     */
//    @Delete("delete from dish where id = #{id}")
//    void deleteById(Long id);

    /**
     * 删除操作中的 :
     * 批量删除菜品根据id
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据Id查询菜品数据
     */
    @Select("select * from dish where id = #{id}")
    Dish queryByid(Long id);

    /**
     * 动态修改菜品基本信息
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 更新状态
     */
    @Update("update dish set status = #{status} where id = #{id}")
    @AutoFill(OperationType.UPDATE)
    void updateStartOrStop(Dish dish);

    /**
     * 新增套餐操作中:
     * 根据分类id动态条件查询菜品
     */
    List<Dish> getBycategoryId(Dish dish);
}
