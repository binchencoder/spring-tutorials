package com.binchencoder.sso.authserver.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
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

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//    clients
//        .jdbc(this.dataSource)
//        .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());

    clients
        .inMemory()
        .withClient("first-client")
        .secret(passwordEncoder.encode("noonewilleverguess"))
        .scopes("resource:read")
        .authorizedGrantTypes("authorization_code")
        .autoApprove(true)
        .redirectUris("http://localhost:8083/ui/login", "http://localhost:8083/login",
            "http://www.example.com/")
//        .accessTokenValiditySeconds(3600) // 1 hour
        ;
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    super.configure(endpoints);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    super.configure(security);
    security.tokenKeyAccess("permitAll()")
        .checkTokenAccess("isAuthenticated()");
  }
}
