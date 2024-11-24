package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread{
		
//	private Runnable javi;
//	private Thread pedro;
	private static final int portListen = 4999;
	private static final int portCtrl = 5999;
	private static int portServ = 4445; 
	//TODO quitar esto para sacar el puerto de los mensajes
	private static final String grupoMulticast = "224.48.75.1";
	private MulticastSocket socketListen; // socket para escuchar broadcasts
	private DatagramSocket socketCtrl; // socket para enviar msg de control y recibirlos
	private InetSocketAddress BCADDR;
	private byte[] buf = new byte[256];
 
    public Client() {
    	int puerto1 = this.portListen;
//    	int puerto2 = this.portCtrl;
    	// Mediante este bucle se pueden lanzar varios clientes
    	while(true) {
    		try {
    			this.BCADDR = new InetSocketAddress(InetAddress.getByName(grupoMulticast), portListen);
    			this.socketListen = new MulticastSocket(puerto1);
//    			this.socketCtrl = new DatagramSocket(puerto2);
    			break;
    		} catch (IOException e) {
    			puerto1++;
//    			puerto2++;
    		}
    	}
    }
        
    
    
    public void recibePaquete() {
    	while (true) {
    		try {
    			DatagramPacket pak = new DatagramPacket(buf, buf.length);
    			socketListen.receive(pak);
    			String msg = new String(pak.getData(), 0, pak.getLength());
    			System.out.println(msg);				
			} 
    		catch (IOException e) {
				e.printStackTrace();
				break;
			}
    		//TODO meter esto en un thread para poder interrumpirlo por terminal
//    		catch (InterruptedException e) {
//    			
//    		}
		}	
    }
    
    private Thread listenThread = new Thread(new Runnable() {
		private byte[] buf2 = new byte[256];
    	private DatagramPacket recvPak = new DatagramPacket(buf2, buf2.length);
		@Override
		public void run() {
			for(int i = 0; i < 5; i++){
//			while(true) {
				try {
					socketListen.receive(recvPak);
					String msg = new String(recvPak.getData(), 0, recvPak.getLength());
					System.out.println("De " + recvPak.getSocketAddress() + ": " + msg);
				} 
				catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}			
		};
	});
    
    public void enviaPaquete(String msg) {
    	byte [] bufResp = msg.getBytes();
    	
		DatagramPacket resp;
		try {
			resp = new DatagramPacket(bufResp, bufResp.length, InetAddress.getLocalHost(), 4445);
			for(int i = 0; i <10; i++) { 
				socketCtrl.send(resp); 
				sleep(3000); 
			}
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
    
    private int terminalCliente() {
    	boolean print = true;
    	while(true) {
    		BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
    		if(print) {
    			System.out.println("Esperando input del cliente: ");    			
    		}
    		try {
    			String command = lector.readLine();
    			switch (command) {
    			case "exit":
    				return 0;
    			case "listen":
//    			recibePaquete();
    				this.listenThread.run();
//    				print = false;
    				break;
    			case "stop":
    				this.listenThread.interrupt();
    				break;
    			default:
    				System.out.println("No se esperaba ese comando, inténtalo de nuevo...\n");
    				break;
    			}
    		}
    		catch(IOException e){
    			e.printStackTrace();
    		}
    	}
    }
    
    public static void main(String[] args) {
		Client cli = new Client();
		try {
			cli.socketListen.joinGroup(cli.BCADDR, NetworkInterface.getByName(grupoMulticast));
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println("escuchando en " + cli.socketListen.getLocalPort() + ", controlando en " + cli.socketCtrl.getLocalPort());
		if(cli.terminalCliente() == 0) {
			System.exit(0);
		}
//		cli.recibePaquete();
//		cli.jose.start();
//		cli.enviaPaquete("mensaje del cliente\n");
	}
}

