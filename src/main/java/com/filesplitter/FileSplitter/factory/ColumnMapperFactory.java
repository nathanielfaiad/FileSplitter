package com.filesplitter.FileSplitter.factory;

import com.filesplitter.FileSplitter.constants.JudiColumn;
import com.filesplitter.FileSplitter.constants.PrimeColumn;
import com.filesplitter.FileSplitter.mapper.ColumnMapper;
import com.filesplitter.FileSplitter.mapper.ConcatMapper;
import com.filesplitter.FileSplitter.mapper.ConstantMapper;
import com.filesplitter.FileSplitter.mapper.DirectMapper;
import com.filesplitter.FileSplitter.mapper.PairMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColumnMapperFactory {

  private static final List<DirectMapper> COMMON_DIRECT_MAPPERS = Arrays.asList(
      new DirectMapper(JudiColumn.CARRIER_ID.getIndex(), PrimeColumn.CARRIER_ID.getIndex()),
      new DirectMapper(JudiColumn.ACCT_NUMBER.getIndex(), PrimeColumn.ACCT_NUMBER.getIndex()),
      new DirectMapper(JudiColumn.GROUP_NUMBER.getIndex(), PrimeColumn.GROUP_NUMBER.getIndex()),
      new DirectMapper(JudiColumn.CLIENT_BENEFIT_CODE.getIndex(), PrimeColumn.CBC.getIndex()),
      new DirectMapper(JudiColumn.FULL_PATIENT_ID.getIndex(), PrimeColumn.FULL_PATIENT_ID.getIndex()),
      new DirectMapper(JudiColumn.PATIENT_LAST_NAME.getIndex(), PrimeColumn.PATIENT_LAST_NAME.getIndex()),
      new DirectMapper(JudiColumn.PATIENT_FIRST_NAME.getIndex(), PrimeColumn.PATIENT_FIRST_NAME.getIndex()),
      new DirectMapper(JudiColumn.DATE_OF_SERVICE.getIndex(), PrimeColumn.FULL_DATE_OF_TRANSACTION.getIndex())
  );

  private static final List<ConstantMapper> CONSTANT_MAPPERS = Arrays.asList(
      new ConstantMapper("P", PrimeColumn.MEDICAL_OR_PHARMACY.getIndex())
  );

  private static final List<ColumnMapper> ALT_FALSE_COLUMN_MAPPERS = getColumnMappers(false);

  private static final List<ColumnMapper> ALT_TRUE_COLUMN_MAPPERS = getColumnMappers(true);

  public static List<ColumnMapper> getColumnMappersForFile(String filename) {
    boolean isAltFile = filename.contains("ALT");
    return isAltFile ? ALT_TRUE_COLUMN_MAPPERS : ALT_FALSE_COLUMN_MAPPERS;
  }

  private static List<ColumnMapper> getColumnMappers(boolean isAltFile) {
    List<ColumnMapper> columnMappers = new ArrayList<>(COMMON_DIRECT_MAPPERS);
    columnMappers.addAll(getDeductiblePairMappers(isAltFile));
    columnMappers.addAll(getOopPairMappers(isAltFile));
    return columnMappers;
  }

  private static List<ConstantMapper> getConstantMappers(boolean isAltFile) {
    return Arrays.asList(
        new ConstantMapper(isAltFile ? "Y" : "N", PrimeColumn.INDIVIDUAL_MARKET_INDICATOR.getIndex()),
        new ConstantMapper(isAltFile ? "Y" : "N", PrimeColumn.DUAL_ADJUDICATION_INDICATOR.getIndex())
    );
  }

  private static List<ConcatMapper> getConcatMappers() {
    return Arrays.asList(new ConcatMapper(
        new int[]{
            JudiColumn.CLAIM_ID.getIndex(),
            JudiColumn.REAL_CLAIM_NUMBER.getIndex(),
            JudiColumn.REAL_CLAIM_SEQ_NUMBER.getIndex(),
            JudiColumn.REAL_CLAIM_STATUS.getIndex()
        },
        PrimeColumn.DESCRIPTION.getIndex(),
        "-"
    ));
  }

  private static List<PairMapper> getDeductiblePairMappers(boolean isAltFile) {
    int amountColumnIndex = isAltFile ? PrimeColumn.DEDUCTIBLE_ALTERNATE_DOLLAR_AMOUNT_NEEDED_FROM_PRIME.getIndex()
        : PrimeColumn.DEDUCTIBLE_REAL_DOLLAR_AMOUNT_NEEDED_FROM_PRIME.getIndex();
    return Arrays.asList(
        new PairMapper(
            new int[]{JudiColumn.DED_BOTH.getIndex(), JudiColumn.DED_BOTH_AMOUNT.getIndex()},
            PrimeColumn.DEDUCTIBLE_ACCUM_CODE.getIndex(),
            amountColumnIndex
        ),
        new PairMapper(
            new int[]{JudiColumn.DED_OON.getIndex(), JudiColumn.DED_OON_AMOUNT.getIndex()},
            PrimeColumn.DEDUCTIBLE_ACCUM_CODE.getIndex(),
            amountColumnIndex
        ),
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
    return Arrays.asList(
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

}