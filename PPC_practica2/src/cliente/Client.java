package cliente;

import java.awt.BorderLayout; 
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Client extends JFrame implements Runnable{
		
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
	private JFrame consola;
	private JPanel contentPane;
	private JScrollPane salidaMensajes;
	private JTextArea textAreaEntrada;
	private JTextArea textAreaSalida;
	private String command;
 
    public Client() {
    	// ################# CODIGO DE SOCKETS ###############
    	int puerto2 = this.portCtrl;
    	// Mediante este bucle se pueden lanzar varios clientes
    	while(true) {
    		try {
    			this.BCADDR = new InetSocketAddress(InetAddress.getByName(grupoMulticast), portListen);
    			this.socketListen = new MulticastSocket(portListen);
    			this.socketCtrl = new DatagramSocket(puerto2);
    			break;
    		} catch (IOException e) {
    			puerto2++;
    		}
    	}
    	
    	// ################# CODIGO DE GUI ###############
    	consola = new JFrame("Consola Cliente"); // Creamos la ventana
        consola.setSize(400, 300);
    	consola.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	consola.setBounds(100, 100, 450, 300);
    	contentPane = new JPanel();
    	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    	
    	consola.setContentPane(contentPane);
    	contentPane.setLayout(new BorderLayout(0, 0));
    	
    	JSplitPane splitPane = new JSplitPane();
    	splitPane.setResizeWeight(0.5);
    	splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    	contentPane.add(splitPane, BorderLayout.CENTER);
    	
    	textAreaSalida = new JTextArea();
    	textAreaSalida.setEditable(false);
    	textAreaSalida.setLineWrap(true); // esto hace que se envuelvan las lineas muy largas
    	textAreaSalida.setWrapStyleWord(true); // esto corta las líneas por palabras
    	salidaMensajes = new JScrollPane(textAreaSalida);
    	salidaMensajes.setAutoscrolls(true);
    	splitPane.setLeftComponent(salidaMensajes);
    	
    	textAreaEntrada = new JTextArea();
    	textAreaEntrada.setLineWrap(true);
    	textAreaEntrada.setWrapStyleWord(true);
    	DefaultCaret caret = (DefaultCaret)textAreaEntrada.getCaret();
    	caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    	JScrollPane scrollPaneEntrada = new JScrollPane(textAreaEntrada);
    	scrollPaneEntrada.setAutoscrolls(true);
    	splitPane.setRightComponent(scrollPaneEntrada);
    	
    	anadirKeyListener();
    	
    	try {
    		this.consola.setVisible(true);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    }
        
    private void anadirKeyListener() {
    	textAreaEntrada.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    command = getTextoLinea();
                    synchronized(textAreaEntrada) {
                    	textAreaEntrada.notify();
                    }
                }
            }
        });
    }
    
    private String getTextoLinea() {
        int curPos = textAreaEntrada.getCaretPosition();
        
        // Obtener el número de línea en la que se encuentra el cursor
        try {
            javax.swing.text.Document doc = textAreaEntrada.getDocument();
            int linea = textAreaEntrada.getLineOfOffset(curPos); // Número de línea
            int inicioLinea = textAreaEntrada.getLineStartOffset(linea); // Inicio de la línea
            int finLinea = textAreaEntrada.getLineEndOffset(linea); // Fin de la línea

            // Obtener el texto de esa línea
            return doc.getText(inicioLinea, finLinea - inicioLinea);
        } catch (BadLocationException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public void recibePaquete() {
    	while (true) {
    	// TODO tocar esto pa que lea diferentes tipos, entender el DOM
	    	try {
	//	    	socketListen.joinGroup(BCADDR, NetworkInterface.getByName(grupoMulticast));
	//	    	for(int i = 0; i < 4; i++) {
	    			DatagramPacket pak = new DatagramPacket(buf, buf.length);
	    			socketListen.receive(pak);
	    			String msg = new String(pak.getData(), 0, pak.getLength());
	    			parsearPaquete(msg);
	//			} 
	//	    	socketListen.leaveGroup(BCADDR, NetworkInterface.getByName(grupoMulticast));
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    	} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }
    
    public void enviaControl(String msg) {
    	byte [] bufResp = msg.getBytes();
    	
		DatagramPacket resp;
		try {
			resp = new DatagramPacket(bufResp, bufResp.length, InetAddress.getLocalHost(), 4446);
			socketCtrl.send(resp); 
			socketCtrl.receive(resp);
			System.out.println(new String(resp.getData(), 0, resp.getLength()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    private int terminalCliente() {
    	boolean print = true;
    	textAreaEntrada.append("Teclea \"help\" para ver la lista de comandos.\n");
    	while(true) {
    		
    		if(print) {
    			textAreaEntrada.append("Esperando input...:\n");
    		}
			synchronized (textAreaEntrada) {
	            try {
	                // El hilo se bloquea aquí hasta que se presiona Enter
	                textAreaEntrada.wait();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
			}
			
			switch (command) {
			case "exit":
				return 0;
//			case "listen":
//				recibePaquete();
////    				this.listenThread.run();
////    				print = false;
//				break;
			case "stop":
//    				this.listenThread.interrupt();
				break;
			case "control":
				enviaControl("mensaje de control");
				break;
			case "help":
				textAreaEntrada.append("exit - termina el programa.\n"
//						+ "listen - escucha mensajes de los servidores\n"
						+ "control - envía mensajes de control al servidor\n"
						+ "clear - limpia la terminal\n" 
						+ "help - muestra esta ayuda\n");
				break;
			case "clear":
				textAreaEntrada.setText("");
				break;
			default:
				textAreaEntrada.append("No se esperaba esa palabra, inténtalo de nuevo...\n");
				break;
			}
			
    	}
    }
    
    public void consolePrint(String texto) {
    	textAreaEntrada.append(texto+"\r\n");
    	textAreaEntrada.setCaretPosition(textAreaEntrada.getDocument().getLength());
    }
    
    public static void main(String[] args) {
//		Client cli = new Client();
		SwingUtilities.invokeLater(new Client());
//		try {
//			cli.socketListen.joinGroup(cli.BCADDR, NetworkInterface.getByName(grupoMulticast));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("escuchando en " + cli.socketListen.getLocalPort() + ", controlando en " + cli.socketCtrl.getLocalPort());
//		if(cli.terminalCliente() == 0) {
//			System.exit(0);
//		}
		
	}
    
    public void run() {
//    	Thread hiloConsola = new Thread(()->{
//    		terminalCliente();
//    	});
//    	hiloConsola.start();
    	
    	ExecutorService exec = Executors.newFixedThreadPool(2);
    	
    	// TODO esta vaina 
    	Future <Integer> resultado = exec.submit(() -> terminalCliente());
    	exec.submit(() -> recibePaquete());
    	if(resultado == 0){
    		System.exit(0);
    	};
    	
    	exec.shutdown();
    }
    
    public void parsearPaquete(String msg) {
    	
		try {
			Document reportDoc = ClientParser.loadXMLFromString(msg);
			
			Element root = reportDoc.getDocumentElement();                
			// accedemos a los atribs. del nodo raíz
			String servername = root.getAttribute("servername");
			String formato = root.getAttribute("formato");
			String tipo = root.getAttribute("tipo");
			
			System.out.print(servername + " [formato: " + formato + ", datos: "+ tipo + "]: ");
			NodeList datosList = reportDoc.getElementsByTagName("datos");
			Node datosNode = datosList.item(0);
			NodeList listaValores= ((Element) datosNode).getElementsByTagName(tipo);
			Node nodoValores = listaValores.item(0);
			// según del tipo que sea el mensaje lo deberemos parsear de una forma u otra
			switch(tipo) {
			case "agua":
				// obtenemos el elemento "datos"
				
				// obtenemos los elementos dentro de "agua"
				
				String temperaturaAgua = ((Element) nodoValores).getElementsByTagName("temperatura").item(0).getTextContent();
				String nivel = ((Element) nodoValores).getElementsByTagName("nivel").item(0).getTextContent();
				String ph = ((Element) nodoValores).getElementsByTagName("ph").item(0).getTextContent();
				textAreaSalida.append("temperatura: " + temperaturaAgua + "ºC, nivel: " + nivel + "cm, ph: " + ph + "\n");
				break;
			
			case "aire":				
				// obtenemos los elementos para "viento"	
				String temperaturaViento = ((Element) nodoValores).getElementsByTagName("temperatura").item(0).getTextContent();
				String humedad = ((Element) nodoValores).getElementsByTagName("humedad").item(0).getTextContent();
				String direccion = ((Element) nodoValores).getElementsByTagName("direccion").item(0).getTextContent();
				String velocidad = ((Element) nodoValores).getElementsByTagName("velocidad").item(0).getTextContent();
				textAreaSalida.append("temperatura: " + temperaturaViento + "ºC, humedad: " + humedad 
						+ "%, direccion: " + direccion + ", velocidad: " + velocidad + "km/h\n");
				break;
			
			case "precipitacion":
				String tipoPrecip = ((Element) nodoValores).getElementsByTagName("tipo").item(0).getTextContent();
				String intensidad = ((Element) nodoValores).getElementsByTagName("nivel").item(0).getTextContent();
				String cantidad = ((Element) nodoValores).getElementsByTagName("ph").item(0).getTextContent();
				textAreaSalida.append("tipo: " + tipoPrecip + ", intensidad: " 
						+ intensidad + ", cantidad: " + cantidad + "mm\n" );
				break;
			
			default: break;
			}
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
    }
}

