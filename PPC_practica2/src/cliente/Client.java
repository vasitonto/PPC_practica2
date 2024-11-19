package cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client extends Thread{
		
//	private Runnable javi;
//	private Thread pedro;
	private static int portCli = 4999;
	private static int portServ = 4445;
	private DatagramPacket packMulticast;
	private DatagramSocket socket;
	private byte[] buf = new byte[256];
 
    public Client() {
    	try {
			this.socket = new DatagramSocket(portCli);
		} catch (SocketException e) {
			
			e.printStackTrace();
		}
    }
    @Override
    public void run() {
//    	try {
//    		System.out.println("cliente iniciado");
//			DatagramSocket socket = new DatagramSocket();
//			InetSocketAddress ssocket = new InetSocketAddress(portServ);
//			DatagramPacket pMult = new DatagramPacket(buf, buf.length, Inet4Address.getByName("localhost"), portServ);
//			socket.receive(pMult);
//			System.out.println(pMult.getData().toString());
//		} catch (SocketException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException ioe) {
//			System.err.println("Error de tipo " + ioe.getClass());
//			ioe.printStackTrace();
//		}
    }
    
    public void recibePaquete() {
    	while (true) {
    		try {
    			DatagramPacket pak = new DatagramPacket(buf, buf.length);
    			socket.receive(pak);
    			InetAddress serverAddress = pak.getAddress();
    			String msg = new String(pak.getData(), 0, pak.getLength());
    			System.out.println(msg);
    			sleep(2990);
				
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}	
    }
    
    public static void main(String[] args) {
		Client cli = new Client();
		cli.recibePaquete();
	}
}

