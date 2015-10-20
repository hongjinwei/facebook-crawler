package com.peony.facebook_crawler;

import org.fastdb.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.peony.facebook_crawler.core.FacebookCrawlerTaskService;

/**
 * FacebookCrawler
 *
 */
public class App {

	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	private static void setLocalTest() {
		SystemProps.setTest(true);
		SystemProps.setCacheable(false);
		SystemProps.setStoreToDocDB(false);
		SystemProps.setStoreUrl(false);
		SystemProps.setStoreToSql(false);
	}

	public static void main(String[] args) {
		// setLocalTest();
		FacebookCrawlerTaskService.start();
	}
}
