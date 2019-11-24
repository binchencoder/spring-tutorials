# spring-boot-security-sso-authserver

Authorization service for OAuth2 login

## Curl

- oauth/token

```
curl first-client:noonewilleverguess@localhost:8081/auth/oauth/token -dgrant_type=password -dscope=resource:read -dusername=chenbin -dpassword=chenbin

{
    "access_token":"b7687808-4750-4253-af31-4495cdbdd3f4",
    "token_type":"bearer",
    "expires_in":43196,
    "scope":"resource:read"
}
```

指定authorizedGrantTypes中增加`password`
```java
clients
        .inMemory()
        .withClient("first-client")
        .secret(passwordEncoder.encode("noonewilleverguess"))
        .scopes("resource:read", "write")
        .authorizedGrantTypes("authorization_code", "password")
        .autoApprove(true)
        .redirectUris("http://localhost:8083/ui/login", "http://localhost:8083/login",
            "http://www.example.com/");
```

- oauth/check_token

```
curl first-client:noonewilleverguess@localhost:8081/auth/oauth/check_token -dtoken=b7687808-4750-4253-af31-4495cdbdd3f4

{
    "active":true,
    "exp":1574622117,
    "user_name":"chenbin",
    "authorities":[
        "ROLE_USER"
    ],
    "client_id":"first-client",
    "scope":[
        "resource:read"
    ]
}
```

- oauth/authorize


## References

* [单点登录的三种实现方式](https://blog.csdn.net/L18270919464/article/details/53300846)