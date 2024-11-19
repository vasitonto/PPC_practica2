package servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ServerQueryResponder extends Thread {
	private DatagramSocket socket;
	private byte[] buf = new byte[256];
	
	public ServerQueryResponder(InetAddress dir, int port) {
		try {
			this.socket = new DatagramSocket(port, dir);
		}
		catch (SocketException e) {
			e.printStackTrace();
			System.out.println("error en el constructor"); //TODO DELETE THIS
		}
	}
	
	public void run() {
		try {
			DatagramPacket query = new DatagramPacket(buf, buf.length);
			socket.receive(query);
			System.out.println("Recibido: " + query.getData().toString());
		}
		catch(IOException e) {
			System.out.println("error en el run"); //TODO DELETE THIS
			e.printStackTrace();
		}
	}
}
