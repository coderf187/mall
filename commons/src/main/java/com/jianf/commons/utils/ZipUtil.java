package com.jianf.commons.utils;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解压zip,rar压缩文件
 *
 * @auth dongcen
 * @create 2016/8/24
 **/
public class ZipUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipUtil.class);

    private ZipUtil() {
    }

    /**
     * 解压rar文件 fengjian
     *
     * @param sourceRar
     *            原文件路径
     * @param destDir
     *            解压后路径
     */
    public static void unRar(String sourceRar, String destDir) {
        Archive a = null;
        FileOutputStream fos = null;
        try {
            a = new Archive(new File(sourceRar));
            FileHeader fh = a.nextFileHeader();

            while (fh != null) {
                // 如果解压fh是目录,直接生成目录即可,不用写入
                if (!fh.isDirectory()) {
                    // 处理中文乱码
                    String fileName = fh.getFileNameW().trim();
                    if (!existZH(fileName)) {
                        fileName = fh.getFileNameString().trim();
                    }
                    fileName = FileUtil.getName(fileName);

                    // 解压文件路径
                    String destFileName = destDir;// 创建压缩跟目录
                    String destDirName = destDir + "/" + fileName;// 创建具体文件地址
                    // 非windows系统
                    // 2创建压缩文件地址
                    File dir = new File(destFileName);
                    if (!dir.exists() || !dir.isDirectory()) {
                        dir.mkdirs();
                    }
                    // 3解压缩文件
                    fos = new FileOutputStream(new File(destDirName));
                    a.extractFile(fh, fos);
                    fos.close();
                }
                fh = a.nextFileHeader();
            }

        } catch (IOException | RarException e) {
            LOGGER.error("解压文件失败，失败原因{}" + e.getMessage());
            throw new IllegalStateException(e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {

                }
            }
            if (a != null) {
                try {
                    a.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 解压zip文件 fengjian
     *
     * @param zipFilePath
     *            源文件路径
     * @param unzipDirectory
     *            解压后文件目录
     * @throws IOException
     */
    public static void unZip(String zipFilePath, String unzipDirectory) {
        // 定义输入输出流对象
        BufferedInputStream input = null;
        try {
            // 创建文件对象
            File file = new File(zipFilePath);
            // 创建zip文件对象
            ZipFile zipFile = new ZipFile(file, "GBK");
            // 创建本zip文件解压目录
            File unzipFile = new File(unzipDirectory);
            if (!unzipFile.exists())
                unzipFile.mkdirs();
            // 得到zip文件条目枚举对象
            Enumeration zipEnum = zipFile.getEntries();

            // 循环读取条目
            while (zipEnum.hasMoreElements()) {
                // 得到当前条目
                ZipEntry entry = (ZipEntry) zipEnum.nextElement();
                entry.setUnixMode(644);// 解决linux乱码
                String entryName = entry.getName();
                String names[] = entryName.split("\\/");
                // 用/分隔条目名称
                int length = names.length;
                String path = unzipFile.getAbsolutePath();
                for (int v = 0; v < length; v++) {
                    if (v < length - 1) { // 最后一个目录之前的目录
                        path += "/" + names[v] + "/";
                    } else if (entryName.endsWith("/")) { // 为目录
                        entry.setUnixMode(755);
                    } else { // 为文件,则输出到文件
                        input = new BufferedInputStream(zipFile.getInputStream(entry));
                        String savePath = unzipFile.getAbsolutePath() + File.separator;
                        if (!FileUtil.isExists(savePath)) {
                            FileUtil.mkdirs(savePath);
                        }
                        String finalPath = savePath + FileUtil.getName(entryName);
                        File zipFile2 = new File(finalPath);
                        IoUtil.saveFileNoEncoding(zipFile2, IOUtils.toByteArray(input));
                        // 关闭流
                        input.close();
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("压缩文件异常");
        } finally {
            try {
                if(input !=null) {
                    input.close();
                }
            } catch (IOException e) {
                LOGGER.error("流关闭异常");
            }
        }
    }

    /**
     * 压缩文件文zip fengjian
     *
     * @param files
     *            被压缩文件的file数组
     * @param zipFilePath
     *            压缩后的文件路径
     * @param name
     *            压缩的文件名
     * @throws IOException
     */
    public static void compressFiles2Zip(File[] files, String zipFilePath, String name) throws IOException {

        if (files != null && files.length > 0 && FileUtil.isEndsWith(name, ".zip")) {
            File zipFile = new File(zipFilePath);
            if (!zipFile.exists()) {
                zipFile.mkdirs();
            }
            File newZipFile = new File(zipFilePath + name);
            LOGGER.info("创建的 zip 文件是：" + newZipFile.getName());
            ZipArchiveOutputStream zaps = new ZipArchiveOutputStream(newZipFile);
            // Use Zip64 extensions for all entries where they are
            // required
            zaps.setUseZip64(Zip64Mode.AsNeeded);

            // 将每个文件用ZipArchiveEntry封装
            // 再用ZipArchiveOutputStream写到压缩文件中
            for (File file : files) {
                ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, file.getName());
                zaps.putArchiveEntry(zipArchiveEntry);
                InputStream is = new FileInputStream(file);
                byte[] buffer = new byte[1024 * 5];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    // 把缓冲区的字节写入到ZipArchiveEntry
                    zaps.write(buffer, 0, len);
                }
                // Writes all necessary data for this entry.
                zaps.closeArchiveEntry();
                is.close();
            }
            LOGGER.info("创建的 zip 文件大小是：" + newZipFile.getTotalSpace());
            zaps.flush();
            zaps.finish();
            zaps.close();
        }

    }

    /**
     * 判断是否有中文 fengjian
     *
     * @param str
     * @return
     */
    private static boolean existZH(String str) {
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

}
