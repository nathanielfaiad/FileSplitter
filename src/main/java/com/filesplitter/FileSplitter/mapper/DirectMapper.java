package com.filesplitter.FileSplitter.mapper;

public class DirectMapper implements ColumnMapper {

  private int sourceColumnIndex;

  private int targetColumnIndex;

  public DirectMapper(int sourceColumnIndex, int targetColumnIndex) {
    this.sourceColumnIndex = sourceColumnIndex;
    this.targetColumnIndex = targetColumnIndex;
  }

  @Override
  public String[] mapValue(String[] row) {
    String[] result = new String[1];
    result[0] = row[sourceColumnIndex];
    return result;
  }

  @Override
  public int[] getTargetColumnIndexes() {
    return new int[]{targetColumnIndex};
  }

}