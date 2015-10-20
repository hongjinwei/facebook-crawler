package com.peony.facebook_crawler;

import com.peony.util.StringUtils;

/**
 * 系统属性，可以通过虚拟机参数设置全局参数
 * 
 * @author guor
 * @date 2015年3月15日上午9:47:16
 */
public class SystemProps {

	public static final String LOG_ENABLED = "log";

	public static final String CACHEABLE = "cacheable";

	public static final String STOREABLE = "storeable";

	public static final String STOREURL = "storeurl";

	public static final String TEST = "test";

	public static final String STORE_TO_SQL = "store_to_sql";

	/**
	 * set if test
	 * 
	 * @param b
	 *            boolean, if true, set isTest true
	 */
	public static void setTest(boolean b) {
		if (b) {
			System.setProperty(TEST, "true");
		} else {
			System.setProperty(TEST, "false");
		}
	}

	public static void setStoreToSql(boolean b) {
		if (b) {
			System.setProperty(STORE_TO_SQL, "true");
		} else {
			System.setProperty(STORE_TO_SQL, "false");
		}
	}

	public static boolean storeToSql() {
		String property = System.getProperty(STOREURL);
		if (StringUtils.isEmpty(property)) {
			return true;// 默认保存到sql
		}
		return StringUtils.parseBoolean(property, true);
	}

	/**
	 * set if store the url to memcache
	 * 
	 * @param b
	 */
	public static void setStoreUrl(boolean b) {
		if (b) {
			System.setProperty(STOREURL, "true");
		} else {
			System.setProperty(STOREURL, "false");
		}
	}

	/**
	 * set if store the page to doc server
	 * 
	 * @param b
	 */
	public static void setStoreToDocDB(boolean b) {
		if (b) {
			System.setProperty(STOREABLE, "true");
		} else {
			System.setProperty(STOREABLE, "false");
		}
	}

	public static boolean storeurl() {
		String property = System.getProperty(STOREURL);
		if (StringUtils.isEmpty(property)) {
			return true;// 默认保存url
		}
		return StringUtils.parseBoolean(property, true);
	}

	public static void setLogEnabled(boolean b) {
		if (b) {
			System.setProperty(LOG_ENABLED, "true");
		} else {
			System.setProperty(LOG_ENABLED, "false");
		}
	}

	public static boolean isTest() {
		String property = System.getProperty(TEST);
		if (StringUtils.isEmpty(property)) {
			return false;// 默认不是test
		}
		return StringUtils.parseBoolean(property, true);
	}

	public static boolean storeToDocDB() {
		String property = System.getProperty(STOREABLE);
		if (StringUtils.isEmpty(property)) {
			return true;// 默认保存到docDB
		}
		return StringUtils.parseBoolean(property, true);
	}

	public static void setCacheable(boolean b) {
		if (b) {
			System.setProperty(CACHEABLE, "true");
		} else {
			System.setProperty(CACHEABLE, "false");
		}
	}

	public static boolean logEnabled() {
		String property = System.getProperty(LOG_ENABLED);
		if (StringUtils.isEmpty(property)) {
			return true;// 默认记日志
		}
		return StringUtils.parseBoolean(property, true);
	}

	public static boolean cacheable() {
		String property = System.getProperty(CACHEABLE);
		if (StringUtils.isEmpty(property)) {
			return true;// 默认使用缓存
		}
		return StringUtils.parseBoolean(property, true);
	}
}
