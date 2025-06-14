package com.filesplitter.FileSplitter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

@Configuration
public class MessageConverterConfig {

  @Bean
  public MessageConverter messageConverter(ObjectMapper objectMapper) {
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setObjectMapper(objectMapper);
    return converter;
  }

}