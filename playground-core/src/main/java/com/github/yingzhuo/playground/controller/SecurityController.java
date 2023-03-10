package com.github.yingzhuo.playground.controller;

import com.github.yingzhuo.playground.controller.valueobject.LoginVO;
import com.github.yingzhuo.playground.include.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.turbo.exception.ValidationException;
import spring.turbo.module.security.jwt.JwtTokenFactory;
import spring.turbo.module.security.jwt.JwtTokenMetadata;
import spring.turbo.module.security.token.Token;
import spring.turbo.module.security.user.CurrentToken;
import spring.turbo.webmvc.api.Json;

import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("/security")
@AllArgsConstructor
public class SecurityController {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenFactory tokenFactory;

    @PostMapping("/login")
    public Json login(@Validated @RequestBody LoginVO vo, BindingResult bindingResult) {
        log.debug("尝试登录");
        ValidationException.raiseIfNecessary(bindingResult);

        UserDetails user;

        try {
            user = userDetailsManager.loadUserByUsername(vo.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new BusinessException("用户名错误");
        }

        if (passwordEncoder.matches(vo.getPassword(), user.getPassword())) {
            final String token = tokenFactory.create(
                    JwtTokenMetadata.builder()
                            .putPayloadClaim("id", 1L)
                            .putPayloadClaim("username", vo.getUsername())
                            .putPayloadClaim("roles", getRole(user))
                            .expiresAtFuture(Duration.ofDays(30))
                            .build()
            );

            return Json.newInstance()
                    .payload("token", token);
        } else {
            throw new BusinessException("密码错误");
        }
    }

    private String[] getRole(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::toString)
                .map(r -> r.substring("ROLE_".length()))
                .toArray(String[]::new);
    }

    @GetMapping("/test-token")
    public Json testToken(@CurrentToken Token token) {
        return Json.newInstance()
                .payload("token", token);
    }

}
