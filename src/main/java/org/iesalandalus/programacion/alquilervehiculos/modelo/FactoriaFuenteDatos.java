package org.iesalandalus.programacion.alquilervehiculos.modelo;

import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IFuenteDatos;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros.FuenteDatosMemoria;

public enum FactoriaFuenteDatos {
	FICHEROS {
		@Override
		public IFuenteDatos crear() {
			return new FuenteDatosMemoria();
		}
	};

	abstract IFuenteDatos crear();
}
