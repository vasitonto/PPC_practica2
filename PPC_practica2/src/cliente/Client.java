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
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	private Map<String, InetSocketAddress> servidores;
 
    public Client() {
    	
    	int puerto1 = this.portListen;
    	int puerto2 = this.portCtrl;
    	// Mediante este bucle se pueden lanzar varios clientes
    	while(true) {
    		try {
    			this.BCADDR = new InetSocketAddress(InetAddress.getByName(grupoMulticast), portListen);
    			this.socketListen = new MulticastSocket(portListen);
    			this.socketCtrl = new DatagramSocket(puerto2);
    			break;
    		} catch (IOException e) {
    			puerto1++;
    			puerto2++;
    		}
    	}
    }
        
    
    public void recibePaquete() {
//    	while (true) {
    	// TODO tocar esto pa que lea diferentes tipos, entender el DOM
    	try {
	    	socketListen.joinGroup(BCADDR, NetworkInterface.getByName(grupoMulticast));
	    	for(int i = 0; i < 4; i++) {
    			DatagramPacket pak = new DatagramPacket(buf, buf.length);
    			socketListen.receive(pak);
    			String msg = new String(pak.getData(), 0, pak.getLength());
    			Document reportDoc = ClientParser.loadXMLFromString(msg);
    			NodeList reportNodeList = reportDoc.getElementsByTagName("report");
    			Element reportElement = reportDoc.getDocumentElement();
    			
    			
    			Element root = reportDoc.getDocumentElement();                
                // Paso 4: Acceder a los atributos del elemento raíz
                String servername = root.getAttribute("servername");
                String formato = root.getAttribute("formato");
                String tipo = root.getAttribute("tipo");
                
                System.out.print(servername + " [formato: " + formato + ", datos: "+ tipo + "]: ");
                
                // Paso 5: Obtener los elementos <datos> y dentro los elementos <agua>
                NodeList datosList = reportDoc.getElementsByTagName("datos");
                Node datosNode = datosList.item(0);
                
                // Obtener los elementos <agua> dentro de <datos>
                NodeList aguaList = ((Element) datosNode).getElementsByTagName("agua");
                Node aguaNode = aguaList.item(0);
                
                String temperatura = ((Element) aguaNode).getElementsByTagName("temperatura").item(0).getTextContent();
                String nivel = ((Element) aguaNode).getElementsByTagName("nivel").item(0).getTextContent();
                String ph = ((Element) aguaNode).getElementsByTagName("ph").item(0).getTextContent();
                System.out.println("temperatura: " + temperatura + ", nivel: " + nivel + ", ph: " + ph);
    			
    			
			} 
	    	socketListen.leaveGroup(BCADDR, NetworkInterface.getByName(grupoMulticast));
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
//TODO investigar como interrumpir este hilo o la funcion de arriba    
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
    
    public void enviaControl(String msg) {
    	byte [] bufResp = msg.getBytes();
    	
		DatagramPacket resp;
		try {
			resp = new DatagramPacket(bufResp, bufResp.length, InetAddress.getLocalHost(), 4446);
			socketCtrl.send(resp); 
			sleep(3000);
			socketCtrl.receive(resp);
			System.out.println(new String(resp.getData(), 0, resp.getLength()));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
    
    private int terminalCliente() {
    	boolean print = true;
    	System.out.println("Teclea \"help\" para ver la lista de comandos.");
    	while(true) {
    		BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
    		if(print) {
    			System.out.println("Esperando input...: ");    			
    		}
    		try {
    			String command = lector.readLine();
    			switch (command) {
    			case "exit":
    				return 0;
    			case "listen":
    				recibePaquete();
//    				this.listenThread.run();
//    				print = false;
    				break;
    			case "stop":
//    				this.listenThread.interrupt();
    				break;
    			case "control":
    				enviaControl("mensaje de control");
    				break;
    			case "help":
    				System.out.println("exit - termina el programa.\nlisten - escucha mensajes de los servidores\n"
    						+ "control - envía mensajes de control al servidor\nhelp - muestra esta ayuda");
    				break;
    			default:
    				System.out.println("No se esperaba esa palabra, inténtalo de nuevo...");
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
//		try {
//			cli.socketListen.joinGroup(cli.BCADDR, NetworkInterface.getByName(grupoMulticast));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("escuchando en " + cli.socketListen.getLocalPort() + ", controlando en " + cli.socketCtrl.getLocalPort());
		if(cli.terminalCliente() == 0) {
			System.exit(0);
		}
//		cli.recibePaquete();
	}
}

