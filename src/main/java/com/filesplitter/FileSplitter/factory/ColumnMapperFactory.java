package com.filesplitter.FileSplitter.factory;

import com.filesplitter.FileSplitter.constants.JudiColumn;
import com.filesplitter.FileSplitter.constants.PrimeColumn;
import com.filesplitter.FileSplitter.mapper.ColumnMapper;
import com.filesplitter.FileSplitter.mapper.ValueMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ColumnMapperFactory {

  private static final List<ColumnMapper> COMMON_MAPPERS = List.of(
      ColumnMapper.directMapper(PrimeColumn.CARRIER_ID.getIndex(), JudiColumn.CARRIER_ID.getIndex()),
      ColumnMapper.directMapper(PrimeColumn.ACCT_NUMBER.getIndex(), JudiColumn.ACCT_NUMBER.getIndex()),
      ColumnMapper.directMapper(PrimeColumn.GROUP_NUMBER.getIndex(), JudiColumn.GROUP_NUMBER.getIndex()),
      ColumnMapper.directMapper(PrimeColumn.CBC.getIndex(), JudiColumn.CLIENT_BENEFIT_CODE.getIndex()),
      ColumnMapper.directMapper(PrimeColumn.FULL_PATIENT_ID.getIndex(), JudiColumn.FULL_PATIENT_ID.getIndex()),
      ColumnMapper.directMapper(PrimeColumn.PATIENT_LAST_NAME.getIndex(), JudiColumn.PATIENT_LAST_NAME.getIndex()),
      ColumnMapper.directMapper(
          PrimeColumn.PATIENT_FIRST_NAME.getIndex(),
          JudiColumn.PATIENT_FIRST_NAME.getIndex()
      ),
      ColumnMapper.directMapper(
          PrimeColumn.FULL_DATE_OF_TRANSACTION.getIndex(),
          JudiColumn.DATE_OF_SERVICE.getIndex()
      )
  );

  private static final List<ColumnMapper> CONSTANT_MAPPERS = List.of(
      ColumnMapper.constantMapper(PrimeColumn.MEDICAL_OR_PHARMACY.getIndex(), "P")
  );

  private static final List<ColumnMapper> STANDARD_COLUMN_MAPPERS = getColumnMappersForFileType(false);

  private static final List<ColumnMapper> ALT_COLUMN_MAPPERS = getColumnMappersForFileType(true);

  public static List<ColumnMapper> getColumnMappers(boolean isAltFile) {
    return isAltFile ? ALT_COLUMN_MAPPERS : STANDARD_COLUMN_MAPPERS;
  }

  private static List<ColumnMapper> getColumnMappersForFileType(boolean isAltFile) {
    List<ColumnMapper> columnMappers = new ArrayList<>(CONSTANT_MAPPERS);
    columnMappers.addAll(COMMON_MAPPERS);
    columnMappers.addAll(getConstantMappers(isAltFile));
    columnMappers.addAll(getDeductibleMappers(isAltFile));
    columnMappers.addAll(getOutOfPocketMappers(isAltFile));
    columnMappers.addAll(getConcatMappers());
    return Collections.unmodifiableList(columnMappers);
  }

  private static List<ColumnMapper> getConstantMappers(boolean isAltFile) {
    List<ColumnMapper> constantMappers = new ArrayList<>();
    String value = isAltFile ? "Y" : "N";
    constantMappers.add(ColumnMapper.constantMapper(PrimeColumn.INDIVIDUAL_MARKET_INDICATOR.getIndex(), value));
    constantMappers.add(ColumnMapper.constantMapper(PrimeColumn.DUAL_ADJUDICATION_INDICATOR.getIndex(), value));
    return constantMappers;
  }

  private static List<ColumnMapper> getConcatMappers() {
    return List.of(ColumnMapper.concatMapper(
        PrimeColumn.DESCRIPTION.getIndex(),
        "-",
        JudiColumn.CLAIM_ID.getIndex(),
        JudiColumn.REAL_CLAIM_NUMBER.getIndex(),
        JudiColumn.REAL_CLAIM_SEQ_NUMBER.getIndex(),
        JudiColumn.REAL_CLAIM_STATUS.getIndex()
    ));
  }

  private static List<ColumnMapper> getDeductibleMappers(boolean isAltFile) {
    int amountColumnIndex = isAltFile ? PrimeColumn.DEDUCTIBLE_ALTERNATE_DOLLAR_AMOUNT_NEEDED_FROM_PRIME.getIndex()
        : PrimeColumn.DEDUCTIBLE_REAL_DOLLAR_AMOUNT_NEEDED_FROM_PRIME.getIndex();

    return List.of(
        new ColumnMapper(row -> {
          int[][] inputIndexesArray = new int[][]{
              {JudiColumn.DED_BOTH.getIndex(), JudiColumn.DED_BOTH_AMOUNT.getIndex()},
              {JudiColumn.DED_OON.getIndex(), JudiColumn.DED_OON_AMOUNT.getIndex()},
              {JudiColumn.DED_ONC.getIndex(), JudiColumn.DED_ONC_AMOUNT.getIndex()},
              {JudiColumn.DED_ONC_OON.getIndex(), JudiColumn.DED_ONC_OON_AMOUNT.getIndex()},
              {JudiColumn.DED_SPEC.getIndex(), JudiColumn.DED_SPEC_AMOUNT.getIndex()}
          };

          for (int[] inputIndexes : inputIndexesArray) {
            Optional<List<ValueMapper>> optionalList = getAmountMapping(
                row,
                inputIndexes[0], // inputCodeIndex
                inputIndexes[1], // inputAmountIndex
                PrimeColumn.DEDUCTIBLE_ACCUM_CODE.getIndex(),
                amountColumnIndex,
                PrimeColumn.DEDUCTIBLE_ADJUSTMENT_REASON_CODE.getIndex()
            );
            if (optionalList.isPresent()) {
              return optionalList.get();
            }
          }

          // Default to nothing if a non-zero value is not found
          return Collections.emptyList();
        })
    );
  }

  private static List<ColumnMapper> getOutOfPocketMappers(boolean isAltFile) {
    int amountColumnIndex = isAltFile ? PrimeColumn.OUT_OF_POCKET_ALTERNATE_DOLLAR_AMOUNT_NEEDED_FROM_PRIME.getIndex()
        : PrimeColumn.OUT_OF_POCKET_REAL_DOLLAR_AMOUNT_NEEDED_FROM_PRIME.getIndex();

    return List.of(
        new ColumnMapper(row -> {
          int[][] inputIndexesArray = new int[][]{
              {JudiColumn.OOP_BOTH.getIndex(), JudiColumn.OOP_BOTH_AMOUNT.getIndex()},
              {JudiColumn.OOP_OON.getIndex(), JudiColumn.OOP_OON_AMOUNT.getIndex()},
              {JudiColumn.OOP_ONC.getIndex(), JudiColumn.OOP_ONC_AMOUNT.getIndex()},
              {JudiColumn.OOP_ONC_OON.getIndex(), JudiColumn.OOP_ONC_OON_AMOUNT.getIndex()},
              {JudiColumn.OOP_SPEC.getIndex(), JudiColumn.OOP_SPEC_AMOUNT.getIndex()}
          };

          for (int[] inputIndexes : inputIndexesArray) {
            Optional<List<ValueMapper>> optionalList = getAmountMapping(
                row,
                inputIndexes[0], // inputCodeIndex
                inputIndexes[1], // inputAmountIndex
                PrimeColumn.OUT_OF_POCKET_ACCUM_CODE.getIndex(),
                amountColumnIndex,
                PrimeColumn.OUT_OF_POCKET_ADJUSTMENT_REASON_CODE.getIndex()
            );
            if (optionalList.isPresent()) {
              return optionalList.get();
            }
          }

          // Default to nothing if a non-zero value is not found
          return Collections.emptyList();
        })
    );
  }

  private static Optional<List<ValueMapper>> getAmountMapping(
      String[] row,
      int inputCodeIndex,
      int inputAmountIndex,
      int outputCodeIndex,
      int outputAmountIndex,
      int adjustmentReasonCodeIndex
  ) {
    String value = row[inputAmountIndex];
    if (value != null && !value.trim().isEmpty()) {
      try {
        double number = Double.parseDouble(value);
        if (number != 0) {
          return Optional.of(List.of(
              // Absolute value of amount
              new ValueMapper(outputAmountIndex, String.valueOf(Math.abs(number))),

              // Accum Code
              new ValueMapper(outputCodeIndex, row[inputCodeIndex]),

              // Adjustment Reason Code
              new ValueMapper(adjustmentReasonCodeIndex, "JUDISEED"),

              // Transaction Sign based on positive/negative amount
              new ValueMapper(PrimeColumn.TRANSACTION_SIGN.getIndex(), number > 0 ? "P" : "X")
          ));
        }
      } catch (NumberFormatException ignore) {

      }
    }
    return Optional.empty();
  }

}