package com.github.yingzhuo.playground.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.lang.Nullable;
import spring.turbo.jackson2.JsonMixins;
import spring.turbo.webmvc.api.Json;

public final class BeanSetupUtils {

    private BeanSetupUtils() {
        super();
    }

    // 配置ObjectMapper实例
    public static void setupObjectMapper(@Nullable ObjectMapper om) {
        if (om != null) {
            // 添加module
            om.registerModule(new Jdk8Module());
            om.registerModule(new JavaTimeModule());
            om.registerModule(new GuavaModule());

            // 添加MixIn
            om.addMixIn(Json.class, JsonMixins.Style1.class);
        }
    }

}
