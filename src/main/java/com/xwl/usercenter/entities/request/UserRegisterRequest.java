package com.xwl.usercenter.entities.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author xwl
 */

@Data
public class UserRegisterRequest implements Serializable {

    //版本同一
    private static final long serialVersionUID = -208191197275716974L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}