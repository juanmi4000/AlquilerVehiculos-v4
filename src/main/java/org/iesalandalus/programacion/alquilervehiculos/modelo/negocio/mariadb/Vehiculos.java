package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mariadb;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Autobus;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Furgoneta;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IVehiculos;

public class Vehiculos implements IVehiculos{
	
	private static final String AUTOBUS = "autobus";

	private static final String PMA = "pma";

	private static final String PLAZAS = "plazas";

	private static final String FURGONETA = "furgoneta";

	private static final String CILINDRADA = "cilindrada";

	private static final String TURISMO = "turismo";

	private static final String TIPO = "tipo";

	private static final String MATRICULA = "matricula";

	private static final String MODELO = "modelo";

	private static final String MARCA = "marca";

	private static final String ERROR = "ERROR";
	
	private Connection conexion;
	private static final Vehiculos instancia = new Vehiculos();
	
	
	private Vehiculos() {
		// lo pongo privado para que no se puedan hacer instancias
	}
	
	static Vehiculos getInstancia() {
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
	
	private Vehiculo getVehiculo(ResultSet fila) throws SQLException{
		Vehiculo vehiculo = null;
		String marca = fila.getString(MARCA);
		String modelo = fila.getString(MODELO);
		String matricula = fila.getString(MATRICULA);
		String tipo = fila.getString(TIPO);
		if (tipo.equalsIgnoreCase(TURISMO)) {
			int cilindrada = fila.getInt(CILINDRADA);
			vehiculo = new Turismo(marca, modelo, cilindrada, matricula);
		} else if (tipo.equalsIgnoreCase(FURGONETA)){
			int plazas = fila.getInt(PLAZAS);
			int pma = fila.getInt(PMA);
			vehiculo = new Furgoneta(marca, modelo, pma, plazas, matricula);
		} else {
			int plazas = fila.getInt(PLAZAS);
			vehiculo = new Autobus(marca, modelo, plazas, matricula);
		}
		return vehiculo;
	}
	
	private void prepararSentencia(PreparedStatement sentencia, Vehiculo vehiculo) throws SQLException {
		sentencia.setString(1, vehiculo.getMarca());
		sentencia.setString(2, vehiculo.getModelo());
		sentencia.setString(3, vehiculo.getMatricula());
		if (vehiculo instanceof Turismo turismo) {
			sentencia.setString(4, TURISMO);
			sentencia.setInt(5, turismo.getCilindrada());
			sentencia.setNull(6,Types.INTEGER);
			sentencia.setNull(7, Types.INTEGER);
		}else if (vehiculo instanceof Furgoneta furgoneta) {
			sentencia.setString(4, FURGONETA);
			sentencia.setNull(5, Types.INTEGER);
			sentencia.setInt(6, furgoneta.getPlazas());
			sentencia.setInt(7, furgoneta.getPma());
		} else if (vehiculo instanceof Autobus autobus) {
			sentencia.setString(4, AUTOBUS);
			sentencia.setNull(5, Types.INTEGER);
			sentencia.setInt(6, autobus.getPlazas());
			sentencia.setNull(7, Types.INTEGER);
		}
	}

	@Override
	public List<Vehiculo> get() {
		List<Vehiculo> vehiculos = new ArrayList<>();
		try (Statement sentencia = conexion.createStatement()) {
			ResultSet filas = sentencia.executeQuery("select * from vehiculos");
			while (filas.next()) {
				vehiculos.add(getVehiculo(filas));
				
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return vehiculos;
	}

	@Override
	public void insertar(Vehiculo vehiculo) throws OperationNotSupportedException {
		if (vehiculo == null) {
			throw new NullPointerException("ERROR: No se puede insertar un cliente nulo.");
		}
		try (PreparedStatement sentencia = conexion.prepareStatement("insert into vehiculos values (?, ?, ?, ?, ?, ?, ?)")){
			prepararSentencia(sentencia, vehiculo);
			sentencia.execute();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new OperationNotSupportedException("ERROR: Ya existe un vehículo con esa matrícula.");
		}catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
	}

	@Override
	public Vehiculo buscar(Vehiculo vehiculo) {
		if (vehiculo == null) {
			throw new NullPointerException("ERROR: No se puede buscar un vehículo nulo.");
		}
		try (PreparedStatement sentencia = conexion.prepareStatement("select * from vehiculos where matricula = ? ")){
			sentencia.setString(1, vehiculo.getMatricula());
			ResultSet filas = sentencia.executeQuery();
			vehiculo = filas.first() ? getVehiculo(filas) : null;
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return vehiculo;
	}

	@Override
	public void borrar(Vehiculo vehiculo) throws OperationNotSupportedException {
		if (vehiculo == null) {
			throw new NullPointerException("ERROR: No se puede borrar un vehículo nulo.");
		}
		try (PreparedStatement sentencia = conexion.prepareStatement("delete from vehiculos where matricula = ?")){
			sentencia.setString(1, vehiculo.getMatricula());
			int filas = sentencia.executeUpdate();
			if (filas == 0) {
				throw new OperationNotSupportedException("ERROR: No existe ningún vehículo con esa matrícula.");
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		
	}

}
