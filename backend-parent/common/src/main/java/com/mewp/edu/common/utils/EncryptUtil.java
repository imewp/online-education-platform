package com.mewp.edu.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 加密工具类
 *
 * @author mewp
 */
public class EncryptUtil {
    private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

    /**
     * 将字节数组编码为Base64字符串
     *
     * @param bytes 字节数组
     * @return 编码后的Base64字符串
     */
    public static String encodeBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 将Base64字符串解码为字节数组
     *
     * @param str Base64字符串
     * @return 解码后的字节数组
     */
    public static byte[] decodeBase64(String str) {
        byte[] bytes;
        bytes = Base64.getDecoder().decode(str);
        return bytes;
    }

    /**
     * 将字符串编码为UTF-8格式的Base64字符串
     *
     * @param str 字符串
     * @return 编码后的UTF-8格式的Base64字符串
     */
    public static String encodeUtf8StringBase64(String str) {
        String encoded;
        encoded = Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
        return encoded;

    }

    /**
     * 将UTF-8格式的Base64字符串解码为字符串
     *
     * @param str UTF-8格式的Base64字符串
     * @return 解码后的字符串
     */
    public static String decodeUtf8StringBase64(String str) {
        String decoded;
        byte[] bytes = Base64.getDecoder().decode(str);
        decoded = new String(bytes, StandardCharsets.UTF_8);
        return decoded;
    }

    /**
     * 将字符串编码为URL格式的Base64字符串
     *
     * @param url 字符串
     * @return 编码后的URL格式的Base64字符串
     */
    public static String encodeUrl(String url) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.warn("URLEncode失败", e);
        }
        return encoded;
    }

    /**
     * 将URL格式的Base64字符串解码为字符串
     *
     * @param url URL格式的Base64字符串
     * @return 解码后的字符串
     */
    public static String decodeUrl(String url) {
        String decoded = null;
        try {
            decoded = URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.warn("URLDecode失败", e);
        }
        return decoded;
    }
}
