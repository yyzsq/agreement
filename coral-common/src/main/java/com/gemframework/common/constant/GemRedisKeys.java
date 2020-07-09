
package com.gemframework.common.constant;

public interface GemRedisKeys {

	//auth相关
	interface Auth{
		String PREFIX = "auth:";

		String USER_ROLES = PREFIX+"user_roles";
		String USER_RIGHTS = PREFIX+"user_rights";
	}

	//MQ
	interface Queue{
		String PREFIX = "queue:";

		String LOG_SYNC_DB = PREFIX+"log_sync_db";
		String LOG_SYNC_DB_SAVE = PREFIX+"log_sync_db_save";
	}
}
