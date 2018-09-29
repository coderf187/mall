package com.jianf.commons.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.comparator.SizeFileComparator;
import org.apache.commons.io.filefilter.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.regex.Pattern;


/**
 * @author: fengjian
 * @ClassName: FileUtil
 * @Description: 文件操作工具类
 * @date 2015年11月19日 下午11:53:39
 */
public class FileUtil {
    private static Pattern FILE_PATTERN = Pattern
            .compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");

    private static String MESSAGE = "";

    private FileUtil() {
    }

    /**
     * @auth:fengjian @Title: getFile @Description: 获取文件 @param @param
     *                path @param @return 设定文件 @return File 返回类型 @throws
     */
    public static File getFile(String path) {
        return FileUtils.getFile(path);
    }

    /**
     * @auth:fengjian @Title: getFullPath @Description: 获取全路径 @param @param
     *                path @param @return 设定文件 @return String 返回类型 @throws
     */
    public static String getFullPath(String path) {
        return FilenameUtils.getFullPath(path);
    }

    /**
     * @auth:fengjian @Title: getName @Description: 获取文件名 @param @param
     *                path @param @return 设定文件 @return String 返回类型 @throws
     */
    public static String getName(String path) {
        return FilenameUtils.getName(path);
    }

    /**
     * @auth:fengjian @Title: getExtension @Description: 获取文件后缀 @param @param
     *                path @param @return 设定文件 @return String 返回类型 @throws
     */
    public static String getExtension(String path) {
        return FilenameUtils.getExtension(path);
    }

    /**
     * @auth:fengjian @Title: getBaseName @Description: 获取文件名不带路径
     *                (例如aa.txt,返回结果为aa) @param @param path @param @return
     *                设定文件 @return String 返回类型 @throws
     */
    public static String getBaseName(String path) {
        return FilenameUtils.getBaseName(path);
    }

    /**
     * @auth:fengjian @Title: mkdir @Description: 创建文件夹 @param @param
     *                path @param @return 设定文件 @return boolean 返回类型 @throws
     */
    public static boolean mkdirs(String path) {
       return getFile(path).mkdirs();
    }

    /**
     * @auth:fengjian @Title: mkdir @Description: 创建文件 @param @param path @param
     */
    public static void mkdirsFile(String path) throws IOException {
        getFile(path).createNewFile();
    }

    /**
     * @throws IOException
     * @auth:fengjian @Title: cleanDirectory @Description:
     *                清空目录，但不删除目录 @param @param path 设定文件 @return void
     *                返回类型 @throws
     */
    public static void cleanDirectory(String path) throws IOException {
        FileUtils.cleanDirectory(getFile(path));
    }

    /**
     * @auth:fengjian @Title: deleteDirectory @Description: 删除目录 @param @param
     *                path @param @throws IOException 设定文件 @return void
     *                返回类型 @throws
     */
    public static void deleteDirectory(String path) throws IOException {
        FileUtils.deleteDirectory(getFile(path));
    }

    /**
     * @auth:fengjian @Title: isFile @Description: 判断是否为文件 @param @param
     *                path @param @return 设定文件 @return boolean 返回类型 @throws
     */
    public static boolean isFile(String path) {
        return getFile(path).isFile();
    }

    /**
     * @auth:fengjian @Title: isDirectory @Description: 判断是否为文件夹 @param @param
     *                path @param @return 设定文件 @return boolean 返回类型 @throws
     */
    public static boolean isDirectory(String path) {
        return getFile(path).isDirectory();
    }

    /**
     * @auth:fengjian @Title: exists @Description:判断文件是否存在 @param @param
     *                path @param @return 设定文件 @return boolean 返回类型 @throws
     */
    public static boolean isExists(String path) {
        return getFile(path).exists();
    }

    /**
     * @auth:fengjian @Title: copyFile @Description:
     *                复制文件或者目录,复制前后文件完全一样 @param @param
     *                resFilePath @param @param distFolder @param @throws
     *                IOException 设定文件 @return void 返回类型 @throws
     */
    public static void copyFile(String resFilePath, String distFolder) throws IOException {
        if (!FileUtil.isExists(distFolder))
            FileUtil.mkdirs(distFolder);
        File resFile = getFile(resFilePath);
        File distFile = getFile(distFolder);
        if (isDirectory(resFilePath)) {
            FileUtils.copyDirectoryToDirectory(resFile, distFile);
        } else if (isFile(resFilePath)) {
            FileUtils.copyFileToDirectory(resFile, distFile, true);
        }
    }

    /**
     * url复制到指定路径 fengjian
     * 
     * @param urlPath
     * @param distPath
     * @throws IOException
     */
    public static void copyURLToFile(String urlPath, String distPath) throws IOException {
        URL url = new URL(urlPath);
        File file = new File(distPath);
        FileUtils.copyURLToFile(url, file);
    }

    /**
     * @auth:fengjian @Title: copyDirectory @Description:
     *                将一个目录内容拷贝到另一个目录 @param @param resFilePath @param @param
     *                distFolder @param @throws IOException 设定文件 @return void
     *                返回类型 @throws
     */
    public static void copyDirectory(String resFilePath, String distFolder) throws IOException {
        File resFile = getFile(resFilePath);
        File distFile = getFile(distFolder);
        if (isDirectory(resFilePath)) {
            FileUtils.copyDirectory(resFile, distFile);
        } else if (isFile(resFilePath)) {
            FileUtils.copyFileToDirectory(resFile, distFile, true);
        }
    }

    /**
     * @auth:fengjian @Title: deleteFile @Description: 删除一个文件或者目录 @param @param
     *                targetPath @param @throws IOException 设定文件 @return void
     *                返回类型 @throws
     */
    public static void deleteFile(String targetPath) throws IOException {
        File targetFile = getFile(targetPath);
        if (isDirectory(targetPath)) {
            FileUtils.deleteDirectory(targetFile);
        } else if (isFile(targetPath)) {
            FileUtils.deleteQuietly(targetFile);
        }
    }

    /**
     * @auth:fengjian @Title: moveFile @Description:
     *                移动文件或者目录,移动前后文件完全一样,如果目标文件夹不存在则创建 @param @param
     *                resFilePath @param @param distFolder @param @throws
     *                IOException 设定文件 @return void 返回类型 @throws
     */
    public static void moveFile(String resFilePath, String distFolder) throws IOException {
        File resFile = getFile(resFilePath);
        File distFile = getFile(distFolder);
        if (isDirectory(resFilePath)) {
            FileUtils.moveDirectoryToDirectory(resFile, distFile, true);
        } else if (isFile(resFilePath)) {
            FileUtils.moveFileToDirectory(resFile, distFile, true);
        }
    }

    /**
     * @auth:fengjian @Title: renameFile @Description: 重命名文件或文件夹 @param @param
     *                resFilePath @param @param newFileName @param @return
     *                设定文件 @return boolean 返回类型 @throws
     */
    public static boolean renameFile(String resFilePath, String newFileName) {
        File resFile = getFile(resFilePath);
        File newFile = getFile(newFileName);
        return resFile.renameTo(newFile);
    }

    /**
     * @auth:fengjian @Title: listSuffixFileFilterFilesName @Description:
     *                某个目录下的文件列表 @param @param folder 文件目录 @param @param suffix
     *                文件的后缀名(.xml,.pdf) @param @return 设定文件 @return String[]
     *                返回类型 @throws
     */
    public static String[] listSuffixFileFilterFilesName(String path, String suffix) {
        IOFileFilter fileFilter1 = new SuffixFileFilter(suffix);
        IOFileFilter fileFilter2 = new NotFileFilter(DirectoryFileFilter.INSTANCE);
        FilenameFilter filenameFilter = new AndFileFilter(fileFilter1, fileFilter2);
        return getFile(path).list(filenameFilter);
    }

    /**
     * @auth:fengjian @Title: listsuffixFileFilterFiles @Description:
     *                列出指定目录下指定后缀的所有文件 @param @param path @param @param
     *                suffix @param @return 设定文件 @return Collection
     *                <File> 返回类型 @throws
     */
    public static Collection<File> listsuffixFileFilterFiles(String path, String suffix) {
        return FileUtils.listFiles(getFile(path), FileFilterUtils.suffixFileFilter(suffix, IOCase.INSENSITIVE),
                DirectoryFileFilter.INSTANCE);
    }

    /**
     * @auth:fengjian @Title: listNameFileFifterFiles @Description:
     *                通过名字过滤指定路径下的文件 @param @param path @param @param
     *                acceptedNames @param @return 设定文件 @return String[]
     *                返回类型 @throws
     */
    public static String[] listNameFileFifterFiles(String path, String[] acceptedNames) {
        return getFile(path).list(new NameFileFilter(acceptedNames, IOCase.SENSITIVE));
    }

    /**
     * @auth:fengjian @Title: listNameFileFifterFiles @Description:
     *                通过指定名字过滤指定路径下的文件 @param @param path @param @param
     *                acceptedNames @param @return 设定文件 @return Collection
     *                <File> 返回类型 @throws
     */
    public static Collection<File> listNameFileFifterFiles(String path, String acceptedNames) {
        return FileUtils.listFiles(getFile(path), FileFilterUtils.nameFileFilter(acceptedNames, IOCase.SENSITIVE),
                DirectoryFileFilter.INSTANCE);
    }

    /**
     * @auth:fengjian @Title: listWildcardFileFilterFilesNames @Description:
     *                通过指定关键字通配过滤指定路径下的文件 @param @param path @param @param
     *                wild @param @return 设定文件 @return String[] 返回类型 @throws
     */
    public static String[] listWildcardFileFilterFilesNames(String path, String wild) {
        return getFile(path)
                .list(new WildcardFileFilter(new StringBuffer().append("*").append(wild).append("*").toString()));
    }

    /**
     * @auth:fengjian @Title: listWildcardFileFilterFiles @Description:
     *                通过指定关键字通配过滤指定路径下的文件 @param @param path @param @param
     *                wild @param @return 设定文件 @return Collection
     *                <File> 返回类型 @throws
     */
    public static Collection<File> listWildcardFileFilterFiles(String path, String wild) {
        return FileUtils.listFiles(getFile(path),
                FileFilterUtils.and(
                        new WildcardFileFilter(new StringBuffer().append("*").append(wild).append("*").toString())),
                DirectoryFileFilter.INSTANCE);
    }

    /**
     * @auth:fengjian @Title: listPrefixFileFilterFilesNames @Description:
     *                通过前缀名过滤文件 @param @param path @param @param
     *                prefix @param @return 设定文件 @return String[] 返回类型 @throws
     */
    public static String[] listPrefixFileFilterFilesNames(String path, String prefix) {
        return getFile(path).list(new PrefixFileFilter(prefix));
    }

    /**
     * @auth:fengjian @Title: listPrefixFileFilterFiles @Description:
     *                通过前缀过滤文件 @param @param path @param @param
     *                prefix @param @return 设定文件 @return Collection
     *                <File> 返回类型 @throws
     */
    public static Collection<File> listPrefixFileFilterFiles(String path, String prefix) {
        return FileUtils.listFiles(getFile(path), FileFilterUtils.prefixFileFilter(prefix, IOCase.SENSITIVE),
                DirectoryFileFilter.INSTANCE);
    }

    /**
     * @auth:fengjian @Title: nameFileComparator @Description:
     *                通过文件名排序 @param @param path @param @return 设定文件 @return
     *                File[] 返回类型 @throws
     */
    public static File[] nameFileComparator(String path) {
        if (isDirectory(path)) {
            NameFileComparator comparator = new NameFileComparator(IOCase.SENSITIVE);
            return comparator.sort(getFile(path).listFiles());
        }
        return null;
    }

    /**
     * @auth:fengjian @Title: sizeFileComparator @Description:
     *                通过文件大小排序 @param @param path @param @return 设定文件 @return
     *                File[] 返回类型 @throws
     */
    public static File[] sizeFileComparator(String path) {
        if (isDirectory(path)) {
            SizeFileComparator sizeComparator = new SizeFileComparator(true);
            return sizeComparator.sort(getFile(path).listFiles());
        }
        return null;
    }

    /**
     * @auth:fengjian @Title: lastModifiedFileComparator @Description:
     *                通过修改时间进行排序 @param @param path @param @return 设定文件 @return
     *                File[] 返回类型 @throws
     */
    public static File[] lastModifiedFileComparator(String path) {
        if (isDirectory(path)) {
            LastModifiedFileComparator lastModified = new LastModifiedFileComparator();
            return lastModified.sort(getFile(path).listFiles());
        }
        return null;
    }

    /**
     * @auth:fengjian @Title: getSize @Description: 获取文件或者目录大小 @param @param
     *                path @param @return 设定文件 @return long 返回类型 @throws
     */
    public static long getSize(String path) {
        File file = getFile(path);
        if (isDirectory(path)) {
            return FileUtils.sizeOfDirectory(file);
        } else {
            return FileUtils.sizeOf(file);
        }
    }

    /**
     * @auth:fengjian @Title: filenameFilter @Description:
     *                文件名过滤特殊字符 @param @param str @param @return 设定文件 @return
     *                String 返回类型 @throws
     */
    public static String filenameFilter(String str) {
        return str == null ? null : FILE_PATTERN.matcher(str).replaceAll("");
    }

    /**
     * 判断文件名后缀是否等于suffix fengjian
     * 
     * @param fileName
     *            文件名
     * @param suffix
     *            后缀 例如.zip .rar等等
     * @return
     */
    public static boolean isEndsWith(String fileName, String suffix) {
        boolean flag = false;
        if (fileName != null && !"".equals(fileName.trim()) && suffix != null && !"".equals(suffix.trim()))
            return fileName.toLowerCase().endsWith(suffix);

        return flag;
    }

    /**
     * 获取文件大小 fengjian
     * 
     * @param file
     * @return
     */
    public static String getFileSize(File file) {
        String size;
        if (file.exists() && file.isFile()) {
            long fileS = file.length();
            size = getFileSize(fileS);
        } else if (file.exists() && file.isDirectory()) {
            size = "";
        } else {
            size = "0BT";
        }
        return size;
    }

    /**
     * 获取文件大小 fengjian
     * 
     * @param fileSize
     * @return
     */
    public static String getFileSize(long fileSize) {
        String size;
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileSize < 1024) {
            size = df.format((double) fileSize) + "BT";
        } else if (fileSize < 1048576) {
            size = df.format((double) fileSize / 1024) + "KB";
        } else if (fileSize < 1073741824) {
            size = df.format((double) fileSize / 1048576) + "MB";
        } else {
            size = df.format((double) fileSize / 1073741824) + "GB";
        }
        return size;
    }

    /**
     * 拼凑文件目录 fengjian
     * 
     * @param args
     * @return
     * @throws IOException
     */
    public static String getFilePath(Object... args) throws IOException {
        StringBuilder stringBuffer = new StringBuilder(60);
        for (Object object : args) {
            stringBuffer.append(object);
        }
        File file = FileUtil.getFile(stringBuffer.toString());
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        } else {
            FileUtil.cleanDirectory(stringBuffer.toString());
        }
        return stringBuffer.toString();
    }

    /**
     * 预览 pdf 文件
     * 
     * @param response
     *            http 响应
     * @param file
     *            要被写出的文件
     * @throws IOException
     */
    public static void viewPdf(HttpServletResponse response, File file) throws IOException {
        InputStream input = FileUtils.openInputStream(file);
        byte[] data = IOUtils.toByteArray(input);
        response.setContentType("application/pdf;charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
        IOUtils.closeQuietly(input);
    }

//    public static void downloadPdf(HttpServletResponse response, String filePath, String fileName) throws IOException {
//        byte[] bytes = IoUtil.getFileData(filePath);// 获取该文件的byte数组
//        String fileNameEncoding = new String(fileName.getBytes("GBK"), "ISO8859-1");
//        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameEncoding + PDF_TYPE + "\"");
//        response.addHeader("Content-Length", String.valueOf(bytes.length));
//        response.setContentType("application/octet-stream;charset=UTF-8");
//        IOUtils.write(bytes, response.getOutputStream());
//    }

}
