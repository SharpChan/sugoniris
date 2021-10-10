package com.sugon.iris.sugonservice.service.ExcelService;

import com.sugon.iris.sugondomain.beans.fileBeans.ExcelRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

public interface ExcelService {

    HSSFWorkbook getNewExcel(String id, List<ExcelRow> excelRowList);
}
