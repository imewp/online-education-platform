package com.mewp.edu.common.utils;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author mewp
 * @version 1.0
 * @date 2025/1/21 10:14
 */
public class RSAUtilTest {
    public static final String depository_publickey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJKcP4TjCb9+OKf0uvHkDO6njI8b9KKlu3ZdCkom4SONf8KkZ1jVl6A7XWnJ33gBLnbTGVUm5I+XvFEG5bSWVbkCAwEAAQ==";
    public static final String depository_privateKey = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAkpw/hOMJv344p/S68eQM7qeMjxv0oqW7dl0KSibhI41/wqRnWNWXoDtdacnfeAEudtMZVSbkj5e8UQbltJZVuQIDAQABAkBrkkVw5X0DikNbyM9aKG/ss/cIEgT/SgcwI7gnDDvo7wntxxPuVZ7P+gkhFqb1ByCLdH/GlsXEZW88HCA9M2ZhAiEA65BsW0uGPhnVRS7hJhLZpuuugKVNyJBBO6jGATe0g/UCIQCfVEZ0bvYd5pA165XwXs7ZFGU99rG410EEh7JRxzx0NQIgdNL9ShGck/PP1y22r2Et3CCKPHa+qrcQAvxipnvv5HkCIBITUoblC8DqplOnrXP+nYLdIHs+IH1y1ip4Zo+GheI9AiBdsG0ql4Unbt1ctYm6XdmqE5rdFD+iDFQRS1FFmUVNUQ==";
    public static final String p2p_publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKZKjaBEvudPDolCyuVCBLmfVsSFBu3wfdldLxItRcjSYMzHNoIuYcvHhnMmMi1iXRLeYdbwvI3JQoBHDGN5ad0CAwEAAQ==";
    public static final String p2p_privateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEApkqNoES+508OiULK5UIEuZ9WxIUG7fB92V0vEi1FyNJgzMc2gi5hy8eGcyYyLWJdEt5h1vC8jclCgEcMY3lp3QIDAQABAkAUhQia6UDBXEEH8QUGazIYEbBsSZoETHPLGbOQQ6Pj1tb6CVC57kioBjwtNBnY2jBDWi5K815LnOBcJSSjJPwhAiEA2eO6VZMTkdjQAkpB5dhy/0C3i8zs0c0M1rPoTA/RpkUCIQDDYHJPqHLkQyd//7sEeYcm8cMBTvDKBXyiuGk8eLRauQIgQo6IlalGmg+Dgp+SP5Z9kjD/oCmp0XB0UoVEGS/f140CIQCsG9YXHgi31ACD3T9eHcBVKjvidyveix7UKSdrQdl+4QIgNCtRVLV+783e7PX5hRXD+knsWTQxDEMEsHi1KsAWtPk=";

    @Test
    public void testGetKeyPair() throws Exception {
        // 生成公私钥对
        KeyPair keyPair = RSAUtil.getKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        System.out.println("-----------------publicKey--------------------- ");
        System.out.println(Base64Util.encode(publicKey.getEncoded()));

        System.out.println("-----------------privateKey--------------------- ");
        System.out.println(Base64Util.encode(privateKey.getEncoded()));
    }

    @Test
    public void testSign() {
        String content = "加密原文1234567890加密原文1234567890加密原文1234567890加密原文1234567890加密原文1234567890加密原文1234" +
                "567890加密原文1234567890加密原文1234567890加密原文1234567890加密原文1234567890加密原文1234567890加密原文12345678" +
                "90加密原文1234567890加密原文1234567890加密原文1234567890加密原文1234567890加密原文1234567890加密原文1234567890" +
                "加密原文1234567890加密原文1234567890加密原文1234567890加密原文1234567890加密原文1234567890加密原文1234567890加" +
                "密原文1234567890加密原文1234567890加密原文1234567890加密原文1234567890加密原文1234567890加密原文1234567890";

        System.out.println("-----------------p2p向存管发送数据--------------------- ");
        String signature = RSAUtil.sign(content, p2p_privateKey, "utf-8");
        System.out.println("生成签名,原文为：" + content);
        if (RSAUtil.verify(content, signature, p2p_publicKey, "utf-8")) {
            System.out.println("验证签名成功：" + signature);
        } else {
            System.out.println("验证签名失败！");
        }

        System.out.println("-----------------存管向p2p返回数据--------------------- ");
        String signature1 = RSAUtil.sign(content, depository_privateKey, "utf-8");
        System.out.println("生成签名,原文为：" + content);
        if (RSAUtil.verify(content, signature1, depository_publickey, "utf-8")) {
            System.out.println("验证签名成功：" + signature1);
        } else {
            System.out.println("验证签名失败！");
        }
    }
}
