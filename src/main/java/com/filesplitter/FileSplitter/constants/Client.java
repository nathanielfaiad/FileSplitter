package com.filesplitter.FileSplitter.constants;

import lombok.Getter;

@Getter
public enum Client {
  CAMBIA("CAMBIA"),
  CapitalBlueCross("CBC"),
  CapitalHealthPlan("CHP"),
  WEB_TPA("WEBTPA"),
  BCBS_Kansas("BCBSKS"),
  BCBS_Minnesota_HM("BCBSMNHM"),
  BCBS_Nebraska_HM("BCBSNEHM"),
  BCBS_Nebraska_HE("BCBSNEHE"),
  BCBS_Wyoming_HM("BCBSWYHM"),
  BCBS_RhodeIsland("BCBSRI"),
  BCBS_Florida("BCBSFL"),
  BCBS_Alabama("BCBSAL"),
  BCBS_NorthCarolina("BCBSNC"),
  BCBS_NorthDakota_HM("BCBSNDHM"),
  GPREXH("GPREXH"),
  TRULI("TRULI"),
  HORIZON("HORIZON"),
  HZMED("HZMED"),
  TRUSTMARK("TRUSTMARK"),
  HSC("HSC"),
  HISC("HISC"),
  HCSC_ACCM("HCSC-ACCM"),
  ANTHEM("ANTHEM"),
  U_OF_MN("UMN"),
  UNKNOWN("");

  private final String clientName;

  Client(String clientName) {
    this.clientName = clientName;
  }
}