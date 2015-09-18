package prod.nebula.test;

public class TestThread implements Runnable{

	
	private int a ;
	
	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public void run() {
		
		int i =10;
		boolean b = true;
		while(b){
			
			i--;
			
			if(i<4){
				
				this.a = 100;
				
				b=false;
			}
			
			System.out.println(" --------i = "+i);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("11111111111111111");
		}
		
	}

}
