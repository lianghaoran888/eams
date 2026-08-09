package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setMealMapper;

    /**
     * 新增购物车
     */
    @Override
    @Transactional
    public void add(ShoppingCartDTO shoppingCartDTO) {
        // 判断当前加入到购物车的商品是否已经存在了
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId(); // 从拦截器里截取的id,在这获取到
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.selectIfExists(shoppingCart);

        // 如果存在, 则数量加一
        if(list != null && list.size() > 0){
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumber(cart);
        }else{
            // 如果不存在, 则插入一条购物车数据
            // 判断是菜品还是套餐

            Long dishId = shoppingCart.getDishId();
            Long setmealId = shoppingCart.getSetmealId();

            if(dishId != null){
                // 本次添加的是菜品
                Dish dish = dishMapper.queryByid(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setImage(dish.getImage());
            }else{
                // 本次添加的是套餐
                Setmeal setmeal = setMealMapper.queryById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }


    }

    /**
     * 查看购物车
     */
    @Override
    public List<ShoppingCart> show() {
        // 获取当前用户id
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                                                     .userId(userId)
                                                     .build();
        List<ShoppingCart> list = shoppingCartMapper.showShoppingCart(shoppingCart);
        return list;
    }

    /**
     * 清空购物车
     */
    @Override
    public void cleanAll() {
        // 获取当前用户id
        Long userId = BaseContext.getCurrentId();
        // 清空购物车
        shoppingCartMapper.cleanAll(userId);
    }

    /**
     * 减少购物车物品
     */
    @Override
    @Transactional
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        // 判断当前加入到购物车的商品是否已经存在了
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId(); // 从拦截器里截取的id,在这获取到
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.selectIfExists(shoppingCart);

        // 如果存在, 则数量减一
        if(list != null && list.size() > 0){
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() - 1);
            shoppingCartMapper.updateNumber(cart);
            if(cart.getNumber() == 0){
                // 如果数量为0, 则删除该条数据
                shoppingCartMapper.deleteById(cart.getId());
            }
        }
    }
}
