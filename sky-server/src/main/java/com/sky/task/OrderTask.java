package com.sky.task;

import com.sky.constant.MessageConstant;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 用户端 :
     * 处理待支付超时订单 (超过15分钟未支付, 自动取消订单)
     */
    @Scheduled(cron = "0 * * * * ? ") // 每分钟检查一次
    public void taskOutTimePayOrder(){
        log.info("处理待支付超时订单 {}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusMinutes(-15); // (当前时间 - 15min)

        // select * from orders where status = 1待付款 and order_time < (当前时间 - 15min)
        List<Orders> outTimePayOrders = orderMapper.getByStatusAndOutTime(Orders.PENDING_PAYMENT, time);

        // 如果查询有结果,就更新orders集合
        if(outTimePayOrders != null && outTimePayOrders.size() > 0){
            for (Orders outTimePayOrder : outTimePayOrders) {
                outTimePayOrder.setStatus(Orders.CANCELLED); // 6已取消
                outTimePayOrder.setCancelReason(MessageConstant.ORDER_TIME_OUT);
                outTimePayOrder.setCancelTime(LocalDateTime.now());

                ////更新orders集合
                orderMapper.update(outTimePayOrder);
            }
        }
    }

    /**
     * 商家端 :
     * 处理一直处于派送中状态的订单 (订单处于派送中一天时间后, 如果忘记点击已完成, 自动改为已完成)
     */
    @Scheduled(cron = "0 0 1 * * ? ") // 每天凌晨一点检查一次
    public void taskDeliveryOrder(){
        log.info("处理一直处于派送中状态的订单 {}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusMinutes(-60); // (当前时间 - 60min)

        // select * from orders where status = 4派送中 and order_time < (当前时间 - 60min)
        List<Orders> alwaysDeliveryOrders = orderMapper.getByStatusAndOutTime(Orders.DELIVERY_IN_PROGRESS, time);

        // 如果查询有结果, 就将orders集合更新
        if(alwaysDeliveryOrders != null && alwaysDeliveryOrders.size() > 0){
            for (Orders alwaysDeliveryOrder : alwaysDeliveryOrders) {
                alwaysDeliveryOrder.setStatus(Orders.COMPLETED); // 5已完成

                ////将orders集合更新
                orderMapper.update(alwaysDeliveryOrder);
            }
        }
    }
}
