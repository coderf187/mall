package com.jianf.commons.advice;

import com.jianf.commons.aop.annotation.Validate;
import com.jianf.commons.enums.ResponseCode;
import com.jianf.commons.exception.BusinessException;
import com.jianf.commons.utils.JsonUtils;
import com.jianf.commons.utils.ValidatorUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by dongchen on 2017/7/15.
 */
@ControllerAdvice(basePackages = "com.finup.loan.controller")
public class RequestAdvice implements RequestBodyAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestAdvice.class);


    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            String bodyStr = IOUtils.toString(new LoanInputMessage(inputMessage).getBody());
            return JsonUtils.fromJsonToObject(bodyStr, Class.forName(targetType.getTypeName()));
        } catch (Exception e) {
            LOGGER.error("body处理失败：", e);
            return body;
        }
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        try {
            return new LoanInputMessage(inputMessage);
        } catch (Exception e) {
            LOGGER.error("body处理失败：", e);
            return inputMessage;
        }
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = parameter.getMethod();
        Annotation[][] annotations = method.getParameterAnnotations();
        Validate validate = null;

        for (int i = 0; i < annotations.length; i++) {
            for (int j = 0; j < annotations[i].length; j++) {
                if (annotations[i][j] instanceof Validate) {
                    validate = (Validate) annotations[i][j];
                }
            }
        }

        if(validate == null){
            return body;
        }

        String errorMessage = ValidatorUtils.validateModel(body, validate.value());
        if (StringUtils.isNotBlank(errorMessage)) {
            LOGGER.error("请求参数错误：{}", errorMessage);
            throw new BusinessException(ResponseCode.PARAMETER_ERROR.getValue(), errorMessage);
        }
        return body;
    }


}
