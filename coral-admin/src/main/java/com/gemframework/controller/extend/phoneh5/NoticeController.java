package com.gemframework.controller.extend.phoneh5;

import com.alibaba.fastjson.JSONObject;
import com.gemframework.annotation.Log;
import com.gemframework.common.utils.GemBeanUtils;
import com.gemframework.constant.GemModules;
import com.gemframework.controller.prekit.BaseController;
import com.gemframework.model.common.BaseResultData;
import com.gemframework.model.entity.po.MonthlyReport;
import com.gemframework.model.entity.po.User;
import com.gemframework.model.entity.vo.MonthlyReportVo;
import com.gemframework.model.enums.OperateType;
import com.gemframework.service.MonthlyReportService;
import com.gemframework.service.UserService;
import com.gemframework.utils.wxpaysdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@RequestMapping(GemModules.Extend.PATH_PRE+"/phoneh5")
public class NoticeController extends BaseController {


    @PostMapping("/notify")
    public void getPostStr(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        String line = "";
        StringBuffer inputString = new StringBuffer();
        try{
            while ((line = reader.readLine()) != null) {
                inputString.append(line);
            }
            request.getReader().close();
            System.out.println("----接收到的报文---"+inputString.toString());
            Map<String,String> map= WXPayUtil.xmlToMap(inputString.toString());
            //String appid=json.get("appid")+"";
            //String mch_id=json.get("mch_id")+"";
            if(map.get("return_code").equals("SUCCESS")){
                if(map.get("result_code").equals("SUCCESS")){
                    String transaction_id=map.get("transaction_id")+"";//微信支付订单号
                    String out_trade_no=map.get("out_trade_no")+"";//商户订单号
                    String openid=map.get("openid")+"";
                    String trade_type=map.get("trade_type")+"";
                    //接下来是做自己的业务处理
                    //开一个线程，更新订单状态
                    //WXPayFinishThread wxp=new WXPayFinishThread(openid,transaction_id,out_trade_no,trade_type);
                    //wxp.start();

                }
            }
            //告诉微信服务器，我收到信息了，不要在调用回调action了
            response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
            System.out.println("----结束---"+inputString.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
