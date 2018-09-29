package com.jianf.commons.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class IDCardValidateUtil {
    private static final Logger logger = LoggerFactory.getLogger(IDCardValidateUtil.class);

    private static final Map<String, String> areaMap = new HashMap<>(35);
    static {
        areaMap.put("11", "北京");
        areaMap.put("12", "天津");
        areaMap.put("13", "河北");
        areaMap.put("14", "山西");
        areaMap.put("15", "内蒙古");
        areaMap.put("21", "辽宁");
        areaMap.put("22", "吉林");
        areaMap.put("23", "黑龙江");
        areaMap.put("31", "上海");
        areaMap.put("32", "江苏");
        areaMap.put("33", "浙江");
        areaMap.put("34", "安徽");
        areaMap.put("35", "福建");
        areaMap.put("36", "江西");
        areaMap.put("37", "山东");
        areaMap.put("41", "河南");
        areaMap.put("42", "湖北");
        areaMap.put("43", "湖南");
        areaMap.put("44", "广东");
        areaMap.put("45", "广西");
        areaMap.put("46", "海南");
        areaMap.put("50", "重庆");
        areaMap.put("51", "四川");
        areaMap.put("52", "贵州");
        areaMap.put("53", "云南");
        areaMap.put("54", "西藏");
        areaMap.put("61", "陕西");
        areaMap.put("62", "甘肃");
        areaMap.put("63", "青海");
        areaMap.put("64", "宁夏");
        areaMap.put("65", "新疆");
        areaMap.put("71", "台湾");
        areaMap.put("81", "香港");
        areaMap.put("82", "澳门");
        areaMap.put("91", "国外");
    }

    private IDCardValidateUtil(){}


    /**
     * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
     * 八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码(前六位数）
     * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 3、出生日期码（第七位至十四位）
     * 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。 4、顺序码（第十五位至十七位）
     * 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数）
     * （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
     * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 
     * （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
     */

    /**
     * 功能：身份证的有效验证
     *
     * @param idStr
     *            身份证号
     * @return 有效：返回"" 无效：返回String信息
     */
    @SuppressWarnings("unchecked")

    public static String idCardValidate(String idStr) {
        String errorInfo;// 记录错误信息
        String[] valCodeArr = {"1", "0", "X", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (idStr.length() != 15 && idStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (idStr.length() == 18) {
            ai = idStr.substring(0, 17);
        } else if (idStr.length() == 15) {
            ai = idStr.substring(0, 6) + "19" + idStr.substring(6, 15);
        }
        if (!isNumeric(ai)) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = ai.substring(6, 10);// 年份
        String strMonth = ai.substring(10, 12);// 月份
        String strDay = ai.substring(12, 14);// 月份
        if (!isDate(strYear + "-" + strMonth + "-" + strDay)) {
            errorInfo = "身份证生日无效。";
            return errorInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                return errorInfo;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return errorInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return errorInfo;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        if (areaMap.get(ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return errorInfo;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int totalMulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            totalMulAiWi = totalMulAiWi
                    + Integer.parseInt(String.valueOf(ai.charAt(i)))
                    * Integer.parseInt(wi[i]);
        }
        int modValue = totalMulAiWi % 11;
        String strVerifyCode = valCodeArr[modValue];
        ai = ai + strVerifyCode;

        if (idStr.length() == 18) {
            if (!ai.equals(idStr)) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return errorInfo;
            }
        } else {
            return null;
        }
        // =====================(end)=====================
        return null;
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 功能：判断字符串是否为日期格式
     *
     * @param strDate
     * @return
     */
    private static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        return m.matches();
    }

    public static void main(String[] args) {
        String idNo = "13082319930516577X";

        System.out.println(IDCardValidateUtil.idCardValidate(idNo));
    }
}