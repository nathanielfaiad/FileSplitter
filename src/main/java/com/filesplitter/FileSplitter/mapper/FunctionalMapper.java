package com.filesplitter.FileSplitter.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FunctionalMapper {

  private Function<String[], List<OutputMapping>> mappingFunction;

  public FunctionalMapper(Function<String[], List<OutputMapping>> mappingFunction) {
    this.mappingFunction = mappingFunction;
  }

  public static FunctionalMapper directMapper(int outputIndex, int inputIndex) {
    return new FunctionalMapper(row -> {
      List<OutputMapping> outputMappings = new ArrayList<>();
      outputMappings.add(new OutputMapping(outputIndex, row[inputIndex]));
      return outputMappings;
    });
  }

  public static FunctionalMapper constantMapper(int outputIndex, String constantValue) {
    return new FunctionalMapper(row -> {
      List<OutputMapping> outputMappings = new ArrayList<>();
      outputMappings.add(new OutputMapping(outputIndex, constantValue));
      return outputMappings;
    });
  }

  public static FunctionalMapper concatMapper(int outputIndex, String delimiter, int... inputIndexes) {
    return new FunctionalMapper(row -> {
      List<OutputMapping> outputMappings = new ArrayList<>();
      String[] values = new String[inputIndexes.length];
      for (int i = 0; i < inputIndexes.length; i++) {
        values[i] = row[inputIndexes[i]];
      }
      outputMappings.add(new OutputMapping(outputIndex, String.join(delimiter, values)));
      return outputMappings;
    });
  }

  public List<OutputMapping> mapValues(String[] row) {
    return mappingFunction.apply(row);
  }

}