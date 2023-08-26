package com.mewp.edu.content.handler;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义处理器
 * 将Mybatis 两列结果转换成map（key，value）
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/27 02:28
 */
@SuppressWarnings("all")
public class MapResultHandler<K, V> implements ResultHandler<Map<K, V>> {
    private final Map<K, V> mappedResults = new HashMap<>();

    @Override
    public void handleResult(ResultContext context) {
        Map map = (Map) context.getResultObject();
        mappedResults.put((K) map.get("key"), (V) map.get("value"));
    }

    public Map<K, V> getMappedResults() {
        return mappedResults;
    }
}
