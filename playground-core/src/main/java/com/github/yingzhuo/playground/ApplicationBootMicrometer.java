package com.github.yingzhuo.playground;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.turbo.bean.injection.ApplicationName;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationBootMicrometer {

    private static final String ACTUATOR_BASE_PATH = "/actuator";

    @ApplicationName
    private String applicationName;

    @Bean
    public TimedAspect timedAspect() {
        return new TimedAspect();
    }

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer() {
        return it -> it.config()
                .commonTags("application", this.applicationName)
                .meterFilter(MeterFilter.deny(id -> {
                    val uri = id.getTag("uri");
                    return uri != null && uri.startsWith(ACTUATOR_BASE_PATH);
                }))
                .meterFilter(MeterFilter.deny(id -> {
                    val uri = id.getTag("uri");
                    return uri != null && uri.contains("favicon");
                }));
    }

}
