/**  
 * ����ƣ�TvNavigateService 
 * �������� 
 * �����ˣ�PengFei   
 * ����ʱ�䣺2014-9-26 ����01:08:48 
 */
package dto;

import java.util.ArrayList;
import java.util.List;

import hibernate.HibernateSessionFactory;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pojo.Config;
import pojo.MobileDetailPage;
import pojo.MobileMainPage;
import pojo.Page;
import pojo.TvNavigate;
import pojo.VodResourceInfo;

import servlet.template.TvNavigateServlet;

/**   
 * ����ƣ�ConfigListService   
 * ��������   ����һ����Service
 * �����ˣ�PengFei   
 * ����ʱ�䣺2014-9-26 ����01:08:48   
 * ��ע��   
 * @version    
 *    
 */
public class ConfigListService {
   
	private static final Logger logger = LoggerFactory.getLogger(ConfigListService.class);
	
	
	
	

	public List<Config> getConfigList(){
		 Session session = HibernateSessionFactory.getSession();
		String hql1 = " from TvNavigate where state=0 and useState=1 ";
		String hql2 = " from MobileMainPage where state=0 and useState=1 ";
		String hql3 = " from MobileDetailPage where state=0 and useState=1 ";

	    Query query = 	session.createQuery(hql1);
	    TvNavigate  tn =  (TvNavigate) query.uniqueResult();
	    Config cf1 = new Config();
	    cf1.setId(String.valueOf(tn.getId()));
	    cf1.setName("TV����ģ��");
	    cf1.setUrl(tn.getUrl());
	    
	    Query query2 = 	session.createQuery(hql2);
	    MobileMainPage  mm =  (MobileMainPage) query2.uniqueResult(); 
	    
	    Config cf2 = new Config();
	    cf2.setId(String.valueOf(mm.getId()));
	    cf2.setName("�ֻ���ҳģ��");
	    cf2.setUrl(mm.getUrl());
	    
	    
	    Query query3 = 	session.createQuery(hql3);
	    MobileDetailPage  mdp =  (MobileDetailPage) query3.uniqueResult(); 
	    Config cf3 = new Config();
	    cf3.setId(String.valueOf(mdp.getId()));
	    cf3.setName("�ֻ�����ģ��");
	    cf3.setUrl(mdp.getUrl());
	    
	    List<Config> list = new ArrayList<Config>();
	    list.add(cf1);
	    list.add(cf2);
	    list.add(cf3);
	    
	   return list;

	}
	


	
}
