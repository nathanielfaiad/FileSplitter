package com.filesplitter.FileSplitter.messaging;

import com.filesplitter.FileSplitter.file.GcsFileProcessor;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccumInBalanceConsumer implements Consumer<Message<GcsMessage>> {

  private final GcsFileProcessor gcsFileProcessor;

  @Override
  public void accept(Message<GcsMessage> msg) {
    log.debug("Message received: {}", msg);

    try {
      gcsFileProcessor.processFile(msg.getPayload());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}