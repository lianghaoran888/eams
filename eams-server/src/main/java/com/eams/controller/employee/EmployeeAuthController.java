package com.eams.controller.employee;

import com.eams.context.BaseContext;
import com.eams.dto.EmployeeLoginDTO;
import com.eams.entity.Employee;
import com.eams.result.Result;
import com.eams.service.EmployeeService;
import com.eams.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工 H5 端：登录/退出/当前用户
 */
@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeAuthController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录（手机号 + 密码）
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工 H5 登录：{}", employeeLoginDTO.getPhone());
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
     * 当前登录员工信息
     */
    @GetMapping("/me")
    public Result<Employee> me() {
        return Result.success(employeeService.queryById(BaseContext.getCurrentId()));
    }
}