package com.yuyutechnology.exchange.utils;

import java.util.EnumSet;

/**
 * 
 * @author suzan.wu
 *
 */
public class LanguageUtils {

	public static enum Language {
		en_US("US"), zh_CN("CN"), zh_TW("TW");
		
		private String value;
		private Language(String value){
			this.value=value;
		}
		public String getValue() {
			return value;
		}
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
