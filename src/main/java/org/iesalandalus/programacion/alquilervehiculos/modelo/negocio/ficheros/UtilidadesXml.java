package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class UtilidadesXml {

	private UtilidadesXml() {

	}

	public static DocumentBuilder crearConstructorDocumentoXml() {
		DocumentBuilder constructor = null;
		try {
			DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();
			factoria.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			constructor = factoria.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.out.println("Error al crear el constructor.");
		}
		return constructor;
	}

	public static void escribirXmlAFichero(Document documento, File salida) {
		try (FileWriter ficheroSalida = new FileWriter(salida)) {
			TransformerFactory factoria = TransformerFactory.newInstance();
			factoria.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			factoria.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
			Transformer conversor = factoria.newTransformer();
			conversor.setOutputProperty(OutputKeys.INDENT, "yes");
			conversor.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			conversor.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			StreamResult destino = new StreamResult(salida);
			DOMSource fuente = new DOMSource(documento);
			conversor.transform(fuente, destino);
			System.out.printf("El fichero %s ha sido escrito correctamente.%n", salida);
		} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
			System.out.println("Imposible crear el conversor.");
		} catch (TransformerException e) {
			System.out.println("Error irecuperable en la conversión.");
		} catch (IOException e) {
			System.out.printf("No existe el directorio de destino o no tengo permiso de escritura: %s.%n", salida);
		}
	}

	public static Document leerXmlDeFichero(File ficheroXml) {
		Document documentoXml = null;
		try {
			DocumentBuilder constructor = crearConstructorDocumentoXml();
			if (constructor != null) {
				documentoXml = constructor.parse(ficheroXml);
				documentoXml.getDocumentElement().normalize();
			}
		} catch (SAXException e) {
			System.out.println("Documento XML mal formado.");
		} catch (IOException e) {

		}
		return documentoXml;
	}
}
