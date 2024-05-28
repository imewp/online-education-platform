package com.mewp.edu.checkcode.service.impl;

import com.mewp.edu.checkcode.service.CheckCodeService;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * UUID生成器
 *
 * @author mewp
 * @version 1.0
 * @date 2024/5/28 00:34
 */
@Component
public class UUIDKeyGenerator implements CheckCodeService.KeyGenerator {
    @Override
    public String generate(String prefix) {
        return prefix + UUID.randomUUID().toString().replace("-", "");
    }
}
