package com.github.yingzhuo.playground.service;

import com.github.yingzhuo.playground.config.Author;

public sealed interface MiscService permits MiscServiceImpl {

    public Author getAuthor();

    public String getPingResponse();

}
