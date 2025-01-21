package com.mewp.edu.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA加密工具类
 *
 * @author mewp
 */
@Slf4j
public class RSAUtil {

    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    static final Base64.Decoder DECODER = Base64.getDecoder();
    static final Base64.Encoder ENCODER = java.util.Base64.getEncoder();

    /**
     * RSA签名
     *
     * @param content      待签名数据
     * @param privateKey   商户私钥
     * @param inputCharset 编码格式
     * @return 签名值
     */
    public static String sign(String content, String privateKey, String inputCharset) {
        try {
            PKCS8EncodedKeySpec priPkCs8 = new PKCS8EncodedKeySpec(DECODER.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPkCs8);

            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(inputCharset));
            byte[] signed = signature.sign();

            return ENCODER.encodeToString(signed);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * RSA验签名检查
     *
     * @param content      待签名数据
     * @param sign         签名值
     * @param publicKey    公钥
     * @param inputCharset 编码格式
     * @return 布尔值
     */
    public static boolean verify(String content, String sign, String publicKey, String inputCharset) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = DECODER.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(inputCharset));
            return signature.verify(DECODER.decode(sign));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 解密
     *
     * @param content      密文
     * @param privateKey   商户私钥
     * @param inputCharset 编码格式
     * @return 解密后的字符串
     * @throws Exception 异常
     */
    public static String decrypt(String content, String privateKey, String inputCharset) throws Exception {
        PrivateKey prikey = getPrivateKey(privateKey);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, prikey);

        InputStream ins = new ByteArrayInputStream(DECODER.decode(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        // rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
        byte[] buf = new byte[128];
        int bufl;
        while ((bufl = ins.read(buf)) != -1) {
            byte[] block;
            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                System.arraycopy(buf, 0, block, 0, bufl);
            }
            writer.write(cipher.doFinal(block));
        }
        return writer.toString(inputCharset);
    }


    /**
     * 得到私钥
     *
     * @param key 密钥字符串（经过base64编码）
     * @return PrivateKey
     * @throws Exception 异常信息
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = DECODER.decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }


    /**
     * 生成密钥对
     *
     * @return KeyPair
     * @throws Exception 异常
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        // 可以理解为：加密后的密文长度，实际原文要小些 越大 加密解密越慢
        keyGen.initialize(512);
        return keyGen.generateKeyPair();
    }
}
