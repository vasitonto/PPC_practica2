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
		
	private static int myPort = 4445; // puerto en el que el servidor va a recibir los mensajes
	private static final int BCPORT = 4999; // broadcast port
	private InetSocketAddress BCADDR;
	private DatagramSocket socket;
	private byte[] buf = new byte[256];
	
	public Server(){ 
		try {
			socket = new DatagramSocket(myPort);
			socket.setBroadcast(true);
			this.BCADDR = new InetSocketAddress(InetAddress.getByName("255.255.255.255"), BCPORT);
			
		} catch (SocketException | UnknownHostException e) {
//			System.err.println("Error en el establecimiento del puerto. Reintentando...\n");
			e.printStackTrace();
		}
	}
	
	 private void broadcast(){
	        Thread t = new Thread(new Runnable() {           
	            public void run() { 
	    			for(int i = 0; i < 3; i++) {
//	    			while (true) {
    					try {
    						buf = "hola, buenos dias".getBytes();
    						DatagramPacket packet = new DatagramPacket(buf, buf.length, BCADDR);
    						socket.send(packet);
    						sleep(2990);
    					} catch (IOException | InterruptedException e) {
    						e.printStackTrace();
    					}
    				}
	            } 
	        });
	        t.start();
	    }
	 
		private void respond(){
			Thread t = new Thread(new Runnable() {           
				public void run() { 
					for(int i = 0; i < 3; i++) {
//	    			while (true) {
	 					try {
	 						buf = "me cago encima".getBytes();
	 						DatagramPacket packet = new DatagramPacket(buf, buf.length, BCADDR);
	 						socket.send(packet);
	 						sleep(2990);
	 					} catch (IOException | InterruptedException e) {
	 						e.printStackTrace();
	 					}
	 				}
				}
	        });
		        t.start();
	    }
	
//	private Runnable broadcaster = new Runnable() {
//		@Override
//		public void run() {
//			for(int i = 0; i < 3; i++) {
////			while (true) {
//				try {
//					buf = "hola, buenos dias".getBytes();
//					System.out.println("Broadcasting: " + buf.toString());
//					DatagramPacket packet = new DatagramPacket(buf, buf.length, BCADDR);
//					socket.send(packet);
//					sleep(2990);
//				} catch (IOException | InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	};
//	
//	private Runnable responder = new Runnable() {
//		public void run() {
//			while(true) {
//				try {
//					buf = "me cago encima".getBytes();
//					DatagramPacket packet = new DatagramPacket(buf, buf.length, BCADDR);
//					socket.send(packet);
//					sleep(2990);
//				} catch (IOException | InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	};
	
	private void enviarPaq() {
		while(true) {
			try {
				buf = "me cago encima".getBytes();
				DatagramPacket packet = new DatagramPacket(buf, buf.length, BCADDR);
				socket.send(packet);
				sleep(2990);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
			Server server = new Server();
			ServerBroadcaster broadcaster = new ServerBroadcaster(server.BCADDR, server.socket);
			ServerQueryResponder responder = new ServerQueryResponder(server.BCADDR.getAddress(), 4445);
			broadcaster.start();
			responder.start();
			
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

