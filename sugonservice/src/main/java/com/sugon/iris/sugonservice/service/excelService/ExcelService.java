package com.sugon.iris.sugonservice.service.excelService;

import com.sugon.iris.sugondomain.beans.fileBeans.ExcelRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public interface ExcelService {

    HSSFWorkbook getNewExcel(String id, List<ExcelRow> excelRowList);

    XSSFWorkbook getNewExcelX(String id, List<ExcelRow> excelRowList);

    void getNewExcelXForSheet(XSSFWorkbook workbook , String sheetName, List<ExcelRow> excelRowList);
}
