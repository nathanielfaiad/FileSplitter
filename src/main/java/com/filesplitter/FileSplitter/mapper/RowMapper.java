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

  public void mapRow(Sheet sheet, String[] csvRow, int rowIndex) {
    Row excelRow = sheet.createRow(rowIndex);
    for (ColumnMapper mapper : columnMappers) {
      List<ValueMapper> mappedValues = mapper.mapValues(csvRow);
      for (ValueMapper valueMapper : mappedValues) {
        String value = valueMapper.getValue();
        if (value != null) {
          if (isDateIndex(valueMapper.getOutputIndex())) {
            value = formatDate(value);
          }
          excelRow.createCell(valueMapper.getOutputIndex()).setCellValue(value);
        }
      }
    }
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