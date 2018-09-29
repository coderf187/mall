package com.jianf.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author: dongchen
 * @ClassName: SwaggerConfig
 * @Description: Swagger配置
 * @date 2016年5月7日 下午10:02:52
 */
@Conditional(SwaggerCondition.class)
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /**
     * @auth:dongchen
     * @Title: userApi
     * @Description: 构建swagger api
     * @param @return 设定文件
     * @return Docket 返回类型
     * @throws
     */
    @Bean
    public Docket userApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().paths(PathSelectors.regex("/(api|decisiondata|mock|risk|callback).*"))
                .build();
    }

    private static ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("闪借app接口测试").description("闪借app接口测试文档")
                .termsOfServiceUrl("http://wiki.puhuitech.cn").contact("闪借团队！").version("1.0").build();
    }
}
