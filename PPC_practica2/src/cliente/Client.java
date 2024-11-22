package cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client extends Thread{
		
//	private Runnable javi;
//	private Thread pedro;
	private static int portListen = 4999;
	private static int portCtrl = 5000;
	private static int portServ = 4445; //TODO quitar esto para sacar el puerto de los mensajes
	private DatagramSocket socketListen; // socket para escuchar broadcasts
	private DatagramSocket socketCtrl; // socket para enviar msg de control y recibirlos
	private byte[] buf = new byte[256];
 
    public Client() {
    	try {
    		//TODO implementar el aumento del puerto (static)
			this.socketListen = new DatagramSocket(portListen);
			this.socketCtrl = new DatagramSocket(portCtrl);
		} catch (SocketException e) {
			e.printStackTrace();
		}
    }
    
    private Thread jose = new Thread(new Runnable() {
		private byte[] buf2 = new byte[256];
    	private DatagramPacket recvPak = new DatagramPacket(buf2, buf2.length);
		@Override
		public void run() {
			while(true) {
				try {
					socketListen.receive(recvPak);
					String msg = new String(recvPak.getData(), 0, recvPak.getLength());
					System.out.println("De " + recvPak.getSocketAddress() + ": " + msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}			
		};
	});
    
    public void recibePaquete() {
    	while (true) {
    		try {
    			DatagramPacket pak = new DatagramPacket(buf, buf.length);
    			socketCtrl.receive(pak);
    			String msg = new String(pak.getData(), 0, pak.getLength());
    			System.out.println(msg);
				
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}	
    }
    
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public static void main(String[] args) {
		Client cli = new Client();
//		cli.recibePaquete();
		cli.jose.start();
		cli.enviaPaquete("mensaje del cliente\n");
	}
}

