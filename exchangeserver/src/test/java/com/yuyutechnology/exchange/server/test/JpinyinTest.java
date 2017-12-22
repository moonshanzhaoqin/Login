package com.yuyutechnology.exchange.server.test;

import org.junit.Test;

import com.yuyutechnology.exchange.util.PinyinUtils;

public class JpinyinTest extends BaseSpringJunit4 {
	@Test
	public void test() {
	System.out.println(PinyinUtils.toPinyin("你好"));	
	System.out.println(PinyinUtils.toPinyin("123你好"));	
		
		
		
		
		
//		try {
//			String str = "你好世界,義氣，裘千仞，单县，4548678675457867";
//			// 设置声调表示格式
//			System.out.println(PinyinHelper.convertToPinyinString(str, "", PinyinFormat.WITH_TONE_MARK)); // nǐ,hǎo,shì,jiè
//			// 数字表示声调
//			System.out.println(PinyinHelper.convertToPinyinString(str, "", PinyinFormat.WITH_TONE_NUMBER)); // ni3,hao3,shi4,jie4
//			// 无声调
//			System.out.println(PinyinHelper.convertToPinyinString(str, "", PinyinFormat.WITHOUT_TONE)); // ni,hao,shi,jie
//			// 获取拼音首字母
//			System.out.println(PinyinHelper.getShortPinyin(str)); // nhsj
//			// 判断是否多音字
//			System.out.println(PinyinHelper.hasMultiPinyin('啊'));// true
//			 //简体转繁体
//		    String traditionalChinese = ChineseHelper.convertToTraditionalChinese("義氣");
//			System.out.println(traditionalChinese);
//		} catch (PinyinException e) {
//			e.printStackTrace();
//		}
	}
}
