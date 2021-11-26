package com.binchencoder.retry;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;

public class RecoveryCallbackTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(RecoveryCallbackTest.class);

  private AtomicInteger count = new AtomicInteger(0);

  @Test
  public void testRecovery() {
    RetryTemplate template = RetryTemplate.builder()
        .maxAttempts(5)
        .exponentialBackoff(100, 2, 10000)
        .retryOn(RemoteAccessException.class)
        .traversingCauses()
        .build();

    Foo foo = template.execute(
        (RetryCallback<Foo, RemoteAccessException>) context -> {
          // business logic here
          LOGGER.info("Retry service()");
          for (; count.get() <= 3; ) {
            count.incrementAndGet();
            if (count.get() != 3) {
              LOGGER.error("throw RemoteAccessException");
              throw new RemoteAccessException("retry i:" + count.get());
            }
          }
          return new Foo();
        },
        retryContext -> {
          LOGGER.info("Recover");
          return new Foo();
        });
  }
}

class Foo {

}
