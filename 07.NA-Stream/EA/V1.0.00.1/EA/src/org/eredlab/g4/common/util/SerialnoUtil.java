package org.eredlab.g4.common.util;

import java.util.Random;

public class SerialnoUtil {
	public static String getRandomString(int length) {
	    StringBuffer buffer = new StringBuffer("1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"); 
	    StringBuffer sb = new StringBuffer(); 
	    Random r = new Random(); 
	    int range = buffer.length(); 
	    for (int i = 0; i < length; i ++) { 
	        sb.append(buffer.charAt(r.nextInt(range))); 
	    } 
	    return sb.toString(); 
	}
}
