
package com.gemframework.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.WechatNoticeMapper;
import com.gemframework.model.entity.po.*;
import com.gemframework.model.entity.vo.WechatNoticeVo;
import com.gemframework.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Title: WechatNoticeServiceImpl
 * @Date: 2020-05-13 11:57:21
 * @Version: v1.0
 * @Description: 微信通知绑定服务实现类
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Service
public class WechatNoticeServiceImpl extends ServiceImpl<WechatNoticeMapper, WechatNotice> implements WechatNoticeService {

    @Value("${wx.appid}")
    private String appid;
    @Value("${wx.secret}")
    private String secret;
    private String accessTokens="";
    @Value("${wpost}")
    private String wpost;
    @Value("${ym.http}")
    private String http;
    @Value("${ym.ip}")
    private String ip;

    @Autowired
    private ContractInfoService contractInfoService;
    @Autowired
    private CollectionInfoService collectionInfoService;
    @Autowired
    private CustomerResearchService researchService;
    @Autowired
    private WechatNoticeMapper wechatNoticeMapper;
    @Qualifier("shiroUserServiceImpl")
    @Autowired
    private UserService userService;
    @Autowired
    private WechatJiluService wechatJiluService;

    @Scheduled(cron = "0 0 9 * * ?")
    //@Scheduled(cron = "0 */1 * * * ?")
    public void testAsyncJob1( ) {

        // 返回body
        String body = null;
        SSLContext sslcontext = SSLContexts.createDefault();
        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1.2" },
                null,SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", factory).build();  // 用来配置支持的协议
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setMaxTotal(10);
        connectionManager.setDefaultMaxPerRoute(10);
        // 获取连接客户端工具
        CloseableHttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connectionManager).setConnectionManagerShared(true).build();
        CloseableHttpResponse httpResponse = null;
        String accessToken = null;
        String urlInfo = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;
        CloseableHttpClient client = null;
        try {

            client = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(urlInfo);
            HttpResponse resp = client.execute(httpGet);
            if (resp.getStatusLine().getStatusCode() == 200) {
                String info = EntityUtils.toString(resp.getEntity(), "utf-8");
                JSONObject obj = JSONObject.parseObject(info);
                accessToken = (String) obj.get("access_token");

                // 2、创建一个HttpPost请求
                HttpPost post = new HttpPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken);
                // 5、设置header信息
                /**header中通用属性*/

                //获取所有到期并发送
                //List<CollectionInfo> list=collectionInfoService.list();
                List<ContractInfo> list=contractInfoService.list();
                DateFormat df = new SimpleDateFormat("yyyyMMdd");
                DateFormat ymddf = new SimpleDateFormat("yyyy年MM月dd日");
                String endTime=null;
                String endTime2=null;
                String endTime3=null;
                String startTime = df.format(new Date());
                for(ContractInfo contractInfo:list){
                    QueryWrapper wrapper=new QueryWrapper();
                    wrapper.eq("customer_name",contractInfo.getCustomerName());
                    wrapper.orderByDesc("service_end_time");
                    List<ContractInfo> list1=contractInfoService.list(wrapper);
                    if(list1.size()>1){
                        contractInfo=list1.get(0);
                    }
                    WechatJilu wechatJilu=new WechatJilu();
                    //CustomerResearch research=researchService.getById(contractInfo.getCustomerId());
                    wechatJilu.setContractId(contractInfo.getContractId());
                    wechatJilu.setFsTime(new Date());
                    //获取前30天
                    Calendar calc =Calendar.getInstance();
                    calc.setTime(contractInfo.getServiceTime());
                    calc.add(calc.DATE, -30);
                    Date minDate = calc.getTime();
                    endTime = df.format(minDate);
                    //获取前7天
                    calc.setTime(contractInfo.getServiceTime());
                    calc.add(calc.DATE, -7);
                    minDate = calc.getTime();
                    endTime2 = df.format(minDate);
                    //获取后15天
                    calc.setTime(contractInfo.getServiceTime());
                    calc.add(calc.DATE, +15);
                    minDate = calc.getTime();
                    endTime3 = df.format(minDate);
                    JSONObject jsonObject = new JSONObject();
                    WechatNoticeVo vo=new WechatNoticeVo();
                    List<WechatNotice> voList = this.findList(contractInfo.getCustomerName());
                    if(voList.size()==0){
                        continue;
                    }

                    jsonObject.put("template_id", "SJxbl7WbN5RuvNEcN17NEALspUplQ4sD72Txnnj4leY");
                    //String http=request.getScheme();
                    //String ip = request.getServerName();
                    String jiesuan=http+"://"+ip+"/extend/phoneh5/payInfo.html?contractId="+contractInfo.getContractId();
                    String jiesUrl= URLEncoder.encode(jiesuan);
                    String jiesSqUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6d826bb9ce90147c&redirect_uri="+jiesUrl+"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
                    jsonObject.put("url",jiesSqUrl);
                    JSONObject json=new JSONObject();
//                    infoJson.put("value","代理记账");
                    json.put("keyword1",JSONObject.parseObject("{'value':'代理记账'}"));
                    json.put("keyword2",JSONObject.parseObject("{'value':'"+contractInfo.getContractId()+"'}"));
                    json.put("keyword3",JSONObject.parseObject("{'value':'"+new SimpleDateFormat("yyyy-MM-dd").format(contractInfo.getServiceTime())+"','color':'red'}"));
                    for (WechatNotice notice:voList){
                        wechatJilu.setJsRen(notice.getName());
                        if(startTime.equals(endTime)){
                            wechatJilu.setFsType("0");
                            User user = userService.getById(contractInfo.getAccounting());
                            json.put("first",JSONObject.parseObject("{'value':'"+"温馨提示！尊敬的"+notice.getName()+"总：您好！贵公司（"+contractInfo.getCustomerName()+"公司）与我公司签约的《代理记账协议》于30日后到期，请您及时办理续约手续。祝您生意兴隆、财源广进！"+"'}"));
                            json.put("remark",JSONObject.parseObject("{'value':'"+"记账会计："+(user==null?"暂无":user.getRealname())+"  联系电话："+user==null?"暂无":user.getPhone()+"" +
                                    "客服电话：0951-6828555"+"'}"));


                        }else if(startTime.equals(endTime2)){
                            wechatJilu.setFsType("1");
                            User user = userService.getById(contractInfo.getAccounting());
                            json.put("first",JSONObject.parseObject("{'value':'"+"温馨提示！尊敬的"+notice.getName()+"总：您好！贵公司（"+contractInfo.getCustomerName()+"公司）与我公司签约的《代理记账协议》于7日后即将到期，希望您在百忙之中及时与我们办理续约手续，期待与您合作！"+"'}"));
                            json.put("remark",JSONObject.parseObject("{'value':'"+"记账会计："+(user==null?"暂无":user.getRealname())+"  联系电话："+(user==null?"暂无":user.getPhone())+"" +
                                    " 客服电话：0951-6828555"+"'}"));
                        }else if(startTime.equals(endTime3)){
                            wechatJilu.setFsType("2");
                            json.put("first",JSONObject.parseObject("{'value':'"+"尊敬的客户：您好！贵公司贵公司（"+contractInfo.getCustomerName()+"公司）与我公司签约的合同已经到期，所有纳税申报工作已经全部结束，为避免影响您公司的正常财务工作，即日起，请您于3日内联系我公司人员办理续约手续。否则，我公司将根据合同法规定，自动解除与贵公司的合作关系。自合同到期之日（"+ymddf.format(contractInfo.getServiceTime())+"）起，我公司将不负责贵公司一切事务，因此产生的未申报及罚款，我公司均不承担。敬请谅解！"+"'}"));
                            json.put("remark",JSONObject.parseObject("{'value':'客服电话：0951-6828555'}"));
                        }else{
                            continue;
                        }
                        jsonObject.put("data",json);
                        jsonObject.put("touser", notice.getOperId());
                        try {
                            StringEntity entity1 = new StringEntity(jsonObject.toString(), "UTF-8");
                            entity1.setContentEncoding("UTF-8");
                            entity1.setContentType("application/json");
                            post.setEntity(entity1);

                            // 7、执行post请求操作，并拿到结果
                            httpResponse = httpClient.execute(post);
                            // 获取结果实体
                            HttpEntity entity = httpResponse.getEntity();
                            if (entity != null) {
                                // 按指定编码转换结果实体为String类型
                                body = EntityUtils.toString(entity, "UTF-8");
                                JSONObject objInfo = JSONObject.parseObject(body);
                                if(objInfo.getString("errcode").equals("0")){
                                    wechatJilu.setFsStatus("1");
                                }else{
                                    wechatJilu.setFsStatus("0");
                                }
                                wechatJiluService.save(wechatJilu);
                            }
                            try {
                                httpResponse.close();
                                httpClient.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                if(httpResponse!=null)
                httpResponse.close();
                if(httpClient!=null)
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<WechatNotice> findList(String customerName){
       return  wechatNoticeMapper.findList(customerName);
    }
}