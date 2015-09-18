/** 
 * Project: mtp-web
 * author : PengSong
 * File Created at 2013-11-21 
 *
 * Copyright bbcvision Corporation Limited. 
 * All rights reserved. 
 *
 * This software is the confidential and proprietary information of 
 * bbcvision company. ("Confidential Information"). You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with bbcvision.com. 
 */
package prod.nebula.msi.web.listener;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;


/** 
 * TODO Comment of MtpContextLoaderListener 
 * 
 * @author PengSong 
 */
public class MsiContextLoaderListener extends ContextLoaderListener  {
	private final Logger logger = Logger.getLogger(MsiContextLoaderListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		logger.info("***********msi application contextInitialized***********");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
		logger.info("***********msi application destroyed***********");
	}
}
