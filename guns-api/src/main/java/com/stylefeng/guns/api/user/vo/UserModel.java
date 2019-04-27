package com.stylefeng.guns.api.user.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserModel implements Serializable {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
}
