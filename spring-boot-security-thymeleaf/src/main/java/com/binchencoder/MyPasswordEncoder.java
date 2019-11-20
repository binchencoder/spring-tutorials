package com.binchencoder;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by chenbin on 2019/11/20
 *
 * @author chenbin
 */
public class MyPasswordEncoder implements PasswordEncoder {

  @Override
  public String encode(CharSequence charSequence) {
    return charSequence.toString();
  }

  @Override
  public boolean matches(CharSequence charSequence, String s) {
    return s.equals(charSequence.toString());
  }
}
