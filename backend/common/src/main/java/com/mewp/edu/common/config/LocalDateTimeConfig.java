package com.mewp.edu.common.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期时间配置类
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/19 16:11
 */
@Configuration
public class LocalDateTimeConfig {

    private static final String SIMPLE_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 序列化内容（服务器返回给客户端内容）
     * LocalDateTime -> string
     *
     * @return 序列化配置
     */
    @Bean
    public LocalDateTimeSerializer localDateTimeSerializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(SIMPLE_DATETIME_FORMAT));
    }

    /**
     * 反序列化内容（客户端传入服务器数据）
     * string -> LocalDateTime
     *
     * @return 反序列化配置
     */
    @Bean
    public LocalDateTimeDeserializer localDateTimeDeserializer() {
        return new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(SIMPLE_DATETIME_FORMAT));
    }

    /**
     * 配置
     *
     * @return json配置
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.serializerByType(LocalDateTime.class, localDateTimeSerializer());
            builder.deserializerByType(LocalDateTime.class, localDateTimeDeserializer());
        };
    }
}
