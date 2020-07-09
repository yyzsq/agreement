
package com.gemframework.controller.extend.phoneh5;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gemframework.annotation.Log;
import com.gemframework.common.utils.GemBeanUtils;
import com.gemframework.constant.GemModules;
import com.gemframework.controller.prekit.BaseController;
import com.gemframework.model.common.BaseResultData;
import com.gemframework.model.common.PageInfo;
import com.gemframework.model.common.validator.SaveValidator;
import com.gemframework.model.common.validator.UpdateValidator;
import com.gemframework.model.entity.po.CustomerResearch;
import com.gemframework.model.entity.vo.CustomerResearchVo;
import com.gemframework.model.enums.ErrorCode;
import com.gemframework.model.enums.OperateType;
import com.gemframework.service.CustomerResearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.gemframework.model.entity.po.WechatNotice;
import com.gemframework.model.entity.vo.WechatNoticeVo;
import com.gemframework.service.WechatNoticeService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Title: WechatNoticeController
 * @Date: 2020-05-13 11:57:21
 * @Version: v1.0
 * @Description: 微信通知绑定控制器
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.Extend.PATH_PRE + "/phoneh5/wechatNotice")
public class WechatNoticeController extends BaseController {

    public static Map wxMap=new HashMap();
    private static final String moduleName = "微信通知绑定";
    @Value("${wx.appid}")
    private String appid;
    @Value("${wx.secret}")
    private String secret;
    @Autowired
    private WechatNoticeService wechatNoticeService;
    @Autowired
    private CustomerResearchService customerResearchService;

    /**
     * 获取列表分页
     *
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("wechatNotice:page")
    public BaseResultData page(PageInfo pageInfo, WechatNoticeVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        queryWrapper.orderByDesc("id");
        Page page = wechatNoticeService.page(setOrderPage(pageInfo), queryWrapper);
        return BaseResultData.SUCCESS(page.getRecords(), page.getTotal());
    }

    /**
     * 获取列表
     *
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("wechatNotice:list")
    public BaseResultData list(WechatNoticeVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = wechatNoticeService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }

    /**
     * 添加
     *
     * @return
     */
    @Log(type = OperateType.ALTER, value = "保存" + moduleName)
    @PostMapping("/save")
    public BaseResultData save(@RequestBody WechatNoticeVo vo, HttpServletRequest request) {
        String operId=(String)request.getSession().getAttribute("operId");

        CustomerResearchVo researchVo = new CustomerResearchVo();
        researchVo.setCorporateName(vo.getCustomerName());
        QueryWrapper queryWrapper = makeQueryMaps(researchVo);
        List<CustomerResearch> list = customerResearchService.list(queryWrapper);
        if(list.size()>0) {
            vo.setCustomerId(list.get(0).getId() + "");
        }
        vo.setOperId(operId);
        vo.setStatus("0");
        GemValidate(vo, SaveValidator.class);
        WechatNotice entity = GemBeanUtils.copyProperties(vo, WechatNotice.class);
        if (!wechatNoticeService.save(entity)) {
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        return BaseResultData.SUCCESS(entity);
    }

    @Log(type = OperateType.ALTER, value = "保存" + moduleName)
    @PostMapping("/baocun")
    public BaseResultData baocun(@RequestBody WechatNoticeVo weVo,HttpServletRequest request) {
        String operId=(String)request.getSession().getAttribute("operId");

            CustomerResearchVo researchVo = new CustomerResearchVo();
            researchVo.setCorporateName(weVo.getCustomerName());
            QueryWrapper queryWrapper = makeQueryMaps(researchVo);
            List<CustomerResearch> list = customerResearchService.list(queryWrapper);
            if(list.size()>0) {
                weVo.setCustomerId(list.get(0).getId() + "");
            }
            weVo.setOperId(operId);
            weVo.setStatus("0");
            GemValidate(weVo, SaveValidator.class);
            WechatNotice entity = GemBeanUtils.copyProperties(weVo, WechatNotice.class);
            if (!wechatNoticeService.save(entity)) {
                return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
            }
        return BaseResultData.SUCCESS();
    }

    /**
     * 删除 & 批量刪除
     *
     * @return
     */
    @Log(type = OperateType.ALTER, value = "删除" + moduleName)
    @PostMapping("/delete")
    @RequiresPermissions("wechatNotice:delete")
    public BaseResultData delete(Long id, String ids) {
        if (id != null) wechatNoticeService.removeById(id);
        if (StringUtils.isNotBlank(ids)) {
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            if (listIds != null && !listIds.isEmpty()) {
                wechatNoticeService.removeByIds(listIds);
            }
        }
        return BaseResultData.SUCCESS();
    }


    /**
     * 编辑
     *
     * @return
     */
    @Log(type = OperateType.ALTER, value = "编辑" + moduleName)
    @PostMapping("/update")
    @RequiresPermissions("wechatNotice:update")
    public BaseResultData update(@RequestBody WechatNoticeVo vo) {
        GemValidate(vo, UpdateValidator.class);
        WechatNotice entity = GemBeanUtils.copyProperties(vo, WechatNotice.class);
        if (!wechatNoticeService.updateById(entity)) {
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        return BaseResultData.SUCCESS(entity);
    }

    @Log(type = OperateType.ALTER, value = "审核" + moduleName)
    @PostMapping("/shenhe")
    @RequiresPermissions("wechatNotice:shenhe")
    public BaseResultData shenhe(@RequestBody WechatNoticeVo vo) {
        GemValidate(vo, UpdateValidator.class);
        WechatNotice entity = GemBeanUtils.copyProperties(vo, WechatNotice.class);
        if (!wechatNoticeService.updateById(entity)) {
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        return BaseResultData.SUCCESS(entity);
    }

    /**
     * 获取用户信息ById
     *
     * @return
     */
    @Log(type = OperateType.NORMAL, value = "查看" + moduleName)
    @GetMapping("/info")
    public BaseResultData info(Long id) {
        WechatNotice info = wechatNoticeService.getById(id);
        return BaseResultData.SUCCESS(info);
    }

    @GetMapping("/getInfo")
    public BaseResultData getInfo(HttpServletRequest request){
        String operId=(String)request.getSession().getAttribute("operId");
        WechatNoticeVo vo=new WechatNoticeVo();
        vo.setOperId(operId);
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List<WechatNotice> info = wechatNoticeService.list(queryWrapper);
        return BaseResultData.SUCCESS(info);
    }

    @GetMapping("/getKehuInfo")
    public BaseResultData getKehuInfo(){
        Map map=new HashMap();
        List<CustomerResearch> list=customerResearchService.list();
        int number=0;
        for(CustomerResearch research:list){
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("customer_name",research.getCorporateName());
            int count=wechatNoticeService.count(queryWrapper);
            if(count>0){
                number+=1;
            }
        }
        map.put("zongkehu",list.size());
        map.put("ybd",number);
        return BaseResultData.SUCCESS(map);
    }
}