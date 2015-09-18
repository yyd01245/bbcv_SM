/**
 * 
 */
package prod.nebula.mcs.core.executor;

/**
 * XMEM模块接口
 * 
 * @author 严东军
 * 
 */
public interface COMXMEMInterface {
	/**
	 * 增加元素
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return boolean 布尔值 true成功 false失败
	 */
	public boolean addValue(String key, String value);

	/**
	 * 增加元素
	 * 
	 * @param key
	 *            键
	 * @param exp
	 *            超时时间(秒)
	 * @param value
	 *            值
	 * @return boolean 布尔值 true成功 false失败
	 */
	public boolean addValue(String key, int exp, String value);

	/**
	 * 删除元素
	 * 
	 * @param key
	 *            键
	 * @return boolean 布尔值 true成功 false失败
	 */
	public boolean delValue(String key);

	/**
	 * 更新元素
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return boolean 布尔值 true成功 false失败
	 */
	public boolean repValue(String key, String value);

	/**
	 * 更新元素
	 * 
	 * @param key
	 *            键
	 * @param exp
	 *            超时时间(秒)
	 * @param value
	 *            值
	 * @return boolean 布尔值 true成功 false失败
	 */
	public boolean repValue(String key, int exp, String value);

	public String getValue(String key);

	/**
	 * 如果存在该键值，更新元素，如果不存在该键值，新增元素
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return boolean 新增或更新成功true 新增或更新失败false
	 */
	public boolean putValue(String key, String value);

	/**
	 * 如果存在该键值，更新元素，如果不存在该键值，新增元素
	 * 
	 * @param key
	 *            键
	 * @param exp
	 *            超时时间(秒)
	 * @param value
	 *            值
	 * @return boolean 新增或更新成功true 新增或更新失败false
	 */
	public boolean putValue(String key, int exp, String value);

	/**
	 * 判断mem中是否有key对应的元素
	 * 
	 * @param key
	 *            元素键值
	 * @return boolean 布尔值 true存在 false不存在
	 */
	public boolean isKeyInCache(String key);

	/**
	 * 增加元素
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return boolean 布尔值 true成功 false失败
	 */
	public boolean addObject(String key, Object value);

	/**
	 * 增加元素
	 * 
	 * @param key
	 *            键
	 * @param exp
	 *            超时时间(秒)
	 * @param value
	 *            值
	 * @return boolean 布尔值 true成功 false失败
	 */
	public boolean addObject(String key, int exp, Object value);

	/**
	 * 如果存在该键值，更新元素，如果不存在该键值，新增元素
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return boolean 新增或更新成功true 新增或更新成功失败false
	 */
	public boolean putObject(String key, Object value);

	/**
	 * 如果存在该键值，更新元素，如果不存在该键值，新增元素
	 * 
	 * @param key
	 *            键
	 * @param exp
	 *            超时时间(秒)
	 * @param value
	 *            值
	 * @return boolean 新增或更新成功true 新增或更新成功失败false
	 */
	public boolean putObject(String key, int exp, Object value);

	/**
	 * 删除元素
	 * 
	 * @param key
	 *            键
	 * @return boolean 布尔值 true成功 false失败
	 */
	public boolean delObject(String key);

	/**
	 * 更新元素
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return boolean 布尔值 true成功 false失败
	 */
	public boolean repObject(String key, Object value);

	/**
	 * 更新元素
	 * 
	 * @param key
	 *            键
	 * @param exp
	 *            超时时间(秒)
	 * @param value
	 *            值
	 * @return boolean 布尔值 true成功 false失败
	 */
	public boolean repObject(String key, int exp, Object value);

	public Object getObject(String key);

	/**
	 * 判断mem中是否有key对应的元素
	 * 
	 * @param key
	 *            元素键值
	 * @return boolean 布尔值 true存在 false不存在
	 */
	public boolean isObjectInCache(String key);
	
	/**
	 * 清空缓存数据
	 * @return boolean 布尔值 true存在 false不存在
	 */
	public boolean flushAll();
}
