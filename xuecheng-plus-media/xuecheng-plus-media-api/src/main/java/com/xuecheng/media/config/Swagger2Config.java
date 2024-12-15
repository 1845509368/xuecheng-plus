package com.xuecheng.media.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 */

@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket docket() {
        // 创建一个 swagger 的 bean 实例
        return new Docket(DocumentationType.SWAGGER_2)
                // 配置基本信息
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build();

    }


    /**
     * 基本信息设置
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 标题
                .title("学成在线接口文档")
                // 描述
                .description("众里寻他千百度，慕然回首那人却在灯火阑珊处")
                // 服务条款链接
                .termsOfServiceUrl("https://www.baidu.com")
                // 许可证
                .license("...")
                // 许可证链接
                .licenseUrl("...")
                // 联系我
                .contact(new Contact(
                        "张星宇",
                        "https://www.baidu.com",
                        "1845509368@qq.com"))
                // 版本
                .version("1.0")
                .build();
    }

}

