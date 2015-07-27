package com.peony.facebook_crawler.core;

import org.fastdb.DB;

import com.peony.facebook_crawler.model.WebPage;

public class WebPageRepository {

	public static void save(WebPage page) throws Exception{
		DB.persist(page);
	}
}
