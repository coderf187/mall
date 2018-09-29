package com.jianf.commons.utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: fengjian
 * @ClassName: ValidatorResultHandler
 * @Description: 通用验证组件
 * @date 2016年5月7日 下午10:03:27
 */
public class ValidatorUtils {


    public static String validateModel(Object obj, Class<?>... aClass) {
        StringBuilder buffer = new StringBuilder(64);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = null;
        if (aClass != null) {
            constraintViolations = validator.validate(obj,aClass);
        } else {
            constraintViolations = validator.validate(obj);
        }
        Iterator<ConstraintViolation<Object>> iter = constraintViolations.iterator();

        if(iter.hasNext()){
            return iter.next().getMessage();
        }

//        while (iter.hasNext()) {
//            String message = iter.next().getMessage();
//            buffer.append(message).append("\r\n");
//        }

        return buffer.toString();
    }

}