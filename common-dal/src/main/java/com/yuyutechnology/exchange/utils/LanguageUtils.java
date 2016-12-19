package com.yuyutechnology.exchange.utils;

import java.util.EnumSet;

/**
 * 
 * @author suzan.wu
 *
 */
public class LanguageUtils {

	public static enum Language {
		en_US, zh_CN, zh_HK, zh_TW;
	};

	public static Language standard(String language) {
		EnumSet<Language> languages = EnumSet.allOf(Language.class);
		for (Language language2 : languages) {
			if (language2.name().toLowerCase().contains(language.toLowerCase())) {
				return language2;
			}
		}
		return null;
	}
}
