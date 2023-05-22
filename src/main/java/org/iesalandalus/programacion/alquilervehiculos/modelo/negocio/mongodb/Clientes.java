package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mongodb;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IClientes;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.result.DeleteResult;

public class Clientes implements IClientes {
	
	private static final String COLECCION = "clientes";
	private static final String NOMBRE = "nombre";
	private static final String DNI = "dni";
	private static final String TELEFONO = "telefono";
	
	private MongoCollection<Document> coleccionClientes;
	private static final Clientes instancia = new Clientes();
	
	private Clientes() {
	}
	
	static Clientes getInstancia() {
		return instancia;
	}
	
	@Override
	public void comenzar() {
		coleccionClientes = MongoDB.getBD().getCollection(COLECCION);
	}

	@Override
	public void terminar() {
		MongoDB.cerrarConexion();
	}
	
	Cliente getCliente(Document documento) {
		Cliente cliente = null;
		if (documento != null) {
			cliente = new Cliente(documento.getString(NOMBRE), documento.getString(DNI), documento.getString(TELEFONO));
		}
		return cliente;
	}
	
	Document getDocumento(Cliente cliente) {
		Document documento = null;
		if (cliente != null) {
			String nombre = cliente.getNombre();
			String dni = cliente.getDni();
			String telefono = cliente.getTelefono();
			documento = new Document().append(NOMBRE, nombre).append(DNI, dni).append(TELEFONO, telefono);
		}	
		return documento;
	}
	
	private Bson getCriterioBusqueda(Cliente cliente) {
		return eq(DNI, cliente.getDni());
	}

	@Override
	public List<Cliente> get() {
		List<Cliente> clientes = new ArrayList<>();
		for (Document documento : coleccionClientes.find()) {
			clientes.add(getCliente(documento));
		}
		return clientes;
	}

	@Override
	public void insertar(Cliente cliente) throws OperationNotSupportedException {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede insertar un cliente nulo.");
		}
		FindOneAndReplaceOptions opciones = new FindOneAndReplaceOptions().upsert(true);
		Document resultado = coleccionClientes.findOneAndReplace(getCriterioBusqueda(cliente), getDocumento(cliente), opciones);
		if (resultado != null) {
			throw new OperationNotSupportedException("ERROR: Ya existe un cliente con ese DNI.");
		}
	}

	@Override
	public void modificar(Cliente cliente, String nombre, String telefono) throws OperationNotSupportedException {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede modificar un cliente nulo.");
		}
		Bson modificacion = null;
		if (nombre != null && !nombre.isBlank()) {
			modificacion = set(NOMBRE, nombre);
		}
		if (telefono != null && !telefono.isBlank()) {
			modificacion = (modificacion == null) ? set(TELEFONO, telefono) : combine(modificacion, set(TELEFONO, telefono));
		}
		if (modificacion != null) {
			Document clienteModificado = coleccionClientes.findOneAndUpdate(getCriterioBusqueda(cliente), modificacion, new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));
			if (clienteModificado == null) {
				throw new OperationNotSupportedException("ERROR: No existe ningún cliente con ese DNI.");
			} else {
				Alquileres.getInstancia().modificarCliente(clienteModificado);
			}
		}
	}

	@Override
	public Cliente buscar(Cliente cliente) {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede buscar un cliente nulo.");
		}
		return getCliente(coleccionClientes.find(getCriterioBusqueda(cliente)).first());

	}

	@Override
	public void borrar(Cliente cliente) throws OperationNotSupportedException {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede borrar un cliente nulo.");
		}
		DeleteResult resultado = coleccionClientes.deleteOne(getCriterioBusqueda(cliente));
		if (resultado.getDeletedCount() == 0) {
			throw new OperationNotSupportedException("ERROR: No existe ningún cliente con ese DNI.");
		}
	}

}
