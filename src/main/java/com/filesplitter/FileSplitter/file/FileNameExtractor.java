package com.filesplitter.FileSplitter.file;

import lombok.Getter;

public class FileNameExtractor {

  public static FileNameResult extractFileName(String fullFileName) {
    String fileNameWithExt = fullFileName.substring(fullFileName.lastIndexOf("/") + 1);
    String fileName = fileNameWithExt.substring(0, fileNameWithExt.lastIndexOf("."));
    boolean isAltFile = fileName.toUpperCase().contains("ALT") || fileName.toUpperCase().contains("ALTERNATE");

    return new FileNameResult(fileName, isAltFile);
  }

  @Getter
  public static class FileNameResult {

    public String fileName;

    public boolean isAltFile;

    public FileNameResult(String fileName, boolean isAltFile) {
      this.fileName = fileName;
      this.isAltFile = isAltFile;
    }

  }

}