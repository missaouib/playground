package com.github.yingzhuo.playground;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static com.github.yingzhuo.playground.config.PackagesNames.PKG_TOP;

@Slf4j
@Configuration
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = PKG_TOP)
public class ApplicationBootConfig {
}
