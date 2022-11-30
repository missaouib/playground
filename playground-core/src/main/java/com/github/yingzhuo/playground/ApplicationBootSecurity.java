package com.github.yingzhuo.playground;

import com.github.yingzhuo.playground.config.SM2KeyPairProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import spring.turbo.module.security.encoder.EncodingIds;
import spring.turbo.module.security.encoder.PasswordEncoderFactories;
import spring.turbo.module.security.exception.SecurityExceptionHandler;
import spring.turbo.module.security.jwt.AlgorithmFactory;
import spring.turbo.module.security.jwt.SM2AlgorithmFactory;
import spring.turbo.util.propertysource.YamlPropertySourceFactory;
import spring.turbo.webmvc.token.BasicTokenResolver;
import spring.turbo.webmvc.token.TokenResolver;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
@EnableConfigurationProperties({
        SM2KeyPairProperties.class
})
@RequiredArgsConstructor
@PropertySource(value = "classpath:config/sm2.yaml", factory = YamlPropertySourceFactory.class)
public class ApplicationBootSecurity {

    @Bean
    public AlgorithmFactory algorithmFactory(SM2KeyPairProperties sm2KeyPair) {
        return new SM2AlgorithmFactory(sm2KeyPair.getPublicKey(), sm2KeyPair.getPrivateKey());
    }

    @Primary
    @Bean(name = "primaryTokenResolver")
    public TokenResolver tokenResolver() {
        return TokenResolver.builder()
                .bearerToken()
                .fromHttpHeader("X-Jwt-Token")
                .build();
    }

    @Bean(name = "basicTokenResolver")
    public TokenResolver basicTokenResolver() {
        return new BasicTokenResolver();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(EncodingIds.SM3);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // enabled
        http.anonymous();

        // enabled
        http.cors();

        // disabled
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // disabled
        http.csrf()
                .disable();

        // disabled
        http.httpBasic()
                .disable();

        // disabled
        http.jee()
                .disable();

        // disabled
        http.formLogin()
                .disable();

        // disabled
        http.x509()
                .disable();

        // disabled
        http.logout()
                .disable();

        // disabled
        http.rememberMe()
                .disable();

        // disabled
        http.requestCache()
                .disable();

        // enabled
        final ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
        final SecurityExceptionHandler exceptionHandler = applicationContext.getBean(SecurityExceptionHandler.class);
        http.exceptionHandling()
                .authenticationEntryPoint(exceptionHandler)
                .accessDeniedHandler(exceptionHandler);

        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/security/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/actuator", "/actuator/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/test").permitAll()
                .anyRequest().hasRole("USER");

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web
                .debug(false)
                .ignoring()
                .requestMatchers(HttpMethod.GET, "/favicon.ico");
    }

}
