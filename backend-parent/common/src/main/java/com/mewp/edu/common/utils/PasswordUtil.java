package com.mewp.edu.common.utils;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.util.Objects;
import java.util.Random;


/**
 * <P>
 * MD5加盐加密
 * </p>
 *
 * @author mewp
 */

public class PasswordUtil {
    /**
     * 生成含有随机盐的密码
     *
     * @param password 加密前的密码
     * @return 加密后的密码
     */
    public static String generate(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();
        password = md5Hex(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            if (password != null) {
                cs[i] = password.charAt(i / 3 * 2);
                cs[i + 2] = password.charAt(i / 3 * 2 + 1);
            }
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
        }
        return new String(cs);
    }

    /**
     * 校验密码是否正确
     *
     * @param password 需要校验的密码
     * @param md5      前端传入的MD5值
     * @return 若密码与MD5值匹配则返回true，否则返回false
     */
    public static boolean verify(String password, String md5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }
        String salt = new String(cs2);
        return Objects.equals(md5Hex(password + salt), new String(cs1));
    }

    /**
     * 获取十六进制字符串形式的MD5摘要
     *
     * @param src 要进行MD5摘要的字符串
     * @return 十六进制形式的MD5摘要字符串，如果发生异常则返回null
     */
    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }
}