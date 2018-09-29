package com.jianf.commons.interceptor;

import com.jianf.commons.constant.Constant;
import com.jianf.commons.entity.LoanCustomer;
import com.jianf.commons.exception.BusinessException;
import com.jianf.commons.utils.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * H5 token权限校验
 */
public class H5Interceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(H5Interceptor.class);
    private static final String USER_TOKEN_KEY = "superloan_h5_user_token:";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        String remoteAddress = httpServletRequest.getRemoteAddr();
        String Xip = httpServletRequest.getHeader("x-real-ip");
        if (StringUtils.isNotEmpty(Xip) && !"unKnown".equalsIgnoreCase(Xip)) {
            remoteAddress = Xip;
        }

        String requestURI = httpServletRequest.getRequestURI();
        LOGGER.info("H5请求ip:{}, 访问:{}", remoteAddress, requestURI);

        HandlerMethod hm = (HandlerMethod) handler;
        Method method = hm.getMethod();
        if (method.getDeclaringClass().isAnnotationPresent(RestController.class)) {
            return isAccessAllowed(httpServletRequest);
        }
        return true;
    }

    private boolean isAccessAllowed(HttpServletRequest httpServletRequest) {
        String type = httpServletRequest.getHeader("type");
        String version = httpServletRequest.getHeader("version");
        String channel = httpServletRequest.getHeader("channel");
        String timestamp = httpServletRequest.getHeader("timestamp");
        String sign = httpServletRequest.getParameter("sign");
        //String token = httpServletRequest.getHeader("token");
        String h5_token = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("h5_token".equals(cookie.getName())) {
                    h5_token = cookie.getValue();
                    break;
                }
            }
        }

        LOGGER.info("---h5_token: {}", h5_token);
        String token = h5_token;
        //验证header参数不能为空
        if (StringUtils.isBlank(type) || StringUtils.isBlank(version)
                || StringUtils.isBlank(channel) || StringUtils.isBlank(timestamp)
                || StringUtils.isBlank(sign) || StringUtils.isBlank(token)) {
            LOGGER.error("type、version、channel、timestamp、sign、h5_token有一项或多项为空！");
            throw BusinessException.ofErrorUnauthorized();
        }

        if (!"H5".equals(type) || channel.length() > 20) {
            LOGGER.error("type或channel错误！");
            throw BusinessException.ofErrorUnauthorized();
        }

        LoanCustomer loanCustomer = verifyToken(token);
        if (loanCustomer == null) {
            LOGGER.error("H5请求被拒绝,token无效");
            throw BusinessException.ofErrorUnauthorized();
        }

        httpServletRequest.setAttribute("uid", loanCustomer.getId());
        httpServletRequest.setAttribute("channel", channel);
        return true;
    }

    /**
     * 校验token
     *
     * @param token
     * @return
     */
    public LoanCustomer verifyToken(String token) {
        Long uid = TokenUtils.getUID(token);
        Long timeMs = TokenUtils.getTimeMs(token);

        long minute = (System.currentTimeMillis() - timeMs) / (1000 * 60);
        if (minute > Constant.TOKEN_TIMEOUT_TM) {
            LOGGER.error("H5请求被拒绝,token已失效");
            return null;
        }

        String cacheToken = "";
//        String cacheToken = redisService.get(StringUtils.join(USER_TOKEN_KEY, uid));
        if (StringUtils.isEmpty(cacheToken) || !token.equals(cacheToken)) {
            LOGGER.error("H5请求被拒绝,token已覆盖");
            return null;
        }

//        return loanCustomerMapper.selectLoanCustomerById(uid);
        return null;
    }
}
