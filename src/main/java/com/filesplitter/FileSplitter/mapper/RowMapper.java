package com.filesplitter.FileSplitter.mapper;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class RowMapper {

  private final List<ColumnMapper> columnMappers;

  private final int[] dateIndices;

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");

  public RowMapper(List<ColumnMapper> columnMappers, int[] dateIndices) {
    this.columnMappers = columnMappers;
    this.dateIndices = dateIndices;
  }

  public Row mapRow(Sheet sheet, String[] csvRow, int rowIndex) {
    Row excelRow = sheet.createRow(rowIndex);
    for (ColumnMapper mapper : columnMappers) {
      String[] mappedValues = mapper.mapValue(csvRow);
      int[] targetColumnIndexes = mapper.getTargetColumnIndexes();
      for (int i = 0; i < mappedValues.length; i++) {
        String value = mappedValues[i];
        if (isDateIndex(targetColumnIndexes[i])) {
          value = formatDate(value);
        }

        // TODO: Review the null-check
        if (value != null) {
          excelRow.createCell(targetColumnIndexes[i]).setCellValue(value);
        }
      }
    }
    return excelRow;
  }

  private boolean isDateIndex(int index) {
    for (int dateIndex : dateIndices) {
      if (index == dateIndex) {
        return true;
      }
    }
    return false;
  }

  private String formatDate(String utcDate) {
    try {
      OffsetDateTime date = OffsetDateTime.parse(utcDate);
      return date.format(formatter);
    } catch (DateTimeParseException e) {
      // If the date is not in the expected format, return the original value
      return utcDate;
    }
  }

}