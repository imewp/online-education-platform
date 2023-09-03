package com.mewp.edu.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author mewp
 * @version 1.0
 * @date 2023/9/3 11:05
 */
@SpringBootApplication(scanBasePackages = {"com.mewp.edu.media", "com.mewp.edu.common"})
public class MediaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MediaApplication.class, args);
    }
}
