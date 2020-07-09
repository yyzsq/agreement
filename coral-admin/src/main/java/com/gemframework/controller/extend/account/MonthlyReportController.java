
package com.gemframework.controller.extend.account;

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
import com.gemframework.model.entity.po.*;
import com.gemframework.model.entity.vo.WechatNoticeVo;
import com.gemframework.model.enums.ErrorCode;
import com.gemframework.model.enums.OperateType;
import com.gemframework.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.gemframework.model.entity.vo.MonthlyReportVo;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

import static com.gemframework.common.constant.GemSessionKeys.CURRENT_USER_KEY;

/**
 * @Title: MonthlyReportController
 * @Date: 2020-05-15 09:24:01
 * @Version: v1.0
 * @Description: 月报表控制器
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.Extend.PATH_PRE+"/account/monthlyReport")
public class MonthlyReportController extends BaseController {

    @Value("${wpost}")
    private String wpost;
    @Value("${ym.http}")
    private String http;
    @Value("${ym.ip}")
    private String ip;
    @Value("${wx.appid}")
    private String appid;
    @Value("${wx.secret}")
    private String secret;
    private static final String moduleName = "月报表";

    @Autowired
    private MonthlyReportService monthlyReportService;
    @Autowired
    private CustomerResearchService customerResearchService;
    @Qualifier("shiroUserServiceImpl")
    @Autowired
    private UserService userService;
    @Autowired
    private WechatNoticeService wechatNoticeService;
    @Autowired
    private UserRolesService userRolesService;
    @Autowired
    private RoleService roleService;
    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("monthlyReport:page")
    public BaseResultData page(PageInfo pageInfo, MonthlyReportVo vo) {
        User userInfo=(User)SecurityUtils.getSubject().getSession().getAttribute(CURRENT_USER_KEY);
        List<UserRoles> userRoles=userRolesService.findByUserId(userInfo.getId());
        String jhFlag="1";
        String shFlag=vo.getShFlag();
        vo.setShFlag(null);
        if(userInfo.getDeptId()!=0) {
            if(StringUtils.equals(shFlag,"true")&&StringUtils.isNotBlank(vo.getUserId())){
                vo.setUserId(vo.getUserId() + "");
            }else{
                vo.setUserId(userInfo.getId() + "");
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        QueryWrapper queryWrapper = makeQueryMaps(vo);
        queryWrapper.lt("create_time",calendar.getTime());
        queryWrapper.orderByDesc("create_time");
        Page page = monthlyReportService.page(setOrderPage(pageInfo),queryWrapper);
        List list=new ArrayList();
        for(Object obj:page.getRecords()){
            MonthlyReport report=(MonthlyReport)obj;
            MonthlyReportVo entity = GemBeanUtils.copyProperties(report, MonthlyReportVo.class);
            User user=userService.getById(entity.getUserId());
            if(user!=null){
                entity.setUserName(user.getRealname());
            }
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("corporate_name",report.getCustomerName());
            CustomerResearch research=customerResearchService.getOne(wrapper);
            if(research!=null&&research.getPayTaxProperties()!=null){
                entity.setNsxzName(research.getPayTaxProperties());
            }
            if(entity.getShenheId()!=null){
                User user1=userService.getById(entity.getShenheId());
                entity.setShenheName(user1.getRealname());
            }
            list.add(entity);
        }
        return BaseResultData.SUCCESS(list,page.getTotal());
    }
    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("monthlyReport:list")
    public BaseResultData list(MonthlyReportVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = monthlyReportService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }

    /**
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    public BaseResultData save(@RequestBody MonthlyReportVo vo) throws ParseException {
        if(StringUtils.isNotBlank(vo.getCreateTimeStr())){
            vo.setCreateTime(new SimpleDateFormat("yyyy-MM-dd").parse(vo.getCreateTimeStr()));
            vo.setCreateTimeStr(null);
            vo.setUpdateTime(new Date());
        }else{
            vo.setCreateTime(new Date());
            vo.setUpdateTime(new Date());
        }
        vo.setStatus("0");
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("corporate_name",vo.getCustomerName());
        List<CustomerResearch> list=customerResearchService.list(wrapper);
        if(list.size()>0){
            vo.setCustomerId(list.get(0).getId()+"");
        }
        GemValidate(vo, SaveValidator.class);
        MonthlyReport entity = GemBeanUtils.copyProperties(vo, MonthlyReport.class);
        if(!monthlyReportService.save(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        return BaseResultData.SUCCESS(entity);
    }


    /**
     * 删除 & 批量刪除
     * @return
     */
    @Log(type = OperateType.ALTER,value = "删除"+moduleName)
    @PostMapping("/delete")
    @RequiresPermissions("monthlyReport:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) monthlyReportService.removeById(id);
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                    monthlyReportService.removeByIds(listIds);
            }
        }
        return BaseResultData.SUCCESS();
    }


    /**
     * 编辑
     * @return
     */
    @Log(type = OperateType.ALTER,value = "编辑"+moduleName)
    @PostMapping("/update")
    @RequiresPermissions("monthlyReport:update")
    public BaseResultData update(@RequestBody MonthlyReportVo vo) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("corporate_name",vo.getCustomerName());
        List<CustomerResearch> list=customerResearchService.list(wrapper);
        if(list.size()>0){
            vo.setCustomerId(list.get(0).getId()+"");
        }
        GemValidate(vo, UpdateValidator.class);
        MonthlyReport entity = GemBeanUtils.copyProperties(vo, MonthlyReport.class);
        if(!monthlyReportService.updateById(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        return BaseResultData.SUCCESS(entity);
    }


    /**
     * 获取用户信息ById
     * @return
     */
    @Log(type = OperateType.NORMAL,value = "查看"+moduleName)
    @GetMapping("/info")
    @RequiresPermissions("monthlyReport:info")
    public BaseResultData info(Long id) {
        MonthlyReport info = monthlyReportService.getById(id);
        MonthlyReportVo reportVo=GemBeanUtils.copyProperties(info,MonthlyReportVo.class);
        User user=userService.getById(reportVo.getUserId());
        if(user!=null){
            reportVo.setUserName(user.getRealname());
        }
        if(reportVo.getShenheId()!=null){
            User user1=userService.getById(reportVo.getShenheId());
            reportVo.setShenheName(user1.getRealname());
        }
        return BaseResultData.SUCCESS(reportVo);
    }


    @PostMapping("/shenhe")
    @RequiresPermissions("monthlyReport:shenhe")
    public BaseResultData shenhe(Long id){
        User user=(User) SecurityUtils.getSubject().getSession().getAttribute(CURRENT_USER_KEY);
        MonthlyReport entity=new MonthlyReport();
        entity.setId(id);
        entity.setStatus("1");
        entity.setShenheId(user.getId()+"");
        if(!monthlyReportService.updateById(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        return BaseResultData.SUCCESS();
    }

    @PostMapping("/wxtongzhi")
    @RequiresPermissions("monthlyReport:tongzhi")
    public BaseResultData wxtongzhi(Long id, HttpServletRequest request){
        MonthlyReport report=monthlyReportService.getById(id);
        System.out.println(report);
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("customer_name",report.getCustomerName().trim());
        queryWrapper.eq("status","1");
        List<WechatNotice> list=wechatNoticeService.list(queryWrapper);
        User user=userService.getById(report.getUserId());

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
             String info = EntityUtils.toString(resp.getEntity(), "utf-8");
             JSONObject obj = JSONObject.parseObject(info);
             System.out.println(obj);
            if (resp.getStatusLine().getStatusCode() == 200) {
                System.out.println(obj);
                accessToken = (String) obj.get("access_token");

                // 2、创建一个HttpPost请求
                HttpPost post = new HttpPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken);


                JSONObject jsonObject = new JSONObject();
                jsonObject.put("template_id", "wiDnDsSxLNJvIW9z_MP3G_nVA_DZpH0A3J-VM8S8C6o");
                String yuebao = http + "://" + ip + "/extend/phoneh5/yuebaoInfo.html?id=" + id;

                String jiesUrl = URLEncoder.encode(yuebao);
                String jiesSqUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6d826bb9ce90147c&redirect_uri=" + jiesUrl + "&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
                jsonObject.put("url", jiesSqUrl);
                JSONObject json = new JSONObject();
                json.put("first", JSONObject.parseObject("{'value':'尊敬的客户本月报税工作已完成，请点击查看最新的报表！'}"));
                json.put("keyword1", JSONObject.parseObject("{'value':'" + report.getCustomerName() + "'}"));
                json.put("keyword2", JSONObject.parseObject("{'value':'"+(report.getCreateTime().getMonth()+1)+"月账期'}"));
                json.put("keyword3", JSONObject.parseObject("{'value':'" + user.getRealname() + "'}"));
                DateFormat ymddf = new SimpleDateFormat("yyyy年MM月dd日");
                json.put("keyword4", JSONObject.parseObject("{'value':'" + ymddf.format(report.getUpdateTime()) + "'}"));
                json.put("remark",JSONObject.parseObject("{'value':'客服电话：0951-6828555'}"));
                System.out.println("执行list");
                System.out.println(list);
                for (WechatNotice notice : list) {
                    System.out.println(notice);
                    System.out.println("当前微信用户");

                    jsonObject.put("data", json);
                    jsonObject.put("touser", notice.getOperId());
                    System.out.println(jsonObject);
                    try {
                        StringEntity entity1 = new StringEntity(jsonObject.toString(), "UTF-8");
                        entity1.setContentEncoding("UTF-8");
                        entity1.setContentType("application/json");
                        post.setEntity(entity1);

                        // 7、执行post请求操作，并拿到结果
                        httpResponse = httpClient.execute(post);
                        // 获取结果实体
                        HttpEntity entity = httpResponse.getEntity();
                        System.out.println(entity);
                        if (entity != null) {
                            // 按指定编码转换结果实体为String类型
                            body = EntityUtils.toString(entity, "UTF-8");
                            JSONObject jsonObject1=(JSONObject) JSONObject.parse(body);
                            if(jsonObject1.get("errcode").toString().equals("0")){
                                MonthlyReport report1=new MonthlyReport();
                                report1.setId(report.getId());
                                report1.setStatus("3");
                                monthlyReportService.updateById(report1);
                            }
                            System.out.println(body);
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
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return BaseResultData.SUCCESS();
    }
}