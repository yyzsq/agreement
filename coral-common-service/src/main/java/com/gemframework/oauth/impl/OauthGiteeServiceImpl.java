package com.gemframework.oauth.impl;

import com.alibaba.fastjson.JSONObject;
import com.gemframework.common.constant.DictionaryKeys;
import com.gemframework.common.exception.GemException;
import com.gemframework.oauth.BaseOauthService;
import com.gemframework.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OauthGiteeServiceImpl implements BaseOauthService {

    @Autowired
    OauthGitee oauthGitee;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    DictionaryService dictionaryService;


    public String getClientId(){
        String appid = dictionaryService.getMapValue(DictionaryKeys.GITEE_CLIENT_CFG.KEY,DictionaryKeys.GITEE_CLIENT_CFG.CLIENT_APPID_KEY);
        return appid;
    }
    public String getClientSecret(){
        String secret = dictionaryService.getMapValue(DictionaryKeys.GITEE_CLIENT_CFG.KEY,DictionaryKeys.GITEE_CLIENT_CFG.CLIENT_SECRET_KEY);
        return secret;
    }
    public String getRedirectUri(){
        String redirectUri = dictionaryService.getMapValue(DictionaryKeys.GITEE_CLIENT_CFG.KEY,DictionaryKeys.GITEE_CLIENT_CFG.REDIRECT_URI_KEY);
        return redirectUri;
    }

    @Override
    public String authorizeUri() {
        return oauthGitee.authorize(getClientId(),getRedirectUri());
    }

    @Override
    public String getAccessToken(String code) {
        String token = oauthGitee.accessToken(code,getClientId(),getClientSecret(),getRedirectUri());
        ResponseEntity<Object> entity = null;
        try{
            entity = restTemplate.postForEntity(token, httpEntity(), Object.class);
        }catch (Exception e){
            throw new GemException("拒绝访问");
        }

        Object body = entity.getBody();
        assert body != null;
        String string = body.toString();
        String[] split = string.split("=");
        String accessToken = split[1].toString().split(",")[0];
        return accessToken;
    }

    @Override
    public JSONObject getUserInfo(String accessToken) {
        String userInfo = oauthGitee.userInfo(accessToken);
        ResponseEntity<JSONObject> forEntity = restTemplate.exchange(userInfo, HttpMethod.GET, httpEntity(), JSONObject.class);
        JSONObject body = forEntity.getBody();
        return body;
    }

    public static HttpEntity httpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);
        return request;
    }
}