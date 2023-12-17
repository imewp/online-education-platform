package com.mewp.edu.common.utils;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * <P>
 * 身份证号工具类
 * </p>
 *
 * @author mewp
 */
public class IDCardUtil {

    /**
     * 通过身份证号码获取出生日期、性别、年龄
     *
     * @param idNumber 省份证号
     * @return 返回的出生日期格式：1990-01-01   性别格式：F-女，M-男
     */
    public static Map<String, String> getInfo(String idNumber) {
        String birthday = "";
        String age = "";
        String gender = "";

        int year = LocalDate.now().getYear();
        if (idNumber.length() == 15) {
            birthday = "19" + idNumber.substring(6, 8) + "-" + idNumber.substring(8, 10) + "-" + idNumber
                    .substring(10, 12);
            gender =
                    Integer.parseInt(idNumber.substring(idNumber.length() - 3)) % 2 == 0 ? "F" : "M";
            age = (year - Integer.parseInt("19" + idNumber.substring(6, 8))) + "";
        } else if (idNumber.length() == 18) {
            birthday = idNumber.substring(6, 10) + "-" + idNumber.substring(10, 12) + "-" + idNumber.substring(12, 14);
            gender = Integer.parseInt(idNumber.substring(idNumber.length() - 4, idNumber.length() - 1)) % 2 == 0 ?
                    "女" :
                    "男";
            age = (year - Integer.parseInt(idNumber.substring(6, 10))) + "";
        }
        Map<String, String> map = new HashMap<>();
        map.put("birthday", birthday);
        map.put("age", age);
        map.put("gender", gender);
        return map;
    }

    public static void main(String[] args) {
        System.out.println(getInfo("658182198109222913"));
    }
}
