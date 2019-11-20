package com.binchencoder.sso.authserver.configurations;

import com.binchencoder.sso.authserver.MyAuthenticationProvider;
import com.binchencoder.sso.authserver.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Order(1)
@Configuration
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    // Load user details in memory
//    return new InMemoryUserDetailsManager(
//        User.withDefaultPasswordEncoder()
//            .username("user").password("password").roles("USER")
//            .username("admin").password("password").roles("ADMIN")
//            .build());

    return new MyUserDetailsService();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    AuthenticationProvider authenticationProvider = new MyAuthenticationProvider(
        userDetailsService());
    return authenticationProvider;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // 暂时使用基于内存的AuthenticationProvider
//    auth.inMemoryAuthentication()
//        .withUser("chenbin")
//        .password(passwordEncoder().encode("chenbin"))
//        .roles("USER");

    // 自定义AuthenticationProvider
    auth.authenticationProvider(authenticationProvider());
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/static/**", "/except/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // disabled csrf
    http.csrf().disable();
    // default protection for all resources (including /oauth/authorize)
    http.requestMatchers()
        .antMatchers("/login", "/oauth/authorize")
        .and().authorizeRequests()
        .anyRequest().authenticated()
        // 自定义登录页和注销URL
        .and().formLogin().permitAll()
    ;
  }
}
