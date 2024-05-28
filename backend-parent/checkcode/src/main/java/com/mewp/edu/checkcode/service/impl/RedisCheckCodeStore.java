package com.mewp.edu.checkcode.service.impl;

import com.mewp.edu.checkcode.service.CheckCodeService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 使用redis存储验证码
 *
 * @author mewp
 * @version 1.0
 * @date 2024/5/28 00:43
 */
@Component
@AllArgsConstructor
public class RedisCheckCodeStore implements CheckCodeService.CheckCodeStore {
    private StringRedisTemplate redisTemplate;

    @Override
    public void set(String key, String value, Integer expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
