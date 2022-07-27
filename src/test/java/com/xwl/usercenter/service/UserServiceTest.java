package com.xwl.usercenter.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xwl.usercenter.entities.User;
import com.xwl.usercenter.mapper.UserMapper;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test01() {
        User user = new User();
        user.setUser_name("xwl");
        user.setUser_account("123");
        user.setUser_password("awd13616881911");
        user.setAvatar_url("C:\\Users\\xwl\\Desktop\\java,Mysql\\redis\\02-实战篇\\资料\\素材");
        user.setPhone("13616881911");
        user.setEmail("1531119008@qq.com");
        boolean res = userService.save(user);
        Assertions.assertTrue(res);
        Assertions.assertEquals(true, res);
    }


    @Test
    public void selectOne() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUser_account, "xwl 12");
        queryWrapper.eq(User::getUser_password, "12345678");
        int count = userService.count(queryWrapper);
        Assertions.assertEquals(0, count);
    }

    @Test
    void userRegister() {
        User user = new User();
        String account = "xwl";
        String password = "";
        String checkPassword = "";
        long l = userService.userRegister(account, password, checkPassword);
        Assertions.assertEquals(-1L, l);

        account = "xwl 12";
        password = "12345678";
        checkPassword = "12345678";
        l = userService.userRegister(account, password, checkPassword);
        Assertions.assertTrue(l > 0);

        //account = "xwl";
        //password = "";
        //checkPassword = "";
        //l = userService.userRegister(account, password, checkPassword);
        //Assertions.assertEquals(-1L, l);
        //
        //account = "xwl";
        //password = "";
        //checkPassword = "";
        //l = userService.userRegister(account, password, checkPassword);
        //Assertions.assertEquals(-1L, l);
    }
}