package com.hellogood.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreatePinyinUtil {

	public static String hanyuToPinyin(String name){
        if(StringUtils.isBlank(name) || !isChineseChar(name)){
            return name;
        }
        char[] nameChar = name.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = 
                                           new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        String pinyinName = "";
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray
                                            (nameChar[i], defaultFormat)[0];
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{ 
            	pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }

    /**
     * 是否包含中文
     * @param str
     * @return
     */
    public static boolean isChineseChar(String str) {
        boolean flag = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            flag = true;
        }
        return flag;
    }

    public static void main(String[] args) {
	    System.out.println(new CreatePinyinUtil().hanyuToPinyin(".文静"));


	}

}
