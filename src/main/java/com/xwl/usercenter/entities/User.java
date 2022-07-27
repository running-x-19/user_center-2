package com.xwl.usercenter.entities;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名称
     */
    private String user_name;

    /**
     * 用户账号
     */
    private String user_account;

    /**
     * 用户密码
     */
    private String user_password;

    /**
     * 头像
     */
    private String avatar_url;

    /**
     * 姓名
     */
    private Integer gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 用户状态
     */
    private Integer user_status;

    /**
     * 创立时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;

    /**
     * 用户角色
     */
    private Integer user_role;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer is_delete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}