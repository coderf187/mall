package com.jianf.commons.utils;

import java.math.BigDecimal;

/**
 * 数字计算工具类
 *
 * @author mshi
 */
public class ArithUtil {

    /**
     * 默认精度
     */
    private static final int DEFAULT_DIV_SCALE = 32;

    private static final String ZERO = "The scale must be a positive integer or zero";

    private ArithUtil() {
    }

    public static void main(String[] args) {
        BigDecimal amount = BigDecimal.valueOf(100.10d);
        System.out.println(checkDivide(amount, BigDecimal.valueOf(1000d)));
        System.out.println(checkDecimal(amount));
    }

    /**
     * amount/div 是否整除
     * amount==0 返回false
     */
    public static boolean checkDivide(BigDecimal amount, BigDecimal div){
        if(amount == null || div == null || compare(amount, BigDecimal.ZERO)==0){
            return false;
        }
        BigDecimal[] divArray = amount.divideAndRemainder(div);
        return divArray[1].doubleValue()==0;
    }

    //是否存在小数
    public static boolean checkDecimal(BigDecimal amount){
        double d = amount.doubleValue();
        d = d-(int)d;
        return d>0;
    }

    /**
     * @param @param  v
     * @param @return 设定文件
     * @return BigDecimal 返回类型
     * @throws
     * @auth:fengjian
     * @Title: add
     * @Description: 加法运算
     */
    public static BigDecimal add(BigDecimal... v) {
        BigDecimal b1 = BigDecimal.ZERO;
        for (BigDecimal n : v) {
            b1 = b1.add(n != null ? n : BigDecimal.ZERO);
        }
        return b1;
    }

    /**
     * 加法运算(浮点型)
     *
     * @param v
     * @return 两个参数的和
     * @author mshi
     */
    public static double add(double... v) {
        BigDecimal b1 = BigDecimal.ZERO;
        for (double n : v) {
            b1 = b1.add(new BigDecimal(Double.toString(n)));
        }
        return b1.doubleValue();
    }


    /**
     * 减法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @return 两个参数的差
     * @author mshi
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 乘法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @return 两个参数的积
     * @author mshi
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
        return v1.multiply(v2);
    }

    /**
     * 乘法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @param v3
     * @return 三个参数的积
     * @author mshi
     */
    public static double mul(double v1, double v2, double v3) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal b3 = new BigDecimal(Double.toString(v3));
        return b1.multiply(b2).multiply(b3).doubleValue();
    }

    /**
     * 乘法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @param v3
     * @param v4
     * @return 四个参数的积
     * @author mshi
     */
    public static double mul(double v1, double v2, double v3, double v4) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal b3 = new BigDecimal(Double.toString(v3));
        BigDecimal b4 = new BigDecimal(Double.toString(v4));
        return b1.multiply(b2).multiply(b3).multiply(b4).doubleValue();
    }

    /**
     * 除法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @return 两个参数的商
     * @author mshi
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEFAULT_DIV_SCALE);
    }

    /**
     * 除法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @param scale 精度
     * @return 两个参数的商
     * @author mshi
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(ZERO);
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(ZERO);
        }
        return v1.divide(v2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 除法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @param v3
     * @return 三个参数的商
     * @author mshi
     */
    public static double div(double v1, double v2, double v3) {
        return div(v1, v2, v3, DEFAULT_DIV_SCALE);
    }

    /**
     * 除法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @param v3
     * @param scale 精度
     * @return 三个参数的商
     * @author mshi
     */
    public static double div(double v1, double v2, double v3, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(ZERO);
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal b3 = new BigDecimal(Double.toString(v3));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).divide(b3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 除法运算（整型）
     *
     * @param v1
     * @param v2
     * @return 两个参数的商
     * @author mshi
     */
    public static double div(int v1, int v2) {
        return div(v1, v2, DEFAULT_DIV_SCALE);
    }

    /**
     * 除法运算（整型）
     *
     * @param v1
     * @param v2
     * @param scale 精度
     * @return 两个参数的商
     * @author mshi
     */
    public static double div(int v1, int v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(ZERO);
        }
        BigDecimal b1 = new BigDecimal(Integer.toString(v1));
        BigDecimal b2 = new BigDecimal(Integer.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 计算求和值(浮点型)
     *
     * @param d
     * @return 求和值
     * @author mshi
     */
    public static double sum(double[] d) {
        if (d == null || d.length == 0) {
            return 0D;
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < d.length; i++) {
            sum.add(new BigDecimal(Double.toString(d[i])));
        }
        return sum.doubleValue();
    }

    /**
     * 计算平均值(浮点型)
     *
     * @param d
     * @return 平均值
     * @author mshi
     */
    public static double average(double[] d) {
        if (d == null || d.length == 0) {
            return 0D;
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < d.length; i++) {
            sum.add(new BigDecimal(Double.toString(d[i])));
        }
        sum.divide(new BigDecimal(Integer.toString(d.length)), DEFAULT_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
        return sum.doubleValue();
    }

    /**
     * 计算最大数值(浮点型)
     *
     * @param d
     * @return 最大数值(浮点型)
     * @author mshi
     */
    public static double max(double[] d) {
        if (d == null || d.length == 0) {
            throw new NullPointerException("double array is null or length equal zero.");
        }
        double max = Double.MIN_VALUE;
        for (int i = 0; i < d.length; i++) {
            if (d[i] > max) {
                max = d[i];
            }
        }
        return max;
    }

    /**
     * 计算最小数值(浮点型)
     *
     * @param d
     * @return 最小数值(浮点型)
     * @author mshi
     */
    public static double min(double[] d) {
        if (d == null || d.length == 0) {
            throw new NullPointerException(ZERO);
        }
        double min = Double.MAX_VALUE;
        for (int i = 0; i < d.length; i++) {
            if (d[i] < min) {
                min = d[i];
            }
        }
        return min;
    }

    /**
     * 计算最大数值(整型)
     *
     * @param d
     * @return 最大数值(整型)
     * @author mshi
     */
    public static int max(int[] d) {
        if (d == null || d.length == 0) {
            throw new NullPointerException(ZERO);
        }
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < d.length; i++) {
            if (d[i] > max) {
                max = d[i];
            }
        }
        return max;
    }

    /**
     * 计算最小数值(整型)
     *
     * @param d
     * @return 最小数值(整型)
     * @author mshi
     */
    public static int min(int[] d) {
        if (d == null || d.length == 0) {
            throw new NullPointerException(ZERO);
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < d.length; i++) {
            if (d[i] < min) {
                min = d[i];
            }
        }
        return min;
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     * @author mshi
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 比较两个数值(浮点型)
     *
     * @param v1
     * @param v2
     * @return int
     * @author mshi
     */
    public static int compare(double v1, double v2) {
        if (v1 > v2) {
            return 1;
        } else if (v1 < v2) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 比较两个数值(整型)
     *
     * @param v1
     * @param v2
     * @return int
     * @author mshi
     */
    public static int compare(int v1, int v2) {
        if (v1 > v2) {
            return 1;
        } else if (v1 < v2) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 比较两个BigDecimal(为null当零处理)
     *
     * @param v1
     * @param v2
     * @return
     */
    public static int compare(BigDecimal v1, BigDecimal v2) {
        if (v1 == null) {
            v1 = BigDecimal.ZERO;
        }
        if (v2 == null) {
            v2 = BigDecimal.ZERO;
        }
        return v1.compareTo(v2);
    }

    /**
     * 两个BigDecimal减法(为null当零处理)
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal subtract(BigDecimal v1, BigDecimal v2) {
        if (v1 == null) {
            v1 = BigDecimal.ZERO;
        }
        if (v2 == null) {
            v2 = BigDecimal.ZERO;
        }
        return v1.subtract(v2);
    }

    /**
     * double 转换成 BigDecimal (为null 返回 0)
     *
     * @param v
     * @return
     */
    public static BigDecimal format(Double v) {
        return v == null ? BigDecimal.ZERO : BigDecimal.valueOf(v);
    }
    /**
     * BigDecimal 转换成 double (为null 返回 0)
     *
     * @param v
     * @return
     */
    public static Double format(BigDecimal v) {
        return v == null ? BigDecimal.ZERO.doubleValue() : v.doubleValue();
    }
}