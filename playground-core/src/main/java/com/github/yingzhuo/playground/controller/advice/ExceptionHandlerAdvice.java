package com.github.yingzhuo.playground.controller.advice;

import com.github.yingzhuo.playground.include.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import spring.turbo.exception.ValidationException;
import spring.turbo.util.StringPool;
import spring.turbo.webmvc.api.Json;

import java.util.LinkedList;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    // -----------------------------------------------------------------------------------------------------------------
    // 400
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Json handleBusinessException(BusinessException e) {
        log.trace("处理业务异常");
        return Json.newInstance()
                .code("400")
                .errorMessage(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Json handle(MissingServletRequestParameterException ex) {
        log.debug(ex.getMessage());
        val errorMessage = String.format("请求参数缺失: %s", ex.getParameterName());
        return Json.newInstance()
                .code("400")
                .errorMessage(errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Json handle(ValidationException ex) {

        val messages = new LinkedList<String>();
        for (val oe : ex.getErrors().getAllErrors()) {
            messages.add(oe.getDefaultMessage());
        }

        // 合并多条错误信息
        val errorMessage = StringUtils.join(messages, StringPool.COMMA);
        return Json.newInstance()
                .code("400")
                .errorMessage(errorMessage);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // 404
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Json handle(NoHandlerFoundException ex) {
        log.debug(ex.getMessage());
        return Json.newInstance()
                .code("404")
                .errorMessage("没有此资源");
    }

}
