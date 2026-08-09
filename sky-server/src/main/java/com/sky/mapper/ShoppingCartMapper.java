package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 判断当前加入到购物车的商品是否已经存在了
     */
    List<ShoppingCart> selectIfExists(ShoppingCart shoppingCart);

    /**
     * 更新数量
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumber(ShoppingCart shoppingCart);

    /**
     * 如果不存在, 则插入一条购物车数据
     */
    @Insert("insert into shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time)" +
            " VALUES(#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 查看购物车
     */
    List<ShoppingCart> showShoppingCart(ShoppingCart shoppingCart);

    /**
     * 清空购物车
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void cleanAll(Long userId);

    /**
     * 如果数量为0, 则删除该条购物车数据
     */
    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);

    /**
     * 再来一单 :
     * 批量插入购物车数据
     */
    void insertBatch(List<ShoppingCart> shoppingCartList);
}
