	package servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Server extends Thread{
		
	private static int port = 4445;
	private DatagramSocket socket;
	private boolean running;
	private byte[] buf = new byte[256];
	
	public Server(){ 
		try {
			socket = new DatagramSocket(port);
			socket.setBroadcast(true);
//			socket.setBroadcast(true);
			
		} catch (Exception e) {
//			System.err.println("Error en el establecimiento del puerto. Reintentando...\n");
			e.printStackTrace();
		}
	}
	
	public void enviaPaq(InetAddress destAddr, int destPort) {
		while (true) {
			try {
				buf = "hola, buenos dias".getBytes();
				DatagramPacket packet = new DatagramPacket(buf, buf.length, destAddr, destPort);
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		InetAddress clientAddr;
		try {
			clientAddr = InetAddress.getByName("localhost");
			int clientPort = 4999;
			Server server = new Server();
			server.enviaPaq(clientAddr, clientPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	public void run(){
//		try {
//			running = true;
//			while (running) {
//				System.out.println("server iniciado");
//				InetSocketAddress dircli = new InetSocketAddress(InetAddress.getLocalHost(), 4999);
//				buf = "hola, buenos dias".getBytes();
//				DatagramPacket packet = new DatagramPacket(buf, buf.length, dircli);
////					socket.receive(packet);
////					InetAddress address = packet.getAddress();
////					int port = packet.getPort();
////					packet = new DatagramPacket(buf, buf.length, address, port);
////					String received = new String(packet.getData(), 0, packet.getLength());
//				
////					if (received.equals("end")) {
////						running = false;
////						continue;
////					}
//				socket.send(packet);
//				sleep(2990); //duermo durante 3 segundos
//			}
//		} 
//		
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//			
//	socket.close();
	}
}

