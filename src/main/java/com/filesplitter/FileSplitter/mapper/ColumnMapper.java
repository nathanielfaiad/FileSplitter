package com.filesplitter.FileSplitter.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ColumnMapper {

  private final Function<String[], List<ValueMapper>> mappingFunction;

  public ColumnMapper(Function<String[], List<ValueMapper>> mappingFunction) {
    this.mappingFunction = mappingFunction;
  }

  public static ColumnMapper directMapper(int outputIndex, int inputIndex) {
    return new ColumnMapper(row -> {
      List<ValueMapper> valueMappers = new ArrayList<>();
      valueMappers.add(new ValueMapper(outputIndex, row[inputIndex]));
      return valueMappers;
    });
  }

  public static ColumnMapper constantMapper(int outputIndex, String constantValue) {
    return new ColumnMapper(row -> {
      List<ValueMapper> valueMappers = new ArrayList<>();
      valueMappers.add(new ValueMapper(outputIndex, constantValue));
      return valueMappers;
    });
  }

  public static ColumnMapper concatMapper(int outputIndex, String delimiter, int... inputIndexes) {
    return new ColumnMapper(row -> {
      List<ValueMapper> valueMappers = new ArrayList<>();
      String[] values = new String[inputIndexes.length];
      for (int i = 0; i < inputIndexes.length; i++) {
        values[i] = row[inputIndexes[i]];
      }
      valueMappers.add(new ValueMapper(outputIndex, String.join(delimiter, values)));
      return valueMappers;
    });
  }

  public List<ValueMapper> mapValues(String[] row) {
    return mappingFunction.apply(row);
  }

}