
package com.gemframework.common.utils;



import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

@Slf4j
public class GemIPHandler {

    public static void main(String[] args) {
        ipToAddress("171.217.63.114");
    }


    public static String ipToAddress(String ip){
        String resout = "";
        try{
            String str = getHTTPJson("http://ip.taobao.com/service/getIpInfo.php?ip="+ip);
            System.out.println(str);
            JSONObject obj = JSONObject.parseObject(str);
            JSONObject obj2 =  (JSONObject) obj.get("data");
            Integer code = (Integer) obj.get("code");
            if(code == 0){
                String counntry = (((String)obj2.get("country")).equals("")||
                        ((String)obj2.get("country")).equalsIgnoreCase("xx"))?"":(String)obj2.get("country")+",";
                String area = (((String)obj2.get("area")).equals("")||
                        ((String)obj2.get("area")).equalsIgnoreCase("xx"))?"":(String)obj2.get("area")+",";
                String city = (((String)obj2.get("city")).equals("")||
                        ((String)obj2.get("city")).equalsIgnoreCase("xx"))?"":(String)obj2.get("city")+",";
                String isp = (((String)obj2.get("isp")).equals("")||
                        ((String)obj2.get("isp")).equalsIgnoreCase("xx"))?"":(String)obj2.get("isp")+",";
                resout =  counntry+area+city+isp;
                resout = resout.substring(0,resout.length()-1);
            }else{
                resout =  "IP地址有误";
            }
        }catch(Exception e){
            log.error("获取IP地址异常："+e.getMessage());
            resout = "XX,XX";
        }
        log.debug("result: " + resout);
        return resout;
    }

    /**
     * 通过Get请求获取JSON返回
     * @param urlStr
     * @return
     */
    public static String getHTTPJson(String urlStr) {
        try
        {// 获取HttpURLConnection连接对象
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            // 设置连接属性
            httpConn.setConnectTimeout(3000);
            httpConn.setDoInput(true);
            httpConn.setRequestMethod("GET");
            // 获取相应码
            int respCode = httpConn.getResponseCode();
            if (respCode == 200)
            {
                return ConvertStream2Json(httpConn.getInputStream());
            }
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    private static String ConvertStream2Json(InputStream inputStream) {
        String jsonStr = "";
        // ByteArrayOutputStream相当于内存输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        // 将输入流转移到内存输出流中
        try
        {
            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1)
            {
                out.write(buffer, 0, len);
            }
            // 将内存流转换为字符串
            jsonStr = new String(out.toByteArray());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return jsonStr;
    }

    /**
     * 获取客户端IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }

        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }


    /**
     * 获取客户端主机名称
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
        }
        return "未知";
    }
}