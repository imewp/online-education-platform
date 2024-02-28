package com.mewp.edu.content;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author mewp
 */
@EnableFeignClients(basePackages = {"com.mewp.edu.content.feignclient"})
@SpringBootApplication(scanBasePackages = {"com.mewp.edu.content", "com.mewp.edu.common", "com.mewp.edu.messagesdk"})
public class ContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
    }
}
