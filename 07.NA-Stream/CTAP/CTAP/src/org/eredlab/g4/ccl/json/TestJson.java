package org.eredlab.g4.ccl.json;

import java.util.ArrayList;
import java.util.List;

import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;

public class TestJson {

	public static void main(String[] args) {
		/*
		String string = "[{\"xmid\":\"1000143024\",\"sfdlbm\":\"01\",\"xmmc\":\"盐酸头孢他美酯片1\"},{\"xmid\":\"1000143023\",\"sfdlbm\":\"01\",\"xmmc\":\"盐酸头孢他美酯片(薄膜衣)3\"}]";
		string = string.substring(1, string.length() - 1);
		System.out.println(string);
		String[] strings = string.split("},");
		for (int i = 0; i < strings.length; i++) {
			if (i != strings.length - 1) {
				strings[i] += "}";
			}
			System.out.println(strings[i]);
		}
		*/
		Dto dto = new BaseDto();
		List list = new ArrayList();
		Dto term1 = new BaseDto();
		term1.put("term_id",  "234141241241FANBB");
		term1.put("term_type",  1001);
		list.add(term1);
		
		Dto term2 = new BaseDto();
		term2.put("term_id",  "234141241241FANBCV5665");
		term2.put("term_type",  1002);
		list.add(term2);
		
		dto.put("subscribe_id", "211321");
		dto.put("terms", list);
		
		String jsonStr = JsonHelper.encodeObject2Json(dto);
		
		System.out.println(jsonStr);
	}

}
