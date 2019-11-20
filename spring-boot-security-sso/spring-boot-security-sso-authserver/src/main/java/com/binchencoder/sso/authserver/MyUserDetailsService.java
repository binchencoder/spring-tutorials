package com.binchencoder.sso.authserver;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsService implements UserDetailsService {

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // TODO(chenbin) Load user from db
    return User.withUsername("chenbin")
        .password("chenbin")
        .roles("USER")
        .build();
  }
}
