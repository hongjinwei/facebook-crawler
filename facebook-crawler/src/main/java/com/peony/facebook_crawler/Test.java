package com.peony.facebook_crawler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.peony.facebook_crawler.model.FBSource;
import com.peony.facebook_crawler.model.ParseResult;
import com.peony.facebook_crawler.model.WebPage;
import com.peony.util.StringUtils;
import com.peony.util.http.HttpQuery;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static Element cleanFacebookPage(String htmlPage) {

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
				elemContent = (matchContentList.size() == 0) ? "" : matchContentList.get(0);
				Document codeDoc = Jsoup.parse(elemContent);
				// int numberOfPTag = codeDoc.select("p").size();

				String content = StringUtils.excludeHTML(codeDoc.text());
				int weight = (content == null) ? 0 : content.length();
				if (weight > maxWeight) {
					maxWeight = weight;
					contentElem = codeDoc;
				}
				// System.out.println(elemContent);
				// System.out.println("=====");
				// System.out.println(StringUtils.excludeHTML(codeDoc.text()));
				// System.out.println("------------------------------------\n");
			}
			return contentElem;
		}
	}

	public static List<ParseResult> newparse(String html, FBSource source) throws Exception {
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

	public static void main(String[] args) throws Exception {
		String html = HttpQuery.getInstance().get("https://www.facebook.com/llchu").asString();
//		System.out.println(cleanFacebookPage(html).outerHtml());
		
		FBSource source = new FBSource("朱立伦", "https://www.facebook.com/llchu", 0);
		List<ParseResult> res = newparse(html, source);
		for(ParseResult s : res){
			System.out.println(s.getContent());
			System.out.println(s.getPage().getUrl());
			System.out.println(s.getPage().getTitle());
			System.out.println(s.getPage().getPublishDate());
			System.out.println("==================");
		}
	}

}
