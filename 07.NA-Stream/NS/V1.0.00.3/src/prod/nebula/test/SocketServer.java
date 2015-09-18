package prod.nebula.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


 
public class SocketServer {

	public class ClientHandler implements Runnable {
 
		Socket socket = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;

		public ClientHandler(Socket socket) {
			this.socket = socket; 
		}

		public void run() {
			byte[] buffer = new byte[1024];
			//读取数据
            int length;
			try {
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
				length = inputStream.read(buffer); 
	            System.out.println(new String(buffer,0,length)); 
	            outputStream.write("niyehao".getBytes());
			} catch (IOException e) {
 				e.printStackTrace(); 
			}finally{
				try {
					inputStream.close();
					outputStream.close();
					socket.close();
				} catch (IOException e) { 
 					e.printStackTrace();
				}
			}
		} 
	}
	
	public static void main(String[] args){
		new SocketServer().StartServer();  
 
	}
	
	public void StartServer(){
		ServerSocket serverSocket  = null;
		Socket socket = null;
		int port=5555;
		try{
			serverSocket = new ServerSocket(port);
			System.out.println("server start");
			while(true){
				socket = serverSocket.accept(); 
				System.out.println("client connectioned,IP:"+socket.getInetAddress()); 
				Thread t = new Thread(new ClientHandler(socket));
                t.start();
			}
		}catch(IOException e){
			e.printStackTrace();
		} finally{
			 
		}
	}
}