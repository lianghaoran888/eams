package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 新增购物车
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     */
    List<ShoppingCart> show();

    /**
     * 清空购物车
     */
    void cleanAll();

    /**
     * 减少购物车物品
     */
    void sub(ShoppingCartDTO shoppingCartDTO);
}
