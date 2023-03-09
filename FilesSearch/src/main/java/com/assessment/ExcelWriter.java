package com.assessment;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {
    private Workbook workbook;
    private Sheet sheet;
    private int rowNum;
    
    public ExcelWriter() throws IOException {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet();
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("File Name");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Email");
        headerRow.createCell(3).setCellValue("Mobile Number");
        rowNum = 1;
    }
    
    public void addResult(SearchResult result) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(result.getFileName());
        row.createCell(1).setCellValue(result.getName());
        row.createCell(2).setCellValue(result.getEmail());
        row.createCell(3).setCellValue(result.getMobileNumber());
    }
    
    public void save() throws IOException {
        FileOutputStream outputStream = new FileOutputStream("C:\\Documents\\output.xlsx");
        workbook.write(outputStream);
        workbook.close();
    }
}