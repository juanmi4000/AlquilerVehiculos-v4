package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mariadb;

import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IAlquileres;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IClientes;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IFuenteDatos;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IVehiculos;

public class FuenteDatosMariaDB implements IFuenteDatos {
	@Override
	public IClientes crearClientes() {
		return Clientes.getInstancia();
	}

	@Override
	public IVehiculos crearVehiculos() {
		return Vehiculos.getInstancia();
	}

	@Override
	public IAlquileres crearAlquileres() {
		return Alquileres.getInstancia();
	}
}
