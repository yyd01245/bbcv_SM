/**
 * 
 */
package prod.nebula.mcs.core.executor.impl;

import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import prod.nebula.mcs.core.executor.XMEMInterface;



public class DefaultXMEMImpl implements XMEMInterface {

	public DefaultXMEMImpl() {
	}

	public boolean addValue(String key, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean delValue(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean repValue(String key, String value) {
		// TODO Auto-generated method stub
		return false;
	}


	public String getValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean addValueForLock(String key, int timeoutTime, String value) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean delValueForLock(String key) {
		// TODO Auto-generated method stub
		return false;
	}


	public String getValueForLock(String key) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<String> getListValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean addListValue(String key, List<String> value) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean repListValue(String key, List<String> value) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean flushAll() {
		// TODO Auto-generated method stub
		return false;
	}




	public boolean addQueueValue(String key, Queue<String> value) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean repQueueValue(String key, Queue<String> value) {
		// TODO Auto-generated method stub
		return false;
	}

	public Vector<String> getVectorValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean addVectorValue(String key, Vector<String> value) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean repVectortValue(String key, Vector<String> value) {
		// TODO Auto-generated method stub
		return false;
	}

	public ConcurrentLinkedQueue<String> getQueueValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getObject(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
