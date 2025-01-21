package com.mewp.edu.common.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * 支付工具类
 *
 * @author mewp
 */
public class PaymentUtil {
    private static final Pattern PATTERN = Pattern.compile("SJPAY(,\\S+){4}");
    public static final String SHANJUPAY_PREFIX = "XC";

    /**
     * 校验支付订单号是否合法
     *
     * @param attach 正则表达式
     * @return true/false
     */
    public static boolean checkPayOrderAttach(String attach) {
        if (StringUtils.isBlank(attach)) {
            return false;
        }
        return PATTERN.matcher(attach).matches();
    }

    /**
     * 生成支付订单号
     *
     * @return 支付订单号
     */
    public static String genUniquePayOrderNo() {
        String dateTime = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS").format(LocalDateTime.now());
        return SHANJUPAY_PREFIX + dateTime + RandomStringUtils.randomAlphanumeric(15);
    }
}

