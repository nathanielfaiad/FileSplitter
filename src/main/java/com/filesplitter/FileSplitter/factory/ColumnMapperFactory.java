package com.filesplitter.FileSplitter.factory;

import com.filesplitter.FileSplitter.constants.JudiColumn;
import com.filesplitter.FileSplitter.constants.PrimeColumn;
import com.filesplitter.FileSplitter.mapper.ColumnMapper;
import com.filesplitter.FileSplitter.mapper.FunctionalMapper;
import com.filesplitter.FileSplitter.mapper.OutputMapping;
import com.filesplitter.FileSplitter.mapper.PairMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ColumnMapperFactory {

  private static final List<FunctionalMapper> COMMON_MAPPERS = List.of(
      FunctionalMapper.directMapper(PrimeColumn.CARRIER_ID.getIndex(), JudiColumn.CARRIER_ID.getIndex()),
      FunctionalMapper.directMapper(PrimeColumn.ACCT_NUMBER.getIndex(), JudiColumn.ACCT_NUMBER.getIndex()),
      FunctionalMapper.directMapper(PrimeColumn.GROUP_NUMBER.getIndex(), JudiColumn.GROUP_NUMBER.getIndex()),
      FunctionalMapper.directMapper(PrimeColumn.CBC.getIndex(), JudiColumn.CLIENT_BENEFIT_CODE.getIndex()),
      FunctionalMapper.directMapper(PrimeColumn.FULL_PATIENT_ID.getIndex(), JudiColumn.FULL_PATIENT_ID.getIndex()),
      FunctionalMapper.directMapper(PrimeColumn.PATIENT_LAST_NAME.getIndex(), JudiColumn.PATIENT_LAST_NAME.getIndex()),
      FunctionalMapper.directMapper(
          PrimeColumn.PATIENT_FIRST_NAME.getIndex(),
          JudiColumn.PATIENT_FIRST_NAME.getIndex()
      ),
      FunctionalMapper.directMapper(
          PrimeColumn.FULL_DATE_OF_TRANSACTION.getIndex(),
          JudiColumn.DATE_OF_SERVICE.getIndex()
      )
  );

  private static final List<FunctionalMapper> CONSTANT_MAPPERS = List.of(
      FunctionalMapper.constantMapper(PrimeColumn.MEDICAL_OR_PHARMACY.getIndex(), "P")
  );

  private static final List<ColumnMapper> ALT_FALSE_COLUMN_MAPPERS = getColumnMappersForFileType(false);

  private static final List<ColumnMapper> ALT_TRUE_COLUMN_MAPPERS = getColumnMappersForFileType(true);

  private static final Function<String, String> BASE_AMOUNT_FUNCTION = baseValue -> {
    // Evaluate the base value here
    try {
      double number = Double.parseDouble(baseValue);
      return String.valueOf(Math.abs(number));
    } catch (NumberFormatException e) {
      return null;
    }
  };

  public static List<ColumnMapper> getColumnMappers(boolean isAltFile) {
    return isAltFile ? ALT_TRUE_COLUMN_MAPPERS : ALT_FALSE_COLUMN_MAPPERS;
  }

  public static List<FunctionalMapper> getFunctionalMappers(boolean isAltFile) {

    return List.of();
  }

  private static List<ColumnMapper> getColumnMappersForFileType(boolean isAltFile) {
    List<ColumnMapper> columnMappers = new ArrayList<>();
    columnMappers.addAll(getDeductiblePairMappers(isAltFile));
    columnMappers.addAll(getOopPairMappers(isAltFile));
    return Collections.unmodifiableList(columnMappers);
  }

  private static List<FunctionalMapper> getFunctionalMappersForFileType(boolean isAltFile) {
    List<FunctionalMapper> columnMappers = new ArrayList<>(CONSTANT_MAPPERS);
    columnMappers.addAll(COMMON_MAPPERS);
    columnMappers.addAll(getConstantFunctionalMappers(isAltFile));
    columnMappers.addAll(getConcatMappers());
    return Collections.unmodifiableList(columnMappers);
  }

  private static List<FunctionalMapper> getConstantFunctionalMappers(boolean isAltFile) {
    List<FunctionalMapper> constantMappers = new ArrayList<>();
    String value = isAltFile ? "Y" : "N";
    constantMappers.add(FunctionalMapper.constantMapper(PrimeColumn.INDIVIDUAL_MARKET_INDICATOR.getIndex(), value));
    constantMappers.add(FunctionalMapper.constantMapper(PrimeColumn.DUAL_ADJUDICATION_INDICATOR.getIndex(), value));
    return constantMappers;
  }

  private static List<FunctionalMapper> getConcatMappers() {
    return List.of(FunctionalMapper.concatMapper(
        PrimeColumn.DESCRIPTION.getIndex(),
        "-",
        JudiColumn.CLAIM_ID.getIndex(),
        JudiColumn.REAL_CLAIM_NUMBER.getIndex(),
        JudiColumn.REAL_CLAIM_SEQ_NUMBER.getIndex(),
        JudiColumn.REAL_CLAIM_STATUS.getIndex()
    ));
  }

  //  private static List<ConditionalMapper> getDeductibleMappers(boolean isAltFile) {
  //    int amountColumnIndex = isAltFile ? PrimeColumn.DEDUCTIBLE_ALTERNATE_DOLLAR_AMOUNT_NEEDED_FROM_PRIME.getIndex()
  //        : PrimeColumn.DEDUCTIBLE_REAL_DOLLAR_AMOUNT_NEEDED_FROM_PRIME.getIndex();
  //
  //    ConditionalTarget[] commonTargets = new ConditionalTarget[]{new ConditionalTarget(
  //        PrimeColumn.DEDUCTIBLE_ACCUM_CODE.getIndex(),
  //        (row, baseValue) -> baseValue != null && Double.parseDouble(baseValue) > 0 ? "P" : "X"
  //    ), new ConditionalTarget(
  //        PrimeColumn.DEDUCTIBLE_ADJUSTMENT_REASON_CODE.getIndex(),
  //        (row, baseValue) -> baseValue != null ? "JUDISEED" : null
  //    )};
  //
  //    return List.of(new ConditionalMapper(
  //        amountColumnIndex,
  //        BASE_AMOUNT_FUNCTION,
  //        commonTargets
  //    ));
  //  }

  private static List<FunctionalMapper> getDeductibleMappers(boolean isAltFile) {
    int amountColumnIndex = isAltFile ? PrimeColumn.DEDUCTIBLE_ALTERNATE_DOLLAR_AMOUNT_NEEDED_FROM_PRIME.getIndex()
        : PrimeColumn.DEDUCTIBLE_REAL_DOLLAR_AMOUNT_NEEDED_FROM_PRIME.getIndex();

    return List.of(
        new FunctionalMapper(row -> {
          Optional<List<OutputMapping>> optionalList = getAmountMapping(
              row,
              JudiColumn.DED_BOTH.getIndex(),
              JudiColumn.DED_BOTH_AMOUNT.getIndex(),
              PrimeColumn.DEDUCTIBLE_ACCUM_CODE.getIndex(),
              amountColumnIndex
          );
          if (optionalList.isPresent()) {
            return optionalList.get();
          }

          Optional<List<OutputMapping>> optionalList2 = getAmountMapping(
              row,
              JudiColumn.DED_OON.getIndex(),
              JudiColumn.DED_OON_AMOUNT.getIndex(),
              PrimeColumn.DEDUCTIBLE_ACCUM_CODE.getIndex(),
              amountColumnIndex
          );
          if (optionalList2.isPresent()) {
            return optionalList2.get();
          }

          // TODO: Complete the rest and create another method
          return Collections.emptyList();
        })
    );
  }

  private static List<PairMapper> getDeductiblePairMappers(boolean isAltFile) {
    int amountColumnIndex = isAltFile ? PrimeColumn.DEDUCTIBLE_ALTERNATE_DOLLAR_AMOUNT_NEEDED_FROM_PRIME.getIndex()
        : PrimeColumn.DEDUCTIBLE_REAL_DOLLAR_AMOUNT_NEEDED_FROM_PRIME.getIndex();
    return List.of(
        new PairMapper(
            new int[]{JudiColumn.DED_ONC.getIndex(), JudiColumn.DED_ONC_AMOUNT.getIndex()},
            PrimeColumn.DEDUCTIBLE_ACCUM_CODE.getIndex(),
            amountColumnIndex
        ),
        new PairMapper(
            new int[]{JudiColumn.DED_ONC_OON.getIndex(), JudiColumn.DED_ONC_OON_AMOUNT.getIndex()},
            PrimeColumn.DEDUCTIBLE_ACCUM_CODE.getIndex(),
            amountColumnIndex
        ),
        new PairMapper(
            new int[]{JudiColumn.DED_SPEC.getIndex(), JudiColumn.DED_SPEC_AMOUNT.getIndex()},
            PrimeColumn.DEDUCTIBLE_ACCUM_CODE.getIndex(),
            amountColumnIndex
        )
    );
  }

  private static List<PairMapper> getOopPairMappers(boolean isAltFile) {
    int amountColumnIndex = isAltFile ? PrimeColumn.OUT_OF_POCKET_ALTERNATE_DOLLAR_AMOUNT_NEEDED_FROM_PRIME.getIndex()
        : PrimeColumn.OUT_OF_POCKET_REAL_DOLLAR_AMOUNT_NEEDED_FROM_PRIME.getIndex();
    return List.of(
        new PairMapper(
            new int[]{JudiColumn.OOP_BOTH.getIndex(), JudiColumn.OOP_BOTH_AMOUNT.getIndex()},
            PrimeColumn.OUT_OF_POCKET_ACCUM_CODE.getIndex(),
            amountColumnIndex
        ),
        new PairMapper(
            new int[]{JudiColumn.OOP_OON.getIndex(), JudiColumn.OOP_OON_AMOUNT.getIndex()},
            PrimeColumn.OUT_OF_POCKET_ACCUM_CODE.getIndex(),
            amountColumnIndex
        ),
        new PairMapper(
            new int[]{JudiColumn.OOP_ONC.getIndex(), JudiColumn.OOP_ONC_AMOUNT.getIndex()},
            PrimeColumn.OUT_OF_POCKET_ACCUM_CODE.getIndex(),
            amountColumnIndex
        ),
        new PairMapper(
            new int[]{JudiColumn.OOP_ONC_OON.getIndex(), JudiColumn.OOP_ONC_OON_AMOUNT.getIndex()},
            PrimeColumn.OUT_OF_POCKET_ACCUM_CODE.getIndex(),
            amountColumnIndex
        ),
        new PairMapper(
            new int[]{JudiColumn.OOP_SPEC.getIndex(), JudiColumn.OOP_SPEC_AMOUNT.getIndex()},
            PrimeColumn.OUT_OF_POCKET_ACCUM_CODE.getIndex(),
            amountColumnIndex
        )
    );
  }

  private static Optional<List<OutputMapping>> getAmountMapping(
      String[] row,
      int inputCodeIndex,
      int inputAmountIndex,
      int outputCodeIndex,
      int outputAmountIndex
  ) {
    String value = row[inputAmountIndex];
    if (value != null && !value.trim().isEmpty()) {
      try {
        double number = Double.parseDouble(value);
        if (number != 0) {
          return Optional.of(List.of(
              new OutputMapping(outputAmountIndex, String.valueOf(Math.abs(number))),
              new OutputMapping(outputCodeIndex, row[inputCodeIndex]),
              new OutputMapping(PrimeColumn.TRANSACTION_SIGN.getIndex(), number > 0 ? "P" : "X")
          ));
        }
      } catch (NumberFormatException ignore) {

      }
    }
    return Optional.empty();
  }

}