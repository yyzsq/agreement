/**   
 * @Title: User.java 
 * @Package com.navict.cloudwifi.model.po 
 * @Description: WifiUser对象
 * Copyright: Copyright (c) 2014
 * Company: Navict(Beijing) Information Consulting Co.,Ltd.
 *
 * @author Sean.P   
 * @date 2014年6月7日 下午7:04:43 
 * @version V1.0
 */
package com.gemframework.service.queue;

import com.gemframework.common.queue.GemQueueMessage;

import java.util.Map;

/**
 * @ClassName: MAP
 * @Description: MAP消息对象
 * @author zhangys
 */
public class MapQueueMessage implements GemQueueMessage<Map<String,Object>> {

	private Map<String,Object> data;

	@Override
	public Map<String, Object> getData() {
		return data;
	}

	@Override
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
