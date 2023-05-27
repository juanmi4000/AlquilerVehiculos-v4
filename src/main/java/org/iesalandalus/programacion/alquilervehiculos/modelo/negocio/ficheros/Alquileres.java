package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.DocumentBuilder;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IAlquileres;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Alquileres implements IAlquileres {

	private static final File FICHERO_ALQUILERES = new File(
			String.format("%s%s%s", "datos", File.separator, "alquileres.xml"));
	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final String RAIZ = "alquileres";
	private static final String ALQUILER = "alquiler";
	private static final String CLIENTE = "cliente";
	private static final String VEHICULO = "vehiculo";
	private static final String FECHA_ALQUILER = "fechaAlquiler";
	private static final String FECHA_DEVOLUCION = "fechaDevolucion";

	private static Alquileres instancia;

	List<Alquiler> coleccionAlquileres;

	private Alquileres() {
		coleccionAlquileres = new ArrayList<>();
	}

	static Alquileres getInstancia() {
		if (instancia == null) {
			instancia = new Alquileres();
		}
		return instancia;
	}

	@Override
	public void comenzar() {
		Document documento = UtilidadesXml.leerXmlDeFichero(FICHERO_ALQUILERES);
		if (documento != null) {
			System.out.println("El fichero XML de Alquileres, ha sido leído correctamente.");
			leerDom(documento);
		} else {
			System.out.printf("No se puede leer el fichero de entrada: %s.%n", FICHERO_ALQUILERES);
		}
	}

	private void leerDom(Document documentoXml) {
		NodeList alquileres = documentoXml.getElementsByTagName(ALQUILER);
		for (int i = 0; i < alquileres.getLength(); i++) {
			Node alquiler = alquileres.item(i);
			if (alquiler.getNodeType() == Node.ELEMENT_NODE) {
				String cadenaError = String.format("ERROR: El error esta en el alquiler que ocupa la posición: %s%n",
						i);
				try {
					insertar(getAlquiler(((Element) alquiler)));
				} catch (DateTimeParseException e) {
					System.out.println("ERROR: no se ha podido poner la fecha.");
					System.out.print(cadenaError);
				} catch (NullPointerException | IllegalArgumentException | OperationNotSupportedException e) {
					System.out.println(e.getMessage());
					System.out.print(cadenaError);
				}

			}
		}
	}

	private Alquiler getAlquiler(Element elemento) throws OperationNotSupportedException {
		Cliente clienteEncontrado = Clientes.getInstancia()
				.buscar(Cliente.getClienteConDni(elemento.getAttribute(CLIENTE)));
		if (clienteEncontrado == null) {
			throw new NullPointerException("ERROR: no existe ningún cliente con dni.");
		}
		Vehiculo vehiculoEncontrado = Vehiculos.getInstancia()
				.buscar(Vehiculo.getVehiculoConMatricula(elemento.getAttribute(VEHICULO)));

		if (vehiculoEncontrado == null) {
			throw new NullPointerException("ERROR: no existe ningún vehiculo con esa matricula");
		}
		// fecha de alquiler lo pongo en una variable para que no me salga un chorizo
		LocalDate fechaAlquiler = LocalDate.parse(elemento.getAttribute(FECHA_ALQUILER), FORMATO_FECHA);
		Alquiler alquiler = new Alquiler(clienteEncontrado, vehiculoEncontrado, fechaAlquiler);
		if (elemento.hasAttribute(FECHA_DEVOLUCION)) {
			alquiler.devolver(LocalDate.parse(elemento.getAttribute(FECHA_DEVOLUCION), FORMATO_FECHA));
		}
		return alquiler;
	}

	@Override
	public void terminar() {
		UtilidadesXml.escribirXmlAFichero(crearDom(), FICHERO_ALQUILERES);

	}

	private Document crearDom() {
		DocumentBuilder constructor = UtilidadesXml.crearConstructorDocumentoXml();
		Document documentoXml = null;
		if (constructor != null) {
			documentoXml = constructor.newDocument();
			documentoXml.appendChild(documentoXml.createElement(RAIZ));
			for (Alquiler alquiler : coleccionAlquileres) {
				Element elementoAlquiler = getElemento(documentoXml, alquiler);
				documentoXml.getDocumentElement().appendChild(elementoAlquiler);
			}
		}
		return documentoXml;
	}

	private Element getElemento(Document documentoXml, Alquiler alquiler) {
		Element elementoAlquiler = documentoXml.createElement(ALQUILER);
		elementoAlquiler.setAttribute(CLIENTE, alquiler.getCliente().getDni());
		elementoAlquiler.setAttribute(FECHA_ALQUILER, alquiler.getFechaAlquiler().format(FORMATO_FECHA));
		LocalDate fechaDevolucion = alquiler.getFechaDevolucion();
		if (fechaDevolucion != null) {
			elementoAlquiler.setAttribute(FECHA_DEVOLUCION, fechaDevolucion.format(FORMATO_FECHA));
		}
		elementoAlquiler.setAttribute(VEHICULO, alquiler.getVehiculo().getMatricula());
		return elementoAlquiler;
	}

	@Override
	public List<Alquiler> get() {
		return new ArrayList<>(coleccionAlquileres);
	}

	@Override
	public List<Alquiler> get(Cliente cliente) {
		List<Alquiler> auxColeccion = new ArrayList<>();
		for (Alquiler coleccionAlquiler : coleccionAlquileres) {
			if (coleccionAlquiler.getCliente().equals(cliente)) {
				auxColeccion.add(coleccionAlquiler);
			}
		}
		return auxColeccion;
	}

	@Override
	public List<Alquiler> get(Vehiculo vehiculo) {
		List<Alquiler> auxColeccion = new ArrayList<>();
		for (Alquiler coleccionAlquiler : coleccionAlquileres) {
			if (coleccionAlquiler.getVehiculo().equals(vehiculo)) {
				auxColeccion.add(coleccionAlquiler);
			}
		}
		return auxColeccion;
	}

	@Override
	public void insertar(Alquiler alquiler) throws OperationNotSupportedException {
		if (alquiler == null) {
			throw new NullPointerException("ERROR: No se puede insertar un alquiler nulo.");
		}
		comprobarAlquiler(alquiler.getCliente(), alquiler.getVehiculo(), alquiler.getFechaAlquiler());
		coleccionAlquileres.add(alquiler);

	}

	private void comprobarAlquiler(Cliente cliente, Vehiculo vehiculo, LocalDate fechaAlquiler)
			throws OperationNotSupportedException {
		for (Alquiler coleccionAlquiler : coleccionAlquileres) {
			if (coleccionAlquiler.getFechaDevolucion() == null) {
				if (coleccionAlquiler.getCliente().equals(cliente)) {
					throw new OperationNotSupportedException("ERROR: El cliente tiene otro alquiler sin devolver.");
				}
				if (coleccionAlquiler.getVehiculo().equals(vehiculo)) {
					throw new OperationNotSupportedException("ERROR: El vehículo está actualmente alquilado.");
				}
			} else {
				if (coleccionAlquiler.getCliente().equals(cliente)
						&& !coleccionAlquiler.getFechaDevolucion().isBefore(fechaAlquiler)) {
					throw new OperationNotSupportedException("ERROR: El cliente tiene un alquiler posterior.");
				}
				if (coleccionAlquiler.getVehiculo().equals(vehiculo)
						&& !coleccionAlquiler.getFechaDevolucion().isBefore(fechaAlquiler)) {
					throw new OperationNotSupportedException("ERROR: El vehículo tiene un alquiler posterior.");
				}
			}
		}
	}

	@Override
	public void devolver(Cliente cliente, LocalDate fechaDevolucion) throws OperationNotSupportedException {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede devolver un alquiler de un cliente nulo.");
		}
		Alquiler alquiler = getAlquilerAbierto(cliente);
		if (alquiler == null) {
			throw new OperationNotSupportedException("ERROR: No existe ningún alquiler abierto para ese cliente.");
		} else {
			alquiler.devolver(fechaDevolucion);
		}
	}

	private Alquiler getAlquilerAbierto(Cliente cliente) {
		Alquiler auxAlquiler = null;
		Iterator<Alquiler> iterator = coleccionAlquileres.iterator();
		while (iterator.hasNext() && auxAlquiler == null) {
			Alquiler alquiler = iterator.next();
			if (alquiler.getCliente().equals(cliente) && alquiler.getFechaDevolucion() == null) {
				auxAlquiler = alquiler;
			}
		}
		return auxAlquiler;
	}

	@Override
	public void devolver(Vehiculo vehiculo, LocalDate fechaDevolucion) throws OperationNotSupportedException {
		if (vehiculo == null) {
			throw new NullPointerException("ERROR: No se puede devolver un alquiler de un vehículo nulo.");
		}
		Alquiler alquiler = getAlquilerAbierto(vehiculo);
		if (alquiler == null) {
			throw new OperationNotSupportedException("ERROR: No existe ningún alquiler abierto para ese vehículo.");
		} else {
			alquiler.devolver(fechaDevolucion);

		}

	}

	private Alquiler getAlquilerAbierto(Vehiculo vehiculo) {
		Alquiler auxAlquiler = null;
		Iterator<Alquiler> iterator = coleccionAlquileres.iterator();
		while (iterator.hasNext() && auxAlquiler == null) {
			Alquiler alquiler = iterator.next();
			if (alquiler.getVehiculo().equals(vehiculo) && alquiler.getFechaDevolucion() == null) {
				auxAlquiler = alquiler;
			}
		}
		return auxAlquiler;
	}

	@Override
	public Alquiler buscar(Alquiler alquiler) {
		if (alquiler == null) {
			throw new NullPointerException("ERROR: No se puede buscar un alquiler nulo.");
		}
		int indiceAlquiler = coleccionAlquileres.indexOf(alquiler);
		if (indiceAlquiler == -1) {
			alquiler = null;
		} else {
			alquiler = coleccionAlquileres.get(indiceAlquiler);
		}
		return alquiler;
	}

	@Override
	public void borrar(Alquiler alquiler) throws OperationNotSupportedException {
		if (alquiler == null) {
			throw new NullPointerException("ERROR: No se puede borrar un alquiler nulo.");
		}
		if (!coleccionAlquileres.contains(alquiler)) {
			throw new OperationNotSupportedException("ERROR: No existe ningún alquiler igual.");
		}
		coleccionAlquileres.remove(alquiler);
	}
}
