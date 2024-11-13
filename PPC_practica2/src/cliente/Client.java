package cliente;

import java.net.DatagramSocket;

public class Client extends Thread{
		
	private static Runnable javi;
	private static Runnable pedro;
	private static int port;
	
	private DatagramSocket s;
	
	public Client() {
		try {
			s = new DatagramSocket(port);
			
		} catch (Exception e) { e.printStackTrace(); }
	}
	
    static Thread t1 = new Thread( () -> { 
        while (true)  
            System.out.println("A"); 
       }); 
 
    static Thread t2 = new Thread( () -> { 
        while (true)  
            System.out.println("B"); 
       }); 
 
    public static void main(String[] args) { 
    	
    } 
    
    @Override
    public void run() {
    	// TODO Auto-generated method stub
    	super.run();
    }
}

