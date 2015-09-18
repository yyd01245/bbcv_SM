package prod.nebula.commons.string;

import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

public class OidUtils {

		public static String newId() {
			
		
			char x36s[] = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

			
			short len = 10;
			
		
			char[] chs = new char[len];

		
			long v = (System.currentTimeMillis() - 936748800000L) >> 1; // 1999-9-9
		
			for (int i=7; i>0; i--) {
				chs[i] = x36s[(int)(v % 36)];
				v = v / 36;
			}
			chs[0] = x36s[(int)(v % 26) + 10]; 
		
			UUID u = UUID.randomUUID();
			v = u.getLeastSignificantBits() ^ u.getMostSignificantBits();
			if (v < 0) {
				v = -v;
			}

			for (int i=8; i<len; i++) {
				chs[i] = x36s[(int)(v % 36)];
				v = v / 36;
			}

			return new String(chs);
		}

		public static long newLongId(Integer num) {
			
		
			char x10s[] = "0123456789".toCharArray();

		
			short shortLength = num.shortValue();


			char[] chs = new char[shortLength];
			
			
			long v = (System.currentTimeMillis() - 936748800000L) >> 1; // 1999-9-9
		
			for (int i=2; i >= 0; i--) {
				chs[i] = x10s[(int)(v % 10)];
				v = v / 10;
			}

		
			UUID u = UUID.randomUUID();
			v = u.getLeastSignificantBits() ^ u.getMostSignificantBits();
			if (v < 0) {
				v = -v;
			}

			for (int i=3; i<shortLength; i++) {
				chs[i] = x10s[(int)(v % 10)];
				v = v / 10;
			}

			return Long.parseLong(new String(chs));
		}
		
		public static String newId(String startWith){
			
			if(StringUtils.isBlank(startWith)){
				startWith="";
			}
			
			StringBuilder stingBu=new StringBuilder();
			stingBu.append(startWith);
			
			char x36s[] = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
			short len = (short)(20-startWith.length());
			char[] chs = new char[len];

			long v = (System.currentTimeMillis() - 936748800000L) >> 1;
			for (int i=7; i>0; i--) {
				chs[i] = x36s[(int)(v % 36)];
				v = v / 36;
			}
			chs[0] = x36s[(int)(v % 26) + 10];
			
			chs[len-1] = x36s[(int)(v % 26) + 10];

			UUID u = UUID.randomUUID();
			v = u.getLeastSignificantBits() ^ u.getMostSignificantBits();
			if (v < 0) {
				v = -v;
			}

			for (int i=8; i<len-1; i++) {
				chs[i] = x36s[(int)(v % 36)];
				v = v / 36;
			}
			stingBu.append(new String(chs));

			return stingBu.toString();
		}
		
		public static String macAddress(char split){
			
			
			char x62s[] = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

		
			short len = 17;
			
			int splitLen = 0;
		
			char[] chs = new char[len];

			long v = (System.currentTimeMillis() - 936748800000L) >> 1; // 1999-9-9
			
			for (int i = 0; i < 9; i++) {
				if (splitLen > 0 && splitLen % 2 == 0) {
					chs[i] = split;
					splitLen = 0;
				} else {
					chs[i] = x62s[(int)(v % 62)];
					splitLen = splitLen + 1;
				}
				
				v = v / 36;
			}

		
			UUID u = UUID.randomUUID();
			v = u.getLeastSignificantBits() ^ u.getMostSignificantBits();
			if (v < 0) {
				v = -v;
			}

			for (int i = 9; i < len; i++) {
				if(splitLen > 0 && splitLen % 2 == 0){
					chs[i] = split;
					splitLen = 0;
				} else {
					chs[i] = x62s[(int)(v % 62)];
					splitLen = splitLen + 1;
				}
				
				v = v / 36;
			}

			return new String(chs);
		}
		
		public static String macAddress() {
			
			String mac = "";
			
			Random random = new Random();
			
			int[] sn = {
					0x00, 0x16, 0x3e,
					random.nextInt(0x7f + 1),
					random.nextInt(0xff + 1),
					random.nextInt(0xff + 1)
			};
			
			for(int i = 0; i < sn.length; i++) {
				mac += String.format("%02x", sn[i]) + ":";
			}
			
			return mac.substring(0, mac.lastIndexOf(":"));
		}
		public static String newSerialno(int k) {
			
			
			char x36s[] = "0123456789abcdefghigklmnopqrstuvwxyz".toCharArray();

			
			int len = k;
			
		
			char[] chs = new char[len];

		
			long v = (System.currentTimeMillis() - 936748800000L) >> 1; // 1999-9-9
		
			for (int i=7; i>0; i--) {
				chs[i] = x36s[(int)(v % 36)];
				v = v / 36;
			}
			chs[0] = x36s[(int)(v % 26) + 10]; 
		
			UUID u = UUID.randomUUID();
			v = u.getLeastSignificantBits() ^ u.getMostSignificantBits();
			if (v < 0) {
				v = -v;
			}

			for (int i=8; i<len; i++) {
				chs[i] = x36s[(int)(v % 36)];
				v = v / 36;
			}

			return new String(chs);
		}
	}


