package com.jianf.commons.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * @Description: 流操作的工具类
 * @date 2015年11月15日 下午7:45:41
 */
public class IoUtil {

    private IoUtil() {
    }

    /**
     * @auth:fengjian @Title: saveFile @Description:
     *                将文件数据存到指定路径文件中 @param @param file @param @param
     *                sealPdfData 设定文件 @return void 返回类型 @throws
     */
    public static void saveFile(File file, byte[] data, String encoding) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        IOUtils.write(data, bufferedWriter, encoding);
        IOUtils.closeQuietly(bufferedWriter);
    }

    /**
     * @throws IOException
     *             @auth:fengjian @Title: saveFileNoEncoding @param @param
     *             file @param @param data 设定文件 @return void 返回类型 @throws
     */
    public static void saveFileNoEncoding(File file, byte[] data) throws IOException {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        IOUtils.write(data, bufferedOutputStream);
        IOUtils.closeQuietly(bufferedOutputStream);
    }


    /**
     * 获取指定路径的文件的数据
     * 
     * @author fengjian
     * @param fileName
     * @return
     * @throws IOException
     */
    public static byte[] getFileData(String fileName) throws IOException {
        File file;
        InputStream inputStream = null;
        byte[] data = null;
        if (StringUtils.isNotBlank(fileName)) {
            file = new File(fileName);
            inputStream = FileUtils.openInputStream(file);
            data = IOUtils.toByteArray(inputStream);
        }
        IOUtils.closeQuietly(inputStream);
        return data;
    }

    public static char[] getFileDataByChar(String fileName) throws IOException {
        File file;
        InputStream inputStream = null;
        char[] data = null;
        if (StringUtils.isNotBlank(fileName)) {
            file = new File(fileName);
            inputStream = FileUtils.openInputStream(file);
            data = IOUtils.toCharArray(inputStream);
        }
        IOUtils.closeQuietly(inputStream);
        return data;
    }

}
