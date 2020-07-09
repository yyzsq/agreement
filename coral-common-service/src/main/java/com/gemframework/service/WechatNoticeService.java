
package com.gemframework.service;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gemframework.model.entity.po.CustomerResearch;
import com.gemframework.model.entity.po.WechatNotice;
import com.gemframework.service.impl.CustomerResearchServiceImpl;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * @Title: WechatNoticeService
 * @Date: 2020-05-13 11:57:21
 * @Version: v1.0
 * @Description: 微信通知绑定服务接口
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
public interface WechatNoticeService extends IService<WechatNotice> {

    public List<WechatNotice> findList(String customerId);

}