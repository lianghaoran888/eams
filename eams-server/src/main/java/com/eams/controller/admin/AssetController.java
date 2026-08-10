package com.eams.controller.admin;

import com.eams.dto.AssetDTO;
import com.eams.dto.AssetPageQueryDTO;
import com.eams.result.PageResult;
import com.eams.result.Result;
import com.eams.service.AssetService;
import com.eams.vo.AssetVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端：资产管理（资产台账）
 */
@RestController("adminAssetController")
@RequestMapping("/admin/asset")
@Slf4j
public class AssetController {

    @Autowired
    private AssetService assetService;

    /**
     * 新增资产
     */
    @PostMapping
    public Result add(@RequestBody AssetDTO assetDTO) {
        log.info("新增资产：{}", assetDTO);
        assetService.add(assetDTO);
        return Result.success();
    }

    /**
     * 资产分页查询
     */
    @GetMapping("/page")
    public Result<PageResult> page(AssetPageQueryDTO assetPageQueryDTO) {
        log.info("资产分页查询：{}", assetPageQueryDTO);
        return Result.success(assetService.pageQuery(assetPageQueryDTO));
    }

    /**
     * 资产详情
     */
    @GetMapping("/{id}")
    public Result<AssetVO> getById(@PathVariable Long id) {
        return Result.success(assetService.getById(id));
    }

    /**
     * 修改资产
     */
    @PutMapping
    public Result update(@RequestBody AssetDTO assetDTO) {
        assetService.update(assetDTO);
        return Result.success();
    }

    /**
     * 删除资产（仅允许删除在库资产）
     */
    @DeleteMapping
    public Result deleteById(Long id) {
        assetService.deleteById(id);
        return Result.success();
    }

    /**
     * 更新资产状态（如标记报废 / 回在库）
     */
    @PutMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, Long id) {
        assetService.updateStatus(id, status);
        return Result.success();
    }
}