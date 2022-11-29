package com.github.yingzhuo.playground.controller.valueobject;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class LoginVO implements Serializable {

    @NotEmpty(message = "登录用户名不可为空")
    private String username;

    @NotEmpty(message = "登录密码不可为空")
    private String password;

}
