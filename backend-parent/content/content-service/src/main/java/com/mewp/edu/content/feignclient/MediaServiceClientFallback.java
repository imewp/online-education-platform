package com.mewp.edu.content.feignclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/2/28 09:35
 */
@Slf4j
@Component
public class MediaServiceClientFallback implements MediaServiceClient {
    @Override
    public String uploadFile(MultipartFile upload, String folder, String objectName) {
        log.debug("方式一：熔断处理，无法获取异常");
        return null;
    }
}
