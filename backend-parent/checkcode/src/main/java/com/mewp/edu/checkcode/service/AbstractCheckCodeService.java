package com.mewp.edu.checkcode.service;

import com.mewp.edu.checkcode.model.dto.CheckCodeParamsDTO;
import com.mewp.edu.checkcode.model.dto.CheckCodeResultDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * 抽象验证码类
 *
 * @author mewp
 * @version 1.0
 * @date 2024/5/28 00:17
 */
@Slf4j
public abstract class AbstractCheckCodeService implements CheckCodeService {
    protected CheckCodeGenerator checkCodeGenerator;
    protected KeyGenerator keyGenerator;
    protected CheckCodeStore checkCodeStore;

    public abstract void setCheckCodeGenerator(CheckCodeGenerator checkCodeGenerator);

    public abstract void setKeyGenerator(KeyGenerator keyGenerator);

    public abstract void setCheckCodeStore(CheckCodeStore checkCodeStore);

    public abstract CheckCodeResultDTO generate(CheckCodeParamsDTO checkCodeParams);

    /**
     * 生成验证码公用方法
     *
     * @param checkCodeParams 生成验证码参数
     * @param codeLength      验证码长度
     * @param keyPrefix       key的前缀
     * @param expire          过期时间
     * @return 生成结果
     */
    public GenerateResult generate(CheckCodeParamsDTO checkCodeParams, Integer codeLength, String keyPrefix, Integer expire) {
        // 生成验证码
        String code = checkCodeGenerator.generate(codeLength);
        log.info("生成的验证码为：{}", code);
        String key = keyGenerator.generate(keyPrefix);
        // 存储验证码
        checkCodeStore.set(key, code, expire);
        // 返回验证码生成结果
        return new GenerateResult(key, code);
    }

    @Override
    public boolean verify(String key, String code) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(code)) {
            return false;
        }
        String codeCache = checkCodeStore.get(key);
        if (codeCache == null) {
            return false;
        }
        boolean result = codeCache.equalsIgnoreCase(code);
        // 删除验证码
        if (result) {
            checkCodeStore.remove(key);
        }
        return result;
    }

    /**
     * 生成验证码结果
     */
    @Data
    @AllArgsConstructor
    protected class GenerateResult {
        String key;
        String code;
    }
}
