package com.github.yingzhuo.playground.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.turbo.webmvc.api.Json;

@RestController
public class TestController {

    @PostMapping("/test")
    public Json test() {
        return Json.newInstance();
    }

}
