/**
 * 
 */
package prod.nebula.mcs.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * BASE64编解码工具类
 * 
 * @author 严东军
 * 
 */
public class Base64Util {
	private static BASE64Encoder encoder = new BASE64Encoder();
	private static BASE64Decoder decoder = new BASE64Decoder();
	
	/**
	 * 解码
	 * 
	 * @param s
	 * @return
	 */
	public static String getBASE64(String s) {
		String st = null;
		if (s != null) {
			try {
				st = encoder.encode(s.getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return st == null ? "" : st;
	}

	/**
	 * 加密
	 * 
	 * @param s
	 * @return
	 */
	public static String getFromBASE64(String s) {
		byte[] b = null;
		if (s != null) {
			try {
				b = decoder.decodeBuffer(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return b == null ? "" : new String(b);
	}
	
	public static byte[] addBytes(byte[] a, byte[] b) {
	  	int count = a.length+b.length;
//	  	System.out.println("count:"+count+"a.length:"+a.length+"b.length:"+b.length);
        byte[] bs = new byte[count];
        for (int i=0; i<count; i++){
        	if(i>=a.length){
        		bs[i]=b[i-a.length];
        	}else{
        		bs[i]=a[i];
        	}
        }
        return bs;
    }
	
	  public static byte[] subBytes(byte[] src, int begin, int count) {
	        byte[] bs = new byte[count];
	        	for (int i=begin; i<count; i++){
	        		bs[i-begin] = src[i];
	        	}
	        return bs;
	    }

	
	public static void main(String[] args){
		String s = "rtsp://218.108.0.69:8845/yjy_ipqam/0808/gongfu.ts";
		
		System.out.println(getBASE64(s));
	}
	
	  

}
