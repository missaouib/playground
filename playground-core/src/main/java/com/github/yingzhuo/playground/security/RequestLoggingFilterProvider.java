package com.github.yingzhuo.playground.security;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import spring.turbo.module.security.filter.HumanReadableRequestLoggingFilter;
import spring.turbo.module.security.filter.RequestLoggingFilterFactory;

import static spring.turbo.module.security.util.RequestMatcherFactories.*;

@Component
@Profile("!prod")
public class RequestLoggingFilterProvider implements RequestLoggingFilterFactory {

    @Override
    public Filter create() {
        var filter = new HumanReadableRequestLoggingFilter();
        filter.setSkipRequestMatcher(
                or(
                        header(HttpHeaders.USER_AGENT, "^.*kube.*$"),
                        antPath("/actuator/**")
                )
        );
        return filter;
    }

}
