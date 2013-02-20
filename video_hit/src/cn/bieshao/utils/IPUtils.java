package cn.bieshao.utils;

import javax.servlet.http.HttpServletRequest;

public class IPUtils {
	public static final String ERROR_IP = "127.0.0.1";

	public static String getUserIPString(HttpServletRequest request) {

		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if ("0:0:0:0:0:0:0:1".equals(ip))
				ip = ERROR_IP;
		}

		if ("unknown".equalsIgnoreCase(ip)) {
			ip = ERROR_IP;
			return ip;
		}

		int pos = ip.indexOf(',');
		if (pos >= 0) {
			ip = ip.substring(0, pos);
		}

		return ip;
	}

}
