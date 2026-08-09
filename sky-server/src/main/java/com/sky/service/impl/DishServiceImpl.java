package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    /**
     * 新增菜品和口味
     */
    @Override
    @Transactional
    public void addWithFlavor(DishDTO dishDTO) {
        // 向菜品表中插入1条菜品
        Dish dish = new Dish();
        // 属性拷贝
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.insert(dish);

// ------------------------------------------------

        // 从insert中获取返回的dish的Id
        Long dishId = dish.getId();

        // 从传入的dishDTO中得到口味flavors集合
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(f -> {
                f.setDishId(dishId);
            });
            // 向口味表中插入n条口味
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品分页条件查询
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        // 开始分页查询
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除菜品
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 判断菜品是否可以删除--是否是启售状态??
        for (Long id : ids) {
            Dish dish = dishMapper.selectById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                // 当前菜品处于启售中, 不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 判断菜品是否可以删除--是否关联了套餐??
        List<Long> setMealIds = setMealDishMapper.selectByDishIds(ids);
        if(setMealIds != null && setMealIds.size() > 0){
            // 当前菜品关联了套餐, 不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 删除菜品数据
//        for (Long id : ids) {
//            dishMapper.deleteById(id);
//            // 删除与菜品相关联的口味数据
//            dishFlavorMapper.deleteByDishId(id);
//        }

        // 删除菜品数据 🌟优化:
        // 批量删除菜品数据
        dishMapper.deleteByIds(ids);

        // 批量删除与菜品相关联的口味数据
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /**
     * 根据Id查询菜品(查询回显)和对应的口味
     */
    @Override
    @Transactional
    public DishVO getByIdWithFlavor(Long id) {
        // 根据Id查询菜品数据
        Dish dish = dishMapper.queryByid(id);

        // 根据菜品id查询口味数据
        List<DishFlavor> dishFlavor = dishFlavorMapper.queryByDishId(id);

        // 把数据封装到DishVo中
        DishVO dishVO = new DishVO();
        // 属性拷贝
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavor);

        return dishVO;
    }

    /**
     * 修改菜品
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        // 修改菜品基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.update(dish);

        // 修改菜品相关联的口味数据
        // 1. 先删除原先的口味数据

        dishFlavorMapper.delByDishId(dishDTO.getId());

        // 2. 再插入新的口味数据
        // 从获取的dishDTO中得到口味flavors集合
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(f -> {
                f.setDishId(dishDTO.getId());
            });
            // 向口味表中插入n条口味
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 启用/禁用菜品状态
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        // 将status,id封装到Dish实体类中
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.updateStartOrStop(dish);
    }

    /**
     * 新增套餐操作中:
     * 根据分类id查询菜品
     */
    @Override
    public List<Dish> getBycategoryId(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.getBycategoryId(dish);
    }

    /**
     * 用户端:
     * 条件查询菜品和口味
     */
    @Override
    @Transactional
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.getBycategoryId(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.queryByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
