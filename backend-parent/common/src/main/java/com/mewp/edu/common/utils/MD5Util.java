package com.mewp.edu.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author mewp
 */
@Slf4j
public class MD5Util {
    /**
     * 获取字符串的MD5摘要计算结果。
     *
     * @param plainText 要计算MD5摘要的字符串。
     * @return 计算得到的MD5摘要十六进制字符串，长度为32位。
     */
    public static String getMd5(String plainText) {
        if (StringUtils.isBlank(plainText)) {
            throw new IllegalArgumentException("Input string cannot be null");
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] bytes = md.digest();

            StringBuilder buf = new StringBuilder();
            for (byte value : bytes) {
                buf.append(String.format("%02x", value & 0xff));
            }
            //32位加密  
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
