package servidor;

import java.io.IOException; 
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.net.MulticastSocket;

public class ServerBroadcaster extends Thread{
	
	private InetSocketAddress BCADDR;
	private DatagramSocket socket;
	private byte[] buf = new byte[256];
	private DOMParser parser = new DOMParser();
	
	public ServerBroadcaster(InetSocketAddress dir, MulticastSocket socket) {
		this.BCADDR = dir;
		this.socket = socket;
	}
	
	public void run() {
//		for(int i = 0; i < 3; i++) {
		while (true) {
			try {
				String fecha = LocalDateTime.now().toString();
				buf = fecha.substring(11, 22).getBytes();
				DatagramPacket packet = new DatagramPacket(buf, buf.length, BCADDR);
				socket.send(packet);
				sleep(3000);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
