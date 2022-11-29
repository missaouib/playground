package com.github.yingzhuo.playground.include.exception;

import org.springframework.lang.Nullable;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {

    public BusinessException() {
        this(null);
    }

    public BusinessException(@Nullable String message) {
        super(message);
    }

    public static BusinessException of(@Nullable String message) {
        return new BusinessException(message);
    }

}
