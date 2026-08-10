package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.AssetOverviewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端：报表统计
 */
@RestController
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 资产总览（总数/在库/已领用/维修中/报废）
     */
    @GetMapping("/overview")
    public Result<AssetOverviewVO> overview() {
        return Result.success(reportService.overview());
    }
}