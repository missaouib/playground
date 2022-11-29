package com.github.yingzhuo.playground.controller

import com.github.yingzhuo.playground.include.exception.BusinessException
import com.github.yingzhuo.playground.service.MiscService
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import spring.turbo.webmvc.api.Json

@RestController
class MiscController(private val miscService: MiscService) {

    @GetMapping("/author")
    @Retryable(exclude = [BusinessException::class], backoff = Backoff(delay = 500L))
    fun author(): Json {
        val authorProperties = miscService.getAuthor();

        return Json.newInstance()
            .payload("name", authorProperties.name)
            .payload("email", authorProperties.email)
    }

    @GetMapping("/ping")
    fun ping(): Json =
        Json.newInstance()
            .payload("response", miscService.getPingResponse())

}
