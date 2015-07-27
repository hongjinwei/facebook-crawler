package com.peony.facebook_crawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;

import md.base.storage.WebPageStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.peony.util.StringUtils;
import com.peony.util.cache.CacheClient;
import com.peony.util.cache.CacheClientPool;
import com.peony.util.cache.CacheClientPoolFactory;

public class CommonUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

	private static final String defaultDocUrl = "http://119.254.110.32:8080/HBaseDfs/dfs";
	
	public static String absUrl(String baseUrl, String relUrl) {
		URL base;
		try {
			try {
				base = new URL(baseUrl);
			} catch (MalformedURLException e) {
				URL abs = new URL(relUrl);
				return abs.toExternalForm();
			}
			if (relUrl.startsWith("?"))
				relUrl = base.getPath() + relUrl;
			URL abs = new URL(base, relUrl);
			return abs.toExternalForm();
		} catch (MalformedURLException e) {
			return "";
		}
	}



	private static CacheClientPool pool = CacheClientPoolFactory.getObject();

	/**
	 * @return 获取MinaSession对象
	 */
	public static CacheClient getCacheClient() {
		try {
			if (!SystemProps.cacheable()) {
				LOGGER.info("设置了系统参数，不使用urlcahce");
				return null;
			}
			return pool.getResource();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
	}

	public static void recycleCacheClient(CacheClient mc) {
		if (mc != null) {
			pool.returnResource(mc);
		}
	}

	/**
	 * 在缓存中检测URL，如果session为空，或者检测失败，都返回false，不会写入URL到缓存中
	 * 
	 * @param mc
	 *            MemMinaClient对象
	 * @param url
	 *            待检测的URL
	 * @return true URL缓存命中，false 没命中
	 */
	public static boolean checkUrl(CacheClient mc, String url) {
		try {
			long s = System.currentTimeMillis();
			if (mc != null && mc.checkUrl(url, false)) {
				return true;
			}
			long e = System.currentTimeMillis();
			if ((e - s) > 100) {
				LOGGER.info("检测URL耗时：" + (e - s));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 将一个URL保存到缓存中
	 * 
	 * @param mc
	 *            MemMinaClient对象
	 * @param url
	 *            待缓存的url
	 */
	public static void storeUrl(CacheClient mc, String url) {
		// 如果设置了storeurl参数为false，就不保存url
		if (!SystemProps.storeurl()) {
			LOGGER.info("设置了系统参数，不存储url到url缓存！");
			return;
		}

		if (mc != null) {
			try {
				long s = System.currentTimeMillis();
				mc.storeUrl(url, true);
				long e = System.currentTimeMillis();
				if ((e - s) > 100) {
					LOGGER.info("缓存URL耗时：" + (e - s));
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 保存到文档服务器,如果baseUrl为空，就不保存
	 * 
	 * @param baseUrl
	 *            文档服务器地址
	 * @param comp
	 *            boolean值，是否压缩，一般为true
	 * @param id
	 *            文章id
	 * @param content
	 *            文章完整内容
	 * @param isPureText
	 *            是否是纯文本
	 * @throws Exception
	 */
	public static void storage(String baseUrl, boolean comp, String id, String content, boolean isPureText) throws Exception {
		if(!SystemProps.storeable()){
			LOGGER.info("设置了系统参数，不存储文章到文档服务器！");
			return;
		}
		
		if (StringUtils.isEmpty(baseUrl)) {
			return;
		}
		WebPageStorage storage = new WebPageStorage(comp);
		storage.useHttpFileSystem(baseUrl);
		storage.put(id, content, true, isPureText);
	}
	
	public static void storage(boolean comp, String id, String content, boolean isPureText) throws Exception {
		if(!SystemProps.storeable()){
			LOGGER.info("设置了系统参数，不存储文章到文档服务器！");
			return;
		}
		
		WebPageStorage storage = new WebPageStorage(comp);
		storage.useHttpFileSystem(defaultDocUrl);
		storage.put(id, content, true, isPureText);
	}
	
}
