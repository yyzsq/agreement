package com.gemframework.oauth;

public interface BaseOauth {

    /**
     * 授权地址
     *
     * @return 授权地址
     */
    String authorize(String clietId,String redirectUrl);

    /**
     * 获取accessToken
     *
     * @param code 请求编码
     * @return accessToken
     */
    String accessToken(String code,String clientId,String clientSecret,String redirectUrl);

    /**
     * 获取用户信息
     *
     * @param accessToken token
     * @return user
     */
    String userInfo(String accessToken);
}