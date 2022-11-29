package com.github.yingzhuo.playground;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Slf4j
@Configuration
@ImportResource("classpath:/com/github/yingzhuo/playground/include/spring-include.xml")
@RequiredArgsConstructor
public class ApplicationBootBean {

    private final FormatterRegistry registry;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.setConnectTimeout(Duration.ofSeconds(2L)).setReadTimeout(Duration.ofSeconds(2L)).build();
    }

    @PostConstruct
    private void init() {
    }

}
