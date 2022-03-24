package com.dongyimai.oauth.service;

import com.dongyimai.oauth.util.AuthToken;

public interface AuthService {

    /***
     * 授权认证方法
     * @param username 用户名
     * @param password 密码
     * @param clientId 客户端id
     * @param clientSecret 秘钥
     */
    AuthToken login(String username, String password, String clientId, String clientSecret);
}