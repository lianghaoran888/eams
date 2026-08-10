package com.eams.controller.admin;

import com.eams.annotation.RateLimit;
import com.eams.dto.AssetApplicationAuditDTO;
import com.eams.dto.AssetApplicationPageQueryDTO;
import com.eams.result.PageResult;
import com.eams.result.Result;
import com.eams.service.AssetApplicationService;
import com.eams.vo.AssetApplicationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端：申请审批
 */
@RestController("adminAssetApplicationController")
@RequestMapping("/admin/application")
@Slf4j
public class AssetApplicationController {

    @Autowired
    private AssetApplicationService assetApplicationService;

    /**
     * 申请单分页查询
     */
    @GetMapping("/page")
    public Result<PageResult> page(AssetApplicationPageQueryDTO assetApplicationPageQueryDTO) {
        log.info("申请单分页查询：{}", assetApplicationPageQueryDTO);
        return Result.success(assetApplicationService.pageQuery(assetApplicationPageQueryDTO));
    }

    /**
     * 申请单详情
     */
    @GetMapping("/{id}")
    public Result<AssetApplicationVO> getById(@PathVariable Long id) {
        return Result.success(assetApplicationService.getById(id));
    }

    /**
     * 审批（通过/拒绝），接口限流：每分钟最多 10 次
     */
    @PutMapping("/audit")
    @RateLimit(key = "applicationAudit", limit = 10, period = 60)
    public Result audit(@RequestBody AssetApplicationAuditDTO auditDTO) {
        log.info("审批申请单：{}", auditDTO);
        assetApplicationService.audit(auditDTO);
        return Result.success();
    }

    /**
     * 标记已完成（资产归还）
     */
    @PutMapping("/complete/{id}")
    public Result complete(@PathVariable Long id) {
        assetApplicationService.complete(id);
        return Result.success();
    }
}