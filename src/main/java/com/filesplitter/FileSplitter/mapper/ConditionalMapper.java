package com.filesplitter.FileSplitter.mapper;

import java.util.function.Function;

public class ConditionalMapper implements ColumnMapper {

  private int baseColumnIndex;

  private Function<String, String> baseValueFunction;

  private ConditionalTarget[] conditionalTargets;

  public ConditionalMapper(
      int baseColumnIndex,
      Function<String, String> baseValueFunction,
      ConditionalTarget... conditionalTargets
  ) {
    this.baseColumnIndex = baseColumnIndex;
    this.baseValueFunction = baseValueFunction;
    this.conditionalTargets = conditionalTargets;
  }

  @Override
  public String[] mapValue(String[] row) {
    String baseValue = row[baseColumnIndex];
    String evaluatedBaseValue = baseValueFunction.apply(baseValue);

    String[] result = new String[conditionalTargets.length];
    for (int i = 0; i < conditionalTargets.length; i++) {
      ConditionalTarget target = conditionalTargets[i];
      result[i] = target.getValue(row, evaluatedBaseValue);
    }

    return result;
  }

  @Override
  public int[] getTargetColumnIndexes() {
    int[] targetColumnIndexes = new int[conditionalTargets.length];
    for (int i = 0; i < conditionalTargets.length; i++) {
      targetColumnIndexes[i] = conditionalTargets[i].getTargetColumnIndex();
    }
    return targetColumnIndexes;
  }

}