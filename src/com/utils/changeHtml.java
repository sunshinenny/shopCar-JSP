package com.utils;

import java.io.UnsupportedEncodingException;

public class changeHtml {
	public static String changeStringToHtml(String request) {
		String content = request;
		content = content.replace("<", "&lt;");
		content = content.replace(">", "&gt;");
		return content;
	}

	public static String toChinese(String str) {

		if (str == null)
			str = "";
		changeStringToHtml(str);
		try {
			// str = new String(str.getBytes("ISO-8859-1"), "gb2312");
			byte[] tempByte = str.getBytes("gb2312");
			str = new String(tempByte, "ISO8859_1");
		} catch (UnsupportedEncodingException e) {
			// TODO: handle exception
			str = "";
			e.printStackTrace();
		}
		return str;
	}

}
