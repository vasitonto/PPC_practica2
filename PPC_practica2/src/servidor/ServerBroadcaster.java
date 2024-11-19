package servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ServerBroadcaster extends Thread{
	
	private static int myPort = 4445; // puerto en el que el servidor va a recibir los mensajes
	private static final int BCPORT = 4999; // broadcast port
	private InetSocketAddress BCADDR;
	private DatagramSocket socket;
	private byte[] buf = new byte[256];
	
	public ServerBroadcaster() {
		try {
			this.BCADDR = new InetSocketAddress(InetAddress.getByName("255.255.255.255"), BCPORT);
			
		} catch (UnknownHostException e) {
//			System.err.println("Error en el establecimiento del puerto. Reintentando...\n");
			e.printStackTrace();
		}
	}
	
	public void run(DatagramSocket socket) {
		for(int i = 0; i < 3; i++) {
//		while (true) {
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
}
