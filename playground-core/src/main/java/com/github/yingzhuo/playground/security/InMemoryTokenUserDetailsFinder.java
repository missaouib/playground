package com.github.yingzhuo.playground.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import spring.turbo.module.security.authentication.UserDetailsFinder;

@Component
@AllArgsConstructor
public class InMemoryTokenUserDetailsFinder implements UserDetailsFinder {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsernameAndPassword(String username, String password) throws AuthenticationException {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            return null;
        }

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            return userDetails;
        } else {
            return null;
        }
    }

}
