package com.mewp.edu.auth.ucenter.feignclient;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/5/28 13:11
 */
@Slf4j
@Component
public class CheckCodeClientFactory implements FallbackFactory<CheckCodeClient> {
    @Override
    public CheckCodeClient create(Throwable throwable) {
        return new CheckCodeClient() {
            @Override
            public Boolean verify(String key, String code) {
                log.error("调用验证码服务熔断异常：{}", throwable.getMessage());
                return null;
            }
        };
    }
}
