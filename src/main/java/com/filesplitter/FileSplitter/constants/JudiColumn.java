package com.filesplitter.FileSplitter.constants;

public enum JudiColumn {
  CARRIER_ID(0),
  ACCT_NUMBER(1),
  GROUP_NUMBER(2),
  CLIENT_BENEFIT_CODE(3),
  FULL_PATIENT_ID(4),
  PATIENT_LAST_NAME(5),
  PATIENT_FIRST_NAME(6),
  INDIVIDUAL_MARKET_INDICATOR(7),
  DUAL_ADJUDICATION_INDICATOR(8),
  REAL_CLAIM_NUMBER(9),
  REAL_CLAIM_SEQ_NUMBER(10),
  REAL_CLAIM_STATUS(11),
  FULL_DATE_OF_TRANSACTION(12),
  DATE_OF_SERVICE(13),
  DED_BOTH(14),
  DED_BOTH_AMOUNT(15),
  DED_OON(16),
  DED_OON_AMOUNT(17),
  DED_ONC(18),
  DED_ONC_AMOUNT(19),
  DED_ONC_OON(20),
  DED_ONC_OON_AMOUNT(21),
  DED_SPEC(22),
  DED_SPEC_AMOUNT(23),
  OOP_BOTH(24),
  OOP_BOTH_AMOUNT(25),
  OOP_OON(26),
  OOP_OON_AMOUNT(27),
  OOP_ONC(28),
  OOP_ONC_AMOUNT(29),
  OOP_ONC_OON(30),
  OOP_ONC_OON_AMOUNT(31),
  OOP_SPEC(32),
  OOP_SPEC_AMOUNT(33),
  CLAIM_ID(34);

  private final int index;

  JudiColumn(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }
}