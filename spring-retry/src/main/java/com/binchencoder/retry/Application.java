package com.binchencoder.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

@Configuration
@EnableRetry
@SpringBootApplication
public class Application {

  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  @Bean
  public TestService service() {
    return new TestService();
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}

@org.springframework.stereotype.Service
class TestService {

  @Retryable(RemoteAccessException.class)
  public void service() {
    System.out.print("Retry service()");
    for (int i = 1; i <= 3; i++) {
      if (i != 3) {
        throw new RemoteAccessException("retry");
      }
    }
  }

  @Recover
  public void recover(RemoteAccessException e) {
    System.out.println("Recover");
  }
}