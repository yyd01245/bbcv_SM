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

import pojo.BannerCount;
import pojo.BannerInfo;
import pojo.BannerVodMapping;
import pojo.MainpageInfo;
import pojo.Page;
import pojo.TvNavigate;
import pojo.VodResourceInfo;
import pojo.dao.BannerVodView;

import servlet.template.TvNavigateServlet;

/**   
 * ����ƣ�TvNavigateService   
 * ��������   
 * �����ˣ�PengFei   
 * ����ʱ�䣺2014-9-26 ����01:08:48   
 * ��ע��   
 * @version    
 *    
 */
public class BannerCountService {
   
private static final Logger logger = LoggerFactory.getLogger(BannerCountService.class);
	
	
	
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public List<BannerCount>  findBCCount(){
		
		 Session session = HibernateSessionFactory.getSession();
		String hql = "select f.bannerId ,f.bannerName,count(f.bannerId) as total from BannerVodView as f group by f.bannerId";
		
		Query query = session.createQuery(hql);
		 List<BannerCount>  listBC = new ArrayList<BannerCount>();
		   List<Object> list = query.list();  
		   for(int i=0;i<list.size();i++){
		   
		    Object ob[]=(Object[]) list.get(i);
		   
		    
		    BannerCount bc = new BannerCount();
		    bc.setBangnerId(ob[0].toString());
		    bc.setBannerName(ob[1].toString());
		    bc.setTotal(ob[2].toString());
		    listBC.add(bc);
		   }
		
		return listBC;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<MainpageInfo>  findMI(){
		 Session session = HibernateSessionFactory.getSession();
		
		String hql = " from MainpageInfo";
		
		Query query = session.createQuery(hql);
		
		   List<MainpageInfo> list = query.list();  
	
		
		return list;
	}
	
	
	
	
	
	
	
	
	

	
	
	
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
		
		
		Query query = session.createQuery(" update BannerVodMapping t set t.bannerId=? ,t.vodId=?,t.orderId=?  where t.id=?");

		query.setInteger(0, tn.getBannerId());
		query.setInteger(1, tn.getVodId());
		query.setInteger(2, tn.getOrderId());
	
		query.setInteger(3,tn.getId());
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
	
	   public int getTvNavigetTotal(){
		   Session session = HibernateSessionFactory.getSession();
			String hql = " from BannerVodMapping ";
		    Query query = 	session.createQuery(hql);
		   int count = query.list().size();
		   
		   
		   return count;
	   }
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
