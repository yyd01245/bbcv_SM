
package dto;

import hibernate.HibernateSessionFactory;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pojo.BannerInfo;
import pojo.BannerVodMapping;
import pojo.Page;
import pojo.dao.BannerVodView;

/**   
 * BannerConfigService   
 * 
 * PengFei   
 * 2014-9-26  
 * 
 * @version    
 *    
 */
public class BannerConfigService {
   
private static final Logger logger = LoggerFactory.getLogger(BannerConfigService.class);
	
	
	
	
	public BannerVodView getTvNavigate(String id){
		 Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		 
		BannerVodView tn=	(BannerVodView) session.get(BannerVodView.class, Integer.parseInt(id));
		ts.commit();
		session.flush();
		return tn;
	}
	
	
	public void add(BannerVodMapping bvm){
		 Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		 
		session.save(bvm);
		ts.commit();
		session.close();
		
		
	}
	
	public void update(BannerVodMapping tn){
		 Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		
		
		Query query = session.createQuery(" update BannerVodMapping t set t.bannerId=? ,t.vodId=?,t.orderId=? ,t.resolution=? where t.id=?");

		query.setInteger(0, tn.getBannerId());
		query.setInteger(1, tn.getVodId());
		query.setInteger(2, tn.getOrderId());
		query.setString(3, tn.getResolution());
		query.setInteger(4,tn.getId());
		query.executeUpdate();
		System.out.println("query.getQueryString()= "+query.toString());
		ts.commit();
		session.close();
		
	}
	

	
	   public void updateState(BannerVodMapping tn){
		
		   Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();

		 String sql = " update BannerVodMapping set state=1 where id="+tn.getId();
		 session.createQuery(sql).executeUpdate();
	
		ts.commit();
		session.close();
		
	}	
	
	   public void updateSliderPoster(BannerVodMapping tn){
			
		   Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();

		 String sql = " update BannerVodMapping set sliderposter="+tn.getSliderPoster()+" where id="+tn.getId();
		 session.createQuery(sql).executeUpdate();
	
		ts.commit();
		session.close();
		
	}	
	   
	   
	   public int getTvNavigetTotal(){
		   Session session = HibernateSessionFactory.getSession();
			String hql = " from BannerVodView";
		    Query query = 	session.createQuery(hql);
		   int count = query.list().size();
		   
		   
		   return count;
	   }
		@SuppressWarnings("unchecked")
		public List<BannerInfo> findBI(){
			 Session session = HibernateSessionFactory.getSession();
		   String hql = " from BannerInfo where state=0 ";
			    Query query = 	session.createQuery(hql);
		    List<BannerInfo>  list = query.list(); 

			    return list;

			}
	   
	   
	public List<BannerVodView> findTNList(Page p,String ascendName  ,String ascending ,String urlKey){
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
		StringBuffer sb = new StringBuffer(" from BannerVodView where state=0 order by bannerId asc");
		
		if(urlKey!=null && !"".equals(urlKey)){
			sb.append(" and path like '%"+urlKey+"%'");
		}
		

		if(ascending!=null && !"".equals(ascending)){
			
			sb.append(" "+ascOrdesc);
		}
	
		
		
		

	    Query query = 	session.createQuery(sb.toString());
	  
	   
	    query.setMaxResults(p.getPerPageRecords());
	    query.setFirstResult(p.getCurrentPage()==1?0:((p.getCurrentPage()-1)*p.getPerPageRecords()));
	    List<BannerVodView>  list = query.list(); 

	 //   Session s = HibernateUtil.getSession();
	
	    return list;
		
		
		//return list;
	}
}
