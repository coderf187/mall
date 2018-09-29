package com.jianf.commons.utils;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


/**
 * fengjian 通过身份证提取信息
 */
public class IDCardUtil {
    private IDCardUtil() {
    }

    /**
     * 身份证获取性别 fengjian
     * 
     * @param idCard
     *            身份证号码
     * @return
     */
    public static String getGender(String idCard) {
        int length = idCard.length();
        int temp = length == 18 ? Integer.parseInt(idCard.substring(length - 2, length - 1), 10)
                : Integer.parseInt(idCard.substring(length - 1), 10);

        return temp % 2 == 0 ? "FEMALE" : "MALE";
    }

    public static String getZHGender(String idCard) {
        int length = idCard.length();
        int temp = length == 18 ? Integer.parseInt(idCard.substring(length - 2, length - 1), 10)
                : Integer.parseInt(idCard.substring(length - 1), 10);

        return temp % 2 == 0 ? "女" : "男";
    }

    /**
     * 身份证获取生日
     * 
     * @param idCard
     * @return
     */
    public static Date getBirthday(String idCard) {
        int length = idCard.length();
        String birthday = length == 18 ? idCard.substring(6, 10) + idCard.substring(10, 12) + idCard.substring(12, 14)
                : "19" + idCard.substring(6, 8) + idCard.substring(8, 10) + idCard.substring(10, 12);
        return   DateUtils.stringFormatDate2(birthday,"yyyyMMdd");
    }
    /**
     * 根据随机号生成爱钱进用户名。
     *
     * @author xiaobowen
     * @return
     */
    public static String createIQianJinName() {
        // 前缀
        String prefix = "ph";

        // 后缀
        String suffix = "80";

        // 日期，精确到“天”。
        String date = new DateTime().toString("MMddyyyyhhmmssSS");

        // 四位随机号。
        int randomNo = 0;
        do {
            randomNo = new Random().nextInt(10000);
        } while (randomNo < 1000);

        return prefix + date + randomNo + suffix;
    }

    /**
     * 根据进件号生成爱钱进用户名。
     *
     * @author xiaobowen
     * @param lendRequestId
     *            进件号
     * @return
     */
    public static String createIQianJinName(Long lendRequestId) {
        if (lendRequestId == null) {
            // 进件号为空，根据随机号生成用户名。
            return createIQianJinName();
        } else {
            // 前缀
            String prefix = "ph";

            // 后缀
            String suffix = "80";

            // 日期，精确到“天”。
            String date = new SimpleDateFormat("yyyyMMdd").format(new Date());

            return prefix + date + lendRequestId.toString() + suffix;
        }
    }
}
