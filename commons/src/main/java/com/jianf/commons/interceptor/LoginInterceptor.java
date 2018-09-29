package com.jianf.commons.interceptor;

import com.jianf.commons.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 验证是否登录---调用凡普信接口
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        HandlerMethod hm = (HandlerMethod) handler;
        Method method = hm.getMethod();
        if (method.getDeclaringClass().isAnnotationPresent(RestController.class)) {
            return isAccessAllowed(httpServletRequest);
        }
        return true;
    }

    private boolean isAccessAllowed(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        String version = httpServletRequest.getHeader("version");
        String type = httpServletRequest.getHeader("type");
        String deviceToken = httpServletRequest.getHeader("deviceToken");

        //验证header参数不能为空
        if (StringUtils.isBlank(token) || StringUtils.isBlank(version)
                || StringUtils.isBlank(type) || StringUtils.isBlank(deviceToken)) {
            LOGGER.error("token,version,type,deviceToken有一项或者多项为空！");
            throw BusinessException.ofErrorUnauthorized();
        }
        //调用凡普信接口验证token，获得uid
        String uid = null;
        httpServletRequest.setAttribute("uid", uid);
        return true;
    }





}