package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController("adminDishController")
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品和口味
     * 🌟未使用Cache注解
     */
/*    @PostMapping
    public Result add(@RequestBody DishDTO dishDTO){
        log.info("新增菜品: {}", dishDTO);
        dishService.addWithFlavor(dishDTO);

        // 清理redis缓存
        String key = "dish_" + dishDTO.getCategoryId();
//        redisTemplate.delete(key);
        clearCache(key);

        return Result.success();
    }*/
    /**
     * 新增菜品和口味
     * 🌟使用Cache注解
     */
    @PostMapping
    @CacheEvict(cacheNames = "dish_new_", key = "#dishDTO.categoryId") // 清除一条缓存, 精确id
    public Result add(@RequestBody DishDTO dishDTO){
        log.info("新增菜品: {}", dishDTO);
        dishService.addWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页条件查询
     */
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("分页条件查询: {}", dishPageQueryDTO);
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品
     * 🌟未使用Cache注解
     */
/*    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        log.info("批量删除菜品: {}", ids);
        dishService.deleteBatch(ids);

        // 清理redis缓存, 将所有的dish_*删除
//        Set keys = redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);
        clearCache("dish_*");

        return Result.success();
    }*/
    /**
     * 批量删除菜品
     * 🌟使用Cache注解
     */
    @DeleteMapping
    @CacheEvict(cacheNames = "dish_new_", allEntries = true) // 清除所有缓存
    public Result delete(@RequestParam List<Long> ids){
        log.info("批量删除菜品: {}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据Id查询菜品(查询回显)
     */
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据Id查询菜品(查询回显): {}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     * 🌟未使用Cache注解
     */
/*    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品: {}", dishDTO);
        dishService.updateWithFlavor(dishDTO);

        // 清理redis缓存, 将所有的dish_*删除
//        Set keys = redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);
        clearCache("dish_*");

        return Result.success();
    }*/
    /**
     * 修改菜品
     * 🌟使用Cache注解
     */
    @PutMapping
    @CacheEvict(cacheNames = "dish_new_", allEntries = true) // 清除所有缓存
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品: {}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 启用/禁用菜品状态
     * 🌟未使用Cache注解
     *
     */
/*    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id){
        log.info("启用/禁用菜品状态: {},{}", status, id);
        dishService.startOrStop(status, id);

        // 清理redis缓存, 将所有的dish_*删除
//        Set keys = redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);
        clearCache("dish_*");

        return Result.success();
    }*/
    /**
     * 启用/禁用菜品状态
     * 🌟使用Cache注解
     */
    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "dish_new_", allEntries = true) // 清除所有缓存
    public Result startOrStop(@PathVariable Integer status, Long id){
        log.info("启用/禁用菜品状态: {},{}", status, id);
        dishService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 新增套餐操作中:
     * 根据分类id查询菜品
     */
    @GetMapping("/list")
    public Result<List<Dish>> getBycategoryId(Long categoryId){
        log.info("根据分类id查询菜品: {}", categoryId);
        List<Dish> list = dishService.getBycategoryId(categoryId);
        return Result.success(list);
    }

    /**
     * 清理缓存方法
     * 🌟未使用Cache注解时的方法
     */
/*    private void clearCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }*/
}
