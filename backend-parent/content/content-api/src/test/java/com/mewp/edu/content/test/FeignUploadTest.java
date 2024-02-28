package com.mewp.edu.content.test;

import com.mewp.edu.content.config.MultipartSupportConfig;
import com.mewp.edu.content.feignclient.MediaServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/2/27 19:50
 */
@SpringBootTest
public class FeignUploadTest {
    @Autowired
    MediaServiceClient mediaServiceClient;

    //远程调用，上传文件
    @Test
    public void test() {
        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(new File("/Users/mewp/Desktop/test/io/test.html"));
        mediaServiceClient.uploadFile(multipartFile, "course", "test.html");
    }

}
