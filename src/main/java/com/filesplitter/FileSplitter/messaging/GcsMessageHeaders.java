package com.filesplitter.FileSplitter.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GcsMessageHeaders {

  private String eventType;

  private String objectId;

}