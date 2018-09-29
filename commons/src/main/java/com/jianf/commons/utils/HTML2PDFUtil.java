package com.jianf.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidParameterException;

/**
 * @ClassName: HTML2PDFUtil
 * @Description: html转pdf工具类
 * @author fengjian
 * @date 2016年4月1日 上午11:35:55
 */
public class HTML2PDFUtil {

    private static Logger logger = LoggerFactory.getLogger(HTML2PDFUtil.class);

    private HTML2PDFUtil() {

    }

    /**
     * @Title: generatePDF @Description: 生成pdf文件 @param fileName @param
     * content @author zhujili @date 2016年4月3日 上午1:29:13 @throws
     */
    public static void generatePDF(String fileName, String content) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                makeDirectory(file.getParentFile());
            }
            String contents = content.replace("utf-8", "GB2312").replace("Microsoft Yahei", "");
            if (StringUtils.isNotBlank(contents)) {
                StringReader reader = new StringReader(contents);
                generatePDF(file, reader);
            }
        } catch (Exception e) {
            logger.error("生成pdf错误，文件名：{}，原因：{}", fileName, e);
        }
    }

    /**
     * @Title: makeDirectory @Description: 递归创建目录结构 @param dir @author
     * zhujili @date 2016年4月3日 上午1:30:13 @throws
     */
    private static void makeDirectory(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDirectory(dir.getParentFile());
        }
        dir.mkdir();
    }

    /**
     * @throws IOException @throws InvalidParameterException @Title:
     * generatePDF @Description: 生成pdf文件 @param file @param reader @throws
     * InvalidParameterException @throws IOException @author zhujili @date
     * 2016年4月3日 上午1:35:33 @throws
     */
    private static void generatePDF(File file, StringReader reader)
            throws InvalidParameterException, IOException {
        FileOutputStream fos = new FileOutputStream(file);
        PD4ML pd4ml = new PD4ML();
        pd4ml.setPageInsets(new Insets(20, 10, 10, 10));
        pd4ml.setHtmlWidth(1300);
//        if (!flag) {
//            pd4ml.setPageSize(pd4ml.changePageOrientation(PD4Constants.A4));
//        }

        pd4ml.setPageSize(PD4Constants.A4);
        pd4ml.useTTF("java:fonts", true);
        pd4ml.setDefaultTTFs("KaiTi_GB2312", "KaiTi_GB2312", "KaiTi_GB2312");
        pd4ml.enableDebugInfo();
        pd4ml.render(reader, fos);
    }

}
