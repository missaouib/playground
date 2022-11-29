package com.github.yingzhuo.playground.service;

import com.github.yingzhuo.playground.config.Author;
import org.springframework.stereotype.Service;

@Service
public final class MiscServiceImpl implements MiscService {

    private final Author author;

    public MiscServiceImpl(Author author) {
        this.author = author;
    }

    @Override
    public Author getAuthor() {
        return this.author;
    }

    @Override
    public String getPingResponse() {
        return "pong";
    }

}
