package com.jianf.commons.interceptor;

import com.jianf.commons.exception.BusinessException;
import com.jianf.commons.utils.MD5Util;
import com.jianf.commons.utils.ZZUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 拦截timestamp是否在请求10分钟内
 * 拦截sign签名认证
 */
public class ValidateInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        String remoteAddress = httpServletRequest.getRemoteAddr();
        String Xip = httpServletRequest.getHeader("x-real-ip");
        if(StringUtils.isNotEmpty(Xip) && !"unKnown".equalsIgnoreCase(Xip)){
            remoteAddress =  Xip;
        }

        String requestURI = httpServletRequest.getRequestURI();
        LOGGER.info("ip:{}, 访问:{}", remoteAddress, requestURI);

        HandlerMethod hm = (HandlerMethod) handler;
        Method method = hm.getMethod();
        if (method.getDeclaringClass().isAnnotationPresent(RestController.class)) {
            return isAccessAllowed(httpServletRequest);
        }
        return true;
    }

    private boolean isAccessAllowed(HttpServletRequest httpServletRequest) {
        String timestamp = httpServletRequest.getHeader("timestamp");
        String deviceToken = httpServletRequest.getHeader("deviceToken");
        String token = httpServletRequest.getHeader("token");
        //String mobileType = httpServletRequest.getParameter("mobileType");
        String sign = httpServletRequest.getParameter("sign");
        //验证header参数不能为空
        if (StringUtils.isBlank(timestamp) || StringUtils.isBlank(sign) || StringUtils.isBlank(deviceToken)) {
            LOGGER.error("timestamp,sign,deviceToken有一项或者多项为空！");
            throw BusinessException.ofErrorUnauthorized();
        }

        if (!ZZUtil.valid(ZZUtil.NUMBER, timestamp)) {
            LOGGER.error("timestamp非时间类型。");
            throw BusinessException.ofErrorUnauthorized();
        }

        long timestamps = Long.parseLong(timestamp);
        long minute = (System.currentTimeMillis() - timestamps) / (1000 * 60);

        if (minute > 10) {
            LOGGER.error("请求被拒绝,原因请求时间大于10分钟");
            throw BusinessException.ofErrorUnauthorized();
        }
        //获取加密密钥
        String secretKey = StringUtils.isNotBlank(token) ? token : deviceToken;
        byte[] bytes= DigestUtils.md5(timestamp+secretKey);
        String hexSign = MD5Util.byteToHexStringNoUpper(bytes);
        if (!Objects.equals(sign,  hexSign)) {
            LOGGER.error("请求被拒绝,原因签名错误");
            throw BusinessException.ofErrorUnauthorized();
        }
        return true;
    }
}
