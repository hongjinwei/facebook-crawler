package com.peony.facebook_crawler.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.peony.facebook_crawler.model.FBSource;

public class FBSourceManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(FBSourceManager.class);

	private static final String SOURCEXML = "/FBSource.xml";

	private static final List<FBSource> sourceList = new LinkedList<FBSource>();

	private static int p = 0;

	private static int threadNumber = 1;

	private static int threadCount = 1;

	private static int eachPollNumber = 1;

	private static int lastPollNumber = 1;

	public static void setThreadNumber(int n) {
		threadNumber = n;
		if (n != 0) {
			int t = getSourceNumber() / n;
			eachPollNumber = (t == 0) ? 1 : t;
			int p = getSourceNumber() - eachPollNumber * (threadNumber - 1);
			lastPollNumber = (p <= 0) ? 0 : p;
		}
	}

	static {
		loadSources();
	}

	public static boolean loadSources() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(FBSourceManager.class.getResourceAsStream(SOURCEXML)));
			String xml = "";
			String tmp;
			while ((tmp = br.readLine()) != null) {
				xml += tmp;
			}
			Document doc = Jsoup.parse(xml);
			Elements items = doc.select("item");
			for (Element item : items) {
				try {
					String homepage = item.select("homepage").get(0).text();
					String name = item.select("name").get(0).text();
					int cycle = 1;
					Elements cycles = item.select("cycle");
					if (cycles.size() > 0) {
						String cycleStr = cycles.get(0).text();
						cycle = Integer.parseInt(cycleStr);
					}
					FBSource fbsource = new FBSource(name, homepage, cycle);
					sourceList.add(fbsource);
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage() + "初始化配置失败！", e);
		}
		return false;
	}

	synchronized private static FBSource pollSource() {
		if (sourceList.size() > 0 && p < sourceList.size()) {
			FBSource source = sourceList.get(p);
			p = p + 1;
			return source;
		}
		return null;
	}

	public static int getSourceNumber() {
		return sourceList.size();
	}

	synchronized public static List<FBSource> getSources() {
		if (threadCount == threadNumber) {
			List<FBSource> ans = getSources(lastPollNumber);
			p = 0;
			threadCount = 1;
			return ans;
		}
		threadCount = (threadCount % threadNumber) + 1;
		return getSources(eachPollNumber);
	}

	public static List<FBSource> getSources(int number) {
		List<FBSource> sources = new ArrayList<FBSource>();
		for (int i = 0; i < number; i++) {
			FBSource source = pollSource();
			if (source != null) {
				sources.add(source);
			} else {
				break;
			}
		}
		return sources;
	}

}
