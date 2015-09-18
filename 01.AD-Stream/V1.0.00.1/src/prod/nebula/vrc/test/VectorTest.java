package prod.nebula.vrc.test;

import java.util.Vector;

public class VectorTest {
	public static void main(String[] args) {
		Thread t = new Thread();
		Thread t1 = new Thread();
		Thread t2 = new Thread();
		Thread t3 = new Thread();
		Vector<Thread> v = new Vector<Thread>();
		v.addElement(t);
		v.addElement(t1);
		v.addElement(t2);
		v.addElement(t3);
		
		System.out.println(v.toString());
	}
}
