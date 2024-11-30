package pruebas;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import cliente.Client;
import servidor.Server;

public class Lanzador {
	public static void main(String[] args) {
		Server s1 = new Server("agua");
		Server s2 = new Server("precipitacion");
		
		new Thread(s1::run).start();
//        new Thread(s2::run).start();
        
        System.out.println("Presiona ENTER para salir...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine(); // Espera entrada del usuario

        s1.stop();
//        s2.stop();
        System.out.println("Servidores detenidos. Saliendo del programa.");
	}
}
