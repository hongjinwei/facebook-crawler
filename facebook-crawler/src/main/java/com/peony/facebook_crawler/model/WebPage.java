package com.peony.facebook_crawler.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.peony.util.StringUtils;

/**
 * 与爬虫结果表对应的javabean对象
 * 
 * @author guor
 * @version 2014年9月17日 上午10:05:41
 */
@Entity
@Table(name = "wdyq_pages")
public class WebPage {

	/**
	 * 网页ID，目前采用对url进行MD5加密
	 */
	@Column(name = "id")
	private String pageId;

	/**
	 * 网页URL
	 */
	@Column(name = "url")
	private String url;

	/**
	 * 站点名称
	 */
	@Column(name = "webSite")
	private String webSite;

	/**
	 * 子版块
	 */
	@Column(name = "webSiteplate")
	private String webSiteplate;

	/**
	 * 下载时间
	 */
	@Column(name = "downloadDate")
	private Timestamp downloadDate;

	/**
	 * 标题
	 */
	@Column(name = "title")
	private String title;

	/**
	 * 快照
	 */
	@Column(name = "summary")
	private String summary;

	/**
	 * 类型
	 */
	@Column(name = "type")
	private int type;

	/**
	 * 发布时间
	 */
	@Column(name = "publishDate")
	private Timestamp publishDate;

	/**
	 * 网站优先级
	 */
	@Column(name = "sitePriority")
	private int sitePriority = 2; // 默认值

	@Column(name = "indexedStatus")
	private int indexedStatus;

	@Column(name = "authorName")
	private String authorName;

	@Column(name = "authorUrl")
	private String authorUrl;

	public String getAuthorUrl() {
		return authorUrl;
	}

	public void setAuthorUrl(String aouthorUrl) {
		this.authorUrl = aouthorUrl;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String aouthorName) {
		this.authorName = aouthorName;
	}

	/**
	 * @return
	 */
	public int getIndexedStatus() {
		return indexedStatus;
	}

	/**
	 * set indexedStatus to 0,just store url into sql
	 * 
	 * @param indexedStatus
	 */
	public void setIndexedStatus(int indexedStatus) {
		this.indexedStatus = indexedStatus;
	}

	/**
	 * @return the id
	 */
	public String getPageId() {
		if (StringUtils.isEmpty(pageId)) {
			this.pageId = StringUtils.MD5(url);
		}
		return this.pageId;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the webSite
	 */
	public String getWebSite() {
		return webSite;
	}

	/**
	 * @param webSite
	 *            the webSite to set
	 */
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	/**
	 * @return the downloadDate
	 */
	public Timestamp getDownloadDate() {
		return downloadDate;
	}

	/**
	 * @param downloadDate
	 *            the downloadDate to set
	 */
	public void setDownloadDate(Timestamp downloadDate) {
		this.downloadDate = downloadDate;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @param summary
	 *            the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the publishDate
	 */
	public Timestamp getPublishDate() {
		return publishDate;
	}

	/**
	 * @param publishDate
	 *            the publishDate to set
	 */
	public void setPublishDate(Timestamp publishDate) {
		this.publishDate = publishDate;
	}

	/**
	 * @return the sitePriority
	 */
	public int getSitePriority() {
		return sitePriority;
	}

	/**
	 * @param sitePriority
	 *            the sitePriority to set
	 */
	public void setSitePriority(int sitePriority) {
		this.sitePriority = sitePriority;
	}

	/**
	 * @return the webSiteplate
	 */
	public String getWebSiteplate() {
		return webSiteplate;
	}

	/**
	 * @param webSiteplate
	 *            the webSiteplate to set
	 */
	public void setWebSiteplate(String webSiteplate) {
		this.webSiteplate = webSiteplate;
	}
}
