package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    /**
     * 3. 判断这个用户是否为新用户
     */
    @Select("select * from user where openid = #{openid}")
    User selectIfExistsOpenId(String openid);

    /**
     * 4. 如果为新用户,则自动注册
     */
    void insert(User user);


    @Select("select * from user where id = #{id}")
    User getById(Long userId);
}
