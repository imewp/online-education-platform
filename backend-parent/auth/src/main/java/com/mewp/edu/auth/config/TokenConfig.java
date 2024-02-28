package com.mewp.edu.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Collections;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/4/24 09:06
 */
@Configuration
public class TokenConfig {
    private static final String SIGNING_KEY = "eXzOBf2GyVYU52992rD8";

    @Autowired
    private TokenStore tokenStore;

//    @Bean
//    public TokenStore tokenStore() {
//        // 使用内存存储令牌（普通令牌）
//        return new InMemoryTokenStore();
//    }

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * JWT token转换器
     *
     * @return JWT token转换器
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGNING_KEY);
        return converter;
    }

    /**
     * 令牌管理服务
     *
     * @return token服务
     */
    @Bean(name = "authorizationServerTokenServicesCustom")
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        // 令牌存储策略
        defaultTokenServices.setTokenStore(tokenStore);
        // 支持刷新令牌
        defaultTokenServices.setSupportRefreshToken(true);
        // 令牌默认有效期2小时
        defaultTokenServices.setAccessTokenValiditySeconds(2 * 60 * 60);
        // 刷新令牌默认有效期3天
        defaultTokenServices.setRefreshTokenValiditySeconds(3 * 24 * 60 * 60);

        // JWT令牌配置
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Collections.singletonList(accessTokenConverter));
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);

        return defaultTokenServices;
    }
}
