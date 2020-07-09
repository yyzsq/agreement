package com.gemframework.controller.prekit.oauth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gemframework.common.exception.GemException;
import com.gemframework.model.entity.po.Member;
import com.gemframework.model.response.GiteeOAuthRespInfo;
import com.gemframework.oauth.impl.OauthGiteeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("oauth/gitee")
public class OAuthGiteeController extends OAuthBaseController{


    @Autowired
    private OauthGiteeServiceImpl giteeService;


    @GetMapping(value = "")
    public String login() {
        return "redirect:" + giteeService.authorizeUri();
    }


    @GetMapping(value = "/callback")
    public String callback(HttpServletRequest request, Model model) {
        try {
            String code = request.getParameter("code");
            JSONObject userInfo = giteeService.getUserInfo(giteeService.getAccessToken(code));
            GiteeOAuthRespInfo info = JSON.parseObject(userInfo.toString(), GiteeOAuthRespInfo.class);
            Member member = saveGiteeMember(info);
            model.addAttribute("memberInfo", JSONObject.toJSON(member));
        }catch (GemException e){
            model.addAttribute("memberInfo", JSONObject.toJSON(e));
        }
        return "modules/prekit/demo/oauth";
    }
}