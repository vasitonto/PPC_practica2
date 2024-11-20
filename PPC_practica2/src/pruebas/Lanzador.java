package pruebas;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import cliente.Client;
import servidor.Server;

public class Lanzador {
	public static void main(String[] args) {
		String bcAddrString = "255.255.255.255";
		try {
			DatagramSocket s = new DatagramSocket(5001);
			System.out.println(s.toString());
//			s.setBroadcast(true);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
