# spring-cloud-sso-sample

OAuth2 login for spring cloud sample

## Curl

1. oauth/token
```
curl first-client:noonewilleverguess@localhost:8080/oauth/token -dgrant_type=password -dscope=any -dusername=chenbin -dpassword=chenbin

{
    "access_token":"4344373e-bf4e-4349-91cc-ecdec9ef5fea",
    "token_type":"bearer",
    "refresh_token":"9d99b650-24e5-4413-8b3e-001ab5a99368",
    "expires_in":43199,
    "scope":"any"
}
```

2. oauth/authorize
```
curl first-client:noonewilleverguess@localhost:8080/oauth/authorize -dgrant_type=authorization_code -dscope=any -dclient_id=first-client -dresponse_type=code
```

> 目前有错误

## References

* [单点登录的三种实现方式](https://blog.csdn.net/L18270919464/article/details/53300846)