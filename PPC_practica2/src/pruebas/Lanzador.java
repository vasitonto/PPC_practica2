package pruebas;
import java.net.InetAddress;
import java.net.UnknownHostException;

import cliente.Client;
import servidor.Server;

public class Lanzador {
	public static void main(String[] args) {
		String bcAddrString = "255.255.255.255";
		
		try {
			InetAddress bcAddr = InetAddress.getByName(bcAddrString);
			System.out.println(bcAddr.toString());
			System.out.println("adios");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
