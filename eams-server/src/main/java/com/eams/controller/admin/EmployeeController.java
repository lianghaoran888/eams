package com.eams.controller.admin;

import com.eams.dto.EmployeeDTO;
import com.eams.dto.EmployeeLoginDTO;
import com.eams.dto.EmployeePageQueryDTO;
import com.eams.dto.PasswordEditDTO;
import com.eams.entity.Employee;
import com.eams.result.PageResult;
import com.eams.result.Result;
import com.eams.service.EmployeeService;
import com.eams.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端：员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录（用户名 + 密码）
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("管理端员工登录：{}", employeeLoginDTO.getUsername());
        return Result.success(employeeService.login(employeeLoginDTO));
    }

    /**
     * 退出
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        employeeService.logout();
        return Result.success();
    }

    /**
     * 新增员工
     */
    @PostMapping
    public Result add(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工: {}", employeeDTO);
        employeeService.add(employeeDTO);
        return Result.success();
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询, 参数为: {}", employeePageQueryDTO);
        return Result.success(employeeService.page(employeePageQueryDTO));
    }

    /**
     * 启用/禁用员工状态
     */
    @PostMapping("/status/{status}")
    public Result startOrStop(Long id, @PathVariable Integer status) {
        log.info("启用/禁用员工状态 {},{}", status, id);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 根据 ID 查询员工信息(查询回显)
     */
    @GetMapping("/{id}")
    public Result<Employee> queryById(@PathVariable Long id) {
        log.info("根据Id查询员工信息: {}", id);
        return Result.success(employeeService.queryById(id));
    }

    /**
     * 编辑员工信息
     */
    @PutMapping
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("编辑员工信息 {}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/editPassword")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("修改密码, empId={}", passwordEditDTO.getEmpId());
        employeeService.editPassword(passwordEditDTO);
        return Result.success();
    }
}