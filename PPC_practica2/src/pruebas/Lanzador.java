package pruebas;
import cliente.Client;
import servidor.Server;

public class Lanzador {
	public static void main(String[] args) {
//		Server serv = new Server();
		Client cliente = new Client();
		
		cliente.start();
//		serv.start();
		
		
	}
}
