package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import org.apache.ibatis.annotations.Select;

public interface UserService {
    /**
     * 微信用户登录
     */
    User wxlogin(UserLoginDTO userLoginDTO);

}
