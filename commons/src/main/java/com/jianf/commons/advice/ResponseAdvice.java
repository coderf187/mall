package com.jianf.commons.advice;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Created by dongchen on 2017/7/16.
 */
@ControllerAdvice(basePackages = "com.finup.loan.controller")
public class ResponseAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        HttpHeaders httpHeaders = request.getHeaders();
        String token = httpHeaders.getFirst("token");
        if(StringUtils.isNotBlank(token)){
            response.getHeaders().add("currentTime", String.valueOf(System.currentTimeMillis()));
        }
        return body;
    }
}
