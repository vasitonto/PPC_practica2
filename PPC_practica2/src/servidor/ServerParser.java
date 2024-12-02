package servidor;

import java.io.StringReader;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.google.gson.Gson;


public class ServerParser{

	private enum aireDireccion {Norte, Sur, Este, Oeste, Noroeste, Noreste, Suroeste, Sureste};
	private enum precipitacionTipo {Lluvia, Nieve, Granizo, Tormenta};
	private enum precipitacionIntensidad {Nula, Suave, Ligera, Moderada, Fuerte, Intensa, Extrema};
	
	
	public static Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory DBFactory= DocumentBuilderFactory.newInstance();
	    DocumentBuilder DocBuilder = DBFactory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return DocBuilder.parse(is);
	}
	
	public static String parseaCtrl(String query) {
		Gson gson = new Gson();
	}
	
	public static String getDatosAgua(String id) {
		String reportAgua = "<?xml version=\"1.0\"?>\r\n"
				+ "<report servername=\"Server" + id + "\" formato=\"XML\" tipo=\"agua\">\r\n"
				+ "	 <datos>\r\n"
				+ "	    <agua>\r\n"
				+ "	        <temperatura>" + ThreadLocalRandom.current().nextInt(1, 64) + "</temperatura>\r\n"
				+ "	        <nivel>" + ThreadLocalRandom.current().nextInt(1, 101)+"</nivel>\r\n"
				+ "	        <ph>" + String.valueOf(ThreadLocalRandom.current().nextDouble(2.0, 8.0)).substring(0, 4) + "</ph>\r\n"
				+ "	    </agua>\r\n"
				+ "	 </datos>\r\n"
				+ "</report>";	
		return reportAgua;
	}
	
	public static String getDatosSuelo(String id) {
		String reportSuelo = "hola";
		return reportSuelo;
	}
	
	public static String getDatosAire(String id) {
		String reportAire = "<?xml version=\"1.0\"?>\r\n"
				+ "<report servername=\"Server" + id + "\" formato=\"XML\" tipo=\"aire\">\r\n"
				+ "<datos>\r\n"
				+ "<aire>\r\n"
				+ "<temperatura>" + ThreadLocalRandom.current().nextInt(-30, 64) + "</temperatura>\r\n"
				+ "<velocidad>" + ThreadLocalRandom.current().nextInt(0, 125)+"</velocidad>\r\n"
				+ "<humedad>" + ThreadLocalRandom.current().nextInt(1, 101) + "</humedad>\r\n"
				+ "<direccion>" + aireDireccion.values()[ThreadLocalRandom.current().nextInt(0, 8)] + "</direccion>\r\n"	
				+ "</aire>\r\n" 
				+ "</datos>\r\n"
				+ "</report>";	
		return reportAire;
	}
	
	public static String getDatosPrecip(String id) {
		String reportPrecip = "<?xml version=\"1.0\"?>\r\n"
				+ "<report servername=\"Server" + id + "\" formato=\"XML\" tipo=\"precipitacion\">\r\n"
				+ "<datos>\r\n"
				+ "<precipitacion>\r\n"
				+ "<tipo>" + precipitacionTipo.values()[ThreadLocalRandom.current().nextInt(0, 4)] + "</tipo>\r\n"
				+ "<intensidad>" + precipitacionIntensidad.values()[ThreadLocalRandom.current().nextInt(0, 7)] +"</intensidad>\r\n"
				+ "<cantidad>" + ThreadLocalRandom.current().nextInt(0, 3001) + "</cantidad>\r\n"
				+ "</precipitacion>\r\n"
				+ "</datos>\r\n"
				+ "</report>";	
		return reportPrecip;
	}
}

