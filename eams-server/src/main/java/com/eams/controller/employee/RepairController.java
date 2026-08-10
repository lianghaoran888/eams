package com.eams.controller.employee;

import com.eams.dto.RepairPageQueryDTO;
import com.eams.dto.RepairRecordDTO;
import com.eams.result.PageResult;
import com.eams.result.Result;
import com.eams.service.RepairService;
import com.eams.vo.RepairRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工 H5 端：报修
 */
@RestController("employeeRepairController")
@RequestMapping("/employee/repair")
@Slf4j
public class RepairController {

    @Autowired
    private RepairService repairService;

    /**
     * 提交报修
     */
    @PostMapping
    public Result submit(@RequestBody RepairRecordDTO repairRecordDTO) {
        log.info("员工提交报修：{}", repairRecordDTO);
        repairService.submit(repairRecordDTO);
        return Result.success();
    }

    /**
     * 我发起的报修记录（分页）
     */
    @GetMapping("/page")
    public Result<PageResult> page(RepairPageQueryDTO repairPageQueryDTO) {
        return Result.success(repairService.pageQueryByEmployee(repairPageQueryDTO));
    }

    /**
     * 报修记录详情
     */
    @GetMapping("/{id}")
    public Result<RepairRecordVO> getById(@PathVariable Long id) {
        return Result.success(repairService.getById(id));
    }
}