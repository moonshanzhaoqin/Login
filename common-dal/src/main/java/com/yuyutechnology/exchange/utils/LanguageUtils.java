package com.yuyutechnology.exchange.utils;

import java.util.EnumSet;

/**
 * 语言工具
 * 
 * @author suzan.wu
 *
 */
public class LanguageUtils {
	public enum Language {
		en_US, zh_CN, zh_TW;
	}

	/**
	 * 语言标准化
	 * 
	 * @param language
	 * @return
	 */
	public static Language standard(String language) {
		EnumSet<Language> languages = EnumSet.allOf(Language.class);
		for (Language language2 : languages) {
			if (language2.name().toLowerCase().contains(language.toLowerCase())) {
				return language2;
			}
		}
		return Language.zh_CN;
	}
}
