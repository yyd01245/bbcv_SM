/**  
 * ����ƣ�TvNavigateService 
 * �������� 
 * �����ˣ�PengFei   
 * ����ʱ�䣺2014-9-26 ����01:08:48 
 */
package dto;

import java.util.List;

import hibernate.HibernateSessionFactory;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pojo.Ad;
import pojo.Page;
import pojo.TvNavigate;
import pojo.VodResourceInfo;

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
public class VodResService {
   
	private static final Logger logger = LoggerFactory.getLogger(VodResService.class);
	
	
	
	public VodResourceInfo getTvNavigate(String id){
		 Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		 
		VodResourceInfo tn=	(VodResourceInfo) session.get(VodResourceInfo.class, Integer.parseInt(id));
		ts.commit();
		session.close();
		return tn;
	}
	
	
	public void add(VodResourceInfo tn){
		 Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		 
		session.save(tn);
		ts.commit();
		session.close();
		
		
	}
	
	public void add(Ad tn){
		 Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		 
		session.save(tn);
		ts.commit();
		session.close();
		
		
	}
	
	public void update(VodResourceInfo tn){
		 Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		
		
		Query query = session.createQuery(" update VodResourceInfo t set t.name=? ,t.grade=?,t.director=?,t.actor=?,t.years=?,t.runtime=?,t.description=?," +
				"t.bigPosterPath=?,t.littlePosterPath=?,t.rtspUrl=?,t.path=? ,t.other=? ,t.type=? ,t.area=? ,t.resolution=? where t.id=?");
		query.setString(0, tn.getName());
		query.setInteger(1, tn.getGrade());
		query.setString(2, tn.getDirector());
		query.setString(3,  tn.getActor());
		query.setString(4, tn.getYears());
		query.setString(5, tn.getRuntime());
		query.setString(6, tn.getDescription());
		query.setString(7, tn.getBigPosterPath());
		query.setString(8, tn.getLittlePosterPath());
		query.setString(9, tn.getRtspUrl());
		
	//	query.setInteger(10, tn.getModelId());
		query.setString(10, tn.getPath());
		query.setString(11,tn.getOther());
		query.setString(12,tn.getType());
		query.setString(13,tn.getArea());
		
		query.setString(14,tn.getResolution());
		query.setInteger(15,tn.getId());
		query.executeUpdate();
		System.out.println("query.getQueryString()= "+query.toString());
		ts.commit();
		session.close();
		
	}
	
	public void updateUseState(VodResourceInfo tn){
		
		 Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		
		
		String sql = " update VodResourceInfo set useState=0 where useState=1";
		
		Query query = session.createQuery(sql);
		query.executeUpdate();
		
		
		 String sql1 = " update TvNavigate set useState=1 where id="+tn.getId();
		 session.createQuery(sql1).executeUpdate();
	
		ts.commit();
		session.close();
		
	}	
	
	   public void updateState(VodResourceInfo tn){
		
		   Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();

		 String sql = " update VodResourceInfo set state=1 where id="+tn.getId();
		 session.createQuery(sql).executeUpdate();
	
		ts.commit();
		session.close();
		
	}	
	
	   public int getTvNavigetTotal(){
		   Session session = HibernateSessionFactory.getSession();
			String hql = " from VodResourceInfo where state=0";
		    Query query = 	session.createQuery(hql);
		   int count = query.list().size();
		   
		   
		   return count;
	   }
	   
	public List<VodResourceInfo> findTNList(Page p,String ascendName  ,String ascending ,String urlKey){
		 Session session = HibernateSessionFactory.getSession();
	//	DetachedCriteria dc = DetachedCriteria.forClass(TvNavigate.class);
	//    Criteria c = dc.getExecutableCriteria(session);
	//    List rs = c.list();
//		String ascOrdesc = null;
//	    if(true==Boolean.parseBoolean(ascending)){
//	    	ascOrdesc = "asc";
//	    }else{
//	    	ascOrdesc = "desc";
//	    }
		StringBuffer sb = new StringBuffer(" from VodResourceInfo where state=0  ");
		
		if(urlKey!=null && !"".equals(urlKey)){
			sb.append(" and path like '%"+urlKey+"%'");
		}
		
//		if(ascendName!=null && !"".equals(ascendName)){
//			sb.append(" order by "+ascendName);
//		}
//		if(ascending!=null && !"".equals(ascending)){
//			
//			sb.append(" "+ascOrdesc);
//		}
	
		
		
		

	    Query query = 	session.createQuery(sb.toString());
	  
	   
	    query.setMaxResults(p.getPerPageRecords());
	    query.setFirstResult(p.getCurrentPage()==1?0:((p.getCurrentPage()-1)*p.getPerPageRecords()));
	    List<VodResourceInfo>  list = query.list(); 

	 //   Session s = HibernateUtil.getSession();
	
	    return list;
		
		
		//return list;
	}

	
}
