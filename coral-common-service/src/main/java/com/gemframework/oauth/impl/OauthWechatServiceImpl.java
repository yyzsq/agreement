package com.gemframework.oauth.impl;
import com.gemframework.common.constant.DictionaryKeys;
import com.gemframework.model.enums.WhetherEnum;
import com.gemframework.service.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.SnsToken;
import weixin.popular.bean.user.User;

import static com.gemframework.common.constant.GemConstant.Lang.CN;

@Slf4j
@Service
public class OauthWechatServiceImpl{

    @Autowired
    DictionaryService dictionaryService;

    public String getAppId(){
        String appid = dictionaryService.getMapValue(DictionaryKeys.WECHAT_MP_CFG.KEY,DictionaryKeys.WECHAT_MP_CFG.APPID_KEY);
        return appid;
    }
    public String getAppSecret(){
        String secret = dictionaryService.getMapValue(DictionaryKeys.WECHAT_MP_CFG.KEY,DictionaryKeys.WECHAT_MP_CFG.SECRET_KEY);
        return secret;
    }
    public String getRedirectUri(){
        String secret = dictionaryService.getMapValue(DictionaryKeys.WECHAT_MP_CFG.KEY,DictionaryKeys.WECHAT_MP_CFG.REDIRECT_URI_KEY);
        return secret;
    }


    /**
     * Oauth鉴权
     * @return
     */
    public String authorizeUri() {
        String param = "";
        return SnsAPI.connectOauth2Authorize(getAppId(),getRedirectUri(),true,param);
    }

    /**
     * 获取微信accessToken
     * @param code
     * @return
     */
    public SnsToken accessToken(String code) {
        SnsToken snsToken = SnsAPI.oauth2AccessToken(getAppId(),getAppSecret(),code);
        return snsToken;
    }

    /**
     * 返回微信用户信息
     * @param accessToken
     * @param openId
     * @return
     */
    public User userInfo(String accessToken, String openId) {
        User user = SnsAPI.userinfo(accessToken,openId, CN, WhetherEnum.YES.getCode());
        return user;
    }
}