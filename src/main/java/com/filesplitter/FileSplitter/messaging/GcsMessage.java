package com.filesplitter.FileSplitter.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GcsMessage {

  private String id;

  private String bucket;

  private String name;

  private GcsMessageHeaders gcsMessageHeaders;

}