package com.mewp.edu.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Json工具类
 *
 * @author mewp
 */
@Slf4j
public class JsonUtil {
    /**
     * 对象转换为json
     *
     * @param object 对象
     * @return JSON字符串
     */
    public static String objectToJson(Object object) {
        return JSON.toJSONString(object, SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * 将List集合转换为JSON字符串
     *
     * @param list 待转换的List集合，可以包含任意类型的对象
     * @param <T>  泛型
     * @return 返回表示List集合的JSON字符串
     */
    public static <T> String listToJson(List<T> list) {
        return JSON.toJSONString(list, SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * 字符串Json格式转换为对象Map
     *
     * @param strJson {"username":"sxb"}
     * @return 根据json转换为Map对象
     */
    public static Map<String, Object> jsonToMap(String strJson) {
        Map<String, Object> jsoMap = new HashMap<>();
        try {
            jsoMap = JSONObject.parseObject(strJson, Map.class);
        } catch (JSONException e) {
            log.error("json转换Map出错：{}", e.getMessage(), e);
        }

        return jsoMap;
    }

    /**
     * 字符串Json格式转换为对象
     *
     * @param strJson {"username":"sxb"}
     * @param tClass  实体Class
     * @param <T>     泛型
     * @return 实体对象
     */
    public static <T> T jsonToObject(String strJson, Class<T> tClass) {
        try {
            return JSON.parseObject(strJson, tClass);
        } catch (JSONException e) {
            log.error("json转换Map出错：{}", e.getMessage(), e);
        }
        return null;
    }


    /**
     * 字符串Json 转换为对象List
     *
     * @param strJson [{"username":"sxb"}]
     * @param tClass  实体Class
     * @param <T>     泛型
     * @return 根据json转换List
     */
    public static <T> List<T> jsonToList(String strJson, Class<T> tClass) {
        try {
            return JSONObject.parseArray(strJson, tClass);
        } catch (JSONException e) {
            log.error("json转换List出错：{}", e.getMessage());
        }
        return null;
    }
}
