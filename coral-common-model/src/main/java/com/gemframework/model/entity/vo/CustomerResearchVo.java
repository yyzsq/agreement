
package com.gemframework.model.entity.vo;

import com.gemframework.model.common.BaseEntityVo;
import com.gemframework.model.entity.po.UploadFile;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Title: CustomerResearchVo
 * @Date: 2020-05-10 17:39:43
 * @Version: v1.0
 * @Description: 客户信息表VO对象
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
public class CustomerResearchVo extends BaseEntityVo {

    /**
     *
     */
    private String corporateName;
    /**
     *
     */
    private String corporateId;
    /**
     *
     */
    private String corporationName;
    /**
     *
     */
    private String phone;

    private String personCharge;

    private String payTaxProperties;

    private String declareType;
    /**
     *
     */
    private String phone2;
    /**
     *
     */
    private String address;

    private String isAddres;

    private String addresDetail;

    private String mainBusiness;
    /**
     *
     */
    private String script;
    /**
     *
     */
    private String copy;
    /**
     *
     */
    private String taxInfo;
    /**
     *
     */
    private String enclosure;
    /**
     *
     */
    private String isBill;
    /**
     *
     */
    private String levyFees;

    private String services;

    private String serviceName;
    /**
     *
     */
    private String other1;
    /**
     *
     */
    private String other2;

    private String fujianList;

    private List<UploadFile> fjList;

    private String yuanjianList;
    private List<UploadFile> yjList;

    private String fuyinjList;
    private List<UploadFile> fyjList;

    private List contractList;

    private List businessList;

    private List serviceList;

    private Map serviceMap;

}
