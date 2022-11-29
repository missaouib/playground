package com.github.yingzhuo.playground;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yingzhuo.playground.config.BeanSetupUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

@Slf4j
@Configuration
public class ApplicationBootJackson {

    @Autowired(required = false)
    public void afterPropertiesSet(@Nullable ObjectMapper om) {
        BeanSetupUtils.setupObjectMapper(om);
    }

}
