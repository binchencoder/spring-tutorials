package com.binchencoder.retry;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;

public class RetryTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(RetryTest.class);

  public void test1() {
    LOGGER.info("Retry service()");
    for (int i = 1; i <= 3; i++) {
      if (i != 3) {
        throw new RemoteAccessException("retry i:" + i);
      }
    }

    LOGGER.info("Successed");
  }

  @Retryable(include = RemoteAccessException.class)
  public void test2() {
    LOGGER.info("Retry service()");
    for (int i = 1; i <= 3; i++) {
      if (i != 3) {
        throw new RemoteAccessException("retry");
      }
    }
  }

  @Test
  public void testRetryAnnotation() {
    test2();
  }

  @Test
  public void testRetryTemplate() {
    RetryTemplate retryTemplate = RetryTemplate.builder()
        .maxAttempts(3)
        .fixedBackoff(1000)
        .retryOn(RemoteAccessException.class)
        .build();
    retryTemplate.execute(ctx -> {
      test1();
      return null;
    });
  }

}
