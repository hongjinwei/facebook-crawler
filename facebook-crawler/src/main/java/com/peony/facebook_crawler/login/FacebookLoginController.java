package com.peony.facebook_crawler.login;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.peony.facebook_crawler.model.FacebookLoginForm;
import com.peony.util.http.EasyCookieSpecProvider;
import com.peony.util.http.HttpQuery;

public class FacebookLoginController {

	/**
	 * 设置连接超时时间,15秒
	 */
	private static final int CONNECTION_TIMEOUT = 15 * 1000;

	/**
	 * 设置等待数据超时时间15秒钟
	 */
	private static final int SO_TIMEOUT = 15 * 1000;

	private static final Logger LOGGER = LoggerFactory.getLogger(FacebookLoginController.class);

	public FacebookLoginController() {
		// TODO Auto-generated constructor stub
	}

	private List<NameValuePair> makePostParams(String u, String p, FacebookLoginForm loginForm) {
		List<NameValuePair> postDict = new ArrayList<NameValuePair>();
		postDict.add(new BasicNameValuePair("email", u));
		postDict.add(new BasicNameValuePair("pass", p));
		postDict.add(new BasicNameValuePair("default_persistent", loginForm.getDefault_persistent()));
		postDict.add(new BasicNameValuePair("timezone", loginForm.getTimezone()));
		postDict.add(new BasicNameValuePair("lgndim", loginForm.getLgndim()));
		postDict.add(new BasicNameValuePair("lgnrnd", loginForm.getLgnrnd()));
		postDict.add(new BasicNameValuePair("lgnjs", loginForm.getLgnjs()));
		postDict.add(new BasicNameValuePair("locale", loginForm.getLocale()));
		postDict.add(new BasicNameValuePair("qsstamp", loginForm.getQsstamp()));
		return postDict;
	}

	private FacebookLoginForm getLoginForm(String u, String p, Document doc) {
		try {
			String email = u;
			String pass = p;
			String default_persistent = "0";
			String timezone = "-480";
			String lgndim = doc.getElementsByAttributeValue("name", "lgndim").get(0).attr("value");
			String lgnrnd = doc.getElementsByAttributeValue("name", "lgnrnd").get(0).attr("value");
			String lgnjs = doc.getElementsByAttributeValue("name", "lgnjs").get(0).attr("value");
			String locale = "zh_CN";
			String qsstamp = doc.getElementsByAttributeValue("name", "qsstamp").get(0).attr("value");
			FacebookLoginForm loginForm = new FacebookLoginForm(email, pass, lgndim, lgnrnd, lgnjs, qsstamp);
			return loginForm;
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
		}
		return null;
	}

	private String getActionUrl(Document doc) {
		try {
			String url = doc.getElementsByAttributeValue("id", "login_form").get(0).attr("action");
			return url;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

	private Map<String, String> makeCookieMap(List<Cookie> cookies) {
		Map<String, String> cookieMap = new HashMap<String, String>();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie.getValue());
			}
		}
		return cookieMap;
	}

	public CookieStore login(String u, String p, String loginUrl) throws Exception {
		String html = HttpQuery.getInstance().get(loginUrl).asString();
		Document doc = Jsoup.parse(html);
		System.out.println(html);
		FacebookLoginForm loginForm = getLoginForm(u, p, doc);
		String actionUrl = getActionUrl(doc);
//		if (loginForm == null || actionUrl == null) {
//			throw new Exception("登录出错！loginForm或者actionUrl为空！");
//		}

		// make a httpclient
		BasicCookieStore cookieStore = new BasicCookieStore();
		RegistryBuilder<CookieSpecProvider> builder = RegistryBuilder.create();
		builder.register(EasyCookieSpecProvider.EASY_PROVIDER_NAME, new EasyCookieSpecProvider());

		Builder configBuilder = RequestConfig.custom();
		configBuilder.setConnectTimeout(CONNECTION_TIMEOUT);
		configBuilder.setSocketTimeout(SO_TIMEOUT);
		configBuilder.setCookieSpec(EasyCookieSpecProvider.EASY_PROVIDER_NAME);

		HttpClientBuilder clientBuilder = HttpClients.custom();
		clientBuilder.setDefaultRequestConfig(configBuilder.build());
		clientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler());
		clientBuilder.setDefaultCookieStore(cookieStore);
		clientBuilder.setDefaultCookieSpecRegistry(builder.build());
		CloseableHttpClient client = clientBuilder.build();
		
		HttpGet getMethod = new HttpGet(loginUrl);
//		HttpPost postMethod = new HttpPost(actionUrl);
//		postMethod.setEntity(new UrlEncodedFormEntity(makePostParams(u, p, loginForm), Charset.defaultCharset()));
//		CloseableHttpResponse httpResponse = client.execute(postMethod);
		CloseableHttpResponse httpResponse = client.execute(getMethod);
		
		Map<String, String> cookieMap = makeCookieMap(cookieStore.getCookies());
		System.out.println(cookieMap);
		InputStream in = httpResponse.getEntity().getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try {
			String tmp;
			while ((tmp = br.readLine()) != null) {
				System.out.println(tmp);
			}
		} finally {
			br.close();
			in.close();
		}
		return cookieStore;
	}

	public static void main(String[] args) {
		FacebookLoginController lc = new FacebookLoginController();
		String u = "";
		String p = "";
		try {
			lc.login(u, p, "https://www.facebook.com");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
