package servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Server extends Thread{
		
	private static int port = 4445;
	
	private DatagramSocket socket;
	private boolean running;
	private byte[] buf = new byte[256];
	
	public Server() { 
		boolean r = true;
		while(r) {
			try {
//					synchronized (r)) {
//						socket = new DatagramSocket(port); 
//						port++;			
//						System.out.println(port);
//					}
				socket = new DatagramSocket(port);
				r = false;
			} catch (Exception e) {
				System.err.println("Error en el establecimiento del puerto. Reintentando...\n");
				e.printStackTrace();
			}
		}
	}
	
	public void run()
	{
		try {
			running = true;
			while (running) {
				buf = "hola, buenos dias".getBytes();
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
//					socket.receive(packet);
//					InetAddress address = packet.getAddress();
//					int port = packet.getPort();
//					packet = new DatagramPacket(buf, buf.length, address, port);
//					String received = new String(packet.getData(), 0, packet.getLength());
				
//					if (received.equals("end")) {
//						running = false;
//						continue;
//					}
				sleep(2990); //duermo durante 3 segundos
				socket.send(packet);
			}
		} 
		
		catch (Exception e) {
			System.err.println("error en algo");
		}
			
	socket.close();
	}
	public static void main(String[] args) {
		Server serv = new Server();
		serv.run();
	}
}

