# Spring Boot security thymeleaf

## Problems

* java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null"

**error message**

```
java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null"
	at org.springframework.security.crypto.password.DelegatingPasswordEncoder$UnmappedIdPasswordEncoder.matches(DelegatingPasswordEncoder.java:250) ~[spring-security-core-5.2.1.RELEASE.jar:5.2.1.RELEASE]
	at org.springframework.security.crypto.password.DelegatingPasswordEncoder.matches(DelegatingPasswordEncoder.java:198) ~[spring-security-core-5.2.1.RELEASE.jar:5.2.1.RELEASE]
	at org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter$LazyPasswordEncoder.matches(WebSecurityConfigurerAdapter.java:592) ~[spring-security-config-5.2.1.RELEASE.jar:5.2.1.RELEASE]
```

https://blog.csdn.net/weixin_39220472/article/details/80865411

