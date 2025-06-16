package com.filesplitter.FileSplitter.converter;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.filesplitter.FileSplitter.file.FileNameExtractor;
import com.filesplitter.FileSplitter.output.LocalOutputService;
import com.filesplitter.FileSplitter.output.OutputService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {CsvToExcelService.class, LocalOutputService.class})
@ActiveProfiles("local")
class CsvToExcelServiceTest {

  @Autowired
  private CsvToExcelService csvToExcelService;

  @Autowired
  private LocalOutputService localOutputService;

  @Autowired
  private OutputService outputService;

  @Test
  public void testCsvToExcelConversion() throws IOException {
    String fileName = "BCBSKS_Pharm_Run_Out20250615";

    // Load the input CSV file
    File inputFile = new File("src/main/resources/static/" + fileName + ".csv");
    FileNameExtractor.FileNameResult result = FileNameExtractor.extractFileName(inputFile.getName());
    try (InputStream inputStream = new FileInputStream(inputFile)) {
      // Load the template file
      ClassPathResource templateResource = new ClassPathResource("templates/prime_template.xlsx");
      try (InputStream templateStream = templateResource.getInputStream()) {
        // Convert CSV to Excel
        SXSSFWorkbook workbook = csvToExcelService.convertCsvToExcel(inputStream, templateStream, result.isAltFile());

        // Output the Excel file
        localOutputService.output(workbook, fileName);
      }
    }

    // Verify the output file exists
    File outputFile = new File("src/main/resources/output/" + fileName + ".xlsx");
    assertTrue(outputFile.exists());
  }

}