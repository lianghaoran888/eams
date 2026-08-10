package com.eams.controller.admin;

import com.eams.dto.RepairPageQueryDTO;
import com.eams.dto.RepairRecordDTO;
import com.eams.result.PageResult;
import com.eams.result.Result;
import com.eams.service.RepairService;
import com.eams.vo.RepairRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端：报修管理
 */
@RestController("adminRepairController")
@RequestMapping("/admin/repair")
@Slf4j
public class RepairController {

    @Autowired
    private RepairService repairService;

    /**
     * 报修记录分页查询
     */
    @GetMapping("/page")
    public Result<PageResult> page(RepairPageQueryDTO repairPageQueryDTO) {
        log.info("报修记录分页查询：{}", repairPageQueryDTO);
        return Result.success(repairService.pageQuery(repairPageQueryDTO));
    }

    /**
     * 报修记录详情
     */
    @GetMapping("/{id}")
    public Result<RepairRecordVO> getById(@PathVariable Long id) {
        return Result.success(repairService.getById(id));
    }

    /**
     * 完成维修（填写维修公司/费用/结果）
     */
    @PutMapping("/{id}")
    public Result complete(@PathVariable Long id, @RequestBody RepairRecordDTO repairRecordDTO) {
        log.info("完成维修：id={}, {}", id, repairRecordDTO);
        repairService.complete(id, repairRecordDTO);
        return Result.success();
    }
}