package com.gemframework.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import weixin.popular.util.JsUtil;


public class WxShareConfig {
	// {"appId":"wx9153920ddc198b74","nonceStr":"tU7Ptb9iXFM6wKOb","timestamp":"1423125240","signature":"cbc03d197c2ea111e3048b2d2191fc827e1723d6"}

	private String timestamp = "";
	private String appId = "";
	private String nonceStr = "";
	private String signature = "";

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * 
	 * @param jsapi_ticket
	 * @param url 不能encode
	 * @return
	 */
	 public static Map<String, String> sign(String jsapi_ticket, String url) {
	        Map<String, String> ret = new HashMap<String, String>();
	        String nonce_str = UUID.randomUUID().toString();
	        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
	        String signature = JsUtil.generateConfigSignature(nonce_str, jsapi_ticket, timestamp, url);
	        
	        ret.put("url",url);
	        ret.put("nonceStr", nonce_str);
	        ret.put("timestamp", timestamp);
	        ret.put("signature", signature);
	        return ret;
	    }
}
