package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mariadb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IAlquileres;

public class Alquileres implements IAlquileres{
	
	private static final String CLIENTE = "cliente";
	private static final String VEHICULO = "vehiculo";
	private static final String FECHA_ALQUILER = "fechaAlquiler";
	private static final String FECHA_DEVOLUCION = "fechaDevolucion";
	private static final String ERROR = "ERROR: ";

	private Connection conexion;
	private static final Alquileres instancia = new Alquileres();
	
	private Alquileres() {
	}
	
	static Alquileres getInstancia() {
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
	
	private Alquiler getAlquiler(ResultSet fila) throws SQLException, OperationNotSupportedException {
		Cliente cliente = Clientes.getInstancia().buscar(Cliente.getClienteConDni(fila.getString(CLIENTE)));
		Vehiculo vehiculo = Vehiculos.getInstancia().buscar(Vehiculo.getVehiculoConMatricula(fila.getString(VEHICULO)));
		LocalDate fechaAqluiler = fila.getDate(FECHA_ALQUILER).toLocalDate();
		Alquiler alquiler = new Alquiler(cliente, vehiculo, fechaAqluiler);
		LocalDate fechaDevolucion = (fila.getDate(FECHA_DEVOLUCION) == null) ? null : fila.getDate(FECHA_DEVOLUCION).toLocalDate();
		if (fechaDevolucion != null) {
			alquiler.devolver(fechaDevolucion);
		}
		return alquiler;
	}
	
	private void prepararSentencia(PreparedStatement sentencia, Alquiler alquiler) throws SQLException {
		sentencia.setString(1, alquiler.getCliente().getDni());
		sentencia.setString(2, alquiler.getVehiculo().getMatricula());
		sentencia.setDate(3, Date.valueOf(alquiler.getFechaAlquiler()));
		sentencia.setNull(4, Types.DATE);
	}

	@Override
	public List<Alquiler> get() {
		List<Alquiler> alquileres = new ArrayList<>();
		try (Statement sentencia = conexion.createStatement()) {
			ResultSet filas = sentencia.executeQuery("select * from alquileres");
			while (filas.next()) {
				alquileres.add(getAlquiler(filas));
			}
		} catch (SQLException | OperationNotSupportedException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return alquileres;
	}

	@Override
	public List<Alquiler> get(Cliente cliente) {
		List<Alquiler> alquileres = new ArrayList<>();
		try (PreparedStatement sentencia = conexion.prepareStatement("select * from alquileres where cliente = ?")) {
			sentencia.setString(1, cliente.getDni());
			ResultSet filas = sentencia.executeQuery();
			while (filas.next()) {
				alquileres.add(getAlquiler(filas));
			}
		} catch (SQLException | OperationNotSupportedException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return alquileres;
	}

	@Override
	public List<Alquiler> get(Vehiculo vehiculo) {
		List<Alquiler> alquileres = new ArrayList<>();
		try (PreparedStatement sentencia = conexion.prepareStatement("select * from alquileres where vehiculo = ?")) {
			sentencia.setString(1, vehiculo.getMatricula());
			ResultSet filas = sentencia.executeQuery();
			while (filas.next()) {
				alquileres.add(getAlquiler(filas));
			}
		} catch (SQLException | OperationNotSupportedException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return alquileres;
	}

	@Override
	public void insertar(Alquiler alquiler) throws OperationNotSupportedException {
		if (alquiler == null) {
			throw new NullPointerException("ERROR: No se puede insertar un alquiler nulo.");
		}
		comprobarAlquiler(alquiler.getCliente(), alquiler.getVehiculo(), alquiler.getFechaAlquiler());
		try (PreparedStatement sentencia = conexion.prepareStatement("insert into alquileres values (?, ?, ?, ?)")) {
			prepararSentencia(sentencia, alquiler);
			sentencia.execute();
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
	}
	
	private void comprobarAlquiler(Cliente cliente, Vehiculo vehiculo, LocalDate fechaAlquiler) throws OperationNotSupportedException {
		try (PreparedStatement sentencia = conexion.prepareStatement("select count(*) from alquileres where cliente = ? and fechaDevolucion is null")) {
			sentencia.setString(1, cliente.getDni());
			ResultSet filas = sentencia.executeQuery();
			filas.first();
			if (filas.getInt(1) == 1) {
				throw new OperationNotSupportedException("ERROR: El cliente tiene otro alquiler sin devolver.");
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		try (PreparedStatement sentencia = conexion.prepareStatement("select count(*) from alquileres where vehiculo = ? and fechaDevolucion is null")) {
			sentencia.setString(1, vehiculo.getMatricula());
			ResultSet filas = sentencia.executeQuery();
			filas.first();
			if (filas.getInt(1) == 1) {
				throw new OperationNotSupportedException("ERROR: El vehículo está actualmente alquilado.");
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		try (PreparedStatement sentencia = conexion.prepareStatement("select count(*) from alquileres where cliente = ? and fechaDevolucion >= ?")) {
			sentencia.setString(1, cliente.getDni());
			sentencia.setDate(2, Date.valueOf(fechaAlquiler));
			ResultSet filas = sentencia.executeQuery();
			filas.first();
			if (filas.getInt(1) == 1) {
				throw new OperationNotSupportedException("ERROR: El cliente tiene un alquiler posterior.");
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		try (PreparedStatement sentencia = conexion.prepareStatement("select count(*) from alquileres where vehiculo = ? and fechaDevolucion >= ?")) {
			sentencia.setString(1, vehiculo.getMatricula());
			sentencia.setDate(2, Date.valueOf(fechaAlquiler));
			ResultSet filas = sentencia.executeQuery();
			filas.first();
			if (filas.getInt(1) == 1) {
				throw new OperationNotSupportedException("ERROR: El vehículo tiene un alquiler posterior.");
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
	}

	@Override
	public void devolver(Cliente cliente, LocalDate fechaDevolucion) throws OperationNotSupportedException {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede devolver un alquiler de un cliente nulo.");
		}
		try (PreparedStatement sentencia = conexion.prepareStatement("select * from alquileres where cliente = ? and fechaDevolucion is null", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
			sentencia.setString(1, cliente.getDni());
			ResultSet filas = sentencia.executeQuery();
			if (filas.first()) {
				filas.updateDate(FECHA_DEVOLUCION, Date.valueOf(fechaDevolucion));
				filas.updateRow();
			} else {
				throw new OperationNotSupportedException("ERROR: No existe ningún alquiler abierto para ese cliente.");
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
	}

	@Override
	public void devolver(Vehiculo vehiculo, LocalDate fechaDevolucion) throws OperationNotSupportedException {
		if (vehiculo == null) {
			throw new NullPointerException("ERROR: No se puede devolver un alquiler de un vehículo nulo.");
		}
		try (PreparedStatement sentencia = conexion.prepareStatement("select * from alquileres where vehiculo = ? and fechaDevolucion is null", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
			sentencia.setString(1, vehiculo.getMatricula());
			ResultSet filas = sentencia.executeQuery();
			if (filas.first()) {
				filas.updateDate(FECHA_DEVOLUCION, Date.valueOf(fechaDevolucion));
				filas.updateRow();
			} else {
				throw new OperationNotSupportedException("ERROR: No existe ningún alquiler abierto para ese vehículo.");
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
	}

	@Override
	public Alquiler buscar(Alquiler alquiler) {
		if (alquiler == null) {
			throw new NullPointerException("ERROR: No se puede buscar un alquiler nulo.");
		}
		try (PreparedStatement sentencia = conexion.prepareStatement("select * from alquileres where cliente = ? and vehiculo = ? and fechaAlquiler = ?")) {
			sentencia.setString(1, alquiler.getCliente().getDni());
			sentencia.setString(2, alquiler.getVehiculo().getMatricula());
			sentencia.setDate(3,  Date.valueOf(alquiler.getFechaAlquiler()));
			ResultSet filas = sentencia.executeQuery();
			alquiler = filas.first() ? getAlquiler(filas) : null;
		} catch (SQLException | OperationNotSupportedException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return alquiler;
	}

	@Override
	public void borrar(Alquiler alquiler) throws OperationNotSupportedException {
		if (alquiler == null) {
			throw new NullPointerException("ERROR: No se puede borrar un alquiler nulo.");
		}
		try (PreparedStatement sentencia = conexion.prepareStatement("delete from alquileres where cliente = ? and vehiculo = ? and fechaAlquiler = ?")) {
			sentencia.setString(1, alquiler.getCliente().getDni());
			sentencia.setString(2, alquiler.getVehiculo().getMatricula());
			sentencia.setDate(3,  Date.valueOf(alquiler.getFechaAlquiler()));
			int filas = sentencia.executeUpdate();
			if (filas == 0) {
				throw new OperationNotSupportedException("ERROR: No existe ningún alquiler igual.");
			}
		} catch (SQLException | OperationNotSupportedException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
	}
	
}
