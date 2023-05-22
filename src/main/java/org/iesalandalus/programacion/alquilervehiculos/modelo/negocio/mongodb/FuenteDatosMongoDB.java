package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mongodb;

import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IAlquileres;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IClientes;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IFuenteDatos;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IVehiculos;

public class FuenteDatosMongoDB implements IFuenteDatos {
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
