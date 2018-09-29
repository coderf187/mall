package com.jianf.commons.aop.aspect;

import com.jianf.commons.aop.annotation.AfterAspectLog;
import com.jianf.commons.aop.annotation.BeforeAspectLog;
import com.jianf.commons.framework.MongodbService;
import net.sf.json.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

/**
 * 切面拦截返回结果和参数
 *
 * @auth fengjian
 * @create 2016/8/23
 **/
@Aspect
@Component
public class LogAspect {
    private final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    private static final String[] TYPES = {"int", "double", "long", "short", "byte", "boolean", "char", "float", "String", "java.lang.Integer", "java.lang.Double", "java.lang.Float", "java.lang.Long", "java.lang.Short", "java.lang.Byte", "java.lang.Boolean", "java.lang.Character", "java.lang.String"};

    @Autowired
    private MongodbService mongodbService;

    /**
     * 异步处理
     * 被拦截方法before进行 切面处理保存请求参数日志,便于排查问题
     */
    @Order(Integer.MAX_VALUE)
    @Before(value = "@annotation(com.jianf.commons.aop.annotation.BeforeAspectLog)")
    public void logBeforeMethod(JoinPoint jp) {
        CompletableFuture.runAsync(() -> {
            MethodSignature signature = (MethodSignature) jp.getSignature();
            // 获取被拦截的方法
            Method method = signature.getMethod();
            // 获取被拦截的参数数组
            Object[] object = jp.getArgs();
            JSONObject jsonObject = getJsonObject(object);
            this.getAnnotationBeforeSave(jsonObject, method);
        });
    }

    /**
     * 异步处理
     * 被拦截方法after后进行 切面处理保存返回结果日志,便于排查问题
     */
    @AfterReturning(value = "@annotation(com.jianf.commons.aop.annotation.AfterAspectLog)", returning = "rtv")
    public void logAfterMethod(JoinPoint jp, Object rtv) {
        CompletableFuture.runAsync(() -> {
            MethodSignature signature = (MethodSignature) jp.getSignature();
            // 获取被拦截的方法
            Method method = signature.getMethod();
            // 获取被拦截的参数数组
            Object[] object = jp.getArgs();
            this.getAnnotationAfterSave(rtv, method, object);
        });
    }

    /**
     * aop切面before
     */
    private void getAnnotationBeforeSave(JSONObject jsonObject, Method method) {
        boolean hasAnnotation = method.isAnnotationPresent(BeforeAspectLog.class);
        // 如果方法上面存在annotation则执行获取annotation说明
        if (hasAnnotation) {
            BeforeAspectLog aspectLog = method.getAnnotation(BeforeAspectLog.class);
            mongodbService.saveJson(aspectLog.collectionName(), jsonObject.toString());
        }

    }

    /**
     * aop切面after
     */
    private void getAnnotationAfterSave(Object rtv, Method method, Object[] object) {
        boolean hasAnnotation = method.isAnnotationPresent(AfterAspectLog.class);
        JSONObject jsonObject = getJsonObject(object);
        // 返回结果放入json
        jsonObject.put("returnResult", rtv);
        // 如果方法上面存在annotation则执行获取annotation说明
        if (hasAnnotation) {
            AfterAspectLog aspectLog = method.getAnnotation(AfterAspectLog.class);
            mongodbService.saveJson(aspectLog.collectionName(), jsonObject.toString());
        }

    }

    /**
     * 组装切入参数放入json中
     */
    private static JSONObject getJsonObject(Object[] object) {
        JSONObject jsonObject = new JSONObject();
        // 参数放入json,暂时不支持list,后果存入mongodb抛错
        for (int i = 0; i < object.length; i++) {
            if (isPrimitive(object[i])) {
                jsonObject.put("params" + i, object[i]);
            } else {
                JSONObject jsonObject1 = JSONObject.fromObject(object[i]);
                jsonObject.put("params" + i, jsonObject1);
            }
        }
        return jsonObject;
    }

    /**
     * 判定是否是基本类型或者是包装类型的参数 donChen
     */
    private static boolean isPrimitive(Object o) {
        boolean flag = false;
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            for (String type : TYPES) {
                if (field.getType().getName().equals(type)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

}
