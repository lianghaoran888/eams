package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品
     * 🌟未用Cache注解, 传统方法
     */
/*    @GetMapping("/list")
    public Result<List<DishVO>> list(Long categoryId) {
        // 构造redis的key, 构造规则: dish_ + 分类id
        String key = "dish_" + categoryId;

        // 判断redis中是否存在菜品数据, 如果存在, 则直接返回redis中的数据
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if(list != null && list.size() > 0){
            return Result.success(list);
        }

        // 如果不存在, 则查询数据库中的菜品数据, 并将数据缓存到redis中

        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品

        list = dishService.listWithFlavor(dish);
        // 将数据缓存到redis中
        redisTemplate.opsForValue().set(key, list);

        return Result.success(list);
    }*/

    /**
     * 根据分类id查询菜品
     * 🌟使用Cache注解
     */
    @GetMapping("/list")
    @Cacheable(cacheNames = "dish_new_", key = "#categoryId") // key命名规则: dish_new_::888
    public Result<List<DishVO>> list(Long categoryId) {
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品

        List<DishVO> list = dishService.listWithFlavor(dish);

        return Result.success(list);
    }

}
