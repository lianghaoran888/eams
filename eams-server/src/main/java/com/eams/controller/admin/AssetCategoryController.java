package com.eams.controller.admin;

import com.eams.dto.AssetCategoryDTO;
import com.eams.dto.AssetCategoryPageQueryDTO;
import com.eams.entity.AssetCategory;
import com.eams.result.PageResult;
import com.eams.result.Result;
import com.eams.service.AssetCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端：资产分类管理
 */
@RestController("adminAssetCategoryController")
@RequestMapping("/admin/category")
@Slf4j
public class AssetCategoryController {

    @Autowired
    private AssetCategoryService assetCategoryService;

    /**
     * 新增分类
     */
    @PostMapping
    public Result add(@RequestBody AssetCategoryDTO assetCategoryDTO) {
        log.info("新增分类：{}", assetCategoryDTO);
        assetCategoryService.add(assetCategoryDTO);
        return Result.success();
    }

    /**
     * 分类分页查询
     */
    @GetMapping("/page")
    public Result<PageResult> page(AssetCategoryPageQueryDTO assetCategoryPageQueryDTO) {
        log.info("分类分页查询：{}", assetCategoryPageQueryDTO);
        return Result.success(assetCategoryService.pageQuery(assetCategoryPageQueryDTO));
    }

    /**
     * 删除分类
     */
    @DeleteMapping
    public Result deleteById(Long id) {
        log.info("删除分类：{}", id);
        assetCategoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改分类
     */
    @PutMapping
    public Result update(@RequestBody AssetCategoryDTO assetCategoryDTO) {
        assetCategoryService.update(assetCategoryDTO);
        return Result.success();
    }

    /**
     * 启用、禁用分类状态
     */
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        assetCategoryService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 查询启用状态的全部分类（Redis 缓存 1 小时）
     */
    @GetMapping("/list")
    public Result<List<AssetCategory>> list() {
        return Result.success(assetCategoryService.list());
    }
}