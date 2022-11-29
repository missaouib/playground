package com.github.yingzhuo.playground.config;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import spring.turbo.util.StringUtils;

import java.io.Serializable;

/**
 * SM2公钥与私钥
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "sm2-keypair")
@Validated
public class SM2KeyPairProperties implements Serializable {

    @NotEmpty(message = "SM2公钥不可为空")
    private String publicKey;

    @NotEmpty(message = "SM2私钥不可为空")
    private String privateKey;

    @PostConstruct
    private void init() {
        this.publicKey = StringUtils.deleteWhitespace(this.publicKey);
        this.privateKey = StringUtils.deleteWhitespace(this.privateKey);
    }

}
