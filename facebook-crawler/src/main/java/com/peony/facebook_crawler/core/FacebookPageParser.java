package com.peony.facebook_crawler.core;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.peony.facebook_crawler.CommonUtils;
import com.peony.facebook_crawler.CrawlerUtils;
import com.peony.facebook_crawler.model.FBSource;
import com.peony.facebook_crawler.model.ParseResult;
import com.peony.facebook_crawler.model.WebPage;
import com.peony.util.StringUtils;

public class FacebookPageParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(FacebookPageParser.class);

	private static String extractHtmlAnotation(String html) {
		return html.replaceAll("<!--", "").replaceAll("-->", "");
	}

	private static Document cleanFacebookPage(String htmlPage) {
		Document doc = Jsoup.parse(htmlPage);
		Elements codeElements = doc.select("code");
		if (codeElements.size() <= 0) {
			return null;
		} else {
			String contentString = null;
			int maxWeight = 0;
			for (Element elem : codeElements) {
				String elemContent = elem.outerHtml();
				int weight = (elemContent == null) ? 0 : elemContent.length();
				if (weight > maxWeight) {
					maxWeight = weight;
					contentString = elemContent;
				}
			}
			contentString = extractHtmlAnotation(contentString);
			Document codeDoc = Jsoup.parse(contentString);
			return codeDoc;
		}
	}

	@Deprecated
	private static Element cleanFacebookPage_backup(String htmlPage) {
		Document doc = Jsoup.parse(htmlPage);
		Elements codeElements = doc.select("code");
		if (codeElements.size() <= 0) {
			return null;
		} else {
			Element contentElem = null;
			int maxWeight = 0;
			for (Element elem : codeElements) {
				String elemContent = elem.outerHtml();
				List<String> matchContentList = StringUtils.match(elemContent, "<!--(.*)-->");
				if (matchContentList.size() == 0) {
					continue;
				}
				elemContent = matchContentList.get(0);
				Document codeDoc = Jsoup.parse(elemContent);
				String content = StringUtils.excludeHTML(codeDoc.text());
				int weight = (content == null) ? 0 : content.length();
				if (weight > maxWeight) {
					maxWeight = weight;
					contentElem = codeDoc;
				}
			}
			return contentElem;
		}
	}

	public static List<ParseResult> parse(String html, FBSource source) throws Exception {
		String parentUrl = source.getHomepage();
		List<ParseResult> ans = new ArrayList<ParseResult>();
		Element contentNode = cleanFacebookPage(html);
		Elements elements = contentNode.getElementsByAttribute("data-ft");
		for (Element el : elements) {
			try {
				String content = el.getElementsByAttributeValue("class", "_5pbx userContent").get(0).text();
				content = StringUtils.cleanEmoji(content);

				String timeStr = el.getElementsByAttribute("data-utime").get(0).attr("data-utime");
				String relUrl = el.getElementsByAttributeValue("class", "_5pcq").get(0).attr("href");
				String url = CommonUtils.absUrl(parentUrl, relUrl);
				Timestamp publishDate = CrawlerUtils.getPublishTime(timeStr);
				String summary = CrawlerUtils.getSummary(content);
				String title = CrawlerUtils.getTitle(content);

				WebPage page = new WebPage();
				page.setTitle(title);
				page.setSummary(summary);
				page.setPublishDate(publishDate);
				page.setUrl(url);
				page.setDownloadDate(new Timestamp(System.currentTimeMillis()));
				page.setWebSite("Facebook");
				page.setType(11);
				page.setIndexedStatus(3);
				page.setAuthorName(source.getName());
				page.setAuthorUrl(source.getHomepage());

				ParseResult res = new ParseResult(content, page);
				ans.add(res);
			} catch (Exception e) {
			}
		}
		return ans;
	}

	@Deprecated
	public static List<ParseResult> parse_old(String html, FBSource source) throws Exception {
		String parentUrl = source.getHomepage();
		List<ParseResult> ans = new ArrayList<ParseResult>();
		Document d = Jsoup.parse(html);
		Element ele = d.getElementsByAttributeValue("class", "hidden_elem").get(7);
		String raw_content = ele.html().replace("<!--", "").replace("-->", "");

		Document doc = Jsoup.parse(raw_content);
		Element element = doc.getElementsByAttributeValue("class", "_5sem").get(0);
		Elements elements = element.getElementsByAttribute("data-ft");
		for (Element el : elements) {
			try {
				String content = el.getElementsByAttributeValue("class", "_5pbx userContent").get(0).text();
				content = StringUtils.cleanEmoji(content);

				String timeStr = el.getElementsByAttribute("data-utime").get(0).attr("data-utime");
				String relUrl = el.getElementsByAttributeValue("class", "_5pcq").get(0).attr("href");
				String url = CommonUtils.absUrl(parentUrl, relUrl);
				Timestamp publishDate = CrawlerUtils.getPublishTime(timeStr);
				String summary = CrawlerUtils.getSummary(content);
				String title = CrawlerUtils.getTitle(content);

				WebPage page = new WebPage();
				page.setTitle(title);
				page.setSummary(summary);
				page.setPublishDate(publishDate);
				page.setUrl(url);
				page.setDownloadDate(new Timestamp(System.currentTimeMillis()));
				page.setWebSite("Facebook");
				page.setType(11);
				page.setIndexedStatus(3);
				page.setAuthorName(source.getName());
				page.setAuthorUrl(source.getHomepage());

				ParseResult res = new ParseResult(content, page);
				ans.add(res);
			} catch (Exception e) {
			}
		}
		return ans;
	}
}
