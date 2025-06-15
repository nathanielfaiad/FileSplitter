package com.filesplitter.FileSplitter.file;

import com.filesplitter.FileSplitter.converter.CsvToExcelService;
import com.filesplitter.FileSplitter.messaging.GcsMessage;
import com.filesplitter.FileSplitter.output.OutputService;
import com.google.cloud.spring.storage.GoogleStorageLocation;
import com.google.cloud.spring.storage.GoogleStorageResource;
import com.google.cloud.storage.NotificationInfo;
import com.google.cloud.storage.Storage;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GcsFileProcessor {

  private final Storage storage;

  private final CsvToExcelService csvToExcelService;

  private final OutputService outputService;

  @Autowired
  public GcsFileProcessor(Storage storage, CsvToExcelService csvToExcelService, OutputService outputService) {
    this.storage = storage;
    this.csvToExcelService = csvToExcelService;
    this.outputService = outputService;
  }

  public void processFile(GcsMessage msg) {
    if (shouldProcess(msg)) {
      FileNameExtractor.FileNameResult result = FileNameExtractor.extractFileName(msg.getName());
      GoogleStorageResource gcsResource = getResource(msg);

      InputStream gcsInputStream;
      try {
        gcsInputStream = gcsResource.getInputStream();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      InputStream templateStream;
      try {
        templateStream = new ClassPathResource("templates/prime_template.xlsx").getInputStream();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      SXSSFWorkbook workbook = csvToExcelService.convertCsvToExcel(gcsInputStream, templateStream, result.isAltFile());
      outputService.output(workbook, result.fileName);
    }
  }

  private boolean shouldProcess(GcsMessage msg) {
    if (!NotificationInfo.EventType.OBJECT_FINALIZE.name().equals(msg.getGcsMessageHeaders().getEventType())) {
      log.info(
          "Message eventType is not OBJECT_FINALIZE. Exiting processing for: {}", msg.getGcsMessageHeaders()
              .getEventType()
      );
      return false;
    }

    if (msg.getGcsMessageHeaders().getObjectId() == null) {
      log.info("Message objectId is null. Exiting processing.");
      return false;
    }

    return true;
  }

  private GoogleStorageResource getResource(GcsMessage msg) {
    GoogleStorageLocation location = GoogleStorageLocation.forFile(msg.getBucket(), msg.getName());
    return new GoogleStorageResource(storage, location, false);
  }

}