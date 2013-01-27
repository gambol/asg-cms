package com.biemian.utils;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

	final static Pattern p = Pattern.compile("[' ']+");

	/**
	 * 将多个连续的空格转成一个<br/>
	 * 
	 * @param content
	 * @return
	 */
	public static String changeContinuesSpaceToBr(String content) {
		Matcher m = p.matcher(content);
		String newContent = m.replaceAll("<br/>");
		return newContent;
	}

	public static boolean isEmpty(String content) {
		if (content == null) {
			return true;
		}

		if (content.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public static String md5(String content) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(content.getBytes());
			byte[] buffer = messageDigest.digest();

			StringBuffer sb = new StringBuffer(buffer.length * 2);
			for (int i = 0; i < buffer.length; i++) {
				sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
				sb.append(Character.forDigit(buffer[i] & 15, 16));
			}
			return sb.toString();
		} catch (Exception e) {

		}
		return null;
	}
}
