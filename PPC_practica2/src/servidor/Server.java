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
	
	//TODO hacer el sistema de forma que se puedan lanzar varios servidores
	// es decir, que el nº del puerto vaya cambiando
	private static final int BCSocketPort = 4445; // puerto en el que el servidor va a recibir los mensajes
	private static final int BCPORT = 4999; // broadcast port
	private static final int RCVPORT = 4446;
	private InetSocketAddress BCADDR;
	private DatagramSocket BCsocket;
	private DatagramSocket RCVsocket;
	private byte[] buf = new byte[256];
	
	public Server(){ 
		try {
			BCsocket = new DatagramSocket(BCSocketPort);
			BCsocket.setBroadcast(true);
			// BCADDR será la dirección a la que se enviarán los paquetes de broadcast
			this.BCADDR = new InetSocketAddress(InetAddress.getByName("127.0.0.255"), BCPORT);
			
		} catch (SocketException | UnknownHostException e) {
//			System.err.println("Error en el establecimiento del puerto. Reintentando...\n");
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start();	
	}
	
	public void run(Server server){
		ServerBroadcaster broadcaster = new ServerBroadcaster(this.BCADDR, this.BCsocket);
//		ServerQueryResponder responder = new ServerQueryResponder(server.BCADDR.getAddress(), 4445);
		broadcaster.start();
//		responder.start();
	}
	
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
	
//	private void enviarPaq() {
//		while(true) {
//			try {
//				buf = "me cago encima".getBytes();
//				DatagramPacket packet = new DatagramPacket(buf, buf.length, BCADDR);
//				socket.send(packet);
//				sleep(2990);
//			} catch (IOException | InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
}

