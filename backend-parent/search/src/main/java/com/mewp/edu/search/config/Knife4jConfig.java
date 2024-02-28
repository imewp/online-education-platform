package com.mewp.edu.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;

/**
 * @author mewp
 * @version 1.0
 * @date 2023/8/20 15:51
 */
@Configuration
@EnableSwagger2WebMvc
@Import(BeanValidatorPluginsConfiguration.class)
public class Knife4jConfig {
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mewp.edu.search.controller")) //配置扫描路径
                .paths(PathSelectors.any()) // 配置过滤哪些
                .build();
    }

    /**
     * api基本信息
     *
     * @return 信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfo("在线教育平台搜索管理系统",
                "搜索管理系统对课程相关信息进行搜索",
                "v1.0",
                "https://edu.mewp.com",
                new Contact("mewp", "https://edu.mewp.com", "mewp@vip.qq.com"),  //作者信息
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>());
    }
}
