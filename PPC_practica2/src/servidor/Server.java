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
	private static final int CtrlSocketPort = 4446;
	private static final int BCPORT = 4999; // puerto en el que escucha el cliente
	private static final String grupoMulticast = "224.48.75.1";
	private InetSocketAddress BCADDR;
	private MulticastSocket BCSocket;
	private DatagramSocket CTRLSocket;
	private String tipoServer;
	
	public Server(String tipo){ 
		this.tipoServer = tipo;
		int puerto1 = BCSocketPort;
		int puerto2 = CtrlSocketPort;
		int puerto3 = BCPORT;
		while(true) {
			try {
				// BCADDR será la dirección a la que se enviarán los paquetes de broadcast
				this.CTRLSocket = new DatagramSocket(puerto2); 
//			this.CTRLSocket.bind(new InetSocketAddress(InetAddress.getByName("localhost"), puerto2));
				this.BCADDR = new InetSocketAddress(InetAddress.getByName(grupoMulticast), puerto3);
				this.BCSocket = new MulticastSocket(puerto1);
			this.BCSocket.bind(BCADDR);
				this.BCSocket.setReuseAddress(true);
				
			} catch (IOException e) {
				puerto1++;
				puerto2++;
				puerto3++;
			}
			
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		Server server = new Server("agua");
		server.BCSocket.joinGroup(server.BCADDR, NetworkInterface.getByName(grupoMulticast));
		ServerQueryResponder responder = new ServerQueryResponder(server.CTRLSocket);
		ServerBroadcaster broadcaster = new ServerBroadcaster(server.BCADDR, server.BCSocket, server.tipoServer);
		responder.start();
		broadcaster.start();
		System.out.println("s1 ehcho");
		// TODO cuando se lanza el segundo servidor no se inicializa correctamente el CTRLSocket (no tiene sentido esto)
		Server server2 = new Server("hola");
		server2.BCSocket.joinGroup(server2.BCADDR, NetworkInterface.getByName(grupoMulticast));
		ServerQueryResponder responder2 = new ServerQueryResponder(server2.CTRLSocket);
		ServerBroadcaster broadcaster2 = new ServerBroadcaster(server2.BCADDR, server2.BCSocket, server2.tipoServer);
		responder2.start();
		broadcaster2.start();
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

