
package dto;

import hibernate.HibernateSessionFactory;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pojo.Ad;
import pojo.BannerInfo;
import pojo.Page;
import pojo.VodResourceInfo;

public class InitVodResourceService {
   
	private static final Logger logger = LoggerFactory.getLogger(InitVodResourceService.class);
	
	
	
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
	   
	@SuppressWarnings("unchecked")
	public List<VodResourceInfo> findTNList(Page p,String ascendName  ,String ascending ,String urlKey,String resolution){
		 Session session = HibernateSessionFactory.getSession();
	
		 String hql = "";
		 if("1".equals(resolution)){
			  hql = " from VodResourceInfo v where v.state=0 and v.resolution="+resolution+" and v.id not in(select n.vodId from NamodelRole n where n.state=0 and n.resolution="+resolution+") order by v.resolution "; 
		 }
		 if("0".equals(resolution)){
			  hql = " from VodResourceInfo v where v.state=0 and v.id not in(select n.vodId from NamodelRole n where n.state=0 and n.resolution="+resolution+") order by v.resolution ";	 
		 }		 
		 
		 Query query= 	session.createQuery(hql);
		 
	   
	    query.setMaxResults(p.getPerPageRecords());
	    query.setFirstResult(p.getCurrentPage()==1?0:((p.getCurrentPage()-1)*p.getPerPageRecords()));
	    List<VodResourceInfo>  list = query.list(); 

	
	    return list;

	}

	@SuppressWarnings("unchecked")
	public List<VodResourceInfo> findBCList(Page p,String ascendName  ,String ascending ,String urlKey,String resolution,BannerInfo bannerInfo){
		 Session session = HibernateSessionFactory.getSession();
	
	
		 
		 System.out.println("resolution--------->"+resolution);
		 String hql = "";
		 if("1".equals(resolution)){
			  hql = " from VodResourceInfo v where v.state=0 and v.resolution="+resolution+" and v.id not in(select n.vodId from BannerVodMapping n where n.state=0 and n.resolution="+resolution+" and n.bannerId="+bannerInfo.getBannerId()+") order by v.resolution "; 
		 }
		 if("0".equals(resolution)){
			  hql = " from VodResourceInfo v where v.state=0 and v.id not in(select n.vodId from BannerVodMapping n where n.state=0 and n.resolution="+resolution+" and n.bannerId="+bannerInfo.getBannerId()+") order by v.resolution ";	 
		 }		 
		 
		 Query query= 	session.createQuery(hql);
		 
	   
	    query.setMaxResults(p.getPerPageRecords());
	    query.setFirstResult(p.getCurrentPage()==1?0:((p.getCurrentPage()-1)*p.getPerPageRecords()));
	    List<VodResourceInfo>  list = query.list(); 

	
	    return list;

	}
}
