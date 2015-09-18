package prod.nebula.service.socket.center;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class StringUtil {
	public static String getNowDate(String format){
		return (new SimpleDateFormat(format)).format( new Date());
	}
	
	public static boolean assertNotNull(String str){
		boolean notnull = false;
		if(str!=null&&!str.equals("")&&!str.equals("null"))
			return true;
		return notnull;
	}
	
    public static Object BytetoObject (byte[] bytes) {     
        Object obj = null;     
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream (bytes);       
            ObjectInputStream ois = new ObjectInputStream (bis);       
            obj = ois.readObject();     
            ois.close();  
            bis.close();  
        } catch (IOException ex) {       
            ex.printStackTrace();  
        } catch (ClassNotFoundException ex) {       
            ex.printStackTrace();  
        }     
        return obj;   
    }  
    
    
    public static byte[] toLH(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}


	public static byte[] toLH(float f) {
		return toLH(Float.floatToRawIntBits(f));
	}

	public static byte[] toLH(short s) {
		byte[] b = new byte[2];
		b[0] = (byte) (s & 0xff);
		b[1] = (byte) (s >> 8 & 0xff);
		return b;
	}
	
	
	
}
