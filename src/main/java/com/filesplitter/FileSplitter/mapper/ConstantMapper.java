package com.filesplitter.FileSplitter.mapper;

public class ConstantMapper implements ColumnMapper {

  private String constantValue;

  private int targetColumnIndex;

  public ConstantMapper(String constantValue, int targetColumnIndex) {
    this.constantValue = constantValue;
    this.targetColumnIndex = targetColumnIndex;
  }

  @Override
  public String[] mapValue(String[] row) {
    String[] result = new String[1];
    result[0] = constantValue;
    return result;
  }

  @Override
  public int[] getTargetColumnIndexes() {
    return new int[]{targetColumnIndex};
  }

}