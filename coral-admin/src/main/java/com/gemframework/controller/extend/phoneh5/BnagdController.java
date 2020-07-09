package com.gemframework.controller.extend.phoneh5;

import com.gemframework.constant.GemModules;
import com.gemframework.controller.prekit.BaseController;
import com.gemframework.model.common.BaseResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(GemModules.Extend.PATH_PRE+"/phoneh5/bangd")
public class BnagdController extends BaseController {

    @PostMapping("/save")
    public BaseResultData saveInfo(){
        return null;
    }

}
