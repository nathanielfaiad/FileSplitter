package com.filesplitter.FileSplitter.mapper;

public class ConcatMapper implements ColumnMapper {

  private int[] sourceColumnIndices;

  private int targetColumnIndex;

  private String delimiter;

  public ConcatMapper(int[] sourceColumnIndices, int targetColumnIndex, String delimiter) {
    this.sourceColumnIndices = sourceColumnIndices;
    this.targetColumnIndex = targetColumnIndex;
    this.delimiter = delimiter;
  }

  @Override
  public String[] mapValue(String[] row) {
    String[] result = new String[1];
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < sourceColumnIndices.length; i++) {
      if (i > 0) {
        sb.append(delimiter);
      }
      sb.append(row[sourceColumnIndices[i]]);
    }
    result[0] = sb.toString();
    return result;
  }

  @Override
  public int[] getTargetColumnIndexes() {
    return new int[]{targetColumnIndex};
  }

}