package com.github.yingzhuo.playground;

import com.github.yingzhuo.playground.config.SM2KeyPairProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import spring.turbo.module.security.encoder.EncodingIds;
import spring.turbo.module.security.encoder.PasswordEncoderFactories;
import spring.turbo.module.security.exception.SecurityExceptionHandler;
import spring.turbo.module.security.jwt.AlgorithmFactory;
import spring.turbo.module.security.jwt.SM2AlgorithmFactory;
import spring.turbo.module.security.token.BasicTokenResolver;
import spring.turbo.module.security.token.TokenResolver;
import spring.turbo.util.propertysource.YamlPropertySourceFactory;

import java.util.function.Supplier;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
@EnableConfigurationProperties(SM2KeyPairProperties.class)
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthorizationManager<RequestAuthorizationContext> authorizationManager) throws Exception {
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

        // 鉴权
        http.authorizeHttpRequests(
                (authorize) -> {
                    authorize.withObjectPostProcessor(new ObjectPostProcessor<AuthorizationFilter>() {
                        @Override
                        public <O extends AuthorizationFilter> O postProcess(O filter) {
                            return filter;
                        }
                    });
                    authorize.anyRequest().access(authorizationManager);
                }
        );

        return http.build();
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> requestAuthorizationContextAuthorizationManager(
            HandlerMappingIntrospector introspector,
            RoleHierarchy roleHierarchy) {
        final var builder = new MvcRequestMatcher.Builder(introspector);

        // -------------------------------------------------------------------------------------------------------------
        // 允许全部
        final var permitAll = new OrRequestMatcher(
                builder.pattern(HttpMethod.GET, "/favicon.ico"),
                builder.pattern(HttpMethod.GET, "/static/*"),
                builder.pattern(HttpMethod.POST, "/security/login")
        );
        final var permitAllManager = new AuthorizationManager<RequestAuthorizationContext>() {
            @Override
            public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
                return new AuthorizationDecision(true);
            }
        };

        // -------------------------------------------------------------------------------------------------------------
        // actuator
        final var actuator = new OrRequestMatcher(
                builder.pattern(HttpMethod.GET, "/actuator"),
                builder.pattern(HttpMethod.GET, "/actuator/*")
        );
        final var actuatorManager = AuthorityAuthorizationManager.<RequestAuthorizationContext>hasRole("ACTUATOR");
        actuatorManager.setRoleHierarchy(roleHierarchy);

        // -------------------------------------------------------------------------------------------------------------
        // 需要用户角色
        final var any = AnyRequestMatcher.INSTANCE;
        final var anyManager = AuthorityAuthorizationManager.<RequestAuthorizationContext>hasRole("USER");
        anyManager.setRoleHierarchy(roleHierarchy);

        final AuthorizationManager<HttpServletRequest> manager = RequestMatcherDelegatingAuthorizationManager.builder()
                .add(permitAll, permitAllManager)
                .add(actuator, actuatorManager)
                .add(any, anyManager)
                .build();

        return (auth, object) -> manager.check(auth, object.getRequest());
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        final var bean = new RoleHierarchyImpl();
        bean.setHierarchy("""
                ROLE_ROOT > ROLE_ACTUATOR > ROLE_USER
                """);
        return bean;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web
                .debug(false)
                .ignoring().requestMatchers(HttpMethod.GET, "/favicon.ico");
    }

    @Bean
    public UserDetailsManager userDetailsManager(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("root")
                        .password(encoder.encode("root"))
                        .roles("ROOT").build(),
                User.builder()
                        .username("actuator")
                        .password(encoder.encode("actuator"))
                        .roles("ACTUATOR").build(),
                User.builder()
                        .username("user")
                        .password(encoder.encode("user"))
                        .roles("USER").build()
        );
    }

}
