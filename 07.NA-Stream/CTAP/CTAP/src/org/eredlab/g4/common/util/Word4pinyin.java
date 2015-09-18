package org.eredlab.g4.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Word4pinyin {
	public static String getPinYin(String src){
		char[] t1=null;
		t1=src.toCharArray();
		String[] t2 = new String[t1.length];
		HanyuPinyinOutputFormat hypy = new HanyuPinyinOutputFormat();
		hypy.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		hypy.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		hypy.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4="";
		int t0 = t1.length;
		try{
			for(int i=0;i<t0;i++){
				if(Character.toString(t1[i]).matches("[\u4E00-\u9FA5]+")){
					t2=PinyinHelper.toHanyuPinyinStringArray(t1[i],hypy);
                    t4+=t2[0];
				}else{
					// ����Ǻ����ַ���ȡ���ַ����ӵ��ַ�t4��  
					t4 += Character.toString(t1[i]);
				}
			}
		}catch(BadHanyuPinyinOutputFormatCombination e){
			e.printStackTrace();
		}
		return t4;
	}
}
