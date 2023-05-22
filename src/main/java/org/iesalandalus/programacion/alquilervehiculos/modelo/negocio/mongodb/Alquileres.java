package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mongodb;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Updates.set;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IAlquileres;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class Alquileres implements IAlquileres{
	
	private static final String COLECCCION = "alquileres";
	private static final String CLIENTE = "cliente";
	private static final String VEHICULO = "vehiculo";
	private static final String FECHA_ALQUILER = "fechaAlquiler";
	private static final String FECHA_DEVOLUCION = "fechaDevolucion";
	private static final String DNI_CLIENTE = "cliente.dni";
	private static final String MATRICULA_VEHICULO = "vehiculo.matricula";


	private MongoCollection<Document> coleccionAlquileres;
	private static final Alquileres instancia = new Alquileres();
	
	private Alquileres() {
	}
	
	static Alquileres getInstancia() {
		return instancia;
	}
	
	@Override
	public void comenzar() {
		coleccionAlquileres = MongoDB.getBD().getCollection(COLECCCION);
	}

	@Override
	public void terminar() {
		MongoDB.cerrarConexion();
	}
	
	private LocalDate toLocalDate(Date fecha) {
		return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	private Date toDate(LocalDate fecha) {
		return Date.from(fecha.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	private Alquiler getAlquiler(Document documento) {
		Alquiler alquiler = null;
		if (documento != null) {
			Cliente cliente = Clientes.getInstancia().getCliente((Document) documento.get(CLIENTE));
			Vehiculo vehiculo = Vehiculos.getInstancia().getVehiculo((Document) documento.get(VEHICULO));
			LocalDate fechaAlquiler = toLocalDate(documento.getDate(FECHA_ALQUILER));
			alquiler = new Alquiler(cliente, vehiculo, fechaAlquiler);
			if (documento.containsKey(FECHA_DEVOLUCION)) {
				LocalDate fechaDevolcuion = toLocalDate(documento.getDate(FECHA_DEVOLUCION));
				try {
					alquiler.devolver(fechaDevolcuion);
				} catch (OperationNotSupportedException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return alquiler;
	}
	
	private Document getDocumento(Alquiler alquiler) {
		Document documento = null;
		if (alquiler != null) {
			Document documentoCliente = Clientes.getInstancia().getDocumento(alquiler.getCliente());
			Document documentoVehiculo = Vehiculos.getInstancia().getDocumento(alquiler.getVehiculo());
			Date fechaAlquiler = toDate(alquiler.getFechaAlquiler());
			documento = new Document().append(CLIENTE, documentoCliente).append(VEHICULO, documentoVehiculo).append(FECHA_ALQUILER, fechaAlquiler);
			if (alquiler.getFechaDevolucion() != null) {
				documento.append(FECHA_DEVOLUCION, toDate(alquiler.getFechaDevolucion()));
			}
		}
		return documento;
	}
	
	private Bson getCriterioBusqueda(Alquiler alquiler) {
		Date fechaAlquiler = toDate(alquiler.getFechaAlquiler());
		return and(eq(DNI_CLIENTE, alquiler.getCliente().getDni()),
				   eq(MATRICULA_VEHICULO, alquiler.getVehiculo().getMatricula()),
				   eq(FECHA_ALQUILER, fechaAlquiler));
	}
	
	private Bson getCriterioBusqueda(Cliente cliente) {
		return eq(DNI_CLIENTE, cliente.getDni());
	}
	
	private Bson getCriterioBusqueda(Vehiculo vehiculo) {
		return eq(MATRICULA_VEHICULO, vehiculo.getMatricula());
	}

	@Override
	public List<Alquiler> get() {
		List<Alquiler> alquileres = new ArrayList<>();
		for (Document documento : coleccionAlquileres.find()) {
			alquileres.add(getAlquiler(documento));
		}
		return alquileres;
	}

	@Override
	public List<Alquiler> get(Cliente cliente) {
		List<Alquiler> alquileres = new ArrayList<>();
		for (Document documento : coleccionAlquileres.find(getCriterioBusqueda(cliente))) {
			alquileres.add(getAlquiler(documento));
		}
		return alquileres;
	}

	@Override
	public List<Alquiler> get(Vehiculo vehiculo) {
		List<Alquiler> alquileres = new ArrayList<>();
		for (Document documento : coleccionAlquileres.find(getCriterioBusqueda(vehiculo))) {
			alquileres.add(getAlquiler(documento));
		}
		return alquileres;
	}

	@Override
	public void insertar(Alquiler alquiler) throws OperationNotSupportedException {
		if (alquiler == null) {
			throw new NullPointerException("ERROR: No se puede insertar un alquiler nulo.");
		}
		comprobarAlquiler(alquiler.getCliente(), alquiler.getVehiculo(), alquiler.getFechaAlquiler());
		coleccionAlquileres.insertOne(getDocumento(alquiler));
	}
	
	private void comprobarAlquiler(Cliente cliente, Vehiculo vehiculo, LocalDate fechaAlquiler) throws OperationNotSupportedException {
		Bson filtro = and(getCriterioBusqueda(cliente), exists(FECHA_DEVOLUCION, false));
		if (coleccionAlquileres.find(filtro).first() != null) {
			throw new OperationNotSupportedException("ERROR: El cliente tiene otro alquiler sin devolver.");
		}
		filtro = and(getCriterioBusqueda(vehiculo), exists(FECHA_DEVOLUCION, false));
		if (coleccionAlquileres.find(filtro).first() != null) {
			throw new OperationNotSupportedException("ERROR: El vehículo está actualmente alquilado.");
		}
		filtro = and(getCriterioBusqueda(cliente), exists(FECHA_DEVOLUCION, true), gte(FECHA_DEVOLUCION, toDate(fechaAlquiler)));
		if (coleccionAlquileres.find(filtro).first() != null) {
			throw new OperationNotSupportedException("ERROR: El cliente tiene un alquiler posterior.");
		}
		filtro = and(getCriterioBusqueda(vehiculo), exists(FECHA_DEVOLUCION, true), gte(FECHA_DEVOLUCION, toDate(fechaAlquiler)));
		if (coleccionAlquileres.find(filtro).first() != null) {
			throw new OperationNotSupportedException("ERROR: El vehículo tiene un alquiler posterior.");
		}
	}
	
	@Override
	public void devolver(Cliente cliente, LocalDate fechaDevolucion) throws OperationNotSupportedException {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede devolver un alquiler de un cliente nulo.");
		}
		Bson filtro = and(getCriterioBusqueda(cliente), exists(FECHA_DEVOLUCION, false));
		UpdateResult resultado = coleccionAlquileres.updateOne(filtro, set(FECHA_DEVOLUCION, toDate(fechaDevolucion)));
		if (resultado.getMatchedCount() == 0) {
			throw new OperationNotSupportedException("ERROR: No existe ningún alquiler abierto para ese cliente.");
		}
	}
	
	@Override
	public void devolver(Vehiculo vehiculo, LocalDate fechaDevolucion) throws OperationNotSupportedException {
		if (vehiculo == null) {
			throw new NullPointerException("ERROR: No se puede devolver un alquiler de un vehículo nulo.");
		}
		Bson filtro = and(getCriterioBusqueda(vehiculo), exists(FECHA_DEVOLUCION, false));
		UpdateResult resultado = coleccionAlquileres.updateOne(filtro, set(FECHA_DEVOLUCION, toDate(fechaDevolucion)));
		if (resultado.getMatchedCount() == 0) {
			throw new OperationNotSupportedException("ERROR: No existe ningún alquiler abierto para ese vehículo.");
		}
	}

	@Override
	public Alquiler buscar(Alquiler alquiler) {
		if (alquiler == null) {
			throw new NullPointerException("ERROR: No se puede buscar un alquiler nulo.");
		}
		return getAlquiler(coleccionAlquileres.find(getCriterioBusqueda(alquiler)).first());
	}

	@Override
	public void borrar(Alquiler alquiler) throws OperationNotSupportedException {
		if (alquiler == null) {
			throw new NullPointerException("ERROR: No se puede borrar un alquiler nulo.");
		}
		DeleteResult resultado = coleccionAlquileres.deleteOne(getCriterioBusqueda(alquiler));
		if (resultado.getDeletedCount() == 0) {
			throw new OperationNotSupportedException("ERROR: No existe ningún alquiler igual.");
		}
	}

	void modificarCliente(Document cliente) {
		Bson filtro = eq(DNI_CLIENTE, cliente.get("dni"));
		Bson modificacion = set(CLIENTE, cliente);
		coleccionAlquileres.updateMany(filtro, modificacion);
	}

}
