package servidor;

import java.io.IOException;  
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import org.w3c.dom.Document;
import java.net.MulticastSocket;

public class ServerBroadcaster extends Thread{
	
	private InetSocketAddress BCADDR;
	private DatagramSocket socket;
	private byte[] buf = new byte[4096];
	private String id;
	private String tipoBC;
	
	public ServerBroadcaster(InetSocketAddress dir, MulticastSocket socket, String serverID[]) {
		this.BCADDR = dir;
		this.socket = socket;
		this.id = serverID[0];
		this.tipoBC = serverID[1];
	}
	
	public void run() {
//		for(int i = 0; i < 3; i++) {
		while (true) {
			try {
				String fecha = LocalDateTime.now().toString();
				String hora = fecha.substring(11, 22);
				switch(this.tipoBC) {
				case "agua":
					String aber = ServerParser.getDatosAgua(id);
					System.out.println("Server "+ id + " Enviando datos del agua");
					buf = aber.getBytes();
					break;
				case "precipitacion":
					buf = ServerParser.getDatosPrecip(id).getBytes();
					System.out.println("Server "+ id + " Enviando datos de precipitaciones");
					break;
				case "viento":
					buf = ServerParser.getDatosAire(id).getBytes();
					System.out.println("Server "+ id + " Enviando datos del viento");
					break;
				default: break;	
				}
				DatagramPacket packet = new DatagramPacket(buf, buf.length, BCADDR);
				socket.send(packet);
				sleep(3000);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
