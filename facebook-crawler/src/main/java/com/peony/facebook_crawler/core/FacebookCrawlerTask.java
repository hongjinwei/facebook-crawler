package com.peony.facebook_crawler.core;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.peony.facebook_crawler.CommonUtils;
import com.peony.facebook_crawler.model.FBSource;
import com.peony.facebook_crawler.model.ParseResult;
import com.peony.facebook_crawler.model.WebPage;
import com.peony.util.StringUtils;
import com.peony.util.cache.CacheClient;
import com.peony.util.http.BaseHttpException;
import com.peony.util.http.HttpQuery;

public class FacebookCrawlerTask implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(FacebookCrawlerTask.class);

	private static final String docUrl = "http://119.254.110.32:8080/HBaseDfs/dfs";

	/**
	 * 当当前时间减去上次操作时间大于cycle（小时）时，即返回true，表示可以爬取，否则表示不需要爬取
	 * 
	 * @param source
	 * @return
	 */
	private boolean shouldCrawl(FBSource source) {
		long curr = System.currentTimeMillis();
		long last = source.getLastoptime();
		return (curr - last) > (source.getCycle() * 60 * 60 * 1000l);
	}

	private String request(String url) throws BaseHttpException {
		String html = HttpQuery.getInstance().get(url).asString();
		return html;
	}

	private void store(List<ParseResult> list, String name) {
		CacheClient cache = CommonUtils.getCacheClient();
		int count = 0;
		try {
			for (ParseResult pr : list) {
				WebPage page = pr.getPage();
				String url = page.getUrl();
				if (!CommonUtils.checkUrl(cache, url)) {
					String content = pr.getContent();
					if (StringUtils.isEmpty(content)) {
						continue;
					}
					try {
						WebPageRepository.save(page);
					} catch (Exception e) {
						LOGGER.error(e.getMessage() + "保存至sql失败！ url：" + page.getUrl(), e);
						continue;
					}
					try {
						CommonUtils.storage(docUrl, true, page.getId(), content, true);
					} catch (Exception e) {
						LOGGER.error(e.getMessage() + "保存至文档服务器失败！", e);
						continue;
					}
					CommonUtils.storeUrl(cache, url);
					count++;
				}
			}
		} finally {
			CommonUtils.recycleCacheClient(cache);
		}
		LOGGER.info(name + "的facebook 成功存储 " + count + " 条");
	}

	private void crawl(FBSource source) {
		if (!shouldCrawl(source)) {
			return;
		}
		String homeUrl = source.getHomepage();
		String html;
		try {
			html = request(homeUrl);
		} catch (BaseHttpException e) {
			String errormsg = "\n 爬取" + source.getName() + "的facebook失败  homepage：" + source.getHomepage();
			LOGGER.error(e.getMessage() + errormsg, e);
			return;
		}
		String name = source.getName();
		try {
			List<ParseResult> list = FacebookPageParser.parse(html, source);
			store(list, name);
			source.setLastoptime(System.currentTimeMillis());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	@Override
	public void run() {
		List<FBSource> sources = FBSourceManager.getSources();
		for (FBSource source : sources) {
			System.out.print(source.getName() + " ");
		}
		System.out.println();
		if (sources.size() == 0) {
			LOGGER.info("没有任务！");
		}
		for (FBSource source : sources) {
			LOGGER.info("开始任务 ：" + source.getName());
			crawl(source);
			LOGGER.info("任务结束：" + source.getName());
		}
	}

}
