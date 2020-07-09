package com.gemframework.oauth.impl;

import com.gemframework.oauth.BaseOauth;
import org.springframework.stereotype.Component;

@Component
public class OauthGithub implements BaseOauth {

    /**
     * 登陆授权类型
     */
    @Override
    public String authorize(String clientId,String redirectUrl) {
        return "https://github.com/login/oauth/authorize?client_id=" + clientId + "&scope=user,public_repo";
    }

    @Override
    public String accessToken(String code,String clientId,String clientSecret,String redirectUrl) {
        return "https://github.com/login/oauth/access_token?client_id=" + clientId + "&client_secret=" + clientSecret + "&code=" + code;
    }

    @Override
    public String userInfo(String accessToken) {
        return "https://api.github.com/user?access_token=" + accessToken + "&scope=public_repo%2Cuser&token_type=bearer";
    }
}