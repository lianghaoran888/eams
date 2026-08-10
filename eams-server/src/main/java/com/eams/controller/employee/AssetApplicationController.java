package com.eams.controller.employee;

import com.eams.dto.AssetApplicationDTO;
import com.eams.dto.AssetApplicationPageQueryDTO;
import com.eams.result.PageResult;
import com.eams.result.Result;
import com.eams.service.AssetApplicationService;
import com.eams.vo.AssetApplicationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工 H5 端：资产申请
 */
@RestController("employeeAssetApplicationController")
@RequestMapping("/employee/application")
@Slf4j
public class AssetApplicationController {

    @Autowired
    private AssetApplicationService assetApplicationService;

    /**
     * 提交资产申请
     */
    @PostMapping
    public Result<AssetApplicationVO> submit(@RequestBody AssetApplicationDTO assetApplicationDTO) {
        log.info("员工提交资产申请：{}", assetApplicationDTO);
        return Result.success(assetApplicationService.submit(assetApplicationDTO));
    }

    /**
     * 我提交的申请单（分页）
     */
    @GetMapping("/page")
    public Result<PageResult> page(AssetApplicationPageQueryDTO assetApplicationPageQueryDTO) {
        return Result.success(assetApplicationService.pageQueryByEmployee(assetApplicationPageQueryDTO));
    }

    /**
     * 申请单详情
     */
    @GetMapping("/{id}")
    public Result<AssetApplicationVO> getById(@PathVariable Long id) {
        return Result.success(assetApplicationService.getById(id));
    }
}