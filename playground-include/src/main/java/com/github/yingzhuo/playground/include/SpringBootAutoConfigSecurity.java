package com.github.yingzhuo.playground.include;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AutoConfiguration
public class SpringBootAutoConfigSecurity {

    private static final List<String> CORS_ALLOWED_METHODS =
            Arrays.stream(HttpMethod.values())
                    .map(HttpMethod::toString)
                    .collect(Collectors.toList());

    private static final List<String> CORS_ALLOWED_HEADERS = Arrays.asList(
            "Authorization",
            "WWW-Authorization",
            "Content-Type",
            "Last-Modified",
            "Accept",
            "Origin",
            "X-Requested-With"
    );

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(CORS_ALLOWED_METHODS);
        configuration.setAllowedHeaders(CORS_ALLOWED_HEADERS);
        configuration.setExposedHeaders(List.of("X-Jwt-Token"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
