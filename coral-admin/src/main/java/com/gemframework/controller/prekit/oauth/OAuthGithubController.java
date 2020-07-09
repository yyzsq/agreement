package com.gemframework.controller.prekit.oauth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gemframework.common.exception.GemException;
import com.gemframework.model.entity.po.Member;
import com.gemframework.model.response.GithubOAuthRespInfo;
import com.gemframework.oauth.impl.OauthGithubServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("oauth/github")
public class OAuthGithubController extends OAuthBaseController{

    @Autowired
    private OauthGithubServiceImpl githubService;


    @GetMapping(value = "")
    public String login() {
        return "redirect:" + githubService.authorizeUri();
    }


    @GetMapping(value = "/callback")
    public String callback(HttpServletRequest request, Model model) {
        try {
            String code = request.getParameter("code");
            JSONObject userInfo = githubService.getUserInfo(githubService.getAccessToken(code));
            GithubOAuthRespInfo info = JSON.parseObject(userInfo.toString(), GithubOAuthRespInfo.class);
            Member member = saveGithubMember(info);
            model.addAttribute("memberInfo", JSONObject.toJSON(member));
        }catch (GemException e){
            model.addAttribute("memberInfo", JSONObject.toJSON(e));
        }
        return "modules/prekit/demo/oauth";
    }
}