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
	private static int myPort = 4999;
	private static int portServ = 4445; //TODO quitar esto para sacar el puerto de los mensajes
	private DatagramSocket socket;
	private byte[] buf = new byte[256];
 
    public Client() {
    	try {
    		//TODO implementar el aumento del puerto (static)
			this.socket = new DatagramSocket(myPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
    }
    
    public void recibePaquete() {
    	while (true) {
    		try {
    			DatagramPacket pak = new DatagramPacket(buf, buf.length);
    			socket.receive(pak);
    			String msg = new String(pak.getData(), 0, pak.getLength());
    			System.out.println(msg);
    			buf = "mensaje recibido".getBytes();
    			DatagramPacket resp = new DatagramPacket(buf, buf.length, pak.getSocketAddress());
    			System.out.println(pak.getSocketAddress());
    			socket.send(resp);
				
			} catch (IOException e) {
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

