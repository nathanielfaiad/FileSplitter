package com.filesplitter.FileSplitter.messaging;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GcsMessageHeaders {

  private String eventType;

  private String objectId;

}