package com.mewp.edu.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <P>
 * 通用工具类
 * </p>
 *
 * @author mewp
 */
@Slf4j
public class CommonUtil {
    /**
     * 可以用来匹配时间格式。
     * 可以出现1次到3次，每次可以是"xx"、"xx:xx"或"xx:xx:xx"的形式，其中每个部分都是1到2位数字，之间可以用冒号或中文的"："分隔
     */
    public static final Pattern TIMEPATTERN = Pattern.compile("(\\d{1,2}[：:]?){1,3}");

    /**
     * 隐藏手机号中间4位
     *
     * @param mobile 手机号
     * @return 隐藏中间4位后的手机号
     */
    public static String hiddenMobile(String mobile) {
        return StringUtils.isBlank(mobile) ? "" : mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 校验用户手机号是否合法
     *
     * @param phone 手机号
     * @return 校验结果
     */
    public static Boolean isMatches(String phone) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 将字符串转换为BigDecimal对象。
     * 如果字符串为空或者不是数字，则返回null。
     * 如果isFen为真，则将BigDecimal表示的值除以100，保留两位小数。
     *
     * @param s     待转换的字符串
     * @param isFen 是否表示分（true：表示分；false：表示其他单位）
     * @return 转换后的BigDecimal对象，或者null
     */
    public static BigDecimal strToBigDecimal(String s, Boolean isFen) {
        if (StringUtils.isBlank(s) || !NumberUtils.isNumber(s)) {
            return null;
        }
        BigDecimal bd = new BigDecimal(s);
        if (isFen != null && isFen) {
            bd = bd.divide(new BigDecimal(100), RoundingMode.HALF_UP);
        }
        return bd;
    }

    public static Long timeStrToSeconds(String timeStr) {
        if (StringUtils.isBlank(timeStr)) {
            return 0L;
        }
        long totalSeconds = 0L;
        timeStr = timeStr.replaceAll(" ", "");
        boolean matched = TIMEPATTERN.matcher(timeStr).matches();
        if (matched) {
            String[] sfmArr = timeStr.split("[:：]");
            int length = sfmArr.length;
            for (int i = 0; i < length; i++) {
                try {
                    totalSeconds += Long.parseLong(sfmArr[i]) * (long) Math.pow(60, length - 1 - i);
                } catch (NumberFormatException e) {
                    log.warn(e.getMessage());
                    // 处理异常，返回0
                    return 0L;
                }
            }
        }
        return totalSeconds;
    }

    /**
     * 将下划线映射到驼峰命名
     *
     * @param str 待转换的字符串
     * @return 转换后的驼峰命名的字符串
     */
    public static String mapUnderscoreToCamelCase(String str) {
        // 先将字符串转为全小写
        str = str.toLowerCase();
        // 创建一个StringBuilder对象，用于操作字符串
        StringBuilder sb = new StringBuilder(str);
        int index;
        // 使用while循环，直到字符串中不包含下划线，或者下划线后面还有字符
        while ((index = sb.indexOf("_")) != -1 && index + 1 < sb.length()) {
            // 删除下划线
            sb.deleteCharAt(index);
            // 将下划线后一个字符转为大写，并放到当前位置
            sb.setCharAt(index, Character.toUpperCase(sb.charAt(index)));
        }
        // 返回转换后的字符串
        return sb.toString();
    }

    /**
     * 将驼峰命名转换为下划线命名
     *
     * @param str 驼峰命名字符串
     * @return 下划线命名字符串
     */
    public static String mapCamelCaseToUnderscore(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                sb.append("_");
                sb.append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
