
package com.gemframework.controller.prekit;

import com.gemframework.common.exception.GemException;
import com.gemframework.model.enums.ErrorCode;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping(value = "/kaptcha")
public class KaptchaController {

	/* 注入Kaptcha */
    @Resource
    private DefaultKaptcha defaultKaptcha;

    @GetMapping(value = "/code")
    public void defaultKaptcha(HttpServletRequest request, HttpServletResponse response){
        byte[] captchaChallengeAsJpeg;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        /**
         * 生成验证码字符串并保存到session中
         */
        String createText = defaultKaptcha.createText();
        HttpSession session = request.getSession();
        Cookie[] cookies = request.getCookies();
        if (cookies!=null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equalsIgnoreCase("JSESSIONID")){
                    session.setAttribute(KAPTCHA_SESSION_KEY+":"+cookie.getValue(), createText);
                }
            }
        }
        /**
         * 使用生成的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
         */
        BufferedImage challenge = defaultKaptcha.createImage(createText);
        try {
            ImageIO.write(challenge,"jpg",jpegOutputStream);
        } catch (IOException e) {
            log.error("生成图形验证码失败",e);
            // 抛出异常，可以不用关心
            throw new GemException(ErrorCode.CRATE_IMAGE_ERROR);
        }
        /**
         * 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
         */
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires",0);
        response.setContentType("image/jpeg");
        try {
            ServletOutputStream servletOutputStream = response.getOutputStream();
            servletOutputStream.write(captchaChallengeAsJpeg);
            servletOutputStream.flush();
            servletOutputStream.close();
        } catch (IOException e) {
            log.error("输出验证码失败",e);
            // 抛出异常，可以不用关心
            throw new GemException(ErrorCode.CRATE_IMAGE_ERROR);
        }
    }
}