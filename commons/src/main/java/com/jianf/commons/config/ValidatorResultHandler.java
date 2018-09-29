package com.jianf.commons.config;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author: fengjian
 * @ClassName: ValidatorResultHandler
 * @Description: 通用验证组件
 * @date 2016年5月7日 下午10:03:27
 */
@Component
public class ValidatorResultHandler {

    /**
     * @auth:fengjian
     * @Title: handle
     * @Description: 参数验证
     * @param @param result 设定文件
     * @return void 返回类型
     * @throws
     */
    public void handle(BindingResult result) {

        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            ObjectError oe = list.get(0);
            throw new IllegalArgumentException(oe.getDefaultMessage());
        }
    }

    public static String validateModel(Object obj) {
        StringBuilder buffer = new StringBuilder(64);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);

        Iterator<ConstraintViolation<Object>> iter = constraintViolations.iterator();
        while (iter.hasNext()) {
            String message = iter.next().getMessage();
            buffer.append(message).append("\r\n");
        }
        return buffer.toString();
    }

}