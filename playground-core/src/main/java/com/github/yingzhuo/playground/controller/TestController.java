package com.github.yingzhuo.playground.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spring.turbo.bean.DateZones;
import spring.turbo.bean.jsr380.DecentDateZones;
import spring.turbo.webmvc.api.Json;

import java.io.Serializable;

@RestController
public class TestController {

    @PostMapping("/test")
    public Json test(@RequestBody @Validated TestVO vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Json.newInstance()
                    .payload("hasError", true);
        }
        return Json.newInstance()
                .payload("vo", vo);
    }

    @Getter
    @Setter
    @ToString
    public static class TestVO implements Serializable {

        @DecentDateZones(message = "日期带非法")
        private DateZones dateZones;
    }

}
