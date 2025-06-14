package com.filesplitter.FileSplitter.converter;

import com.filesplitter.FileSplitter.mapper.RowMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Slf4j
public class CsvToExcelConverter {

  private final RowMapper rowMapper;

  public CsvToExcelConverter(RowMapper rowMapper) {
    this.rowMapper = rowMapper;
  }

  public void convertCsvToExcel(String csvFilePath) {
    convertCsvToExcel(csvFilePath, null, 0);
  }

  public void convertCsvToExcel(String csvFilePath, String templateFilePath, int startRowIndex) {
    try (SXSSFWorkbook workbook = templateFilePath != null
        ? new SXSSFWorkbook(new XSSFWorkbook(new FileInputStream(templateFilePath)), 100)
        : new SXSSFWorkbook(100)) {
      Sheet sheet = workbook.getSheetAt(0);
      if (sheet == null) {
        sheet = workbook.createSheet();
      }

      // Read CSV file
      try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(csvFilePath)))
          .withSkipLines(0)
          .build()) {
        String[] nextLine;
        int rowIndex = startRowIndex;
        while ((nextLine = csvReader.readNext()) != null) {
          rowMapper.mapRow(sheet, nextLine, rowIndex++);
        }
      }

      // Write workbook to file
      String excelFilePath = csvFilePath.substring(0, csvFilePath.lastIndexOf('.')) + ".xlsx";
      try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
        workbook.write(outputStream);
      }
    } catch (Exception e) {
      log.error("Error converting CSV to Excel", e);
    }
  }

}