package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetMealService {
    /**
     * 套餐分页条件查询
     */
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐
     */
    void add(SetmealDTO setmealDTO);


    /**
     * 批量删除套餐
     */
    void delete(List<Long> ids);

    /**
     * 修改套餐 : 查询回显
     */
    SetmealVO selectById(Long id);

    /**
     * 修改套餐
     */
    void updateWithSetMealDish(SetmealDTO setmealDTO);

    /**
     * 套餐启售,停售状态
     */
    void startOrStop(Integer status, Long id);

    /**
     * 用户端:
     * 条件查询
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 用户端:
     * 根据id查询菜品选项
     */
    List<DishItemVO> getDishItemById(Long id);
}
