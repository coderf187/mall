package com.jianf.commons.interceptor;

import com.jianf.commons.exception.BusinessException;
import com.jianf.commons.framework.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * 重复提交验证
 */
@Component
public class VerifyRepeatInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyRepeatInterceptor.class);

    private static final String GET = "get";
    private static final String QUERY = "query";
    private static final String SELECT = "select";
    private static final String FIND = "find";
    private static final String RECORD = "record";

    private static List<String> DO_NOT_INTERCEPT_METHOD_PREFIX = newArrayList(GET, QUERY, SELECT, FIND, RECORD);

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        HandlerMethod hm = (HandlerMethod) handler;
        Method method = hm.getMethod();
        return !method.getDeclaringClass().isAnnotationPresent(RestController.class) || isAccessAllowed(httpServletRequest, hm);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerMethod hm = (HandlerMethod) handler;
        Method method = hm.getMethod();
        if (method.getDeclaringClass().isAnnotationPresent(RestController.class)) {
            String methodName = hm.getMethod().getName().toLowerCase();
            String className = hm.getBeanType().getName();
            LOGGER.info("请求方法：[{}], [{}] 处理完", className, methodName);

            if (!shouldNotBeIntercepted(methodName)) {
                String token = String.valueOf(request.getAttribute("uid"));
                String redisKey = StringUtils.join("superloan:class:", className, "-method:", methodName, "-", "token:", token);
                String repeatResult = redisService.get(redisKey);
                if (StringUtils.isNotBlank(repeatResult)) {
                    redisService.del(redisKey);
                    LOGGER.info("删除重复请求方法：[{}], [{}] 的锁", className, methodName);
                }
            }
        }
    }

    /**
     * 重复提交验证,验证时间设置为3秒,3秒内同一个人访问同一个接口第二次以后都会被拒绝
     */
    private boolean isAccessAllowed(HttpServletRequest httpServletRequest, HandlerMethod hm) {
        String methodName = hm.getMethod().getName().toLowerCase();
        String className = hm.getBeanType().getName();
        LOGGER.info("重复验证请求方法：{}, {}", className, methodName);
        //不是查询操作的时候需要验证重复提交
        if (shouldNotBeIntercepted(methodName)) {
            return true;
        } else {
            //String token = httpServletRequest.getHeader("token");
            String token = String.valueOf(httpServletRequest.getAttribute("uid"));
            String redisKey = StringUtils.join("superloan:class:", className, "-method:", methodName, "-", "token:", token);
            String repeatResult = redisService.get(redisKey);
            if (StringUtils.isNotBlank(repeatResult)) {
                LOGGER.info("token:{},方法:{},3秒内被重复调用,已被拒绝", token, methodName);
                throw BusinessException.ofErrorRepeat();
            }
            redisService.expire(redisKey, redisKey, 3);
        }
        return true;
    }

    private boolean shouldNotBeIntercepted(String methodName) {
        return DO_NOT_INTERCEPT_METHOD_PREFIX.stream()
                .anyMatch(prefix -> methodName.startsWith(prefix));
    }
}
