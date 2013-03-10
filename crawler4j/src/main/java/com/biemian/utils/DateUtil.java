package com.biemian.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String getNowtimeStr() {
		SimpleDateFormat dateformat2 = new SimpleDateFormat(
				"yyyy年MM月dd日 HH时mm分");
		String str = dateformat2.format(new Date());

		return str;
	}
	
	public static String getTimeStr(Date d){
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(d);
	}
	
}
