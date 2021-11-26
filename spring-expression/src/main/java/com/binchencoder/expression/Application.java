package com.binchencoder.expression;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@Configuration
@SpringBootApplication
public class Application {

  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    ExpressionParser parser = new SpelExpressionParser();
    Expression exp = parser.parseExpression("'Hello World'");
    String message = (String) exp.getValue();
    System.out.println(message);

    // Turn on:
    // - auto null reference initialization
    // - auto collection growing
    SpelParserConfiguration config = new SpelParserConfiguration(true, true);
    ExpressionParser parser1 = new SpelExpressionParser(config);
    Expression expression = parser1.parseExpression("list[3]");

    Demo demo = new Demo();
    Object o = expression.getValue(demo);
    System.out.println(o);

    SpringApplication.run(Application.class, args);
  }
}

class Demo {
  public List<String> list;
}