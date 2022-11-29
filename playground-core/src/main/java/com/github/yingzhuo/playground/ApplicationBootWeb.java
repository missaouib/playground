package com.github.yingzhuo.playground;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.yingzhuo.playground.config.BeanSetupUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

import static com.github.yingzhuo.playground.config.PackagesNames.PKG_WEB;

@Slf4j
@Configuration
@ServletComponentScan(PKG_WEB)
@AutoConfigureAfter(ApplicationBootJackson.class)
@RequiredArgsConstructor
public class ApplicationBootWeb implements WebMvcConfigurer {

    private static final MediaType MEDIA_TYPE_YAML = MediaType.valueOf("text/yaml");

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON)
                .ignoreAcceptHeader(true)
                .favorParameter(true)
                .parameterName("_format")
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("yaml", MEDIA_TYPE_YAML)
                .mediaType("yml", MEDIA_TYPE_YAML);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(createYamlHttpMessageConverter());
    }

    private HttpMessageConverter<?> createYamlHttpMessageConverter() {
        final ObjectMapper om = new ObjectMapper(new YAMLFactory());
        BeanSetupUtils.setupObjectMapper(om); // 配置ObjectMapper

        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(om);
        converter.setSupportedMediaTypes(
                Collections.singletonList(MEDIA_TYPE_YAML)
        );
        return converter;
    }

}
