package com.mewp.edu.auth.ucenter.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 验证码服务接口
 *
 * @author mewp
 * @version 1.0
 * @date 2024/5/28 10:39
 */
@FeignClient(value = "checkcode", fallbackFactory = CheckCodeClientFactory.class)
public interface CheckCodeClient {
    @PostMapping(value = "/checkcode/verify")
    Boolean verify(@RequestParam("key") String key,
                   @RequestParam("code") String code);
}
