package com.filesplitter.FileSplitter.output;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public interface OutputService {

  void output(SXSSFWorkbook workbook, String fileName);

}