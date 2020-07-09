
package com.gemframework.controller.prekit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gemframework.controller.extend.phoneh5.JieSuanController;
import com.gemframework.controller.extend.phoneh5.WechatNoticeController;
import com.gemframework.model.entity.po.WechatNotice;
import com.gemframework.model.entity.vo.WechatNoticeVo;
import com.gemframework.service.WechatNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * @Title: ViewController
 * @Package: com.gemframework.controller
 * @Date: 2020-03-15 20:38:15
 * @Version: v1.0
 * @Description: 获取页面路径控制器
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Controller
public class ViewController extends BaseController {

	//===============内置模块路径=======================

	private static final String DEFAULT_CHARSET = "UTF-8";
	@Value("${wx.appid}")
	private String appid;
	@Value("${wx.secret}")
	private String secret;
	@Autowired
	private WechatNoticeService wechatNoticeService;

	@RequestMapping("prekit/{url}.html")
	public String prekit(@PathVariable("url") String url){
		return "modules/prekit/" + url;
	}

	@RequestMapping("prekit/{module}/{url}.html")
	public String prekit(@PathVariable("module") String module,@PathVariable("url") String url){
		return "modules/prekit/"+module+"/" + url;
	}

	//===============扩展模块路径=======================

	@RequestMapping("extend/{url}.html")
	public String extend(@PathVariable("url") String url){
		return "modules/extend/" + url;
	}

	@RequestMapping("extend/{module}/{url}.html")
	public String extend(@PathVariable("module") String module, @PathVariable("url") String url, HttpServletRequest request){
		if(url.equals("bangd")||url.equals("payInfo")||url.equals("yuebaoInfo")) {

			String openid=null;
			String code = request.getParameter("code");
			String urlInfo = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code";
			try {
				CloseableHttpClient client = null;

				client = HttpClients.createDefault();
				HttpGet httpGet = new HttpGet(urlInfo);
				HttpResponse resp = null;
				resp = client.execute(httpGet);
				if (resp.getStatusLine().getStatusCode() == 200) {
					String info = EntityUtils.toString(resp.getEntity(), "UTF-8");
					JSONObject obj = JSONObject.parseObject(info);
					 openid = (String) obj.get("openid");
					request.getSession().setAttribute("operId",openid);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			if(url.equals("bangd")) {
				if(openid!=null) {
					WechatNoticeVo wechatNotice = new WechatNoticeVo();
					wechatNotice.setOperId(openid);
					QueryWrapper queryWrapper = makeQueryMaps(wechatNotice);
					List<WechatNotice> list = wechatNoticeService.list(queryWrapper);
					if (list.size() > 0) {
						url = "bangdSuccess";
					}
				}
			}else if(url.equals("payInfo")){
				String contractId=request.getParameter("contractId");
				request.getSession().setAttribute("contractId",contractId);
			}else if(url.equals("yuebaoInfo")){
				String id=request.getParameter("id");
				request.getSession().setAttribute("mothlyId",id);
			}
		}
			return "modules/extend/"+module+"/" + url;
	}

	//===============错误相关=======================
	@GetMapping(value = "error/{url}.html")
	public String error(@PathVariable("url") String url) {
		return "error/"+url;
	}


	//===============登录相关=======================

	@GetMapping("login")
	public String login(){
		return "login";
	}

	@GetMapping("login1")
	public String login1(){
		return "login1";
	}

	@GetMapping({"/", "index"})
	public String index(){
		return "index";
	}

	@GetMapping(value = "/home")
	public String home() {
		return "home";
	}


}
