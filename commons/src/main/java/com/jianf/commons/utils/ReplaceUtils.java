package com.jianf.commons.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 占位符替换
 */
public class ReplaceUtils {
    private final static Pattern FIELD_REG = Pattern.compile("\\$\\{(.+?)\\}");

    private ReplaceUtils() {
    }

    /**
     * 统一占位符替换utils,针对各种模板替换
     */
    public static String replaceParams(Map<String, String> params, String content) {
        Matcher matcher = FIELD_REG.matcher(content);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            String fieldName = matcher.group(1);
            String replaceName = params.get(fieldName);
            if (replaceName != null && replaceName.contains("$")) {
                // 过滤掉$符号
                replaceName = replaceName.replaceAll("\\$", " ");
            }
            if (StringUtils.isBlank(replaceName)) {
                matcher.appendReplacement(stringBuffer, StringUtils.EMPTY);
                continue;
            }
            matcher.appendReplacement(stringBuffer, replaceName);
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    /**
     * 获取随机数
     */
    public static String getRandom(int digit) {
        return RandomStringUtils.randomNumeric(digit);
    }

}