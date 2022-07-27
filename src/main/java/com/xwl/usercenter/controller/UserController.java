package com.xwl.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xwl.usercenter.constant.UserConstant;
import com.xwl.usercenter.entities.User;
import com.xwl.usercenter.entities.request.UserLoginRequest;
import com.xwl.usercenter.entities.request.UserRegisterRequest;
import com.xwl.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author xwl
 */

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     *
     * @param userRegisterRequest json包装的用户登入信息
     * @return
     */
    @PostMapping("/register") //接收请求参数
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {

        //逻辑判断
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1L;
        }

        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    /**
     *
     * @param userLoginRequest json包装的账号与密码
     * @param httpServletRequest session存储用户态
     * @return User
     */
    @PostMapping("/login") //接收请求参数
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {

        //逻辑判断
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        return userService.doLogin(userAccount, userPassword, httpServletRequest);
    }

    /**
     * 查询用户
     * @param userName
     * @param servletRequest
     * @return
     */
    @GetMapping("/search") //名字 String userName 根据用户登录态里面的名字
    public List<User> searchUser(String userName, HttpServletRequest servletRequest) {

        if (!isAdmin(servletRequest)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like(User::getUser_name, userName);
        }

        List<User> userList = userService.list(queryWrapper);

        //搜寻用户, 返回用户脱敏后的用户信息
        //return userList.stream().map((user) -> {return userService.getSafeUser(user);}).collect(Collectors.toList());
        return userList.stream().map(userService::getSafeUser).collect(Collectors.toList());
    }


    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest servletRequest) {
        if (!isAdmin(servletRequest)) {
            return false;
        }

        //增加效率
        if (id <= 0) {
            return false;
        }

        return userService.removeById(id);
    }

    /**
     *
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/current")
    public User getCurrent(HttpServletRequest httpServletRequest) {
        Object obj = httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) obj;
        if (currentUser == null) {
            return null;
        }

        //TODO 检验用户是否合法

        Long userId = currentUser.getId(); //从前端页面的登录态获取信息
        User user = userService.getById(userId);
        //返回脱敏用户信息
        return userService.getSafeUser(user);
    }


    /**
     * 提取复用代码
     * 优化代码 搜索 与 删除用户当中判断是否为管理员
     * 判断是否为管理员
     * @param servletRequest
     * @return
     */
    public boolean isAdmin(HttpServletRequest servletRequest) {
        Object o = servletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) o;
        return user != null && user.getUser_role() != UserConstant.USER_STATE;
    }
}