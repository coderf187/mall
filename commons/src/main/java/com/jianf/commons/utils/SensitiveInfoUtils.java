package com.jianf.commons.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by fengjian on 2017/4/7. 脱敏工具类
 */
public final class SensitiveInfoUtils {

    private static final String REGULAR_MOBILE = "(?<=\\d{3})\\d(?=\\d{4})";
    private static final String REPLACEMENT = "*";
    private static final String REGULAR_ID_NO = "(?<=\\d{3})\\d(?=\\d{2})";
    private static final String REGULAR_BANK_CARD = "(?<=\\d{0})\\d(?=\\d{4})";

    private SensitiveInfoUtils() {
    }

    /**
     * 姓名脱敏工具类
     * fengjian
     * 规则:姓名：仅显示名，如*钱进。
     * 
     * @param name
     * @return
     */
    public static String sensitiveName(final String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        int length = name.length();
        // 获取除第一个字之外的所有字
        String lastName = StringUtils.right(name, length - 1);
        return StringUtils.leftPad(lastName, length, REPLACEMENT);
    }

    /**
     * 手机号脱敏工具类
     * fengjian
     * 规则:手机号：显示前3位和后4位，如138****8000。
     * @param mobile
     * @return
     */
    public static String sensitiveMobile(final String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return null;
        }
        return mobile.replaceAll(REGULAR_MOBILE, REPLACEMENT);
    }

    /**
     * 身份证脱敏工具类
     * fengjian
     * 规则:证件号（身份证、护照等）：仅显示前3位和后2位，如610*************20。
     * @param idNo
     * @return
     */
    public static String sensitiveIdNo(final String idNo) {
        if (StringUtils.isBlank(idNo)) {
            return null;
        }
        return idNo.replaceAll(REGULAR_ID_NO,REPLACEMENT);
    }

    /**
     * 银行卡脱敏工具类
     * fengjian
     * 规则:银行卡号：仅显示最后4位，如************1234。
     * @param bankCard
     * @return
     */
    public static String sensitiveBankCard(final String bankCard) {
        if (StringUtils.isBlank(bankCard)) {
            return null;
        }
        return bankCard.replaceAll(REGULAR_BANK_CARD,REPLACEMENT);
    }
}
