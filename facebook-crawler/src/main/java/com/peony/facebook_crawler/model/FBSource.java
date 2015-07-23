package com.peony.facebook_crawler.model;

public class FBSource {

	private String name;
	private String homepage;
	private int cycle;
	private long lastoptime = 0l;
	
	public FBSource(String name, String homepage, int cycle) {
		super();
		this.name = name;
		this.homepage = homepage;
		this.cycle = cycle;
	}

	public long getLastoptime() {
		return lastoptime;
	}

	public void setLastoptime(long lastoptime) {
		this.lastoptime = lastoptime;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

}
