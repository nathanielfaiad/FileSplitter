package com.filesplitter.FileSplitter.mapper;

import java.util.function.BiFunction;
import lombok.Getter;

public class ConditionalTarget {

  @Getter
  private int targetColumnIndex;

  private ValueProvider valueProvider;

  public ConditionalTarget(int targetColumnIndex, String hardcodedValue) {
    this.targetColumnIndex = targetColumnIndex;
    this.valueProvider = (row, baseValue) -> hardcodedValue;
  }

  public ConditionalTarget(int targetColumnIndex, int columnIndex) {
    this.targetColumnIndex = targetColumnIndex;
    this.valueProvider = (row, baseValue) -> row[columnIndex];
  }

  public ConditionalTarget(int targetColumnIndex, BiFunction<String[], String, String> conditionalFunction) {
    this.targetColumnIndex = targetColumnIndex;
    this.valueProvider = conditionalFunction::apply;
  }

  public String getValue(String[] row, String baseValue) {
    return valueProvider.getValue(row, baseValue);
  }

  @FunctionalInterface
  interface ValueProvider {

    String getValue(String[] row, String baseValue);

  }

}