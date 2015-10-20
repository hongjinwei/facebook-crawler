package com.peony.facebook_crawler.core;

import java.sql.Date;

import org.fastdb.DB;

import com.peony.facebook_crawler.SystemProps;
import com.peony.facebook_crawler.model.WebPage;

public class WebPageRepository {

	public static void save(WebPage page) throws Exception {
		if (SystemProps.storeToSql()) {
			DB.persist(page);
		} else {
			System.out.println(page.getUrl() + " " + page.getAuthorName() + " " + page.getSummary() + " " + page.getPublishDate());
		}
	}

	public static void showPage(WebPage page) {
		System.out.println(page.getUrl() + " " + page.getAuthorName() + " " + page.getSummary() + " " + page.getPublishDate());
	}

	public static void main(String[] args) {
		System.out.println();
	}
}
