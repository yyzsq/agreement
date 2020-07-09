package com.gemframework.controller.prekit.oauth;

import com.alibaba.fastjson.JSONObject;
import com.gemframework.common.exception.GemException;
import com.gemframework.model.entity.po.Member;
import com.gemframework.oauth.impl.OauthWechatServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import weixin.popular.bean.sns.SnsToken;
import weixin.popular.bean.user.User;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("oauth/wechat")
public class OAuthWechatController extends OAuthBaseController{

    @Autowired
    private OauthWechatServiceImpl wechatService;



    @GetMapping(value = "")
    public String login() {
        return "redirect:" + wechatService.authorizeUri();
    }


    @GetMapping(value = "/callback")
    public String callback(HttpServletRequest request, Model model) {
        try {
            String code = request.getParameter("code");
            log.info("=================微信授权成功"+code);
            SnsToken token = wechatService.accessToken(code);
            User user = wechatService.userInfo(token.getAccess_token(),token.getOpenid());
            Member member = saveWechatMember(user);
            model.addAttribute("memberInfo", JSONObject.toJSON(member));
        }catch (GemException e){
            model.addAttribute("memberInfo", JSONObject.toJSON(e));
        }
        return "modules/prekit/demo/oauth";
    }
}