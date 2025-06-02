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
	
	// crea un mensaje de control según el tipo que sea, puede incluir un dato para el cambio de frecuencia
	public static String creaControl(ControlCodes codigo, int dato) {
		String mensaje;
		switch(codigo) {
		case STOP:
			mensaje = "{ \"solicitud\": \"stop\"}";
			break;
		case CONTINUE:
			mensaje = "{ \"solicitud\": \"continue\"}";
			break;
		case SEND_JSON:
			mensaje = "{ \"solicitud\": \"formato\", \"formato\": \"json\"}";
			break;
		case SEND_XML:
			mensaje = "{ \"solicitud\": \"formato\", \"formato\": \"xml\"}";
			break;
		case MOD_FREQ:
			mensaje = "{ \"solicitud\": \"cambioFreq\", \"intervalo\": \"" + dato + "\"}";
			break;
		default:
			mensaje = "{ \"solicitud\": \"hello\"}";
			break;
		}
		return mensaje;
	}
}