package com.jianf.commons.advice;

import com.jianf.commons.dto.base.ResponseVo;
import com.jianf.commons.enums.ResponseCode;
import com.jianf.commons.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

/**
 * 统一错误处理入口
 */
@ControllerAdvice(basePackages = "com.finup.loan.controller")
public class ErrorControllerAdvice {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseVo<String> handleControllerException(Exception ex, HandlerMethod handlerMethod) {
        LOG.error("接口错误统一处理 controller出错，类名：{} 方法名：{}", handlerMethod.getMethod().getDeclaringClass().getName(),
                handlerMethod.getMethod().getName());
        LOG.error("接口错误统一处理：", ex);
        String errorCode = "";
        String errorMessage = "";
        if (ex instanceof BusinessException) {
            errorCode = ((BusinessException) ex).getErrorCode();
            errorMessage = ex.getMessage();
        } else if (ex instanceof IllegalArgumentException) {
            errorCode = ResponseCode.PARAMETER_ERROR.getValue();
            errorMessage = ex.getMessage();
        } else {
            errorCode = ResponseCode.INTERNAL_SERVER_ERROR.getValue();
            errorMessage = ResponseCode.INTERNAL_SERVER_ERROR.getMessage();
        }
        return ResponseVo.ofError(errorCode, errorMessage);
    }
}
