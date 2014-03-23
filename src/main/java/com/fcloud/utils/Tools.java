/**
 * @(#) Tools.java Created on 2014-2-24
 *
 * Copyright © 2013 深圳企业云科技有限公司  版权所有
 */
package com.fcloud.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

import sun.net.www.protocol.http.HttpURLConnection;

/**
 * The class <code>Tools</code>
 * 
 * @author Feng OuYang
 * @version 1.0
 */
public final class Tools {

	/**
	 * 数组转成arraylist
	 * 
	 * @param args
	 * @return
	 */
	public static final ArrayList<String> toArray(String... args) {
		if (null != args) {
			final ArrayList<String> list = new ArrayList<String>();
			for (String s : args) {
				list.add(s);
			}
			return list;
		}
		return null;
	}

	public static final String[] toArrayString(String... args) {
		return args;
	}

	public static final String joinStrings(String[] args) {
		if (null != args && args.length > 0) {
			final StringBuilder builder = new StringBuilder();
			for (String s : args) {
				builder.append(s);
			}
			return builder.toString();
		}
		return null;

	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isNum(String string) {
		if (null != string) {
			return Pattern.compile("\\d+").matcher(string).matches();
		}
		return false;
	}

	public static boolean httpRequest(String url) throws IOException {
		URL url2 = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setReadTimeout(3000);
		conn.setConnectTimeout(3000);
		if (conn.getResponseCode() == 200) {
			final InputStream in = conn.getInputStream();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = reader.readLine();
			if (null != line && line.equals("200")) {
				return true;
			}
		}
		return false;
	}
}
