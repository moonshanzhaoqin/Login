package com.yuyutechnology.exchange.util;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

public class PinyinUtils {
	public static String toPinyin(String string) {
		try {
			return PinyinHelper.convertToPinyinString(string, "", PinyinFormat.WITHOUT_TONE);
		} catch (PinyinException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
