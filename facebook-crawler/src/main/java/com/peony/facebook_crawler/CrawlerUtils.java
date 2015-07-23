package com.peony.facebook_crawler;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.peony.util.StringUtils;

public class CrawlerUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerUtils.class);

	/**
	 * 标题长度
	 */
	public static final int TITILE_LEN = 40;

	/**
	 * summary长度
	 */
	public static final int SUMMARY_LEN = 200;

	/**
	 * 标题取正文前40字符，如果正文为null，返回null
	 * 
	 * @param txt
	 *            正文内容
	 * @return 从正文中获取标题
	 */
	public static final String getTitle(String txt) {
		if (StringUtils.isEmpty(txt)) {
			return null;
		}
		return txt.length() > TITILE_LEN ? txt.substring(0, TITILE_LEN) : txt;
	}

	/**
	 * summay 截取正文200个长度
	 * 
	 * @param txt
	 *            正文内容
	 * @return 从正文中获取summay
	 */
	public static final String getSummary(String txt) {
		if (StringUtils.isEmpty(txt)) {
			return null;
		}
		return txt.length() > SUMMARY_LEN ? txt.substring(0, SUMMARY_LEN) : txt;
	}

	public static Timestamp getPublishTime(String timeStr) {
		try {
			long time = Integer.parseInt(timeStr);
			if (time < 10000000000l) {
				time = time * 1000l;
			}
			return new Timestamp(time);
		} catch (Exception e) {
			LOGGER.error(e.getMessage() + "解析时间失败", e);
		}
		return new Timestamp(0l);
	}
}
