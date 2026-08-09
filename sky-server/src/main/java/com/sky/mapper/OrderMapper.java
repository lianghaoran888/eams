package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 2. 向订单表中插入一条数据
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     */
    void update(Orders orders);

    /**
     * 分页条件查询并按下单时间排序
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 商家端 :
     * 根据状态统计订单数量
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * 用户端 :
     * 处理待支付超时 (超过15分钟未支付, 自动取消订单)
     *
     * 商家端 :
     * 处理一直处于派送中状态的订单 (订单处于派送中一天时间后, 如果忘记点击已完成, 自动改为已完成)
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOutTime(Integer status, LocalDateTime orderTime);
}
