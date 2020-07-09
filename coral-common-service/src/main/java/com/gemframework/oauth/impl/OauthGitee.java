package com.gemframework.oauth.impl;

import com.gemframework.oauth.BaseOauth;
import org.springframework.stereotype.Component;

@Component
public class OauthGitee implements BaseOauth {


    @Override
    public String authorize(String clientId,String redirectUrl) {
        return "https://gitee.com/oauth/authorize?client_id=" + clientId + "&response_type=code&redirect_uri=" + redirectUrl;
    }

    @Override
    public String accessToken(String code,String clientId,String clientSecret,String redirectUrl) {
        return "https://gitee.com/oauth/token?grant_type=authorization_code&code=" + code + "&client_id=" + clientId + "&redirect_uri=" + redirectUrl + "&client_secret=" + clientSecret;
    }

    @Override
    public String userInfo(String accessToken) {
        return "https://gitee.com/api/v5/user?access_token=" + accessToken;
    }
}