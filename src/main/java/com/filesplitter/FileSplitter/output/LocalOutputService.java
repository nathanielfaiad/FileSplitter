package com.filesplitter.FileSplitter.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class LocalOutputService implements OutputService {

  @Override
  public void output(SXSSFWorkbook workbook, String fileName) {
    try {
      File outputDir = new File("src/main/resources/output");
      if (!outputDir.exists()) {
        outputDir.mkdirs();
      }

      File outputFile = new File(outputDir, fileName + ".xlsx");
      try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
        workbook.write(fileOutputStream);
      }
    } catch (IOException e) {
      throw new RuntimeException("Error writing output file", e);
    } finally {
      workbook.dispose();
    }
  }

}