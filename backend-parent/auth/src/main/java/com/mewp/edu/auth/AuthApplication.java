package com.mewp.edu.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/2/28 16:57
 */
@EnableFeignClients(basePackages = {"com.mewp.edu.auth.ucenter.feignclient"})
@SpringBootApplication
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class);
    }
}
