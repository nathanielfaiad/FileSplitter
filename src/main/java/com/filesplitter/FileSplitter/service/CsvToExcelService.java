package com.filesplitter.FileSplitter.service;

import com.filesplitter.FileSplitter.constants.PrimeColumn;
import com.filesplitter.FileSplitter.factory.ColumnMapperFactory;
import com.filesplitter.FileSplitter.mapper.RowMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CsvToExcelService {

  public SXSSFWorkbook convertCsvToExcel(
      InputStream inputFileStream,
      InputStream templateFileStream,
      boolean isAltFile
  ) {
    // Get RowMapper/ColumnMappers for file
    // TODO: Perhaps create a FileMapper class to capture dateIndices (AND ColumnMappers)
    RowMapper rowMapper = new RowMapper(
        ColumnMapperFactory.getColumnMappers(isAltFile),
        new int[]{PrimeColumn.FULL_DATE_OF_TRANSACTION.getIndex()}
    );

    try {
      // Build Output Writer
      XSSFWorkbook templateWorkbook = new XSSFWorkbook(templateFileStream);
      Sheet templateSheet = templateWorkbook.getSheetAt(0);
      int startRowIndex = templateSheet.getLastRowNum() + 1;
      SXSSFWorkbook outputWorkbook = new SXSSFWorkbook(templateWorkbook, 1000);
      Sheet outputSheet = outputWorkbook.getSheetAt(0);
      if (outputSheet == null) {
        outputSheet = outputWorkbook.createSheet();
      }

      // Build CSV Reader and map data
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputFileStream));
      try (CSVReader csvReader = new CSVReaderBuilder(bufferedReader)
          // TODO: Perhaps create a FileMapper class to capture headerRowCount (AND ColumnMappers)
          .withSkipLines(1)
          .build()) {
        String[] nextLine;
        int rowIndex = startRowIndex;
        while ((nextLine = csvReader.readNext()) != null) {
          rowMapper.mapRow(outputSheet, nextLine, rowIndex++);
        }
      }

      return outputWorkbook;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}