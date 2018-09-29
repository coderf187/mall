package com.jianf.commons.interceptor;

import com.jianf.commons.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * pad token权限校验
 */
public class PadInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PadInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        String remoteAddress = httpServletRequest.getRemoteAddr();
        String requestURI = httpServletRequest.getRequestURI();
        LOGGER.info("pad请求ip:{}, 访问:{}", remoteAddress, requestURI);

        HandlerMethod hm = (HandlerMethod) handler;
        Method method = hm.getMethod();
        if (method.getDeclaringClass().isAnnotationPresent(RestController.class)) {
            return isAccessAllowed(httpServletRequest);
        }
        return true;
    }

    private boolean isAccessAllowed(HttpServletRequest httpServletRequest) {
        // RaiseAuditorVo的loginToken属性不能为空
        String loginToken = httpServletRequest.getHeader("loginToken");
        if(null == loginToken || !loginToken.contains("_")) {
            LOGGER.error("pad loginToke 为空，或者不符合要求！");
            throw BusinessException.ofPadTokenError();
        }
        String auditorCode = loginToken.split("_")[0];
        // 信审人员token存在redis里面的key
        String redisLoginToken = "";
        LOGGER.info("从redis查询的token：{}", redisLoginToken);
        LOGGER.info("request获取的token：{}", loginToken);
        if(null == redisLoginToken || !loginToken.equalsIgnoreCase(redisLoginToken)) {
            LOGGER.error("pad loginToke 为空！");
            throw BusinessException.ofPadTokenError();
        }

        return true;
    }
}
