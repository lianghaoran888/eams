package com.eams.controller.employee;

import com.eams.dto.AssetPageQueryDTO;
import com.eams.result.PageResult;
import com.eams.result.Result;
import com.eams.service.AssetService;
import com.eams.vo.AssetVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工 H5 端：资产查看
 */
@RestController("employeeAssetController")
@RequestMapping("/employee/asset")
@Slf4j
public class AssetController {

    @Autowired
    private AssetService assetService;

    /**
     * 资产分页查询（员工可见资产台账）
     */
    @GetMapping("/page")
    public Result<PageResult> page(AssetPageQueryDTO assetPageQueryDTO) {
        return Result.success(assetService.pageQuery(assetPageQueryDTO));
    }

    /**
     * 资产详情
     */
    @GetMapping("/{id}")
    public Result<AssetVO> getById(@PathVariable Long id) {
        return Result.success(assetService.getById(id));
    }
}