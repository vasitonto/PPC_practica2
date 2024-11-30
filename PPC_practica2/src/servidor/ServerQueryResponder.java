package servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public class ServerQueryResponder extends Thread {
	private DatagramSocket socket;
	
//	public ServerQueryResponder(InetAddress dir, int port) {
	public ServerQueryResponder(DatagramSocket socket) {
		this.socket = socket;
	}
	
	public void run() {
		while(true) {
			try {
				byte[] buf = new byte[256];
				DatagramPacket query = new DatagramPacket(buf, buf.length);
				socket.receive(query);
				System.out.println("Recibido: " + new String(query.getData(), 0, query.getLength()));
				// hacer control del mensaje y tal
				byte[] buf2 = new byte[512];
				buf2 = "el server recibió el mensaje de control".getBytes();
				System.out.println(query.getSocketAddress());
				DatagramPacket ack = new DatagramPacket(buf2, buf2.length, new InetSocketAddress(InetAddress.getByName("192.168.1.14"), 5999));
				socket.send(ack);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
