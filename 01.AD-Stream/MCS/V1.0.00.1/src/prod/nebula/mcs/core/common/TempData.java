/**
 * 
 */
package prod.nebula.mcs.core.common;

import java.util.Hashtable;
import java.util.Map;

import prod.nebula.mcs.module.dto.Netcard;
import prod.nebula.mcs.module.dto.Task;



/**
 *  
 *	存放临时数据类
 */
public class TempData {

	//网卡信息
	/*
	 * NetCardINFO
	 * 		  |Map<ip,Netcard>
	 * 				      
	 */
	public static Map<String,Netcard> NetCardINFO = new Hashtable<String,Netcard>();
	
	//任务信息
	/*
	 * MEMINFO
	 * 		  |Map<id,Task>
	 * 					  
	 */
	
	public static Map<Integer,Task> TASKINFO = new Hashtable<Integer,Task>();
	
	
}
