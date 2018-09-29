package com.jianf.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Objects;

/**
 * 数字，货币格式化工具类
 * 
 * @author mshi
 */
public class DecimalUtil {

    private static Log logger = LogFactory.getLog(DecimalUtil.class);

    /** 初始金额格式 */
    private static final String MONEY_PATTERN = "#,##0.00";

    /** 初始数字格式 */
    private static final String FLOAT_PATTERN = "#0.#########################";

    /** 大写数字 */
    private static final String[] NUMBERS = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

    /** 整数部分的单位 */
    private static final String[] IUNIT = { "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰",
            "仟" };
    /** 小数部分的单位 */
    private static final String[] DUNIT = { "角", "分", "厘" };

    private DecimalUtil() {
    }

    /**
     * 格式化金额(千分位)
     * 
     * @param numStr
     * @return
     * @author mshi
     */
    public static double parseMoney(String numStr) {
        return parse(numStr, MONEY_PATTERN);
    }

    /**
     * 格式化数字
     * 
     * @param numStr
     * @return
     * @author mshi
     */
    public static double parseNumber(String numStr) {
        return parse(numStr, FLOAT_PATTERN);
    }

    /**
     * 格式化
     * 
     * @param numStr
     * @param pattern
     *            格式
     * @return
     * @author mshi
     */
    public static double parse(String numStr, String pattern) {
        double decimal = 0;
        try {
            DecimalFormat decFmt = new DecimalFormat(pattern);
            decimal = decFmt.parse(numStr).doubleValue();
        } catch (ParseException e) {
            logger.error("parse(String) parse error", e);
        }
        return decimal;
    }

    /**
     * 格式化金额(千分位)
     * 
     * @param decimal
     * @return
     * @author mshi
     */
    public static String formatMoney(double decimal) {
        return format(decimal, MONEY_PATTERN);
    }

    /**
     * 格式化数字
     * 
     * @param decimal
     * @return
     * @author mshi
     */
    public static String formatNumber(double decimal) {
        return format(decimal, FLOAT_PATTERN);
    }

    /**
     * 格式化金额
     * 
     * @param decimal
     * @param pattern
     * @return String
     * @author mshi
     */
    public static String format(double decimal, String pattern) {
        DecimalFormat decFmt = new DecimalFormat(pattern);
        return decFmt.format(decimal);
    }

    /**
     * 数字转中文
     * 
     * @author mshi
     * @param amount
     * @return String
     */
    public static String toChinese(String amount) {
        if(Objects.equals(amount,"0.00")){
            return "零";
        }
        String str = amount.replaceAll(",", "");// 去掉","
        String integerStr;// 整数部分数字
        String decimalStr;// 小数部分数字
        // 初始化：分离整数部分和小数部分
        if (StringUtils.indexOf(str, ".") > -1) {
            integerStr = str.substring(0, StringUtils.indexOf(str, "."));
            decimalStr = str.substring(StringUtils.indexOf(str, ".") + 1);
        } else if (StringUtils.indexOf(str, ".") == 0) {
            integerStr = "";
            decimalStr = str.substring(1);
        } else {
            integerStr = str;
            decimalStr = "";
        }
        // integerStr去掉首0，不必去掉decimalStr的尾0(超出部分舍去)
        if (StringUtils.isNotEmpty(integerStr)) {
            integerStr = Long.toString(Long.parseLong(integerStr));
            if ("0".equals(integerStr)) {
                integerStr = "";
            }
        }
        // overflow超出处理能力，直接返回
        if (integerStr.length() > IUNIT.length) {
            return str;
        }
        int[] integers = toArray(integerStr);// 整数部分数字
        boolean isMust5 = isMust5(integerStr);// 设置万单位
        int[] decimals = toArray(decimalStr);// 小数部分数字
        return getChineseInteger(integers, isMust5) + getChineseDecimal(decimals);
    }

    /**
     * 整数部分和小数部分转换为数组，从高位至低位
     */
    private static int[] toArray(String number) {
        int[] array = new int[number.length()];
        for (int i = 0; i < number.length(); i++) {
            array[i] = Integer.parseInt(number.substring(i, i + 1));
        }
        return array;
    }

    /**
     * 得到中文金额的整数部分。
     */
    private static String getChineseInteger(int[] integers, boolean isMust5) {
        StringBuilder chineseInteger = new StringBuilder("");
        for (int i = 0, length = integers.length; i < length; i++) {
            chineseInteger.append(integers[i] == 0 ? build(integers, i, isMust5) : (NUMBERS[integers[i]] + IUNIT[length
                    - i - 1]));
        }
        return chineseInteger.toString();
    }

    /**
     * @author mshi
     * @param integers
     * @param i
     * @param isMust5
     * @return
     */
    private static String build(int[] integers, int i, boolean isMust5) {
        int length = integers.length;
        String key = "";
        if (integers[i] == 0) {
            if (((length - i) == 13) || ((length - i) == 5 && isMust5))// 万(亿)(必填)
                key = IUNIT[4];
            else if ((length - i) == 9)// 亿(必填)
                key = IUNIT[8];
            else if ((length - i) == 1)// 元(必填)
                key = IUNIT[0];
            // 0遇非0时补零，不包含最后一位
            if ((length - i) > 1 && integers[i + 1] != 0)
                key += NUMBERS[0];
        }
        return key;
    }

    /**
     * 得到中文金额的小数部分。
     */
    private static String getChineseDecimal(int[] decimals) {
        StringBuilder chineseDecimal = new StringBuilder("");
        for (int i = 0; i < decimals.length; i++) {
            // 舍去3位小数之后的
            if (i == 3)
                break;
            chineseDecimal.append(decimals[i] == 0 ? "" : (NUMBERS[decimals[i]] + DUNIT[i]));
        }
        return chineseDecimal.toString();
    }

    /**
     * 判断第5位数字的单位"万"是否应加。
     */
    private static boolean isMust5(String integerStr) {
        int length = integerStr.length();
        if (length > 4) {
            String subInteger;
            if (length > 8) {
                // 取得从低位数，第5到第8位的字串
                subInteger = integerStr.substring(length - 8, length - 4);
            } else {
                subInteger = integerStr.substring(0, length - 4);
            }
            return Integer.parseInt(subInteger) > 0;
        } else {
            return false;
        }
    }

    public static String convertFormat(BigDecimal money) {
        String text = String.valueOf(money);
        DecimalFormat df = null;
        if (text.indexOf(".") > 0) {
            if (text.length() - text.indexOf(".") - 1 == 0) {
                df = new DecimalFormat("###,##0.");
            } else if (text.length() - text.indexOf(".") - 1 == 1) {
                df = new DecimalFormat("###,##0.00");
            } else {
                df = new DecimalFormat("###,##0.00");
            }
        } else {
            df = new DecimalFormat("###,##0.00");
        }
        double number = 0.00;
        try {
            number = Double.parseDouble(text);
        } catch (Exception e) {
            number = 0.00;
        }
        return df.format(number);
    }
}