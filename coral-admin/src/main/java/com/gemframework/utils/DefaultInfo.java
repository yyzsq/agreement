package com.gemframework.utils;

import org.thymeleaf.standard.expression.Fragment;

import java.util.HashMap;
import java.util.Map;

public class  DefaultInfo {
    public static final String NO_ONE = "1";
    public static final String NO_TWO = "2";
    public static final String NO_THREE = "3";
    public static final String NO_FOUR = "4";
    public static final String NO_FIVE = "5";

    //合同状态
    public final static Map<String, String> CONTRACT_STATUS = new HashMap<String, String>() {
        {
            put(NO_ONE, "新签");
            put(NO_TWO, "续签");
            put(NO_THREE, "停止服务");
            put(NO_FOUR, "注销合同");
        }
    };
    //新签
    //续签
    //服务中
    //服务完成
    //停止服务
    //终止合同
    //注销公司


    //收费标准
    public final static Map<String, String> CHARGING_STANDARD = new HashMap<String, String>() {
        {
            put(NO_ONE, "月");
            put(NO_TWO, "季");
            put(NO_THREE,"半年");
            put(NO_FOUR, "年");
        }
    };

    //合同性质
    public final static Map<String, String> CONTRACT_NATURE = new HashMap<String, String>() {
        {
            put(NO_ONE, "小规模");
            put(NO_TWO, "一般纳税人");
            put(NO_THREE, "个体户");
            put(NO_FOUR, "个体一般纳税人");
        }
    };

    //合同性质
    public final static Map<String, String> IS_BILL = new HashMap<String, String>() {
        {
            put(NO_ONE, "是");
            put(NO_TWO, "否");
        }
    };

    public final static Map<String, String> IMG_Type = new HashMap<String, String>() {
        {
            put(NO_ONE, "客户_客户原件");
            put(NO_TWO, "客户_客户复印件");
            put(NO_THREE, "客户_附件");
        }
    };

    public final static Map<String,String> fileType=new HashMap<String,String>(){
        {
            put("yyzz","营业执照");
            put("sfzzm","法人身份证复印件正面");
            put("sfzfm","法人身份证复印件反面");
            put("qyht","签约合同扫描件");
            put("fcz","房产证");
            put("zlht","租赁合同");
            put("fdsfz","房东身份证复印件");
        }
    };



}
