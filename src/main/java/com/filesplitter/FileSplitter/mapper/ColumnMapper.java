package com.filesplitter.FileSplitter.mapper;

public interface ColumnMapper {

  String[] mapValue(String[] row);

  int[] getTargetColumnIndexes();

}