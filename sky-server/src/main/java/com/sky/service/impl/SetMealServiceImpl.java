package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    /**
     * 套餐分页条件查询
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
       // 开始分页
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        Page<SetmealVO> setmealPage = setMealMapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(setmealPage.getTotal(), setmealPage.getResult());

    }

    /**
     * 新增套餐
     */
    @Override
    @Transactional
    public void add(SetmealDTO setmealDTO) {
        //向套餐表中插入1条套餐数据

        Setmeal setmeal = new Setmeal();
        //属性拷贝
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setMealMapper.insert(setmeal);


        // 获取从insert语句返回的setmeal的id
        Long SetmealId = setmeal.getId();

        // 从传入的setmealDTO中获取setmealDishes集合
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

        if(setmealDishes != null && setmealDishes.size() > 0){
            setmealDishes.forEach(sD -> {
                sD.setSetmealId(SetmealId);
            });
            // 向套餐菜品表中插入n条数据
            setMealDishMapper.insertBatch(setmealDishes);
        }

    }

    /**
     * 批量删除套餐
     */
    @Override
    @Transactional
    public void delete(List<Long> ids) {
        // 判断是否可以删除 -- 是否为启售状态??
        for (Long id : ids) {
            Setmeal setmeal = setMealMapper.selectStatus(id);
            if(setmeal.getStatus() == StatusConstant.ENABLE){
                // 如果为启售状态,则不能删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        // 批量删除套餐
        setMealMapper.deleteBatch(ids);

        // 批量删除与套餐关联的菜品
        setMealDishMapper.deleteWithDishBatch(ids);
    }

    /**
     * 修改套餐 : 查询回显
     */
    @Override
    @Transactional
    public SetmealVO selectById(Long id) {
        // 根据id查询套餐数据
        Setmeal setmeal = setMealMapper.queryById(id);

        // 根据id查询套餐关联的菜品数据
        List<SetmealDish> setmealDish = setMealDishMapper.queryBySetmealId(id);

        // 封装到SetmealVO对象中
        SetmealVO setmealVO = new SetmealVO();
        // 属性拷贝
        BeanUtils.copyProperties(setmeal, setmealVO);

        setmealVO.setSetmealDishes(setmealDish);

        return setmealVO;
    }

    /**
     * 修改套餐
     */
    @Override
    public void updateWithSetMealDish(SetmealDTO setmealDTO) {
        // 修改套餐表基本信息
        Setmeal setmeal = new Setmeal();
        // 属性拷贝
        BeanUtils.copyProperties(setmealDTO, setmeal);

        setMealMapper.update(setmeal);

        // 修改套餐菜品表信息
        // 1.先删除原先的套餐菜品表
        setMealDishMapper.delWithDish(setmealDTO.getId());

        // 2.再插入新的套餐菜品表
        // 获取从setmealDTO传入的setmealDishes集合
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && setmealDishes.size() > 0){
            setmealDishes.forEach(sD -> {
                sD.setSetmealId(setmealDTO.getId());
            });
            // 插入新的套餐菜品表
            setMealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 套餐启售,停售状态
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        // 将status, id封装到实体类Setmeal中
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();
        setMealMapper.updateStatus(setmeal);
    }

    /**
     * 用户端:
     * 条件查询
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setMealMapper.list(setmeal);
        return list;
    }

    /**
     * 用户端:
     * 根据id查询菜品选项
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setMealMapper.getDishItemBySetmealId(id);
    }
}
