
package com.gemframework.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Title: DictionaryMaps
 * @Date: 2020-04-16 23:32:51
 * @Version: v1.0
 * @Description: 字典项
 * @Author: gem
 * @Email: gemframe@163.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryMap {

    private String name;
    private String mapKey;
    private String mapVal;

}
