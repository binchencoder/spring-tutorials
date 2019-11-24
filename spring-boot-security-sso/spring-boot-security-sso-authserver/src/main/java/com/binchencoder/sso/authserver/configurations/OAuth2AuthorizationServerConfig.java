package com.binchencoder.sso.authserver.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserDetailsService userDetailsService;

  AuthenticationManager authenticationManager;

  public OAuth2AuthorizationServerConfig(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//    clients
//        .jdbc(this.dataSource)
//        .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());

    clients
        .inMemory()
        .withClient("first-client")
        .secret(passwordEncoder.encode("noonewilleverguess"))
        .scopes("resource:read", "write")
        .authorizedGrantTypes("authorization_code", "password")
        .autoApprove(true)
        .redirectUris("http://localhost:8083/ui/login", "http://localhost:8083/login",
            "http://www.example.com/")
//        .accessTokenValiditySeconds(3600) // 1 hour
    ;
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.authenticationManager(authenticationManager);
    endpoints.userDetailsService(userDetailsService);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    super.configure(security);
    security.tokenKeyAccess("permitAll()")
        .checkTokenAccess("isAuthenticated()");
  }
}
