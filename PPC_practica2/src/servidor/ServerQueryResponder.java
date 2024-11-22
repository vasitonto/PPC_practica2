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
	
//	public ServerQueryResponder(InetAddress dir, int port) {
	public ServerQueryResponder(DatagramSocket socket) {
		this.socket = socket;
	}
	
	public void run() {
		while(true) {
			try {
				DatagramPacket query = new DatagramPacket(buf, buf.length);
				socket.receive(query);
				System.out.println("Recibido: " + new String(query.getData(), 0, query.getLength()));
			}
			catch(IOException e) {
				System.out.println("error en el run"); //TODO DELETE THIS
				e.printStackTrace();
			}
		}
	}
}
