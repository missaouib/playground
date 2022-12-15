package com.github.yingzhuo.playground.config;

import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Value("classpath:config/users.xml")
public @interface UsersPropertiesFile {
}
