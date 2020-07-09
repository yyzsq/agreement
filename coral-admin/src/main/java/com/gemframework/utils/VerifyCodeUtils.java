
package com.gemframework.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import static com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY;

@Slf4j
public class VerifyCodeUtils {
    /**
     * 将获取到的前端参数转为string类型
     * @param request
     * @param key
     * @return
     */
    public static String getString(HttpServletRequest request, String key) {
        try {
            String result = request.getParameter(key);
            if(result != null) {
                result = result.trim();
            }
            if("".equals(result)) {
                result = null;
            }
            return result;
        }catch(Exception e) {
            return null;
        }
    }
    /**
     * 验证码校验
     * @param request
     * @return
     */
    public static boolean checkVerifyCode(HttpServletRequest request) {
        String verifyCodeExpected = "";
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equalsIgnoreCase("JSESSIONID")){
                    //获取生成的验证码
                    verifyCodeExpected = (String) request.getSession().getAttribute(KAPTCHA_SESSION_KEY+":"+cookie.getValue());
                }
            }
        }
        //获取用户输入的验证码
        String verifyCodeActual = VerifyCodeUtils.getString(request, "validCode");
        if(verifyCodeActual == null ||!verifyCodeActual.equalsIgnoreCase(verifyCodeExpected)) {
            return false;
        }
        return true;
    }
}