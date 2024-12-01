package cliente;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ClientParser {
	//TODO hacer tratamiento de errores
	public static Document loadXMLFromString(String xml) throws ParserConfigurationException, SAXException, IOException {
	    DocumentBuilderFactory DBFactory= DocumentBuilderFactory.newInstance();
	    DocumentBuilder DocBuilder = DBFactory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    Document doc = DocBuilder.parse(is);
	    doc.getDocumentElement().normalize();
	    return doc;
	}
	
		public static String creaControl(int tipo, int dato) {
			String mensaje;
			switch(tipo) {
			case 0:
				mensaje = "{ \"solicitud\": \"formato\", \"formato\": \"json\"}";
				break;
			case 1:
				mensaje = "{ \"solicitud\": \"formato\", \"formato\": \"xml\"}";
				break;
			case 2:
				mensaje = "{ \"solicitud\": \"stop\"}";
				break;
			case 3:
				mensaje = "{ \"solicitud\": \"continue\"}";
				break;
			case 4:
				mensaje = "{ \"solicitud\": \"cambioFreq\", \"intervalo\": \"" + dato + "\"}";
				break;
			default:
				mensaje = "{ \"solicitud\": \"hello\"}";
				break;
			}
			return mensaje;
		}
}