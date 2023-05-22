package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mariadb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IClientes;

public class Clientes implements IClientes {
	
	private static final String NOMBRE = "nombre";
	private static final String DNI = "dni";
	private static final String TELEFONO = "telefono";
	private static final String ERROR = "ERROR: ";
	
	private Connection conexion;
	private static final Clientes instancia = new Clientes();
	
	private Clientes() {
	}
	
	static Clientes getInstancia() {
		return instancia;
	}
	
	@Override
	public void comenzar() {
		conexion = MariaDB.getConexion();
	}

	@Override
	public void terminar() {
		MariaDB.cerrarConexion();
	}
	
	private Cliente getCliente(ResultSet fila) throws SQLException {
		String nombre = fila.getString(NOMBRE);
		String dni = fila.getString(DNI);
		String telefono = fila.getString(TELEFONO);
		return new Cliente(nombre, dni, telefono);
	}
	
	private void prepararSentencia(PreparedStatement sentencia, Cliente cliente) throws SQLException {
		sentencia.setString(1, cliente.getNombre());
		sentencia.setString(2, cliente.getDni());
		sentencia.setString(3, cliente.getTelefono());
	}

	@Override
	public List<Cliente> get() {
		List<Cliente> clientes = new ArrayList<>();
		try (Statement sentencia = conexion.createStatement()) {
			ResultSet filas = sentencia.executeQuery("select * from clientes");
			while (filas.next()) {
				clientes.add(getCliente(filas));
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return clientes;
	}

	@Override
	public void insertar(Cliente cliente) throws OperationNotSupportedException {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede insertar un cliente nulo.");
		}
		try (PreparedStatement sentencia = conexion.prepareStatement("insert into clientes values (?, ?, ?)")) {
			prepararSentencia(sentencia, cliente);
			sentencia.execute();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new OperationNotSupportedException("ERROR: Ya existe un cliente con ese DNI.");
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
	}

	@Override
	public void modificar(Cliente cliente, String nombre, String telefono) throws OperationNotSupportedException {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede insertar un cliente nulo.");
		}
		try (PreparedStatement sentencia = conexion.prepareStatement("select * from clientes where dni = ?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
			sentencia.setString(1, cliente.getDni());
			ResultSet filas = sentencia.executeQuery();
			if (filas.first()) { // si la primera fila existe sino 
				if (nombre != null && !nombre.isBlank()) { 
					filas.updateString(NOMBRE, nombre);// actualiza el nombre
				}
				if (telefono != null && !telefono.isBlank()) {
					filas.updateString(TELEFONO, telefono);
				}
				filas.updateRow();// actualize la fila
			} else {
				throw new OperationNotSupportedException("ERROR: No existe ningún cliente con ese DNI.");
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
	}

	@Override
	public Cliente buscar(Cliente cliente) {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede buscar un cliente nulo.");
		}
		try (PreparedStatement sentencia = conexion.prepareStatement("select * from clientes where dni = ? ")) {
			sentencia.setString(1, cliente.getDni());
			ResultSet filas = sentencia.executeQuery(); // ejecuta la consulta
			cliente = filas.first() ? getCliente(filas) : null;
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return cliente;
	}

	@Override
	public void borrar(Cliente cliente) throws OperationNotSupportedException {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede borrar un cliente nulo.");
		}
		try (PreparedStatement sentencia = conexion.prepareStatement("delete from clientes where dni = ?")) {
			sentencia.setString(1, cliente.getDni());
			int filas = sentencia.executeUpdate(); // elimina la filas
			if (filas == 0) {
				throw new OperationNotSupportedException("ERROR: No existe ningún cliente con ese DNI.");
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
	}

}


/* Los vehiculos 
 * 
 * hacer un string para todos los atributos de un vehicullo
 * getVehiuclo el if lo hago con el tipo
 * 
 * 
 * 
 * 
 * 
*/