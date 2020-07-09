
package com.gemframework.model.common;

import com.gemframework.model.enums.SortType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {
    //页码
    private Long page;
    //限制条数
    private Long limit;
    //排序字段
    private String sort;
    //排序类型
    private SortType order;

    //总数
    private Long total;
    //结果集
    private List rows;

}
