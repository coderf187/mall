package com.jianf.commons.utils;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class EmojiFilterUtils {

    private static Log LOGGER = LogFactory.getLog(EmojiFilterUtils.class);

    /**
     * 检测是否有emoji字符 一旦含有就抛出
     */
    private static boolean containsWord(String source) {
        if (StringUtils.isBlank(source)) {
            return false;
        }

        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isNotEmojiCharacter(codePoint)) {
                return true;
            }
        }
        return false;
    }


    private static boolean isNotEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     */
    public static String filterEmoji(String source) {

        if (!containsWord(source)) {
            LOGGER.info("filterEmoji 不包含文字说明只有表情。");
            return "未知";//如果不包含，直接返回
        }
        //到这里铁定包含
        StringBuilder buf = null;

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isNotEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }
                buf.append(codePoint);
            }
        }

        if (buf == null) {
            return "未知";//如果没有可能到这步吧！
        } else {
            if (buf.length() == len) {//这里的意义在于尽可能少的toString，因为会重新生成字符串
                return source;
            } else {
                return buf.toString();
            }
        }
    }

}
