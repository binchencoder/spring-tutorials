package com.binchencoder.elasticsearch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BaseConfig {

  @Value("${spring.profiles.active}")
  private String env;

  @Bean
  public String env() {
    return env;
  }
}
