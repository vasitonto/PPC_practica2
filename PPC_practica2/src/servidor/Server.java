package servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
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
	private static final int BCSocketPort = 4445; // puerto desde el cual se enviarán los mensajes bcast
//	private static final int CTRLPORT = 4446;
	private static final int BCPORT = 4999; // puerto en el que escucha el cliente
	private static final String grupoMulticast = "224.48.75.1";
	private InetSocketAddress BCADDR;
	private MulticastSocket BCSocket;
	private DatagramSocket CTRLSocket;
	private byte[] buf = new byte[256];
	
	public Server(){ 
		try {
			this.BCSocket = new MulticastSocket(BCSocketPort);
			// BCADDR será la dirección a la que se enviarán los paquetes de broadcast
			this.BCADDR = new InetSocketAddress(InetAddress.getByName(grupoMulticast), BCPORT);
			this.CTRLSocket = new DatagramSocket(4446);
			
		} catch (IOException e) {
//			System.err.println("Error en el establecimiento del puerto. Reintentando...\n");
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		Server server = new Server();
		server.BCSocket.joinGroup(server.BCADDR, NetworkInterface.getByName(grupoMulticast));
		ServerQueryResponder responder = new ServerQueryResponder(server.CTRLSocket);
		ServerBroadcaster broadcaster = new ServerBroadcaster(server.BCADDR, server.BCSocket);
		responder.start();
		broadcaster.start();
	}
	
	public void run(Server server){
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

