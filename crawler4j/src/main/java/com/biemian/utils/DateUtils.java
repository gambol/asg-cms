package com.biemian.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static String getNowtimeStr() {
		SimpleDateFormat dateformat2 = new SimpleDateFormat(
				"yyyy年MM月dd日 HH时mm分");
		String str = dateformat2.format(new Date());

		return str;
	}
}
