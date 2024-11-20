package servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ServerBroadcaster extends Thread{
	
	private InetSocketAddress BCADDR;
	private DatagramSocket socket;
	private byte[] buf = new byte[256];
	
	public ServerBroadcaster(InetSocketAddress dir, DatagramSocket socket) {
		this.BCADDR = dir;
		this.socket = socket;
	}
	
	public void run() {
//		for(int i = 0; i < 3; i++) {
		System.out.println("hilo corriendo");
		while (true) {
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
