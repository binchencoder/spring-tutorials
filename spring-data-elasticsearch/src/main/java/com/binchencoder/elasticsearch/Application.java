package com.binchencoder.elasticsearch;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@Configuration
@SpringBootApplication
@Slf4j
public class Application {

  @Resource
  protected ElasticsearchOperations elasticsearchOperations;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);

    log.info("Spring data elasticsearch started!!!");
  }

}