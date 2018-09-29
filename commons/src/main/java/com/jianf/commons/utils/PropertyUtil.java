package com.jianf.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Auther: wangpk
 * @Date: 2018/7/4 11:01
 * @Description: 读取properties文件
 */
public class PropertyUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtil.class);

    private static Properties props;

    static {
        loadProps();
    }

    synchronized static private void loadProps() {
        LOGGER.info("开始加载properties文件内容.......");
        props = new Properties();
        InputStream in = null;
        try {
            // 通过类加载器进行获取properties文件流
            in = PropertyUtil.class.getClassLoader().getResourceAsStream("application.properties");
            props.load(in);
        } catch (FileNotFoundException e) {
            LOGGER.error("jdbc.properties文件未找到");
        } catch (IOException e) {
            LOGGER.error("出现IOException");
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                LOGGER.error("jdbc.properties文件流关闭出现异常");
            }
        }
        LOGGER.info("加载properties文件内容完成...........");
        LOGGER.info("properties文件内容：" + props);
    }

    public static String getProperty(String key) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }
}
