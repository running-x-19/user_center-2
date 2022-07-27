package com.xwl.usercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author xwl
 */

@SpringBootApplication
@MapperScan("com.xwl.usercenter.mapper")
public class UserCenterApplication {
    public static void main(String[] args) {
        System.out.println("....");
        SpringApplication.run(UserCenterApplication.class, args);
    }
}