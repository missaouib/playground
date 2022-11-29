package com.github.yingzhuo.playground.controller;

import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.turbo.io.FilenameUtils;
import spring.turbo.module.webmvc.entity.AttachmentResponseEntity;
import spring.turbo.webmvc.api.Json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/attachment")
public class AttachmentController {

    private static final String DIR = "/Users/yingzhuo/Downloads";

    @ResponseBody
    @GetMapping
    public Json listAttachments() throws IOException {
        val list = Files.list(Paths.get(DIR))
                .map(Path::toString)
                .filter(path -> FilenameUtils.getExtension(path).equalsIgnoreCase("xlsx"))
                .map(FilenameUtils::getName)
                .collect(Collectors.toList());
        return Json.newInstance()
                .payload("list", list);
    }

    @GetMapping("/download")
    public AttachmentResponseEntity download(@RequestParam("filename") String filename) {
        return AttachmentResponseEntity.builder()
                .attachmentName(filename)
                .content(Paths.get(DIR, filename))
                .build();
    }

}
