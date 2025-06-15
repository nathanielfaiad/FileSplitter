package com.filesplitter.FileSplitter.output;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!local")
public class GcsOutputService implements OutputService {

  @Override
  public void output(SXSSFWorkbook workbook, String fileName) {
    // Output to GCS bucket
  }

}