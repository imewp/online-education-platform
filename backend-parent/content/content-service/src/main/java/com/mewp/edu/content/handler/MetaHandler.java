package com.mewp.edu.content.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 存储数据时自动填充数据
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/27 12:28
 */
@Slf4j
@Component
public class MetaHandler implements MetaObjectHandler {
    /**
     * 新增数据执行
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createDate", LocalDateTime.now(), metaObject);
        this.setFieldValByName("changeDate", LocalDateTime.now(), metaObject);
    }

    /**
     * 修改数据执行
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("changeDate", LocalDateTime.now(), metaObject);
    }
}
