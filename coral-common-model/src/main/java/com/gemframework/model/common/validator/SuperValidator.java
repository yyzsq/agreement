
package com.gemframework.model.common.validator;

import javax.validation.GroupSequence;

/**
 * @Title: SuperValidator
 * @Package: com.gemframework.model.common.validator
 * @Date: 2020-03-22 11:11:52
 * @Version: v1.0
 * @Description: 定义校验顺序，一旦有校验失败就中断
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@GroupSequence({SaveValidator.class, UpdateValidator.class})
public interface SuperValidator {

}
