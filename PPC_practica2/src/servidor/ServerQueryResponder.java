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
	private int[] datos = new int[5];
	
	
	public ServerQueryResponder(DatagramSocket socket, int[] datos) {
		this.socket = socket;
		this.datos = datos;
	}
	
	public void run() {
		while(true) {
			try {
				byte[] buf = new byte[256];
				DatagramPacket query = new DatagramPacket(buf, buf.length);
				socket.receive(query);
				String querystr = new String(query.getData(), 0, query.getLength());
				System.out.println("Recibido: " + querystr);
				procesaSol(ServerParser.parseaCtrl(querystr));
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
	
	private void procesaSol(Solicitud sol) {
		String tipoSol = sol.getTipo();
		switch(tipoSol) {
		case "formato":
			String form = sol.getFormato();
			if(form.equals("xml")) {
				synchronized (datos) {
					this.datos[2] = 0;
				}
			}else {
				synchronized (datos) {
					this.datos[2] = 1;
				}
			}
			break;
		case "stop":
			synchronized (datos) {
				this.datos[3] = 1;
			}
			break;
		case "continue":
			synchronized (datos) {
				this.datos[3] = 0;
			}
			break;
		case "intervalo":
			int interv = sol.getIntervalo();
			synchronized (datos) {
				datos[4] = interv;
				
			}
			break;
		}
	}
}
