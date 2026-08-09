package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品和口味
     */
    void addWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页条件查询
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据Id查询菜品(查询回显)和对应的口味
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 修改菜品
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 启用/禁用菜品状态
     */
    void startOrStop(Integer status, Long id);

    /**
     * 新增套餐操作中:
     * 根据分类id查询菜品
     */
    List<Dish> getBycategoryId(Long categoryId);

    /**
     * 用户端:
     * 条件查询菜品和口味
     */
    List<DishVO> listWithFlavor(Dish dish);
}
