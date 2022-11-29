package com.github.yingzhuo.playground;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ApplicationBoot {

    public static void main(String[] args) {
        final var app = new SpringApplication(ApplicationBoot.class);
        app.run(args);
    }

}
