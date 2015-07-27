package com.peony.facebook_crawler;

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
		SystemProps.setStoreable(false);
		SystemProps.setStoreUrl(false);
	}

	public static void main(String[] args) {
		// setLocalTest();
		FacebookCrawlerTaskService.start();
	}
}
