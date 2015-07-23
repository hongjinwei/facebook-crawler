package com.peony.facebook_crawler.login;

import java.io.File;
import java.io.FileInputStream;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.peony.util.StmUtils;
import com.peony.util.http.HttpQuery;

public class Encrypt {
	public static void main(String[] args) throws Exception {
		//String html = StmUtils.stm2String(new FileInputStream(new File("d:/q.htm")), "utf-8");
		try{
			String html = HttpQuery.getInstance().get("https://www.facebook.com").asString();
			System.out.println(encrypt(html));
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public static String encrypt(String html) throws Exception {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		engine.eval(StmUtils.stm2String(Thread.currentThread().getContextClassLoader().getResourceAsStream("func.js"), "utf-8"));
		Invocable inv = (Invocable) engine;
		html = html.substring(html.indexOf("markup") - 2);
		html = html.substring(0, html.indexOf("</script>"));
		html = html.substring(0, html.indexOf("]]});")+3);
		JSONArray obj = JSON.parseObject(html).getJSONArray("require").getJSONArray(47).getJSONArray(3).getJSONArray(2);
		String res = (String) inv.invokeFunction("solveAndEncode", obj.get(0), obj.get(1), obj.get(2), obj.get(3), obj.get(4));
		StringBuilder buf = new StringBuilder(res);
		buf.insert(res.indexOf('{') + 1, "\"" + obj.get(6) + "\"");
		byte[] base642 = Base64.encodeBase64(buf.toString().getBytes());
		return new String(base642);
	}
}
