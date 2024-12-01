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
//	private static final int CtrlSocketPort = 4446;
	private static final int BCPORT = 4999; // puerto en el que escucha el cliente
	private static final String grupoMulticast = "224.48.75.1";
	private InetSocketAddress BCADDR;
	private MulticastSocket BCSocket;
	private DatagramSocket CTRLSocket;
	private String[] serverID = new String[2];
	
	public Server(String id, String tipo, int escucha){
		this.serverID[0] = id;
		this.serverID[1] = tipo;
		int puerto1 = BCSocketPort;
//		int puerto2 = CtrlSocketPort;
		while(true) {
			try {
				// BCADDR será la dirección a la que se enviarán los paquetes de broadcast
				this.BCADDR = new InetSocketAddress(InetAddress.getByName(grupoMulticast), BCPORT);
				this.BCSocket = new MulticastSocket(puerto1);
				this.BCSocket.setReuseAddress(true);
				break;
			} catch (IOException e1) {
				puerto1++;
			}
		}
		while(true) {
			try {
				// para el socket de control también necesitamos un puerto nuevo, así que 
				// le tendremos que hacer el tratamiento de errores por separado
				this.CTRLSocket = new DatagramSocket(escucha); 
//				this.CTRLSocket.bind(new InetSocketAddress(InetAddress.getByName("localhost"), puerto2));
				break;		
			} catch (IOException e) {
//				puerto2++;
			}	
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		Server server = new Server("1", "agua", 4446);
		server.BCSocket.joinGroup(server.BCADDR, NetworkInterface.getByName(grupoMulticast));
		ServerQueryResponder responder = new ServerQueryResponder(server.CTRLSocket);
		// TODO cambiar el nombre del servidor
		ServerBroadcaster broadcaster = new ServerBroadcaster(server.BCADDR, server.BCSocket, server.serverID);
		responder.start();
		broadcaster.start();
		System.out.println("s1 ehcho");
		
		Server server2 = new Server("2", "viento", 4447);
		server2.BCSocket.joinGroup(server2.BCADDR, NetworkInterface.getByName(grupoMulticast));
		ServerQueryResponder responder2 = new ServerQueryResponder(server2.CTRLSocket);
		ServerBroadcaster broadcaster2 = new ServerBroadcaster(server2.BCADDR, server2.BCSocket, server2.serverID);
		responder2.start();
		broadcaster2.start();
		
		Server server3 = new Server("3", "precipitacion", 4448);
		server3.BCSocket.joinGroup(server3.BCADDR, NetworkInterface.getByName(grupoMulticast));
		ServerQueryResponder responder3 = new ServerQueryResponder(server3.CTRLSocket);
		ServerBroadcaster broadcaster3 = new ServerBroadcaster(server3.BCADDR, server3.BCSocket, server3.serverID);
		responder3.start();
		broadcaster3.start();
		System.out.println("hola");
	}
	
	
//	public void run(Server server){
//		System.out.println("servidor iniciado");
//		try {
//			this.BCSocket.joinGroup(this.BCADDR, NetworkInterface.getByName(grupoMulticast));
//		} catch (SocketException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		ServerQueryResponder responder = new ServerQueryResponder(this.CTRLSocket);
//		ServerBroadcaster broadcaster = new ServerBroadcaster(this.BCADDR, this.BCSocket, this.tipoServer);
//		responder.start();
//		broadcaster.start();
//		boolean running = true;
//		while (running) {
//            try {
//                Thread.sleep(1000); // Simulación de trabajo del servidor
//            } catch (InterruptedException e) {
//                System.out.println("El servidor fue interrumpido.");
//                running = false;
//            }
//		}
//	}
}

