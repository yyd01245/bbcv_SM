
package dto;

import hibernate.HibernateSessionFactory;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pojo.Ad;
import pojo.NamodelRole;
import pojo.Page;
import pojo.dao.VodRoleView;

/**   
 * TvNavigateService   
 * TV导航Service
 * PengFei   
 * 2014-9-26 
 * 
 * @version    
 *    
 */
public class TvNavigateConfigService {
   
private static final Logger logger = LoggerFactory.getLogger(VodResService.class);
	
	
	
	public NamodelRole getTvNavigate(String id){
		 Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		 
		NamodelRole tn=	(NamodelRole) session.get(NamodelRole.class, Integer.parseInt(id));
		ts.commit();
		session.close();
		return tn;
	}
	
	
	public VodRoleView getVodRoleView(String id){
		 Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		 
		VodRoleView tn=	(VodRoleView) session.get(VodRoleView.class, Integer.parseInt(id));
		ts.commit();
		session.close();
		return tn;
	}
	
	
	public Ad getAd(String id){
		 Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		 
		Ad tn=	(Ad) session.get(Ad.class, Integer.parseInt(id));
		ts.commit();
		session.close();
		return tn;
	}	
	public void add(NamodelRole tn){
		 Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		 
		session.save(tn);
		ts.commit();
		session.close();
		
		
	}
	
	public void update(NamodelRole tn){
		 Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		
		
		Query query = session.createQuery(" update NamodelRole t set t.vodId=?,t.pageDwellTime=? ,t.orderId=? ,t.resolution=? where t.id=?");
		query.setInteger(0, tn.getVodId());
		query.setInteger(1, tn.getPageDwellTime());

		query.setInteger(2, tn.getOrderId());
		query.setString(3, tn.getResolution());
		query.setInteger(4,tn.getId());
		query.executeUpdate();
		System.out.println("query.getQueryString()= "+query.toString());
		ts.commit();
		session.close();
		
	}
	
	public void updateAd(Ad tn){
		 Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
	
 	   
 	   
		Query query = session.createQuery(" update Ad t set t.name=?,t.adVideoUrl=? where t.id=?");
		query.setString(0, tn.getName());
		query.setString(1, tn.getAdVideoUrl());
//		query.setInteger(2, tn.getPageDwellTime());
//
//		query.setInteger(3, tn.getOrderId());
//		query.setString(4, tn.getResolution());
//		query.setString(5, tn.getResolution());
		query.setInteger(2,tn.getId());
		query.executeUpdate();
		System.out.println("query.getQueryString()= "+query.toString());
		ts.commit();
		session.close();
		
	}
	
	   public void updateState(NamodelRole tn){
		   Session session = HibernateSessionFactory.getSession();

		Transaction ts = 	session.beginTransaction();

		 String sql = " update NamodelRole set state=1 where id="+tn.getId();
		 session.createQuery(sql).executeUpdate();
	
		ts.commit();
		session.close();
		
	}	
	   public void changeState(Ad tn){
		   Session session = HibernateSessionFactory.getSession();

		Transaction ts = 	session.beginTransaction();

		 String sql = " update Ad set state=1 where id="+tn.getId();
		 session.createQuery(sql).executeUpdate();
	
		ts.commit();
		session.close();
		
	}
	   public int getTvNavigetTotal(String resolution){
		   Session session = HibernateSessionFactory.getSession();
			String hql = " from VodRoleView where resolution="+resolution;
		    Query query = 	session.createQuery(hql);
		   int count = query.list().size();
		   
		   
		   return count;
	   }
	   
	   public int getTvAdTotal(){
		   Session session = HibernateSessionFactory.getSession();
			String hql = " from Ad where state=0";
		    Query query = 	session.createQuery(hql);
		   int count = query.list().size();
		   
		   
		   return count;
	   }
		public List<Ad> findAdList(Page p,String ascendName  ,String ascending ,String urlKey){
			 Session session = HibernateSessionFactory.getSession();
	
			String ascOrdesc = null;
		    if(true==Boolean.parseBoolean(ascending)){
		    	ascOrdesc = "asc";
		    }else{
		    	ascOrdesc = "desc";
		    }
			StringBuffer sb = new StringBuffer(" from Ad where state=0");
			
			if(urlKey!=null && !"".equals(urlKey)){
				sb.append(" and path like '%"+urlKey+"%'");
			}
		//	sb.append(" order by orderId asc");
			if(ascendName!=null && !"".equals(ascendName)){
				sb.append(" order by "+ascendName);
			}
			if(ascending!=null && !"".equals(ascending)){
				
				sb.append(" "+ascOrdesc);
			}
		
			
			 
			

		    Query query = 	session.createQuery(sb.toString());
		  
		   
		    query.setMaxResults(p.getPerPageRecords());
		    query.setFirstResult(p.getCurrentPage()==1?0:((p.getCurrentPage()-1)*p.getPerPageRecords()));
		    List<Ad>  list = new ArrayList<Ad>();
		    	list = query.list(); 
		 //   Session s = HibernateUtil.getSession();
		
		    return list;
			
			
			//return list;
		}   
	public List<VodRoleView> findTNList(Page p,String ascendName  ,String ascending ,String urlKey,String resolution){
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
		StringBuffer sb = new StringBuffer(" from VodRoleView where resolution="+resolution);
		
		if(urlKey!=null && !"".equals(urlKey)){
			sb.append(" and path like '%"+urlKey+"%'");
		}
		sb.append(" order by orderId asc");
		if(ascendName!=null && !"".equals(ascendName)){
			sb.append(" order by "+ascendName);
		}
		if(ascending!=null && !"".equals(ascending)){
			
			sb.append(" "+ascOrdesc);
		}
	
		
		 
		

	    Query query = 	session.createQuery(sb.toString());
	  
	   
	    query.setMaxResults(p.getPerPageRecords());
	    query.setFirstResult(p.getCurrentPage()==1?0:((p.getCurrentPage()-1)*p.getPerPageRecords()));
	    List<VodRoleView>  list = new ArrayList<VodRoleView>();
	    	list = query.list(); 
	 //   Session s = HibernateUtil.getSession();
	
	    return list;
		
		
		//return list;
	}
}
