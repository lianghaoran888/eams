package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminSetMealController")
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetMealController {
    @Autowired
    private SetMealService setMealService;

    /**
     * 套餐分页条件查询
     */
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("🌟套餐分页条件查询: {}", setmealPageQueryDTO);
        PageResult pageResult = setMealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增套餐
     */
    @PostMapping
    @CacheEvict(cacheNames = "setMealCache", key = "#setmealDTO.categoryId") // 清除1条缓存, 精确id
    public Result add(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐: {}", setmealDTO);
        setMealService.add(setmealDTO);
        return Result.success();
    }

    /**
     * 批量删除套餐
     */
    @DeleteMapping
    @CacheEvict(cacheNames = "setMealCache", allEntries = true) // 清除所有的缓存
    public Result delete(@RequestParam List<Long> ids){
        log.info("批量删除套餐: {}", ids);
        setMealService.delete(ids);
        return Result.success();
    }

    /**
     * 修改套餐 : 查询回显
     */
    @GetMapping("/{id}")
    @CacheEvict(cacheNames = "setMealCache", allEntries = true) // 清除所有的缓存
    public Result<SetmealVO> selectById(@PathVariable Long id){
        log.info("修改套餐 : 查询回显 : {}", id);
        SetmealVO setmealVO = setMealService.selectById(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐
     */
    @PutMapping
    @CacheEvict(cacheNames = "setMealCache", allEntries = true) // 清除所有的缓存
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐: {}", setmealDTO);
        setMealService.updateWithSetMealDish(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐启售,停售状态
     */
    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "setMealCache", allEntries = true) // 清除所有的缓存
    public Result startOrStop(@PathVariable Integer status, Long id){
        log.info("套餐启售,停售状态: {},{}", status, id);
        setMealService.startOrStop(status, id);
        return Result.success();

    }


}
