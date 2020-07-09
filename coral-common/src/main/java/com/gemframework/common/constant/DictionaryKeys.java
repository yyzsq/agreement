
package com.gemframework.common.constant;

public interface DictionaryKeys {

	String DATABASE_TYPE = "DATABASE_TYPE";


	interface GITEE_CLIENT_CFG{
		String KEY = "GITEE_CLIENT_CFG";
		String CLIENT_APPID_KEY = "CLIENT_APPID";
		String CLIENT_SECRET_KEY = "CLIENT_SECRET";
		String REDIRECT_URI_KEY = "REDIRECT_URI";
	}

	interface WECHAT_MP_CFG{
		String KEY = "WECHAT_MP_CFG";
		String APPID_KEY = "APPID";
		String SECRET_KEY = "SECRET";
		String REDIRECT_URI_KEY = "REDIRECT_URI";
	}

	interface GITHUB_CLIENT_CFG{
		String KEY = "GITHUB_CLIENT_CFG";
		String CLIENT_ID_KEY = "CLIENT_ID";
		String CLIENT_SECRET_KEY = "CLIENT_SECRET";
	}

	interface OSS_ALIYUN_CFG{
		String KEY = "OSS_ALIYUN_CFG";
		String ENDPOINT = "ENDPOINT";
		String ACCESS_KEY_ID = "ACCESS_KEY_ID";
		String ACCESS_KEY_SECRET = "ACCESS_KEY_SECRET";
		String CDN_HOST = "CDN_HOST";
		//远程文件路径
		String REMOTE_FILE_PATH = "REMOTE_FILE_PATH";
	}

	interface OSS_QINIUYUN_CFG{
		String KEY = "OSS_QINIUYUN_CFG";
	}

}

