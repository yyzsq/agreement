package com.gemframework.oauth;

import com.alibaba.fastjson.JSONObject;

public interface BaseOauthService {

    String authorizeUri();

    String getAccessToken(String code);

    JSONObject getUserInfo(String accessToken);

}