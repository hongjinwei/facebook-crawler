package com.peony.facebook_crawler.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FacebookCrawlerTaskService {

	private static int threadNumber = 5;

	private static ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(threadNumber);

	public static void start() {
		FBSourceManager.setThreadNumber(threadNumber);
		for (int i = 0; i < threadNumber; i++) {
			threadPool.scheduleWithFixedDelay(new FacebookCrawlerTask(), 0, 1, TimeUnit.HOURS);
		}
	}
}
