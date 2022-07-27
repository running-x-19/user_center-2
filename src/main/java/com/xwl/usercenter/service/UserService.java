package com.xwl.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xwl.usercenter.entities.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);


    /**
     *登录
     * @param userAccount
     * @param userPassword
     * @param servletRequest 创建session(通常形容为登录态)
     * @return 脱敏后用户信息
     */
    User doLogin(String userAccount, String userPassword, HttpServletRequest servletRequest);


    /**
     * 提供一个接口
     * @param originUser
     * @return 脱敏后的用户
     */
    User getSafeUser(User originUser);
}