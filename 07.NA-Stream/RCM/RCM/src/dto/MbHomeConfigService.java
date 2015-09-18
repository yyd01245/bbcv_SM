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

import pojo.BannerInfo;
import pojo.MainpageInfo;
import pojo.Page;
import pojo.VodResourceInfo;
import pojo.dao.MainBannerView;

/**   
 * ����ƣ�TvNavigateService   
 * ��������   
 * �����ˣ�PengFei   
 * ����ʱ�䣺2014-9-26 ����01:08:48   
 * ��ע��   
 * @version    
 *    
 */
public class MbHomeConfigService {
   
private static final Logger logger = LoggerFactory.getLogger(VodResService.class);
	
	 
	
	
	public MainpageInfo getTvNavigate(String id){
		Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		 
		MainpageInfo tn=	(MainpageInfo) session.get(MainpageInfo.class, Integer.parseInt(id));
		ts.commit();
		session.close();
		return tn;
	}
	
	
	public void add(MainpageInfo bvm){
		Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		 
		session.save(bvm);
		ts.commit();
		session.close();
		
		
	}
	
	public void update(MainpageInfo tn){
		Session session = HibernateSessionFactory.getSession();
		Transaction ts = 	session.beginTransaction();
		
		
		Query query = session.createQuery(" update MainpageInfo t set t.modelId=?,t.orderId=? ,t.resolution=? where t.mainPage=?");

		query.setInteger(0, tn.getModelId());
		query.setInteger(1, tn.getOrderId());

	   query.setString(2, tn.getResolution());
		query.setInteger(3,tn.getMainPage());
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
	
	   public void updateState(MainpageInfo tn){
		   Session session = HibernateSessionFactory.getSession();

		Transaction ts = 	session.beginTransaction();

		 String sql = " update MainpageInfo set state=1 where mainPage="+tn.getMainPage();
		 session.createQuery(sql).executeUpdate();
	
		ts.commit();
		session.close();
		
	}	
	
	   public int getTvNavigetTotal(String resolution){
		   Session session = HibernateSessionFactory.getSession();
			String hql = " from MainBannerView where resolution="+resolution;
		    Query query = 	session.createQuery(hql);
		   int count = query.list().size();
		   
		   
		   return count;
	   }
		@SuppressWarnings("unchecked")
		public List<BannerInfo> findBI(String resolution){
			Session session = HibernateSessionFactory.getSession();
			//当前正在使用的栏目
			String hql1=" select m.modelId from MainpageInfo m where state=0 and resolution ="+resolution;
			 Query query1 = 	session.createQuery(hql1);
			List<Integer> list1= query1.list(); 
			
			StringBuffer sb = new  StringBuffer();
			for (Integer integer : list1) {
				sb.append(integer+",");
			}
			//删除不用的栏目
			String hql5=" select m.modelId from MainpageInfo m where state=1 and resolution ="+resolution;
			 Query query5 = 	session.createQuery(hql5);
			List<Integer> list5= query5.list(); 
			
			
			// 去除删除中的且还在使用的栏目，即得到当前没用到的栏目且是删除的。
			
		//	String  = list5.r
			list5.removeAll(list1);
			StringBuffer sb5 = new  StringBuffer();
			for (Integer integer : list5) {
				sb5.append(integer+",");
			}
			
			
			
			StringBuffer sb2 = new  StringBuffer("select b.bannerId from BannerVodMapping b where resolution = "+resolution);
			
			
			if((sb.toString().length()-1)>=0){
				logger.info("当前正在使用的栏目 id:"+sb.toString().substring(0, sb.toString().length()-1));
				sb2.append(" and b.bannerId not in("+sb.toString().substring(0, sb.toString().length()-1)+")");
			}else{
				logger.info("当前无配置栏目");
			}
			
			if((sb5.toString().length()-1)>=0){
				logger.info("已删除的栏目 id:"+sb5.toString().substring(0, sb5.toString().length()-1));
				sb2.append(" or b.bannerId in("+sb5.toString().substring(0, sb5.toString().length()-1)+")");
			}else{
				logger.info("无删除的栏目");
			}
			
			//���ҳ���Ŀ������������Ŀ id
//			String hql2=" select b.bannerId  from BannerVodMapping b group by b.bannerId ";
//			 Query query2 = 	session.createQuery(hql2);
//			List<Integer> list2= query2.list(); 			
			
			//在配置好的栏目中查找在首页配置已删除的栏目和不在正在使用的栏目
			String hql3=sb2.toString()+" group by b.bannerId ";
			 Query query3 = 	session.createQuery(hql3);
			List<Integer> list3= query3.list();	
			
			StringBuffer sb1 = new  StringBuffer();
			for (Integer integer : list3) {
				sb1.append(integer+",");
			}		
			
			logger.info("在配置好的栏目中查找在首页配置已删除的栏目和不在正在使用的栏目 id:"+sb1.toString().substring(0, sb1.toString().length()-1));
			
			//手机首页可添加的栏目
			
			
		   String hql4 = " from BannerInfo  where state=0 and bannerId in("+sb1.toString().substring(0, sb1.toString().length()-1)+")";
			Query query4 = 	session.createQuery(hql4);
		    List<BannerInfo>  list4 = query4.list(); 

			    return list4;

			}
	   
	   
	public List<MainBannerView> findTNList(Page p,String ascendName  ,String ascending ,String urlKey,String resolution){
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
		StringBuffer sb = new StringBuffer(" from MainBannerView where state =0 and resolution = "+resolution+" order by orderId asc");
		
		if(urlKey!=null && !"".equals(urlKey)){
			sb.append(" and path like '%"+urlKey+"%'");
		}
		

//		if(ascending!=null && !"".equals(ascending)){
//			
//			sb.append(" "+ascOrdesc);
//		}
	
		
		
		

	    Query query = 	session.createQuery(sb.toString());
	  
	   
	    query.setMaxResults(p.getPerPageRecords());
	    query.setFirstResult(p.getCurrentPage()==1?0:((p.getCurrentPage()-1)*p.getPerPageRecords()));
	    List<MainBannerView>  list = query.list(); 

	 //   Session s = HibernateUtil.getSession();
	
	    return list;
		
		
		//return list;
	}
	

	
}
