package com.mewp.edu.content.test;

import com.mewp.edu.content.model.dto.CoursePreviewDTO;
import com.mewp.edu.content.service.CoursePublishService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/2/27 19:01
 */
@SpringBootTest
public class FreemarkerTest {

    @Autowired
    private CoursePublishService coursePublishService;

    /**
     * 测试页面静态化
     */
    @Test
    void testGenerateHtmlByTemplate() throws IOException, TemplateException {
        // 配置Freemarker
        Configuration configuration = new Configuration(Configuration.getVersion());
        String classPath = this.getClass().getResource("/").getPath();
        configuration.setDirectoryForTemplateLoading(new File(classPath + "/templates/"));
        configuration.setDefaultEncoding("utf-8");
        // 加载模版，并指定模版文件名
        Template template = configuration.getTemplate("course_template.ftl");
        CoursePreviewDTO coursePreviewInfo = coursePublishService.getCoursePreviewInfo(122L);
        Map<String, Object> map = new HashMap<>();
        map.put("model", coursePreviewInfo);
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        InputStream is = IOUtils.toInputStream(content);
        FileOutputStream fos = new FileOutputStream("/Users/mewp/Desktop/test/io/test.html");
        IOUtils.copy(is, fos);
    }

}
