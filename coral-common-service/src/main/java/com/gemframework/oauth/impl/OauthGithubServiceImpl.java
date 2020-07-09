package com.gemframework.oauth.impl;

import com.alibaba.fastjson.JSONObject;
import com.gemframework.common.constant.DictionaryKeys;
import com.gemframework.oauth.BaseOauthService;
import com.gemframework.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class OauthGithubServiceImpl implements BaseOauthService {

    @Autowired
    OauthGithub oauthGithub;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    DictionaryService dictionaryService;


    public String getClientId(){
        String appid = dictionaryService.getMapValue(DictionaryKeys.GITHUB_CLIENT_CFG.KEY,DictionaryKeys.GITHUB_CLIENT_CFG.CLIENT_ID_KEY);
        return appid;
    }
    public String getClientSecret(){
        String secret = dictionaryService.getMapValue(DictionaryKeys.GITHUB_CLIENT_CFG.KEY,DictionaryKeys.GITHUB_CLIENT_CFG.CLIENT_SECRET_KEY);
        return secret;
    }

    @Override
    public String authorizeUri() {
        return oauthGithub.authorize(getClientId(),null);
    }

    @Override
    public String getAccessToken(String code) {
        String token = oauthGithub.accessToken(code,getClientId(),getClientSecret(),null);
        ResponseEntity<Object> forEntity = restTemplate.exchange(token, HttpMethod.GET, httpEntity(), Object.class);
        String[] split = Objects.requireNonNull(forEntity.getBody()).toString().split("=");
        String accessToken = split[1].split(",")[0];
        return accessToken;
    }

    @Override
    public JSONObject getUserInfo(String accessToken) {
        String userInfo = oauthGithub.userInfo(accessToken);
        ResponseEntity<JSONObject> entity = restTemplate.exchange(userInfo, HttpMethod.GET, httpEntity(), JSONObject.class);
        JSONObject body = entity.getBody();
        return body;
    }

    public static HttpEntity httpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);
        return request;
    }
}