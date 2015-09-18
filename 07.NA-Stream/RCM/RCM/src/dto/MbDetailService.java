/**  
 * ����ƣ�TvNavigateService 
 * �������� 
 * �����ˣ�PengFei   
 * ����ʱ�䣺2014-9-26 ����01:08:48 
 */
package dto;

import hibernate.HibernateSessionFactory;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pojo.MobileDetailPage;
import pojo.Page;
import pojo.TvNavigate;

/**   
 * ����ƣ�TvNavigateService   
 * ��������   
 * �����ˣ�PengFei   
 * ����ʱ�䣺2014-9-26 ����01:08:48   
 * ��ע��   
 * @version    
 *    
 */
public class MbDetailService {
   
	private static final Logger logger = LoggerFactory.getLogger(MbDetailService.class);
	
	
	public MobileDetailPage getTvNavigate(String id){
		 Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		 
		MobileDetailPage tn=	(MobileDetailPage) session.get(MobileDetailPage.class, Integer.parseInt(id));
		ts.commit();
		session.close();
		return tn;
	}
	
	
	public void add(MobileDetailPage tn){
		 Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		 
		session.save(tn);
		ts.commit();
		session.close();
		
		
	}
	
	public void update(MobileDetailPage tn){
		 Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		
		Query query = session.createQuery(" update MobileDetailPage t set t.url='"+tn.getUrl()+"' where t.id="+tn.getId());
		query.executeUpdate();
		
		ts.commit();
		session.close();
		
	}
	
	public void updateUseState(MobileDetailPage tn){
		 Session session = HibernateSessionFactory.getSession();

		Transaction ts = 	session.beginTransaction();
		
		
		String sql = " update MobileDetailPage set useState=0 where useState=1";
		
		Query query = session.createQuery(sql);
		query.executeUpdate();
		
		
		 String sql1 = " update MobileDetailPage set useState=1 where id="+tn.getId();
		 session.createQuery(sql1).executeUpdate();
	
		ts.commit();
		session.close();
		
	}	
	
	   public void updateState(MobileDetailPage tn){
		   Session session = HibernateSessionFactory.getSession();

		Transaction ts = 	session.beginTransaction();

		 String sql = " update MobileDetailPage set state=1 where id="+tn.getId();
		 session.createQuery(sql).executeUpdate();
	
		ts.commit();
		session.close();
		
	}	
	
	   public int getTvNavigetTotal(){
		   Session session = HibernateSessionFactory.getSession();
			String hql = " from MobileDetailPage where state=0";
		    Query query = 	session.createQuery(hql);
		   int count = query.list().size();
		   
		   
		   return count;
	   }
	   
	public List<MobileDetailPage> findTNList(Page p,String ascendName  ,String ascending ,String urlKey){
		 Session session = HibernateSessionFactory.getSession();
	//	DetachedCriteria dc = DetachedCriteria.forClass(TvNavigate.class);
	//    Criteria c = dc.getExecutableCriteria(session);
	//    List rs = c.list();
		String ascOrdesc = null;
	    if(true==Boolean.parseBoolean(ascending)){
	    	ascOrdesc = "asc";
	    }else{
	    	ascOrdesc = "desc";
	    }
		StringBuffer sb = new StringBuffer(" from MobileDetailPage where state=0  ");
		
		if(urlKey!=null && !"".equals(urlKey)){
			sb.append(" and url like '%"+urlKey+"%'");
		}
		
		if(ascendName!=null && !"".equals(ascendName)){
			sb.append(" order by "+ascendName);
		}
		if(ascending!=null && !"".equals(ascending)){
			
			sb.append(" "+ascOrdesc);
		}
	
		
		
		

	    Query query = 	session.createQuery(sb.toString());
	  
	   
	    query.setMaxResults(p.getPerPageRecords());
	    query.setFirstResult(p.getCurrentPage()==1?0:((p.getCurrentPage()-1)*p.getPerPageRecords()));
	    List<MobileDetailPage>  list = query.list(); 

	 //   Session s = HibernateUtil.getSession();
	
	    return list;
		
		
		//return list;
	}

	
}
