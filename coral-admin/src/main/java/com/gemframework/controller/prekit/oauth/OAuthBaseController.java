package com.gemframework.controller.prekit.oauth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gemframework.model.entity.po.Member;
import com.gemframework.model.enums.ChannelType;
import com.gemframework.model.enums.StatusEnum;
import com.gemframework.model.enums.ThirdPartyPlat;
import com.gemframework.model.enums.WhetherEnum;
import com.gemframework.model.response.GiteeOAuthRespInfo;
import com.gemframework.model.response.GithubOAuthRespInfo;
import com.gemframework.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import weixin.popular.bean.user.User;
import java.util.Date;

import static com.gemframework.common.constant.GemConstant.System.DEF_PASSWORD;

@Slf4j
public class OAuthBaseController {

    @Autowired
    private MemberService memberService;

    public Member saveGiteeMember(GiteeOAuthRespInfo info){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account",info.getLogin());
        Member member = memberService.getOne(queryWrapper);
        if(member == null){
            member = new Member();
        }
        member.setAccount(info.getLogin());
        member.setPassword(DEF_PASSWORD);
        member.setAvatarUrl(info.getAvatarUrl());
        member.setNickname(info.getName());
        member.setBlog(info.getBlog());
        member.setRemark(info.getBio());
        member.setStatus(WhetherEnum.NO.getCode());
        member.setIsOauth(WhetherEnum.YES.getCode());
        member.setThirdParty(info.getThirdParty());
        member.setChannel(ChannelType.OAUTH.getCode());
        member.setLastLoginTime(new Date());
        memberService.saveOrUpdate(member);
        return member;
    }

    public Member saveGithubMember(GithubOAuthRespInfo info){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account",info.getLogin());
        Member member = memberService.getOne(queryWrapper);
        if(member == null){
            member = new Member();
        }
        member.setAccount(info.getLogin());
        member.setPassword(DEF_PASSWORD);
        member.setAvatarUrl(info.getAvatarUrl());
        member.setNickname(info.getName());
        member.setBlog(info.getBlog());
        member.setRemark(info.getBio());
        member.setStatus(WhetherEnum.NO.getCode());
        member.setIsOauth(WhetherEnum.YES.getCode());
        member.setThirdParty(info.getThirdParty());
        member.setChannel(ChannelType.OAUTH.getCode());
        member.setLastLoginTime(new Date());
        memberService.saveOrUpdate(member);
        return member;
    }

    public Member saveWechatMember(User info){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account",info.getOpenid());
        Member member = memberService.getOne(queryWrapper);
        if(member == null){
            member = new Member();
        }
        member.setAccount(info.getOpenid());
        member.setPassword(DEF_PASSWORD);
        member.setAvatarUrl(info.getHeadimgurl());
        member.setNickname(info.getNickname());
        member.setRemark(info.getRemark());
        member.setStatus(StatusEnum.NORMAL.getCode());
        member.setIsOauth(WhetherEnum.YES.getCode());
        member.setThirdParty(ThirdPartyPlat.WECHAT.getCode());
        member.setChannel(ChannelType.OAUTH.getCode());
        member.setLastLoginTime(new Date());
        memberService.saveOrUpdate(member);
        return member;
    }

}