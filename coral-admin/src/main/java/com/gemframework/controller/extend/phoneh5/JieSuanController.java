package com.gemframework.controller.extend.phoneh5;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gemframework.common.utils.GemBeanUtils;
import com.gemframework.constant.GemModules;
import com.gemframework.controller.prekit.BaseController;
import com.gemframework.model.common.BaseResultData;
import com.gemframework.model.entity.po.ContractInfo;
import com.gemframework.model.entity.po.MonthlyReport;
import com.gemframework.model.entity.po.User;
import com.gemframework.model.entity.vo.ContractInfoVo;
import com.gemframework.model.entity.vo.MonthlyReportVo;
import com.gemframework.service.ContractInfoService;
import com.gemframework.service.MonthlyReportService;
import com.gemframework.service.UserService;
import com.gemframework.utils.wxpaysdk.MyConfig;
import com.gemframework.utils.wxpaysdk.WXPay;
import com.gemframework.utils.wxpaysdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(GemModules.Extend.PATH_PRE+"/phoneh5/jiesuan")
public class JieSuanController extends BaseController {

    @Autowired
    private ContractInfoService contractInfoService;
    @Value("${wpost}")
    private String wpost;
    @Autowired
    private MonthlyReportService monthlyReportService;
    @Qualifier("shiroUserServiceImpl")
    @Autowired
    private UserService userService;


    @PostMapping("/getContractInfo")
    public BaseResultData getContractInfo(HttpServletRequest request){
        String contractId=(String)request.getSession().getAttribute("contractId");
        ContractInfoVo contractInfoVo=new ContractInfoVo();
        contractInfoVo.setContractId(contractId);
        QueryWrapper queryWrapper = makeQueryMaps(contractInfoVo);
        List<ContractInfo> list = contractInfoService.list(queryWrapper);

        return BaseResultData.SUCCESS(list.get(0));
    }

    @PostMapping("/payZhifu")
    public BaseResultData payZhifu(HttpServletRequest request) throws Exception {
        Map payMap=new HashMap();
        String contractId=(String)request.getSession().getAttribute("contractId");
        String opneId=(String)request.getSession().getAttribute("operId");
        ContractInfoVo contractInfoVo=new ContractInfoVo();
        contractInfoVo.setContractId(contractId);
        QueryWrapper queryWrapper = makeQueryMaps(contractInfoVo);
        List<ContractInfo> list = contractInfoService.list(queryWrapper);
        ContractInfo info=list.get(0);
        MyConfig config =new MyConfig();
        WXPay wxpay = new WXPay(config);
        String http=request.getScheme();
        String ip = request.getServerName();
        String notify=http+"://"+ip+"/extend/phoneh5/notify";
        Map<String, String> data = new HashMap<String, String>();
        data.put("body", info.getCustomerName()+"代理记账合同费");
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String ctId="h"+df.format(new Date())+(int)((Math.random()*9+1)*1000);
        String khip=getRemoteHost(request);
        int money=(int)(info.getAmountReceived().doubleValue()*100);
        data.put("openid",opneId);
        data.put("out_trade_no", ctId);
        data.put("device_info", "WEB");
        data.put("fee_type", "CNY");
        data.put("total_fee", money+"");
        data.put("spbill_create_ip", khip);
        data.put("notify_url", notify);
        data.put("trade_type", "JSAPI");  // 此处指定为扫码支付
        data.put("product_id", ctId);
        Map<String, String> resp=null;
        try {
             resp = wxpay.unifiedOrder(data);
            payMap.put("appId",resp.get("appid"));
            payMap.put("timeStamp", WXPayUtil.getCurrentTimestamp()+"");
            payMap.put("nonceStr", WXPayUtil.generateNonceStr());
            payMap.put("package", "prepay_id=" + resp.get("prepay_id"));
            payMap.put("signType", "HMAC-SHA256");

            String paySign = WXPayUtil.generateSignature(payMap,config.getKey() );
            payMap.put("paySign", paySign);
            System.out.println(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BaseResultData.SUCCESS(payMap);
    }

    public String getRemoteHost(javax.servlet.http.HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    }

    @GetMapping("/getInfo")
    public BaseResultData getInfo(HttpServletRequest request) {
        //String id="5";
        String id=(String)request.getSession().getAttribute("mothlyId");
        MonthlyReport info = monthlyReportService.getById(id);
        MonthlyReportVo entity = GemBeanUtils.copyProperties(info, MonthlyReportVo.class);
        User user=userService.getById(entity.getUserId());
        entity.setUserName(user.getRealname());
        User user1=userService.getById(entity.getShenheId());
        entity.setShenheName(user1.getRealname());
        SimpleDateFormat sf=new SimpleDateFormat("yyyy年MM月dd日");
        String time=sf.format(entity.getCreateTime());
        entity.setCreateTimeStr(time);
        return BaseResultData.SUCCESS(entity);
    }
}
