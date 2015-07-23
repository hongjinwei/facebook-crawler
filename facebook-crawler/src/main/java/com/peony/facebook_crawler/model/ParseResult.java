package com.peony.facebook_crawler.model;

public class ParseResult {

	private String content;
	private WebPage page;

	public ParseResult(String content, WebPage page) {
		this.content = content;
		this.page = page;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public WebPage getPage() {
		return page;
	}

	public void setPage(WebPage page) {
		this.page = page;
	}

}
