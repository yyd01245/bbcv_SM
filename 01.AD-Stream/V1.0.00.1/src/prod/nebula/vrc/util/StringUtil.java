package prod.nebula.vrc.util;
import java.util.*;
import java.text.*;
import java.io.*;

public class StringUtil {
	public static String getNowDate(){
		return (new SimpleDateFormat("yyyyMMddHHmmss")).format( new Date());
			
	}
	/*
前台的当前是期显示,包括星期
	 */
	@SuppressWarnings("deprecation")
	public static String getTodayDate(){
		String today="" ;
	    Date now =new Date();
	    //today+= now.toGMTString();
	    int week=now.getDay();
	    String weektemp="" ;
	    switch(week)
	    {
	    case 0 : weektemp="天" ; break;
	    case 1 : weektemp="一" ; break;
	    case 2 : weektemp="二" ; break;
	    case 3 : weektemp="三" ; break;
	    case 4 : weektemp="四" ; break;
	    case 5 : weektemp="五" ; break;
	    case 6 : weektemp="六" ; break;
	    }
	   
		today+=(now.getYear()+1900)+"年"+(now.getMonth()+1)+"月"+now.getDate()+"日"+"   "+"星期"+weektemp;
		return today ;	
	}
	/**格式化日期
	 *created by wxj on May 29,2001
	 *@Param strDate 待格式化字符串(yyyyMMddHHmmss)  (MM表示月份)
	 *@Param strFormat  格式 (可用格式为yyyy  yy  mm dd  hh HH mm ss) 
	 *return value:  if strDate is null, return "", 若无法格式化，return strDate
	 */
	public static String formatDate(String strDate, String strFormat){
		int index=0;
		String s="";   
		
		if(strDate == null)
			return "";
		
		//判断strDate是否包含非数字字符，如有  return strDate	
		for(int i=0; i < strDate.length(); i++){
			if(! Character.isDigit(strDate.charAt(i)))
				return strDate;
		}
		
		index = strFormat.indexOf("yyyy");
		if(index >= 0){
			s = strDate.substring(0,4);
			strFormat = replace(strFormat, "yyyy", s);
		}
		
		index = strFormat.indexOf("yy");
		if(index >= 0){
			s = strDate.substring(2,4);
			strFormat = replace(strFormat, "yy", s);
		}
		
		index = strFormat.indexOf("MM");
		if(index >= 0){
			s = strDate.substring(4,6);
			strFormat = replace(strFormat, "MM", s);
		}
		
		index = strFormat.indexOf("dd");
		if(index >= 0){
			s = strDate.substring(6,8);
			strFormat = replace(strFormat, "dd", s);
		}
		
		//24小时制
		index = strFormat.indexOf("HH");
		if(index >= 0){
			s = strDate.substring(8,10);
			strFormat = replace(strFormat, "HH", s);
		}
		
		//12小时制
		index = strFormat.indexOf("hh");
		if(index >= 0){
			s = strDate.substring(8,10);
			if(Integer.parseInt(s) >12){
				s = Integer.toString(Integer.parseInt(s) - 12);
			}
			strFormat = replace(strFormat, "hh", s);
		}
		
		index = strFormat.indexOf("mm");
		if(index >= 0){
			s = strDate.substring(10,12);
			strFormat = replace(strFormat, "mm", s);
		}
		
		index = strFormat.indexOf("ss");
		if(index >= 0){
			s = strDate.substring(12);
			strFormat = replace(strFormat, "ss", s);
		}
		
		return strFormat;
	}

	
	/**格式化日期
	 *created by pike
	 *@Param strDate 待格式化字符串(yyyy-MM-dd HH:mm:ss)  (MM表示月份)
	 	 
	 */	
	public static String formatDate2(String strDate, String strFormat)
		 {
			String ret="";
			try{
	
			SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			java.util.Date ct=formatter.parse(strDate);
			formatter=new SimpleDateFormat(strFormat);
			ret= formatter.format(ct);
			
			}
			catch(Exception e)
			 {
			 }
			return ret;
		 }
	
	
	
	
	
	
	
	//function static String addQuote(String str) written by wxj on May 11,2001
	//把字符串中的单引号改为'', addQuote("abc'") return  abc''
	//该函数主要用于字符串插入数据库前的操作
	public static String addQuote(String str){
	   return replace(str,"'","''");	   		
	}	
	
	//把str中的所有oldStr替换为newStr
	public static String replace(String str, String oldStr, String newStr){
	   if (str != null){
		int index =0;
		int oldLen = oldStr.length();   //oldStr字符串长度
		int newLen = newStr.length();   //newStr字符串长度
		
		while(true){
			index = str.indexOf (oldStr,index);
			
			if(index == -1){
				return str;
			}
			else{
				str =str.substring(0,index) + newStr + str.substring (index + oldLen);
				index +=newLen;
			}
		}
	   }
	   else{
	   	return "";
	   }	   		
	}	
	
	//把一个字符串转换为html格式
	//替换blank space为&nbsp; 换行为<br>
	public static String strToHtml(String str, boolean supportHtml){
		if (str == null){
	   		return "";
		}
		
		str = replace(str," ","&nbsp;");
		str = replace(str,"\r\n","<br>");
		
		if(supportHtml == false){
			str = replace(str,"&","&amp;");
			str = replace(str,"<","&lt;");
		}
		return str;
	}
	
	public static String strToShow(String str,String showstr){
		if (str == null){
	   		return "";
		}
		//showstr="扩大反函数";
		
		
		str = replace(str," ","&nbsp;");
		str = replace(str,"\n","<br>");
		
		if (showstr.equals("")){
		}else{
			String repstr="<font color=green><b>"+showstr+"</b></font>";	
			str = replace(str,showstr,repstr);
		}		
		
		
		return str;
	}
	//默认支持html语法
	public static String strToHtml(String str){
		return strToHtml(str,true);
	}
	
//	//取web  根路径 
//	public static String getWebDir(javax.servlet.ServletContext application ){
//		String strWebDir=application.getRealPath("/");
//		//strWebDir = strWebDir.substring(0,strWebDir.length());		
//		return strWebDir; 
//	}
//	
//	//取路径分隔符"/" or "\"
//	public static String getPathSplit(javax.servlet.ServletContext application ){
//		String strWebDir=application.getRealPath("/");
//		if(strWebDir.charAt(0) == '/')
//			return "/";
//		else
//			return "\\";
//	}
	//处理空串
	public static String processNull(String str){
		if(str == null)
			return "";
		else
			return str;	 
	}

	//处理乱码中文
	public static String cn_process(String cn_str) throws IOException{
		String str = null;
		if(cn_str != null){
			str = new String(cn_str.getBytes("8859_1"));
		}	
		
		return str;
	}

	public static String   databaseEncode(String origin) {

		if(origin == null) return null;
		try {
			byte[] b = origin.getBytes("gb2312");//
			origin = new String(b, "ISO-8859-1");
		} catch(Exception e) {
			e.printStackTrace();
		}
    	return origin;
	}

	//字符串中子串的个数
	public static int countSubstr(String str,String substr)
	{
		int count=0;
		int index=0;
		while (index<str.length())
		{
			index=str.indexOf(substr,index);
			if (index>-1)
			{	
				count++;
				index=index+substr.length();
			}
			else
				break;

		}
		return count;
	}


	//分割字符串为数组
	@SuppressWarnings("unused")
	public static String[] split2(String str,String substr)
	{
		int substrnum=countSubstr(str,substr);
		String[] ret=new String[substrnum];
		int count=0;
		int index=0;
		int temp=0;
		for (int i=0;i<substrnum;i++)
		{
			temp=str.indexOf(substr,index);
			if (temp>-1)
			{	
				ret[i]=str.substring(index,temp);
				index=temp+substr.length();
			}
		
		}
		
		return ret;
	}
	public static String[] split(String str,String substr)  //substr必须在每个拆分元素之后都有如"aaa|bbb|ccc|"
	{
		StringTokenizer t= new StringTokenizer(str,substr);
		int substrnum=countSubstr(str,substr);
		String[] ret=new String[substrnum];
		int i=0;

    	while(t.hasMoreTokens()&&i<substrnum)
		{
			ret[i]=t.nextToken();
			i++;
		}
		for (int j=i;j<substrnum ;j++ )
		{
			ret[j]="";
		}
		return ret;
	}
	
	public static String repeatStr(String copystr,int num){
		if(num<1)
		  return copystr;
		else{
		  String ret="";	
		  for(int i=0;i<num;i++)	
			ret += copystr;
		  return ret;
		}
		
		
	}
	public static String ToBeString(Object obj){
		if(obj!=null)
		  return obj.toString();
		else{		  
		  return "";
		}
	}
	/**
	 * 通过传入string，右边补足空格
	 * <p>返回 <code>null</code> 如果没有成功 .
	 * @param str 需要补足的字符串对象
	 * @param length 需要补足到多少个字符
	 * @return String 对象,如果成功返回补足的字符串； 或者 <code>null</code> 如果没有成功
	 */
	public static String Rspace(String str,int length)   
	  {   
		  if(str.length()>=length)
			  return str;   
		  char[] c = new char[length];   
		  Arrays.fill(c,' ');//初始化为全空格   
		  char[] cs = str.toCharArray();   
		    
		  System.arraycopy(cs,0,c,0,cs.length);   
		  return new String(c,0,c.length);   
	  }
	/**
	 * 通过传入sql和要增加的and条件，自动向sql增加
	 * <p>返回 <code>null</code> 如果没有成功 .
	 * @param sql 需要处理的sql
	 * @param appendStr 需要增加的and条件
	 * @return String 对象,如果成功返回处理后的sql； 或者返回原sql 如果没有成功
	 */
	public static String sqlAppendAnd(String sql,String appendStr){
		if(sql!=null&&!sql.equals("")){
			if(sql.indexOf(" and "+appendStr)>0)//若已有该字段被and过，则返回原sql
				return sql;
			int where = sql.indexOf(" where ");
			String sql1,sql2;
			if(where>0){//sql中已有where字符串，分割后将新加的条件放在where之后
				sql1 = sql.substring(0,sql.indexOf(" where "));
				sql2 = sql.substring(where+7, sql.length());
				sql = sql1 + " where " + appendStr + " and " + sql2;
			}else{
				sql = sql + " where " + appendStr;
			}
		}
		return sql;
	}
	/**
	 * 通过传入str判断是否不为空
	 * <p>返回 <code>false</code> 如果为空 .
	 * @param str 需要判断的str
	 * @return <code>true</code> 如果不为空； 或者返回false 如果为空
	 */
	public static boolean assertNotNull(String str){
		boolean notnull = false;
		if(str!=null&&!str.equals("")&&!str.equals("null"))
			return true;
		return notnull;
	}
	/**
	 * 通过传入sql和要增加的insert字段，自动向sql增加
	 * <p>返回 <code>null</code> 如果没有成功 .
	 * @param sql 需要处理的sql
	 * @param appendColumn 需要增加的字段
	 * @param appendValue 需要增加的字段值
	 * @return String 对象,如果成功返回处理后的sql； 或者返回原sql 如果没有成功
	 */
	public static String sqlAppendInsert(String sql,String appendColumn,String appendValue){
		if(sql!=null&&!sql.equals("")){
			int values = sql.indexOf(") values(");
			int begin = sql.indexOf("(");
			String sql1,sql2;
			if(values>0){
				if((values-begin)>1){//若sql中已有字段，则先加","后加字段
					sql1 = sql.substring(0,values);
					sql1 = sql1 + "," + appendColumn;
					sql2 = sql.substring(values, sql.length()-1);
					sql2 = sql2 + ",'" + appendValue + "'";
				}else{//否则不加","
					sql1 = sql.substring(0,values);
					sql1 = sql1 + appendColumn;
					sql2 = sql.substring(values, sql.length()-1);
					sql2 = sql2 + "'" + appendValue + "'";
				}
				sql = sql1 + sql2 + ")";
			}
		}
		return sql;
	}
	/**
	 * 通过传入sql和要增加的insert字段，自动向sql增加(包括日期型数据)
	 * <p>返回 <code>null</code> 如果没有成功 .
	 * @param sql 需要处理的sql
	 * @param appendColumn 需要增加的字段
	 * @param appendValue 需要增加的字段值
	 * @param columnType 需要增加的字段类型
	 * @return String 对象,如果成功返回处理后的sql； 或者返回原sql 如果没有成功
	 */
	public static String sqlAppendInsert(String sql,String appendColumn,String appendValue,String columnType){
		if(sql!=null&&!sql.equals("")){
			int values = sql.indexOf(") values(");
			int begin = sql.indexOf("(");
			String sql1,sql2;
			if(values>0){
				if((values-begin)>1){//若sql中已有字段，则先加","后加字段
					sql1 = sql.substring(0,values);
					sql1 = sql1 + "," + appendColumn;
					sql2 = sql.substring(values, sql.length()-1);
					if(columnType.equals("date")){//若为日期型字段
						if(appendValue.equals("")){//无值，默认插入sysdate
							sql2 = sql2 + ",sysdate";
						}else{
							sql2 = sql2 + ",to_date('" + appendValue + "','yyyy-MM-dd hh24:mi:ss')";
						}
					}else{
						sql2 = sql2 + ",'" + appendValue + "'";
					}
				}else{//否则不加","
					sql1 = sql.substring(0,values);
					sql1 = sql1 + appendColumn;
					sql2 = sql.substring(values, sql.length()-1);
					if(columnType.equals("date")){//若为日期型字段
						if(appendValue.equals("")){//无值，默认插入sysdate
							sql2 = sql2 + "sysdate";
						}else{
							sql2 = sql2 + "to_date('" + appendValue + "','yyyy-MM-dd hh24:mi:ss')";
						}
					}else{
						sql2 = sql2 + "'" + appendValue + "'";
					}
				}
				sql = sql1 + sql2 + ")";
			}
		}
		return sql;
	}
	/**
	 * 通过传入sql和要增加的update字段，自动向sql增加
	 * <p>返回 <code>null</code> 如果没有成功 .
	 * @param sql 需要处理的sql
	 * @param appendColumn 需要增加的字段
	 * @param appendValue 需要增加的字段值
	 * @return String 对象,如果成功返回处理后的sql； 或者返回原sql 如果没有成功
	 */
	public static String sqlAppendUpdate(String sql,String appendColumn,String appendValue){
		if(sql!=null&&!sql.equals("")){
			int where = sql.indexOf("where ");
			int begin = sql.indexOf(" set ");
			String sql1,sql2;
			if(where>0){
				if((where-begin)>5){//若sql中已有字段，则先加","后加字段
					sql1 = sql.substring(0,where);
					sql1 = sql1 + "," + appendColumn+" = '"+appendValue+"' ";
					sql2 = sql.substring(where, sql.length());
				}else{//否则不加","
					sql1 = sql.substring(0,where);
					sql1 = sql1 + appendColumn+" = '"+appendValue+"' ";
					sql2 = sql.substring(where, sql.length());
				}
				sql = sql1 + sql2 ;
			}
		}
		return sql;
	}
	/**
	 * 通过传入sql和要增加的update字段，自动向sql增加
	 * <p>返回 <code>null</code> 如果没有成功 .
	 * @param sql 需要处理的sql
	 * @param appendColumn 需要增加的字段
	 * @param appendValue 需要增加的字段值
	 * @param columnType 需要增加的字段类型
	 * @return String 对象,如果成功返回处理后的sql； 或者返回原sql 如果没有成功
	 */
	public static String sqlAppendUpdate(String sql,String appendColumn,String appendValue,String columnType){
		if(sql!=null&&!sql.equals("")){
			int where = sql.indexOf("where ");
			int begin = sql.indexOf(" set ");
			String sql1,sql2;
			if(where>0){
				if((where-begin)>5){//若sql中已有字段，则先加","后加字段
					sql1 = sql.substring(0,where);
					if(columnType.equals("date")){//若为日期型字段
						if(appendValue.equals("")){//无值，默认插入sysdate
							sql1 = sql1 + "," + appendColumn+" = sysdate ";
						}else{
							sql1 = sql1 + "," + appendColumn+" =to_date('" + appendValue + "','yyyy-MM-dd hh24:mi:ss')";
						}
					}else{
						sql1 = sql1 + "," + appendColumn+" = '"+appendValue+"' ";
					}					
					sql2 = sql.substring(where, sql.length());
				}else{//否则不加","
					sql1 = sql.substring(0,where);
					if(columnType.equals("date")){//若为日期型字段
						if(appendValue.equals("")){//无值，默认插入sysdate
							sql1 = sql1 + appendColumn+" = sysdate ";
						}else{
							sql1 = sql1 + appendColumn+" =to_date('" + appendValue + "','yyyy-MM-dd hh24:mi:ss')";
						}
					}else{
						sql1 = sql1 + appendColumn+" = '"+appendValue+"' ";
					}
					sql2 = sql.substring(where, sql.length());
				}
				sql = sql1 + sql2 ;
			}
		}
		return sql;
	}
	public static String firstCharUpperCase(String str){
		StringBuffer sb = new StringBuffer();
		sb.append(Character.toTitleCase(str.charAt(0)));
		sb.append(str.substring(1)).toString();
		return sb.toString();
	}
	 /**
	 * example : replaceUnderlineAndfirstToUpper("ni_hao_abc","_","");
	 */
	public static String replaceUnderlineAndfirstToUpper(String srcStr,String org,String ob)  
	{  
		String newString = "";  
		int first=0;  
		while(srcStr.indexOf(org)!=-1)  
		{  
			first=srcStr.indexOf(org);  
			if(first!=srcStr.length())  
			{  
				newString=newString+srcStr.substring(0,first)+ob;  
				srcStr=srcStr.substring(first+org.length(),srcStr.length());  
				srcStr=firstCharUpperCase(srcStr);  
			}  
	   }  
	   newString=newString+srcStr;  
	   return newString;  
	} 
	public static String getFormatVarName(String str){
		str = StringUtil.firstCharUpperCase(str);
		str = StringUtil.replaceUnderlineAndfirstToUpper(str, "_", "");
		return str;
	}
}
