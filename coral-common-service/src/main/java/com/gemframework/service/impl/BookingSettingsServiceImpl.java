
package com.gemframework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.BookingSettingsMapper;
import com.gemframework.model.entity.po.BookingSettings;
import com.gemframework.service.BookingSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Title: BookingSettingsServiceImpl
 * @Date: 2020-05-11 11:42:02
 * @Version: v1.0
 * @Description: 会计星级服务实现类
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Service
public class BookingSettingsServiceImpl extends ServiceImpl<BookingSettingsMapper, BookingSettings> implements BookingSettingsService {

}