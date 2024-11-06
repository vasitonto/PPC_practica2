package servidor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.locks.Lock;

public class servidor extends Thread{
	private static Lock cerrojoPuertos;
	private static int port = 4445;
	
	private DatagramSocket socket;
	private boolean running;
	private byte[] buf = new byte[256];
	
	public servidor() { 
		boolean r = true;
		while(r) {
			try {
				cerrojoPuertos.lock(); //pido acceso al cerrojo para modificar la variable estática
					socket = new DatagramSocket(port); 
					port++;
				cerrojoPuertos.unlock(); //devuelvo el acceso al cerrojo
				r = false;
			} catch (Exception e) {
				System.err.println("Error en el establecimiento del puerto. Reintentando...\n");
			}
		}
	}
	
	public void run()
	{
		running = true;
		while (running)
		{
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			InetAddress address = packet.getAddress();
			int port = packet.getPort();
			packet = new DatagramPacket(buf, buf.length, address, port);
			String received = new String(packet.getData(), 0, packet.getLength());
			
			if (received.equals("end"))
			{
			running = false;
			continue;
		}
			
		socket.send(packet);
	}
	socket.close();
	}
}

