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
	private int id, tipoBC;
	private int[] datos = new int[5];
	
	public ServerBroadcaster(InetSocketAddress dir, MulticastSocket socket, int[] datos) {
		this.BCADDR = dir;
		this.socket = socket;
		this.id = datos[0];
		this.tipoBC = datos[1];
		this.datos = datos;
	}
	
	public void run() {
		int format;
//		for(int i = 0; i < 3; i++) {
		while (true) {
			try {
				String fecha = LocalDateTime.now().toString();
				String hora = fecha.substring(11, 22);
				synchronized (datos) {
					format = datos[2];
				}
				switch(this.tipoBC) {
				case 0:
					String aber = ServerParser.getDatosAgua(id, format);
					System.out.println("Server "+ id + " Enviando datos del agua");
					buf = aber.getBytes();
					break;
				case 1:
					buf = ServerParser.getDatosPrecip(id, format).getBytes();
					System.out.println("Server "+ id + " Enviando datos de precipitaciones");
					break;
				case 2:
					buf = ServerParser.getDatosAire(id, format).getBytes();
					System.out.println("Server "+ id + " Enviando datos del viento");
					break;
				default: break;	
				}
				DatagramPacket packet = new DatagramPacket(buf, buf.length, BCADDR);
				socket.send(packet);
				sleep(this.datos[4]);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
