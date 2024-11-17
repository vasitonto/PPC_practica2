package cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client extends Thread{
		
//	private Runnable javi;
//	private Thread pedro;
	private static int portCli = 4999;
	private DatagramPacket packMulticast;
	private DatagramSocket socket;
	private byte[] buf = new byte[256];
 
    public Client() {
    	try {
			socket = new DatagramSocket(portCli, InetAddress.getLocalHost());
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Override
    public void run() {
    	try {
			DatagramSocket socket = new DatagramSocket();
			DatagramPacket pMult = new DatagramPacket(buf, buf.length);
			socket.receive(pMult);
			System.out.println(pMult.getData().toString());
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException ioe) {
			System.err.println("Error de tipo " + ioe.getClass());
			ioe.printStackTrace();
		}
    }
}

