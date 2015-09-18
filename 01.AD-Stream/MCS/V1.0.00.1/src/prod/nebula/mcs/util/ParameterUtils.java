package prod.nebula.mcs.util;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ParameterUtils {

	public static String getString(Object object) {
		if (object == null) {
			return null;
		} 
		
		if (object.getClass().isArray()) {
			return String.valueOf(Array.get(object, 0));
		}
		return String.valueOf(object);
	}
	

	
	public static String getString(Map<String, Object> params, String key) {
		if (params == null || key == null) {
			return null;
		} else {
			return getString(params.get(key));
		}
	}

	public static int getInt(Map<String, Object> params, String key) {
		if (params == null || key == null) {
			return 0;
		} else {
			return Integer.parseInt( (String) params.get(key));
		}
	}
	
	public static Map<String, Object> getObjectMap(Map<String, String> map) {
		
		String key, value;
		List<String> keys = new ArrayList<String>(map.keySet());
		Map<String, Object> params = new HashMap<String, Object>();
		
		for (Iterator<String> i = keys.iterator(); i.hasNext();) {
			key = (String) i.next();
			value = getString(map.get(key));
			params.put(key, value);
		}
		
		return params;
	}

	public static Map<String, String> getStringMap(Map<String, Object> map) {
		
		Map<String, String> params = new HashMap<String, String>();
		
		for(Map.Entry<String, Object> e : map.entrySet()) {
			
			String value = "";
			
			if(e.getValue() instanceof String[]) {
				String[] arr = (String[]) e.getValue();
				for (String s : arr) {
					value += s + ",";
				}
				value = value.substring(0, value.lastIndexOf(','));
			} else {
				value = e.getValue().toString();
			}
			
			params.put(e.getKey(), value);
		}
		
		return params;
	}

	
	public static String getUrl(Map<String, String> params) {
		
		String key, value, split = "?";
		StringBuffer buffer = new StringBuffer();
		
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		
		for (Iterator<String> i = keys.iterator(); i.hasNext();) {
			key = (String) i.next();
			value = getString(params.get(key));
			buffer.append(split).append(key).append("=").append(value);
			split = "&";
		}
		
		return buffer.toString();
	}
}
