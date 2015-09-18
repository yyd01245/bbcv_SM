package prod.nebula.mcs.core.executor;

import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface XMEMInterface {

	/**
	 * 增加键值信息
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return
	 */
	public boolean addValue(String key, String value);

	/**
	 * 增加键值信息
	 * 
	 * @param key
	 *            键
	 * @param timeoutTime
	 *            超时时间
	 * @param value
	 *            值
	 * @return
	 */
	public boolean addValueForLock(String key, int timeoutTime, String value);

	/**
	 * 删除键值信息
	 * 
	 * @param key
	 *            键
	 * @return
	 */
	public boolean delValue(String key);

	/**
	 * 删除键值信息
	 * 
	 * @param key
	 *            键
	 * @return
	 */
	public boolean delValueForLock(String key);

	/**
	 * 更新键值信息
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return
	 */
	public boolean repValue(String key, String value);

	/**
	 * 获得键值信息
	 * 
	 * @param key
	 *            键
	 * @return String 值
	 */
	public String getValue(String key);
	
	/**
	 * 获得键值信息
	 * 
	 * @param key
	 *            键
	 * @return List 值
	 */
	public List<String> getListValue(String key);
	/**
	 * 获得键值信息
	 * 
	 * @param key
	 *            键
	 * @return List 值
	 */
	public Vector<String> getVectorValue(String key);
	
	/**
	 * 获得键值信息
	 * 
	 * @param key
	 *            键
	 * @return String 值
	 */
	public String getValueForLock(String key);
	
	/**
	 * 增加List对象的value
	 * 
	 * @param key
	 *            键
	 * @return String 值
	 */
	public boolean addListValue(String key, List<String> value);
	/**
	 * 增加List对象的value
	 * 
	 * @param key
	 *            键
	 * @return String 值
	 */
	public boolean addVectorValue(String key, Vector<String> value);
	/**
	 * 替换List对象的value
	 * 
	 * @param key
	 *            键
	 * @return String 值
	 */
	public boolean repListValue(String key, List<String> value);

	/**
	 * 清空缓存
	 * 
	 * @param key
	 *            键
	 * @return String 值
	 */
	public boolean flushAll();
	
	boolean addQueueValue(String key, Queue<String> value);

	boolean repQueueValue(String key, Queue<String> value);
	
	public boolean repVectortValue(String key, Vector<String> value);
	ConcurrentLinkedQueue<String> getQueueValue(String key);
	
	public Object getObject(String key);
}
