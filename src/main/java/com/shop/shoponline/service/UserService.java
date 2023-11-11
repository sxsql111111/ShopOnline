package com.shop.shoponline.service;

import com.shop.shoponline.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.shoponline.query.UserLoginQuery;
import com.shop.shoponline.vo.LoginResultVO;
import com.shop.shoponline.vo.UserVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sx
 * @since 2023-11-07
 */
public interface UserService extends IService<User> {
//    用户登录
    LoginResultVO login(UserLoginQuery query);

}
