package com.filesplitter.FileSplitter.mapper;

public class PairMapper implements ColumnMapper {

  private int[] pairColumnIndexes;

  private int targetValueColumnIndex;

  private int targetSignColumnIndex;

  public PairMapper(int[] pairColumnIndexes, int targetValueColumnIndex, int targetSignColumnIndex) {
    this.pairColumnIndexes = pairColumnIndexes;
    this.targetValueColumnIndex = targetValueColumnIndex;
    this.targetSignColumnIndex = targetSignColumnIndex;
  }

  @Override
  public String[] mapValue(String[] row) {
    String[] result = new String[2]; // value and sign
    result[0] = null; // value
    result[1] = null; // sign

    for (int i = 0; i < pairColumnIndexes.length; i += 2) {
      String value = row[pairColumnIndexes[i + 1]];
      if (value != null && !value.trim().isEmpty()) {
        try {
          double number = Double.parseDouble(value);
          if (number != 0) {
            result[0] = String.valueOf(Math.abs(number)); // absolute value
            result[1] = number > 0 ? "P" : "X"; // sign
            break;
          }
        } catch (NumberFormatException e) {
          System.out.println();
          // ignore and move on to the next pair
        }
      }
    }

    return result;
  }

  @Override
  public int[] getTargetColumnIndexes() {
    return new int[]{targetValueColumnIndex, targetSignColumnIndex};
  }

}