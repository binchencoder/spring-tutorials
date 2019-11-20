package com.binchencoder.config;

import com.binchencoder.MyPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
// http://docs.spring.io/spring-boot/docs/current/reference/html/howto-security.html
// Switch off the Spring Boot security configuration
//@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private AccessDeniedHandler accessDeniedHandler;

  // roles admin allow to access /admin/**
  // roles user allow to access /user/**
  // custom 403 access denied handler
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // 关闭默认的csrf认证
    http.csrf().disable();
    http.authorizeRequests()
        .antMatchers("/", "/home", "/about").permitAll()
        .antMatchers("/admin/**").hasAnyRole("ADMIN")
        .antMatchers("/user/**").hasAnyRole("USER")
        .anyRequest().authenticated()
        .and().formLogin().loginPage("/login").permitAll()
        .and().logout().permitAll()
        .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .passwordEncoder(new MyPasswordEncoder())
        .withUser("user").password("password").roles("USER")
        .and()
        .withUser("admin").password("password").roles("ADMIN");
  }

  // Spring Boot configured this already.
  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
        .antMatchers("/webjars/**")
        .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
  }
}
