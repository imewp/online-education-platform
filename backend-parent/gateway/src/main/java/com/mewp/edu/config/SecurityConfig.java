package com.mewp.edu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * 安全配置类
 *
 * @author mewp
 * @version 1.0
 * @date 2024/4/24 09:03
 */
@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {
    /**
     * 安全拦截配置，用于配置Spring WebFlux的应用程序的安全规则。
     *
     * @param http 用于配置ServerHttpSecurity的bean。
     * @return 返回一个SecurityWebFilterChain，它定义了请求如何被安全地处理。
     */
    @Bean
    public SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                // 允许所有请求访问
                .pathMatchers("/**").permitAll()
                // 对所有其他请求要求认证
                .anyExchange().authenticated()
                // 禁用CSRF保护
                .and().csrf().disable().build();
    }
}
