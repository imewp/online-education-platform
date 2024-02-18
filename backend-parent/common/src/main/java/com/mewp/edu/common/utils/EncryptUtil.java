package com.mewp.edu.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
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

    public static byte[] decodeBase64(String str) {
        byte[] bytes;
        bytes = Base64.getDecoder().decode(str);
        return bytes;
    }

    public static String encodeUtf8StringBase64(String str) {
        String encoded;
        encoded = Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
        return encoded;

    }

    public static String decodeUtf8StringBase64(String str) {
        String decoded;
        byte[] bytes = Base64.getDecoder().decode(str);
        decoded = new String(bytes, StandardCharsets.UTF_8);
        return decoded;
    }

    public static String encodeUrl(String url) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.warn("URLEncode失败", e);
        }
        return encoded;
    }


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
