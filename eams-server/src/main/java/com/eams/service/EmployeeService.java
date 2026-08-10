package com.eams.service;

import com.eams.dto.EmployeeDTO;
import com.eams.dto.EmployeeLoginDTO;
import com.eams.dto.EmployeePageQueryDTO;
import com.eams.dto.PasswordEditDTO;
import com.eams.entity.Employee;
import com.eams.result.PageResult;
import com.eams.vo.EmployeeLoginVO;

public interface EmployeeService {

    /**
     * 员工登录（支持用户名+密码 / 手机号+密码），成功后生成 JWT 并存 Redis（7 天）
     */
    EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 退出登录（删除 Redis 中的 token）
     */
    void logout();

    /**
     * 新增员工
     */
    void add(EmployeeDTO employeeDTO);

    /**
     * 分页查询
     */
    PageResult page(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用/禁用员工状态
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据 ID 查询员工信息（密码脱敏）
     */
    Employee queryById(Long id);

    /**
     * 编辑员工信息
     */
    void update(EmployeeDTO employeeDTO);

    /**
     * 修改密码
     */
    void editPassword(PasswordEditDTO passwordEditDTO);
}