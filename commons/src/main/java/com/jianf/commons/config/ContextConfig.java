package com.jianf.commons.config;

import com.jianf.commons.interceptor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ContextConfig extends WebMvcConfigurerAdapter {

    private String[] excludeUrlArr = new String[]{"/home/**", "/api/**", "/healthCheck",
            "/callback/**", "/risk/callback/**", "/pad/**", "/noAuth/**", "/mock/**", "/dictionary/**"};
    private String[] h5UrlArr = new String[]{"/h5/**"};
    // 限制更新用户状态操作
    private String[] padUrlArr = new String[]{"/pad/yunxin/**"};

    // for get HttpServletRequest
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // excludePathPatterns 拦截所有，剔除配置的
        // addPathPatterns 剔除所有，拦截配置的
        //VerifyRepeatInterceptor 必须放到最后 -- 依赖uid
        registry.addInterceptor(getValidateInterceptor())
                .excludePathPatterns(excludeUrlArr)
                //.excludePathPatterns(getDevTestExcludeUrlArr())
                .excludePathPatterns(padUrlArr)
                .excludePathPatterns(h5UrlArr);
        registry.addInterceptor(getH5Interceptor())
                .addPathPatterns(h5UrlArr);
        registry.addInterceptor(getLoginInterceptor())
                .excludePathPatterns(padUrlArr)
                .excludePathPatterns(excludeUrlArr)
                //.excludePathPatterns(getDevTestExcludeUrlArr())
                .excludePathPatterns(h5UrlArr);
        registry.addInterceptor(getPadInterceptor())
                .addPathPatterns(padUrlArr);
        registry.addInterceptor(getVerifyRepeatInterceptor())
                .excludePathPatterns(excludeUrlArr);
                //.excludePathPatterns(getDevTestExcludeUrlArr());
    }

//    @Bean
//    @Profile("dev")
//    public String[] getDevTestExcludeUrlArr() {
////        return null;
//        return new String[] {"/huaxiaa/**"};
//    }

    @Bean
    public PadInterceptor getPadInterceptor() {
        return new PadInterceptor();
    }

    @Bean
    public H5Interceptor getH5Interceptor() {
        return new H5Interceptor();
    }

    @Bean
    public ValidateInterceptor getValidateInterceptor() {
        return new ValidateInterceptor();
    }

    @Bean
    public VerifyRepeatInterceptor getVerifyRepeatInterceptor() {
        return new VerifyRepeatInterceptor();
    }

    @Bean
    public LoginInterceptor getLoginInterceptor() {
        return new LoginInterceptor();
    }

}

