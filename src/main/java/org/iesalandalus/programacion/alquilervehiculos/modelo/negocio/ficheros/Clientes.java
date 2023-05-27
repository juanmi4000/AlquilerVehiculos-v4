package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.DocumentBuilder;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IClientes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Clientes implements IClientes {

	private static final File FICHERO_CLIENTES = new File(
			String.format("%s%s%s", "datos", File.separator, "clientes.xml"));
	private static final String RAIZ = "clientes";
	private static final String CLIENTE = "cliente";
	private static final String NOMBRE = "nombre";
	private static final String DNI = "dni";
	private static final String TELEFONO = "telefono";

	private static Clientes instancia;

	private List<Cliente> coleccionClientes;

	private Clientes() {
		coleccionClientes = new ArrayList<>();
	}

	static Clientes getInstancia() {
		if (instancia == null) {
			instancia = new Clientes();
		}
		return instancia;
	}

	@Override
	public void comenzar() {
		Document documento = UtilidadesXml.leerXmlDeFichero(FICHERO_CLIENTES);
		if (documento != null) {
			System.out.println("El fichero XML de Clientes, ha sido leído correctamente.");
			leerDom(documento);
		} else {
			System.out.printf("No se puede leer el fichero de entrada: %s.%n", FICHERO_CLIENTES);
		}

	}

	private void leerDom(Document documentoXml) {
		NodeList clientes = documentoXml.getElementsByTagName(CLIENTE);
		for (int i = 0; i < clientes.getLength(); i++) {
			Node cliente = clientes.item(i);
			if (cliente.getNodeType() == Node.ELEMENT_NODE) {
				try {
					insertar(getCliente((Element) cliente));
				} catch (NullPointerException | IllegalArgumentException | OperationNotSupportedException e) {
					System.out.println(e.getMessage());
					System.out.printf("ERROR: El error esta en el cliente que ocupa la posición: %s%n", i);
				}

			}
		}
	}

	private Cliente getCliente(Element elemento) {
		String nombre = elemento.getAttribute(NOMBRE);
		String dni = elemento.getAttribute(DNI);
		String telefono = elemento.getAttribute(TELEFONO);
		return new Cliente(nombre, dni, telefono);
	}

	@Override
	public void terminar() {
		UtilidadesXml.escribirXmlAFichero(crearDom(), FICHERO_CLIENTES);
	}

	private Document crearDom() {
		DocumentBuilder constructor = UtilidadesXml.crearConstructorDocumentoXml();
		Document documentoXml = null;
		if (constructor != null) {
			documentoXml = constructor.newDocument();
			documentoXml.appendChild(documentoXml.createElement(RAIZ));
			for (Cliente cliente : coleccionClientes) {
				Element elementoCliente = getElemento(documentoXml, cliente);
				documentoXml.getDocumentElement().appendChild(elementoCliente);
			}
		}
		return documentoXml;
	}

	private Element getElemento(Document documentoXml, Cliente cliente) {
		Element elementoCliente = documentoXml.createElement(CLIENTE);
		elementoCliente.setAttribute(NOMBRE, cliente.getNombre());
		elementoCliente.setAttribute(DNI, cliente.getDni());
		elementoCliente.setAttribute(TELEFONO, cliente.getTelefono());
		return elementoCliente;
	}

	@Override
	public List<Cliente> get() {
		return new ArrayList<>(coleccionClientes);
	}

	@Override
	public void insertar(Cliente cliente) throws OperationNotSupportedException {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede insertar un cliente nulo.");
		}
		if (!coleccionClientes.contains(cliente)) {
			coleccionClientes.add(cliente);
		} else {
			throw new OperationNotSupportedException("ERROR: Ya existe un cliente con ese DNI.");
		}
	}

	@Override
	public Cliente buscar(Cliente cliente) {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede buscar un cliente nulo.");
		}
		int indice = coleccionClientes.indexOf(cliente);
		if (indice == -1) {
			cliente = null;
		} else {
			cliente = coleccionClientes.get(indice);
		}
		return cliente;
	}

	@Override
	public void borrar(Cliente cliente) throws OperationNotSupportedException {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede borrar un cliente nulo.");
		}
		if (!coleccionClientes.contains(cliente)) {
			throw new OperationNotSupportedException("ERROR: No existe ningún cliente con ese DNI.");
		}
		coleccionClientes.remove(cliente);

	}

	@Override
	public void modificar(Cliente cliente, String nombre, String telefono) throws OperationNotSupportedException {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede modificar un cliente nulo.");
		}
		Cliente clienteEncontrado = buscar(cliente);
		if (clienteEncontrado == null) {
			throw new OperationNotSupportedException("ERROR: No existe ningún cliente con ese DNI.");
		}
		if (nombre != null && !nombre.isBlank()) {
			clienteEncontrado.setNombre(nombre);
		} else {
			if (nombre.isBlank()) {
				throw new IllegalArgumentException("ERROR: el nombre no puede estar vacio.");
			}
		}
		if (telefono != null && !telefono.isBlank()) {
			clienteEncontrado.setTelefono(telefono);
		} else {
			if (telefono.isBlank()) {
				throw new IllegalArgumentException("ERROR: el teléfono no puede estar vacio.");
			}
		}

	}
}
