package com.github.yingzhuo.playground;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.extensions.spring.SpringMapperConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.github.yingzhuo.playground.config.PackagesNames.PKG_MAPSTRUCT;

@Slf4j
@Configuration
@ComponentScan(PKG_MAPSTRUCT)
@SpringMapperConfig(conversionServiceAdapterPackage = PKG_MAPSTRUCT)
public class ApplicationBootMapstruct {
}
