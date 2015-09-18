package org.eredlab.g4.bmf.base;

import org.eredlab.g4.ccl.properties.PropertiesFactory;
import org.eredlab.g4.ccl.properties.PropertiesFile;
import org.eredlab.g4.ccl.properties.PropertiesHelper;

/**
 * 业务模型实现基类<br>
 * 
 * @author XiongChun
 * @since 2009-07-21
 */
public class BaseServiceImpl implements BaseService {

	/**
	 * Bpo基类中给子类暴露的一个DAO接口<br>
	 */
	protected IDao g4Dao;
	
	public static IDao crsmDao;
	
	public IDao getCrsmDao() {
		return crsmDao;
	}

	public void setCrsmDao(IDao crsmDao) {
		BaseServiceImpl.crsmDao = crsmDao;
	}

	protected final Boolean TRUE = new Boolean(true); 
	protected final Boolean FALSE = new Boolean(false) ;
	
	
	public IDao mtpDao ;
	

	public IDao getMtpDao() {
		return mtpDao;
	}

	public void setMtpDao(IDao mtpDao) {
		this.mtpDao = mtpDao;
	}


	protected static PropertiesHelper pHelper = PropertiesFactory.getPropertiesHelper(PropertiesFile.G4);
    
	public void setG4Dao(IDao g4Dao) {
		this.g4Dao = g4Dao;
	}
}
