package com.mewp.edu.checkcode.service;

import com.mewp.edu.checkcode.model.dto.CheckCodeParamsDTO;
import com.mewp.edu.checkcode.model.dto.CheckCodeResultDTO;

/**
 * 验证码接口
 *
 * @author mewp
 * @version 1.0
 * @date 2024/5/27 23:17
 */
public interface CheckCodeService {
    /**
     * 生成验证码
     *
     * @param checkCodeParams 生成验证码参数
     * @return 验证码结果
     */
    CheckCodeResultDTO generate(CheckCodeParamsDTO checkCodeParams);

    /**
     * 校验验证码
     *
     * @param key  key
     * @param code code
     * @return true 校验成功，false 校验失败
     */
    boolean verify(String key, String code);

    /**
     * 验证码生成器
     */
    interface CheckCodeGenerator {
        /**
         * 验证码生成
         *
         * @param length 长度
         * @return 验证码
         */
        String generate(int length);
    }

    /**
     * key生成器
     */
    interface KeyGenerator {
        /**
         * key生成
         *
         * @param prefix 前缀
         * @return 验证码
         */
        String generate(String prefix);
    }

    /**
     * 验证码存储
     */
    interface CheckCodeStore {
        /**
         * 向缓存设置key
         *
         * @param key    key
         * @param value  value
         * @param expire 过期时间
         */
        void set(String key, String value, Integer expire);

        /**
         * 从缓存获取key
         *
         * @param key key
         * @return value
         */
        String get(String key);

        /**
         * 删除key
         *
         * @param key key
         */
        void remove(String key);
    }
}
