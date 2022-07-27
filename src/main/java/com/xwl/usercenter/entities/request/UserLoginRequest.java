package com.xwl.usercenter.entities.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author xwl
 */

@Data
public class UserLoginRequest implements Serializable {
    //版本同一
    private static final long serialVersionUID = -6860387079654115433L;

    private String userAccount;

    private String userPassword;
}