package com.jianf.commons.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author: dongchen
 * @ClassName: SwaggerCondition
 * @Description: swagger配置
 * @date 2016年8月16日 下午7:29:24
 */
public class SwaggerCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return !"config,production".equals(context.getEnvironment().getProperty("spring.profiles.active"));
    }

}