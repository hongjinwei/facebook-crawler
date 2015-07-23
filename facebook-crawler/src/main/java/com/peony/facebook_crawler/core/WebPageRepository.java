package com.peony.facebook_crawler.core;

import com.avaje.ebean.Ebean;
import com.peony.facebook_crawler.model.WebPage;

public class WebPageRepository {

	public static void save(WebPage page) throws Exception{
		Ebean.save(page);
	}
}
