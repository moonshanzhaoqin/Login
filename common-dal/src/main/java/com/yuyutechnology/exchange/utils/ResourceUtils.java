package com.yuyutechnology.exchange.utils;

import java.util.ResourceBundle;

public class ResourceUtils {
	public static String getBundleValue(String key) {
		ResourceBundle resource = ResourceBundle.getBundle("server-conf");
		return resource.getString(key);
	}

	public static String getBundleValue(String filename, String key) {
		ResourceBundle resource = ResourceBundle.getBundle(filename);
		return resource.getString(key);
	}
	
	public static void clearCache() {
		ResourceBundle.clearCache();
	}
}
