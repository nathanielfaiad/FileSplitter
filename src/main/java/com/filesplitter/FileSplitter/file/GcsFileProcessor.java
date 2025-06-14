package com.filesplitter.FileSplitter.file;

import com.filesplitter.FileSplitter.converter.CsvToExcelService;
import com.filesplitter.FileSplitter.factory.ColumnMapperFactory;
import com.filesplitter.FileSplitter.mapper.ColumnMapper;
import com.filesplitter.FileSplitter.messaging.GcsMessage;
import com.google.cloud.spring.storage.GoogleStorageLocation;
import com.google.cloud.spring.storage.GoogleStorageResource;
import com.google.cloud.storage.NotificationInfo;
import com.google.cloud.storage.Storage;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GcsFileProcessor {

  private final Storage storage;

  private final CsvToExcelService csvToExcelService;

  @Autowired
  public GcsFileProcessor(Storage storage, CsvToExcelService csvToExcelService) {
    this.storage = storage;
    this.csvToExcelService = csvToExcelService;
  }

  public void processFile(GcsMessage msg) {
    if (shouldProcess(msg)) {
      FileNameExtractor.FileNameResult result = FileNameExtractor.extractFileName(msg.getName());

      // TODO: Get RowMapper instead and add dateIndices to the ColumnMapper/Factory
      List<ColumnMapper> columnMappers = ColumnMapperFactory.getColumnMappers(result.isAltFile());

      // TODO: Update CsvToExcelService to accept InputStream and RowMapper and pass those in here

      // TODO: Output the file to the correct GCS bucket

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