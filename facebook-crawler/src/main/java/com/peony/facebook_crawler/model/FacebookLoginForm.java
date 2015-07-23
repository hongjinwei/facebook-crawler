package com.peony.facebook_crawler.model;

public class FacebookLoginForm {

	private String email;
	private String pass;
	private String default_persistent = "0";
	private String timezone = "-480";
	private String lgndim;
	private String lgnrnd;
	private String lgnjs;
	private String locale;
	private String qsstamp;

	public FacebookLoginForm() {
		// TODO Auto-generated constructor stub
	}

	public FacebookLoginForm(String email, String pass, String lgndim, String lgnrnd, String lgnjs, String qsstamp) {
		super();
		this.email = email;
		this.pass = pass;
		this.lgndim = lgndim;
		this.lgnrnd = lgnrnd;
		this.lgnjs = lgnjs;
		this.qsstamp = qsstamp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getDefault_persistent() {
		return default_persistent;
	}

	public void setDefault_persistent(String default_persistent) {
		this.default_persistent = default_persistent;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getLgndim() {
		return lgndim;
	}

	public void setLgndim(String lgndim) {
		this.lgndim = lgndim;
	}

	public String getLgnrnd() {
		return lgnrnd;
	}

	public void setLgnrnd(String lgnrnd) {
		this.lgnrnd = lgnrnd;
	}

	public String getLgnjs() {
		return lgnjs;
	}

	public void setLgnjs(String lgnjs) {
		this.lgnjs = lgnjs;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getQsstamp() {
		return qsstamp;
	}

	public void setQsstamp(String qsstamp) {
		this.qsstamp = qsstamp;
	}

}
