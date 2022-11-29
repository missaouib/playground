package com.github.yingzhuo.playground.config;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

@Validated
@ConfigurationProperties(prefix = "playground")
public record Author(
        @NotBlank(message = "作者名不可为空")
        @DefaultValue("应卓")
        String name,

        @NotBlank(message = "作者电子邮件不可为空")
        @Email(message = "必须是一个合法的电子邮件地址")
        @DefaultValue("yingzhor@gmail.com")
        String email) implements Serializable {
}
