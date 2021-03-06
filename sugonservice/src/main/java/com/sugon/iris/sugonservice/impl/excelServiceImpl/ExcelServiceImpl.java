package com.sugon.iris.sugonservice.impl.excelServiceImpl;

import com.sugon.iris.sugondomain.beans.fileBeans.ExcelRow;
import com.sugon.iris.sugonservice.service.excelService.ExcelService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {


    @Override
    public  HSSFWorkbook getNewExcel(String id, List<ExcelRow> excelRowList) {
        //新建excel对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        //新建工作表
        HSSFSheet sheet = workbook.createSheet(id);
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        //字体应用到当前样式
        style.setFont(font);
        //创建表格行
        for(int i =0 ; i < excelRowList.size();i++) {
            HSSFRow row = sheet.createRow(i);
            List<String> fields = excelRowList.get(i).getFields();
            for(int j = 0;j < fields.size(); j++){
                row.createCell(j).setCellValue(fields.get(j));
            }
        }
        return workbook;
    }

    @Override
    public XSSFWorkbook getNewExcelX(String id, List<ExcelRow> excelRowList) {
        //新建excel对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        //新建工作表
        XSSFSheet sheet = workbook.createSheet(id);
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        XSSFCellStyle style = workbook.createCellStyle();
        // 生成一个字体
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        //字体应用到当前样式
        style.setFont(font);
        //创建表格行
        for(int i =0 ; i < excelRowList.size();i++) {
            XSSFRow row = sheet.createRow(i);
            List<String> fields = excelRowList.get(i).getFields();
            for(int j = 0;j < fields.size(); j++){
                row.createCell(j).setCellValue(fields.get(j));
            }
        }
        return workbook;
    }

    @Override
    public void getNewExcelXForSheet(XSSFWorkbook workbook , String sheetName, List<ExcelRow> excelRowList) {
        //新建工作表
        XSSFSheet sheet = workbook.createSheet(sheetName);
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        XSSFCellStyle style = workbook.createCellStyle();
        // 生成一个字体
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        //字体应用到当前样式
        style.setFont(font);
        //创建表格行
        for(int i =0 ; i < excelRowList.size();i++) {
            XSSFRow row = sheet.createRow(i);
            List<String> fields = excelRowList.get(i).getFields();
            for(int j = 0;j < fields.size(); j++){
                row.createCell(j).setCellValue(fields.get(j));
            }
        }
    }

}
