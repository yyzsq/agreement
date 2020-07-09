
package com.gemframework.model.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemframework.model.enums.ErrorCode;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Title: BasicResult.java
 * @Package: com.gemframework.model
 * @Date: 2019/11/24 13:49
 * @Version: v1.0
 * @Description: 请求基类

 * @Author: zhangysh
 * @Copyright: Copyright (c) 2019 GemStudio
 * @Company: www.gemframework.com
 */

@Data
public class BaseRequest implements Serializable {


    private String access_token;
}
