package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mariadb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDB {
	
	private static final String HOST = "localhost";
	private static final String ESQUEMA = "alquilerVehiculos";
	private static final String USUARIO = "vehiculos";
	private static final String CONTRASENA = "vehiculos";
	
	private static Connection conexion = null;
	
	private MariaDB() {
		//Evitamos que se creen instancias
	}
	
	public static Connection getConexion() {
		if (conexion == null) {
			try {
				String urlConexion = String.format("jdbc:mariadb://%s/%s?user=%s&password=%s", HOST, ESQUEMA, USUARIO, CONTRASENA);
				conexion = DriverManager.getConnection(urlConexion);
				System.out.println("Conexión a MariaDB realizada correctamente.");
			} catch (SQLException e) {
				System.out.println("ERROR de conexión a MariaDB:  "+ e.toString());
				System.exit(-1);
			}
		}
		return conexion;
	}
	
	public static void cerrarConexion() {
		if (conexion != null) {
			try {
				conexion.close();
				conexion = null;
				System.out.println("Conexión a MariaDB cerrada correctamente.");
			} catch (SQLException e) {
				System.out.println("ERROR de MariaDB: "+ e.toString());
			}
		}
	}

}
