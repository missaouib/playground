package com.github.yingzhuo.playground.include;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Slf4j
public class SpringBootAutoConfigSecurityDSL extends AbstractHttpConfigurer<SpringBootAutoConfigSecurityDSL, HttpSecurity> {

    @Override
    public void configure(HttpSecurity http) {
        // nop
    }

}
