package org.eredlab.g4.common.util;
import java.util.*;
import java.text.*;
import java.io.*;

public class StringUtil {
	public static String getNowDate(){
		return (new SimpleDateFormat("yyyyMMddHHmmss")).format( new Date());
			
	}
	/*
ǰ̨�ĵ�ǰ������ʾ,��������
	 */
	public static String getTodayDate(){
		String today="" ;
	    Date now =new Date();
	    //today+= now.toGMTString();
	    int week=now.getDay();
	    String weektemp="" ;
	    switch(week)
	    {
	    case 0 : weektemp="��" ; break;
	    case 1 : weektemp="һ" ; break;
	    case 2 : weektemp="��" ; break;
	    case 3 : weektemp="��" ; break;
	    case 4 : weektemp="��" ; break;
	    case 5 : weektemp="��" ; break;
	    case 6 : weektemp="��" ; break;
	    }
	   
		today+=(now.getYear()+1900)+"��"+(now.getMonth()+1)+"��"+now.getDate()+"��"+"   "+"����"+weektemp;
		return today ;	
	}
	/**��ʽ������
	 *created by wxj on May 29,2001
	 *@Param strDate ���ʽ���ַ�(yyyyMMddHHmmss)  (MM��ʾ�·�)
	 *@Param strFormat  ��ʽ (���ø�ʽΪyyyy  yy  mm dd  hh HH mm ss) 
	 *return value:  if strDate is null, return "", ���޷���ʽ����return strDate
	 */
	public static String formatDate(String strDate, String strFormat){
		int index=0;
		String s="";   
		
		if(strDate == null)
			return "";
		
		//�ж�strDate�Ƿ��������ַ�����  return strDate	
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
		
		//24Сʱ��
		index = strFormat.indexOf("HH");
		if(index >= 0){
			s = strDate.substring(8,10);
			strFormat = replace(strFormat, "HH", s);
		}
		
		//12Сʱ��
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

	
	/**��ʽ������
	 *created by pike
	 *@Param strDate ���ʽ���ַ�(yyyy-MM-dd HH:mm:ss)  (MM��ʾ�·�)
	 	 
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
				System.out.print(e.getMessage());
			 }

			return ret;
		 }
	
	
	
	
	
	
	
	//function static String addQuote(String str) written by wxj on May 11,2001
	//���ַ��еĵ���Ÿ�Ϊ'', addQuote("abc'") return  abc''
	//�ú�����Ҫ�����ַ������ݿ�ǰ�Ĳ���
	public static String addQuote(String str){
	   return replace(str,"'","''");	   		
	}	
	
	//��str�е�����oldStr�滻ΪnewStr
	public static String replace(String str, String oldStr, String newStr){
	   if (str != null){
		int index =0;
		int oldLen = oldStr.length();   //oldStr�ַ���
		int newLen = newStr.length();   //newStr�ַ���
		
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
	
	//��һ���ַ�ת��Ϊhtml��ʽ
	//�滻blank spaceΪ&nbsp; ����Ϊ<br>
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
		//showstr="���󷴺���";
		
		
		str = replace(str," ","&nbsp;");
		str = replace(str,"\n","<br>");
		
		if (showstr.equals("")){
		}else{
			String repstr="<font color=green><b>"+showstr+"</b></font>";	
			str = replace(str,showstr,repstr);
		}		
		
		
		return str;
	}
	//Ĭ��֧��html�﷨
	public static String strToHtml(String str){
		return strToHtml(str,true);
	}
	
	//ȡweb  ��·�� 
	public static String getWebDir(javax.servlet.ServletContext application ){
		String strWebDir=application.getRealPath("/");
		//strWebDir = strWebDir.substring(0,strWebDir.length());		
		return strWebDir; 
	}
	
	//ȡ·���ָ���"/" or "\"
	public static String getPathSplit(javax.servlet.ServletContext application ){
		String strWebDir=application.getRealPath("/");
		if(strWebDir.charAt(0) == '/')
			return "/";
		else
			return "\\";
	}
	//����մ�
	public static String processNull(String str){
		if(str == null)
			return "";
		else
			return str;	 
	}

	//������������
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

	//�ַ����Ӵ��ĸ���
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


	//�ָ��ַ�Ϊ����
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
	public static String[] split(String str,String substr)  //substr������ÿ�����Ԫ��֮������"aaa|bbb|ccc|"
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
	 * ͨ����string���ұ߲���ո�
	 * <p>���� <code>null</code> ���û�гɹ� .
	 * @param str ��Ҫ������ַ����
	 * @param length ��Ҫ���㵽���ٸ��ַ�
	 * @return String ����,���ɹ����ز�����ַ� ���� <code>null</code> ���û�гɹ�
	 */
	public static String Rspace(String str,int length)   
	  {   
		  if(str.length()>=length)
			  return str;   
		  char[] c = new char[length];   
		  Arrays.fill(c,' ');//��ʼ��Ϊȫ�ո�   
		  char[] cs = str.toCharArray();   
		    
		  System.arraycopy(cs,0,c,0,cs.length);   
		  return new String(c,0,c.length);   
	  }
	/**
	 * ͨ����sql��Ҫ���ӵ�and�������Զ���sql����
	 * <p>���� <code>null</code> ���û�гɹ� .
	 * @param sql ��Ҫ�����sql
	 * @param appendStr ��Ҫ���ӵ�and����
	 * @return String ����,���ɹ����ش�����sql�� ���߷���ԭsql ���û�гɹ�
	 */
	public static String sqlAppendAnd(String sql,String appendStr){
		if(sql!=null&&!sql.equals("")){
			if(sql.indexOf(" and "+appendStr)>0)//�����и��ֶα�and���򷵻�ԭsql
				return sql;
			int where = sql.indexOf(" where ");
			String sql1,sql2;
			if(where>0){//sql������where�ַ��ָ���¼ӵ���������where֮��
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
	 * ͨ����str�ж��Ƿ�Ϊ��
	 * <p>���� <code>false</code> ���Ϊ�� .
	 * @param str ��Ҫ�жϵ�str
	 * @return <code>true</code> ���Ϊ�գ� ���߷���false ���Ϊ��
	 */
	public static boolean assertNotNull(String str){
		boolean notnull = false;
		if(str!=null&&!str.equals("")&&!str.equals("null"))
			return true;
		return notnull;
	}
	/**
	 * ͨ����sql��Ҫ���ӵ�insert�ֶΣ��Զ���sql����
	 * <p>���� <code>null</code> ���û�гɹ� .
	 * @param sql ��Ҫ�����sql
	 * @param appendColumn ��Ҫ���ӵ��ֶ�
	 * @param appendValue ��Ҫ���ӵ��ֶ�ֵ
	 * @return String ����,���ɹ����ش�����sql�� ���߷���ԭsql ���û�гɹ�
	 */
	public static String sqlAppendInsert(String sql,String appendColumn,String appendValue){
		if(sql!=null&&!sql.equals("")){
			int values = sql.indexOf(") values(");
			int begin = sql.indexOf("(");
			String sql1,sql2;
			if(values>0){
				if((values-begin)>1){//��sql�������ֶΣ����ȼ�","����ֶ�
					sql1 = sql.substring(0,values);
					sql1 = sql1 + "," + appendColumn;
					sql2 = sql.substring(values, sql.length()-1);
					sql2 = sql2 + ",'" + appendValue + "'";
				}else{//���򲻼�","
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
	 * ͨ����sql��Ҫ���ӵ�insert�ֶΣ��Զ���sql����(�������������)
	 * <p>���� <code>null</code> ���û�гɹ� .
	 * @param sql ��Ҫ�����sql
	 * @param appendColumn ��Ҫ���ӵ��ֶ�
	 * @param appendValue ��Ҫ���ӵ��ֶ�ֵ
	 * @param columnType ��Ҫ���ӵ��ֶ�����
	 * @return String ����,���ɹ����ش�����sql�� ���߷���ԭsql ���û�гɹ�
	 */
	public static String sqlAppendInsert(String sql,String appendColumn,String appendValue,String columnType){
		if(sql!=null&&!sql.equals("")){
			int values = sql.indexOf(") values(");
			int begin = sql.indexOf("(");
			String sql1,sql2;
			if(values>0){
				if((values-begin)>1){//��sql�������ֶΣ����ȼ�","����ֶ�
					sql1 = sql.substring(0,values);
					sql1 = sql1 + "," + appendColumn;
					sql2 = sql.substring(values, sql.length()-1);
					if(columnType.equals("date")){//��Ϊ�������ֶ�
						if(appendValue.equals("")){//��ֵ��Ĭ�ϲ���sysdate
							sql2 = sql2 + ",sysdate";
						}else{
							sql2 = sql2 + ",to_date('" + appendValue + "','yyyy-MM-dd hh24:mi:ss')";
						}
					}else{
						sql2 = sql2 + ",'" + appendValue + "'";
					}
				}else{//���򲻼�","
					sql1 = sql.substring(0,values);
					sql1 = sql1 + appendColumn;
					sql2 = sql.substring(values, sql.length()-1);
					if(columnType.equals("date")){//��Ϊ�������ֶ�
						if(appendValue.equals("")){//��ֵ��Ĭ�ϲ���sysdate
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
	 * ͨ����sql��Ҫ���ӵ�update�ֶΣ��Զ���sql����
	 * <p>���� <code>null</code> ���û�гɹ� .
	 * @param sql ��Ҫ�����sql
	 * @param appendColumn ��Ҫ���ӵ��ֶ�
	 * @param appendValue ��Ҫ���ӵ��ֶ�ֵ
	 * @return String ����,���ɹ����ش�����sql�� ���߷���ԭsql ���û�гɹ�
	 */
	public static String sqlAppendUpdate(String sql,String appendColumn,String appendValue){
		if(sql!=null&&!sql.equals("")){
			int where = sql.indexOf("where ");
			int begin = sql.indexOf(" set ");
			String sql1,sql2;
			if(where>0){
				if((where-begin)>5){//��sql�������ֶΣ����ȼ�","����ֶ�
					sql1 = sql.substring(0,where);
					sql1 = sql1 + "," + appendColumn+" = '"+appendValue+"' ";
					sql2 = sql.substring(where, sql.length());
				}else{//���򲻼�","
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
	 * ͨ����sql��Ҫ���ӵ�update�ֶΣ��Զ���sql����
	 * <p>���� <code>null</code> ���û�гɹ� .
	 * @param sql ��Ҫ�����sql
	 * @param appendColumn ��Ҫ���ӵ��ֶ�
	 * @param appendValue ��Ҫ���ӵ��ֶ�ֵ
	 * @param columnType ��Ҫ���ӵ��ֶ�����
	 * @return String ����,���ɹ����ش�����sql�� ���߷���ԭsql ���û�гɹ�
	 */
	public static String sqlAppendUpdate(String sql,String appendColumn,String appendValue,String columnType){
		if(sql!=null&&!sql.equals("")){
			int where = sql.indexOf("where ");
			int begin = sql.indexOf(" set ");
			String sql1,sql2;
			if(where>0){
				if((where-begin)>5){//��sql�������ֶΣ����ȼ�","����ֶ�
					sql1 = sql.substring(0,where);
					if(columnType.equals("date")){//��Ϊ�������ֶ�
						if(appendValue.equals("")){//��ֵ��Ĭ�ϲ���sysdate
							sql1 = sql1 + "," + appendColumn+" = sysdate ";
						}else{
							sql1 = sql1 + "," + appendColumn+" =to_date('" + appendValue + "','yyyy-MM-dd hh24:mi:ss')";
						}
					}else{
						sql1 = sql1 + "," + appendColumn+" = '"+appendValue+"' ";
					}					
					sql2 = sql.substring(where, sql.length());
				}else{//���򲻼�","
					sql1 = sql.substring(0,where);
					if(columnType.equals("date")){//��Ϊ�������ֶ�
						if(appendValue.equals("")){//��ֵ��Ĭ�ϲ���sysdate
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
