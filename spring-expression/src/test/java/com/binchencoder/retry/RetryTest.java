package com.binchencoder.retry;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;

public class RetryTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(RetryTest.class);

  private AtomicInteger count = new AtomicInteger(0);

  public void test1() {
    LOGGER.info("Retry service()");
    for (; count.get() <= 3; ) {
      count.incrementAndGet();
      if (count.get() != 3) {
        LOGGER.error("throw RemoteAccessException");
        throw new RemoteAccessException("retry i:" + count.get());
      }
    }

    LOGGER.info("Successed");
  }

  @Retryable(value = RemoteAccessException.class, maxAttempts = 4)
  public void test2() {
    LOGGER.info("Retry service()");
    for (; count.get() <= 2; ) {
      count.getAndIncrement();
      if (count.get() != 2) {
        LOGGER.error("throw RemoteAccessException");
        throw new RemoteAccessException("retry i:" + count.get());
      }
    }

    LOGGER.info("Successed");
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
