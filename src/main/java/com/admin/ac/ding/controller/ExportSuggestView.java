package com.admin.ac.ding.controller;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Map;

public class ExportSuggestView extends ExcelView {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void setRow(Sheet sheet, Map<String, Object> map) {
        Row header = sheet.createRow(0);
        int cellIndex = 0;
        header.createCell(cellIndex).setCellValue("提交人");
        header.getCell(cellIndex++).setCellStyle(super.cellStyle);
        header.createCell(cellIndex).setCellValue("提交人电话");
        header.getCell(cellIndex++).setCellStyle(super.cellStyle);
        header.createCell(cellIndex).setCellValue("提交人所在部门");
        header.getCell(cellIndex++).setCellStyle(super.cellStyle);
        header.createCell(cellIndex).setCellValue("提交时间");
        header.getCell(cellIndex++).setCellStyle(super.cellStyle);
        header.createCell(cellIndex).setCellValue("意见描述");
        header.getCell(cellIndex++).setCellStyle(super.cellStyle);
        header.createCell(cellIndex).setCellValue("线上回复");
        header.getCell(cellIndex++).setCellStyle(super.cellStyle);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    @Override
    protected void setStyle(Workbook workbook) {
        super.cellStyle = new DefaultCellStyleImpl().setCellStyle(workbook);
    }
}
