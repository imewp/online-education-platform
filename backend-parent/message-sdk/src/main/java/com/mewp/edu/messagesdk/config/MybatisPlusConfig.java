package com.mewp.edu.messagesdk.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis-Plus 配置
 *
 * @author mewp
 * @version 1.0
 * @date 2024/2/24 16:22
 */
@Configuration
@MapperScan("com.mewp.edu.messagesdk.mapper")
public class MybatisPlusConfig {
}
