package com.gemframework.model.response;

import com.gemframework.model.enums.ThirdPartyPlat;
import lombok.Data;

@Data
public class GithubOAuthRespInfo extends GitXOAuthRespInfo {

    //自定义属性
    private Integer thirdParty = ThirdPartyPlat.GITHUB.getCode();
}