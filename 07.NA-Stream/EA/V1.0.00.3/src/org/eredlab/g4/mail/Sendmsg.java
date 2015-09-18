package org.eredlab.g4.mail;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class Sendmsg {
	private static int i=0;
	private static int j=0;
	public boolean Senfmsginfo(String postbox_address,String title,String msg){
		Boolean ret = new Boolean(false);
		List postList = new ArrayList();
		postList.add("bbcvision@126.com");
		postList.add("bbcvisiontest@126.com");
		postList.add("bbcvision1@126.com");
		postList.add("bbcvision2@126.com");
		postList.add("bbcvision3@126.com");
		postList.add("bbcvision4@126.com");
		postList.add("bbcvision5@126.com");
		postList.add("bbcvision6@126.com");
		postList.add("bbcvision7@126.com");
		postList.add("bbcvision8@126.com");
		postList.add("bbcvision9@126.com");
		postList.add("bbcvision10@126.com");
		postList.add("bbcvision11@126.com");
		postList.add("bbcv12@126.com");
		/**********循环获取发送邮箱开始*************/
		String postbox =(String) postList.get(j+1);
		if(i>=10){
			while(j<postList.size()){
				if(j==postList.size()-2){
					j=-1;
				}
				postbox=(String) postList.get(j+1);
				j++;
				break;
			}
			i=0;
		}		
		/**********循环获取发送邮箱结束*************/
		MailSenderInfo mailInfo = new MailSenderInfo();    
	      mailInfo.setMailServerHost("smtp.126.com");    
	      mailInfo.setMailServerPort("25");    
	      mailInfo.setValidate(true);    
	      mailInfo.setUserName(postbox);    
	      mailInfo.setPassword("bbcv1234");//您的邮箱密码    
	      mailInfo.setFromAddress(postbox);    
	      mailInfo.setToAddress(postbox_address);    
	      mailInfo.setSubject(title);    
	      mailInfo.setContent(msg);    
	         //这个类主要来发送邮件   
	      SimpleMailSender sms = new SimpleMailSender();   
	      if(sms.sendTextMail(mailInfo)){//发送文体格式 
	    	  ret = true;
	    	  i++;
	      }   
//	      sms.sendHtmlMail(mailInfo);//发送html格式  
	      return ret;
	}

	public boolean SenfmsginfoGroup(String[] postboxAddresTo, String title, String msg) {
		Boolean ret = new Boolean(false);
		try {		
		List postList = new ArrayList();
		postList.add("bbcvision@126.com");
		postList.add("bbcvisiontest@126.com");
		postList.add("bbcvision1@126.com");
		postList.add("bbcvision2@126.com");
		postList.add("bbcvision3@126.com");
		postList.add("bbcvision4@126.com");
		postList.add("bbcvision5@126.com");
		postList.add("bbcvision6@126.com");
		postList.add("bbcvision7@126.com");
		postList.add("bbcvision8@126.com");
		postList.add("bbcvision9@126.com");
		postList.add("bbcvision10@126.com");
		postList.add("bbcvision11@126.com");
		postList.add("bbcv12@126.com");
		/**********循环获取发送邮箱开始*************/
		String postbox =(String) postList.get(j+1);
		if(i>=10){
			while(j<postList.size()){
				if(j==postList.size()-2){
					j=-1;
				}
				postbox=(String) postList.get(j+1);
				j++;
				break;
			}
			i=0;
		}		
		/**********循环获取发送邮箱结束*************/
		Properties p = new Properties();
		p.put("mail.smtp.auth", "true");     
        p.put("mail.transport.protocol", "smtp");     
        p.put("mail.smtp.host", "smtp.126.com");     
        p.put("mail.smtp.port", "25");
        MyAuthenticator MyAuth = new MyAuthenticator(postbox, "bbcv1234");
        Session session = Session.getInstance(p,MyAuth);
        Message message = new MimeMessage(session);        
		message.setFrom(new InternetAddress(postbox));//设置发件人
		String toList = getMailList(postboxAddresTo);
		InternetAddress[] iaToList = new InternetAddress().parse(toList);
		message.setRecipients(Message.RecipientType.TO, iaToList);//收件人组
		message.setSentDate(new Date());//发送日期
		message.setSubject(title); // 主题     
		message.setText(msg); //内容   
		Transport transport = session.getTransport("smtp");
		transport.connect("smtp.126.com", postbox, "bbcv1234");
		transport.send(message, message.getAllRecipients());//发送邮箱
		ret = true;
		i++;
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	      return ret;
	}
	private String getMailList(String[] mailArray){  
        
        StringBuffer toList = new StringBuffer();  
    int length = mailArray.length;  
        if(mailArray!=null && length <2){  
             toList.append(mailArray[0]);  
        }else{  
             for(int i=0;i<length;i++){  
                     toList.append(mailArray[i]);  
                     if(i!=(length-1)){  
                         toList.append(",");  
                     }  
  
             }  
         }  
     return toList.toString();  
  
}  
}
