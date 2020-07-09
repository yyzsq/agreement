
package com.gemframework.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gemframework.model.entity.po.UploadFile;
import org.springframework.stereotype.Repository;

/**
 * @Title: UploadFileMapper
 * @Date: 2020-05-16 14:13:04
 * @Version: v1.0
 * @Description: 关联图片地址持久层
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Repository
public interface UploadFileMapper extends BaseMapper<UploadFile> {

}