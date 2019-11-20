package com.binchencoder.oauth2.configrations;

import com.binchencoder.oauth2.MyAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private AccessDeniedHandler accessDeniedHandler;

  @Bean
  public UserDetailsService userDetailsService() {
    // Load user details in memory
    return new InMemoryUserDetailsManager(
        User.withDefaultPasswordEncoder()
            .username("user").password("password").roles("USER")
            .username("admin").password("password").roles("ADMIN")
            .build());
//    return new MyUserDetailsService();
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
    // auth.inMemoryAuthentication().withUser("username").password("password").roles("USER");

    // 自定义AuthenticationProvider
    auth.authenticationProvider(authenticationProvider());
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/static/**", "/except/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.addFilterBefore(new KaptchaAuthenticationFilter("/login", "/login?error"),
        UsernamePasswordAuthenticationFilter.class);
    // disabled csrf
    http.csrf().disable();
    // default protection for all resources (including /oauth/authorize)
    http.requestMatchers()
        .antMatchers("/login", "/oauth/authorize")
        .antMatchers("/", "/home", "/about")
        .and().authorizeRequests()
        .antMatchers("/admin/**").hasAnyRole("ADMIN")
        .antMatchers("/user/**").hasAnyRole("USER")
        .anyRequest().authenticated()
        // 自定义登录页和注销URL
        .and().formLogin().loginPage("/login").failureUrl("/login?error").permitAll()
//        .usernameParameter("username").passwordParameter("password").permitAll()
        .and().logout().logoutUrl("/logout").permitAll()
        .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
  }
}
