package com.mewp.edu.content;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author mewp
 */
@SpringBootApplication(scanBasePackages = {"com.mewp.edu.content", "com.mewp.edu.common"})
public class ContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
    }
}
