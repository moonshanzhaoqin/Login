package com.yuyutechnology.exchange.util;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

public class PinyinUtils {
	public static String toPinyin(String string) {
		try {
			String pinyin = PinyinHelper.convertToPinyinString(string, "", PinyinFormat.WITHOUT_TONE).toUpperCase();
			char initial = pinyin.charAt(0);
			initial = Character.isAlphabetic(initial) ?initial : '#' ;
			return initial+pinyin;
		} catch (PinyinException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "#";
	}
}
