package com.jianf.commons.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ExcelUtils<T> {

    private static final Log logger = LogFactory.getLog(ExcelUtils.class);


    /**
     * 根据Excel后缀创建不同版本的workbook
     *
     * @param is
     * @param excelFileName
     * @return
     * @throws IOException
     */
    public Workbook createWorkbook(InputStream is, String excelFileName) throws IOException {
        if (excelFileName.endsWith(".xls")) {
            return new HSSFWorkbook(is);
        } else if (excelFileName.endsWith(".xlsx")) {
            return new XSSFWorkbook(is);
        }
        return null;
    }

    /**
     * 获取sheet
     *
     * @param workbook
     * @param sheetIndex
     * @return
     */
    public Sheet getSheet(Workbook workbook, int sheetIndex) {
        return workbook.getSheetAt(sheetIndex);
    }


    /**
     * 根据表头和字段映射读取Excel数据
     *
     * @param vo            返回的实体
     * @param is            文件流
     * @param excelFileName 文件名
     * @param cells         读取多少列表格
     * @param rowIndex      第几行开始读
     * @param initFieldName rowIndex为0时，需要提供默认表头
     * @return
     */
    public List<T> importDataFromExcel(T vo, InputStream is, String excelFileName, int cells, int rowIndex, String[] initFieldName) {
        List<T> list = new ArrayList<>();
        try {
            //创建工作簿
            Workbook workbook = this.createWorkbook(is, excelFileName);
            //创建工作表sheet
            Sheet sheet = this.getSheet(workbook, 0);
            //获取sheet中数据的行数
            int rows = sheet.getPhysicalNumberOfRows();
            //获取表头行数据
            Row fieldRow = sheet.getRow(0);
            //利用反射，给JavaBean的属性进行赋值
            Field[] fields = vo.getClass().getDeclaredFields();
            //第一行为标题栏，从第rowIndex行开始取数据,有些不要表头
            for (int i = rowIndex; i < rows; i++) {
                Row row = sheet.getRow(i);
                int index = 0;
                while (index < cells) {
                    Cell cell = row.getCell(index);
                    if (null == cell) {
                        cell = row.createCell(index);
                    }
                    //若initFieldName为空，则说明第一行是表头，否则取Excel第一行为表头参数
                    String fieldName = ArrayUtils.isEmpty(initFieldName) ? fieldRow.getCell(index).getStringCellValue() : initFieldName[index];
                    Field field = Arrays.stream(fields).filter(e -> {
                        if (e.getName().equals(fieldName)) {
                            return true;
                        }
                        return false;
                    }).findFirst().get();
                    String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    //增加判断set方法参数类型
                    Method setMethod = vo.getClass().getMethod(methodName, new Class[]{field.getType()});
                    invokeMethodByType(vo, field, cell, setMethod);
                    index++;
                }
                list.add(vo);
                vo = (T) vo.getClass().getConstructor(new Class[]{}).newInstance(new Object[]{});//重新创建一个vo对象
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return list;
    }

    /**
     * 根据不同字段类型，获取表格的值并转换后调用相应的set方法
     *
     * @param vo
     * @param field
     * @param cell
     * @param method
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void invokeMethodByType(T vo, Field field, Cell cell, Method method) {
        try {
            if (field.getType() == Integer.class) {
                cell.setCellType(Cell.CELL_TYPE_STRING);
                Integer valueI = null == cell.getStringCellValue() ? 0 : Integer.valueOf(cell.getStringCellValue());
                method.invoke(vo, new Object[]{valueI});
            } else if (field.getType() == Double.class) {
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                Double valueD = cell.getNumericCellValue();
                if (checkDouble(valueD)) {
                    logger.info("数据不合法");
                }
                method.invoke(vo, new Object[]{new BigDecimal(String.valueOf(valueD))});
            } else if (field.getType() == Boolean.class) {
                cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                Boolean valueB = cell.getBooleanCellValue();
                method.invoke(vo, new Object[]{valueB});
            } else if (field.getType() == Long.class) {
                cell.setCellType(Cell.CELL_TYPE_STRING);
                Long valueL = null == cell.getStringCellValue() ? 0 : Long.valueOf(cell.getStringCellValue());
                method.invoke(vo, new Object[]{valueL});
            } else {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    DataFormatter dataFormatter = new DataFormatter();
                    method.invoke(vo, new Object[]{dataFormatter.formatCellValue(cell)});
                } else {
                    //默认当字符串处理
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String valueS = null == cell.getStringCellValue() ? "" : cell.getStringCellValue();
                    //赋值
                    method.invoke(vo, new Object[]{valueS});
                }
            }
        } catch (Exception e) {
            logger.error("参数不符合：", e);
        }
    }

    /**
     * 检查double是否符合格式，且大于0
     *
     * @param d
     * @return
     */
    public boolean checkDouble(double d) {
        d = d - (int) d;
        return d > 0;
    }

    /**
     * @param @param  object
     * @param @return
     * @return boolean
     * @throws
     * @Title: isHasValues
     * @Description: 判断一个对象所有属性是否有值，如果一个属性有值(非空)，则返回true
     */
    public boolean isHasValues(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        boolean flag = false;
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method getMethod;
            try {
                getMethod = object.getClass().getMethod(methodName);
                Object obj = getMethod.invoke(object);
                if (null != obj && !"".equals(obj)) {
                    flag = true;
                    break;
                }
            } catch (Exception e) {
                logger.error(e);
            }

        }
        return flag;

    }

    public <T> void exportDataToExcel(List<T> list, String[] headers, String title, OutputStream os) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        //设置表格默认列宽15个字节
        sheet.setDefaultColumnWidth(15);
        //生成一个样式
        HSSFCellStyle style = this.getCellStyle(workbook);
        //生成一个字体
        HSSFFont font = this.getFont(workbook);
        //把字体应用到当前样式
        style.setFont(font);

        //生成表格标题
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 300);
        HSSFCell cell = null;

        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        //将数据放入sheet中
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            T t = list.get(i);
            //利用反射，根据JavaBean属性的先后顺序，动态调用get方法得到属性的值
            Field[] fields = t.getClass().getFields();
            try {
                for (int j = 0; j < fields.length; j++) {
                    cell = row.createCell(j);
                    Field field = fields[j];
                    String fieldName = field.getName();
                    String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method getMethod = t.getClass().getMethod(methodName, new Class[]{});
                    Object value = getMethod.invoke(t, new Object[]{});

                    if (null == value)
                        value = "";
                    cell.setCellValue(value.toString());

                }
            } catch (Exception e) {
                logger.error(e);
            }
        }

        try {
            workbook.write(os);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }

    }


    public HSSFCellStyle getCellStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setLeftBorderColor(HSSFCellStyle.BORDER_THIN);
        style.setRightBorderColor(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        return style;
    }


    public HSSFFont getFont(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.WHITE.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        return font;
    }

    public boolean isIE(HttpServletRequest request) {
        return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 ? true : false;
    }
}
