package com.eams.mapper;

import com.github.pagehelper.Page;
import com.eams.annotation.AutoFill;
import com.eams.dto.EmployeePageQueryDTO;
import com.eams.entity.Employee;
import com.eams.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工（管理端登录）
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 根据手机号查询员工（员工 H5 端登录）
     */
    @Select("select * from employee where phone = #{phone}")
    Employee getByPhone(String phone);

    /**
     * 插入员工数据
     */
    @Insert("insert into employee (name, username, password, phone, sex, id_number, status, role, department, create_time, update_time, create_user, update_user) " +
            "values (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{role},#{department},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AutoFill(OperationType.INSERT)
    void insert(Employee employee);

    /**
     * 分页查询
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用/禁用员工状态 / 编辑员工信息
     */
    @AutoFill(OperationType.UPDATE)
    void update(Employee employee);

    /**
     * 根据 ID 查询员工信息
     */
    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);
}