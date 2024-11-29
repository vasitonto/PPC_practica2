package servidor;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import java.io.StringReader;

public class ServerParser {
	
	public static Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory DBFactory= DocumentBuilderFactory.newInstance();
	    DocumentBuilder DocBuilder = DBFactory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return DocBuilder.parse(is);
	}
	
	public static String getDatosAgua() {
		String reportAgua = "<?xml version=\"1.0\"?>\r\n"
				+ "<report servername=\"Server1\" formato=\"XML\" tipo=\"agua\">\r\n"
				+ "	<datos>\r\n"
				+ "	    <agua>\r\n"
				+ "	        <temperatura>22</temperatura>\r\n"
				+ "	        <nivel>31</nivel>\r\n"
				+ "	        <ph>50</ph>\r\n"
				+ "	    </agua>\r\n"
				+ "	</datos>\r\n"
				+ "</report>";
		return reportAgua;
	}
}
