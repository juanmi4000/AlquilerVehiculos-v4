package org.iesalandalus.programacion.alquilervehiculos.vista.texto;

import javax.naming.OperationNotSupportedException;

public enum Accion {

	SALIR("Salir.") {
		@Override
		public void ejecutar() {
			vistaTexto.terminar();
		}
	},
	INSERTAR_CLIENTE("Insertar cliente.") {
		@Override
		public void ejecutar() {
			vistaTexto.insertarCliente();
		}
	},
	INSERTAR_VEHICULO("Insertar vehículo.") {
		@Override
		public void ejecutar() {
			vistaTexto.insertarVehiculo();

		}
	},
	INSERTAR_ALQUILER("Insertar alquiler.") {
		@Override
		public void ejecutar() {
			vistaTexto.insertarAlquiler();

		}
	},
	BUSCAR_CLIENTE("Buscar cliente.") {
		@Override
		public void ejecutar() {
			vistaTexto.buscarCliente();

		}
	},
	BUSCAR_VEHICULO("Buscar vehículo.") {
		@Override
		public void ejecutar() {
			vistaTexto.buscarVehiculo();

		}
	},
	BUSCAR_ALQUILER("Buscar alquiler.") {
		@Override
		public void ejecutar() {
			vistaTexto.buscarAlquiler();

		}
	},
	MODIFICAR_CLIENTE("Modificar cliente.") {
		@Override
		public void ejecutar() {
			vistaTexto.modificarCliente();

		}
	},
	DEVOLVER_ALQUILER_CLIENTE("Devolver alquiler por un cliente.") {
		@Override
		public void ejecutar() {
			vistaTexto.devolverAlquilerCliente();

		}
	},
	DEVOLVER_ALQUILER_VEHICULO("Devolver alquiler por un vehículo.") {
		@Override
		public void ejecutar() {
			vistaTexto.devolverAlquilerVehiculo();

		}
	},
	BORRAR_CLIENTE("Borrar cliente.") {
		@Override
		public void ejecutar() {
			vistaTexto.borrarCliente();

		}
	},
	BORRAR_VEHICULO("Borrar vehículo.") {
		@Override
		public void ejecutar() {
			vistaTexto.borrarVehiculo();

		}
	},
	BORRAR_ALQUILER("Borrar alquiler.") {
		@Override
		public void ejecutar() {
			vistaTexto.borrarAlquiler();

		}
	},
	LISTAR_CLIENTES("Listar clientes.") {
		@Override
		public void ejecutar() {
			vistaTexto.listarClientes();

		}
	},
	LISTAR_VEHICULOS("Listar vehiculos.") {
		@Override
		public void ejecutar() {
			vistaTexto.listarVehiculos();

		}
	},
	LISTAR_ALQUILERES("Listar alquileres.") {
		@Override
		public void ejecutar() {
			vistaTexto.listarAlquileres();

		}
	},
	LISTAR_ALQUILERES_CLIENTE("Listar alquileres por cliente.") {
		@Override
		public void ejecutar() {
			vistaTexto.listarAlquileresClientes();

		}
	},
	LISTAR_ALQUILERES_VEHICULO("Listar alquileres por vehiculo.") {
		@Override
		public void ejecutar() {
			vistaTexto.listarAlquileresVehiculos();

		}
	},
	MOSTRAR_ESTADISTICAS_MENSUALES("Mostrar estadísticas mensuales.") {
		@Override
		public void ejecutar() {
			vistaTexto.mostrarEstadisticasMensualesTipoVehiculo();

		}
	};

	private static VistaTexto vistaTexto;

	private String texto;

	private Accion(String texto) {
		this.texto = texto;
	}

	static void setVista(VistaTexto vista) {
		Accion.vistaTexto = vista;

	}

	public abstract void ejecutar() throws OperationNotSupportedException;

	private static boolean esOrdinalValido(int ordinal) {

		return ordinal >= 0 && ordinal < Accion.values().length;

	}

	public static Accion get(int ordinal) {
		if (!esOrdinalValido(ordinal)) {
			throw new IllegalArgumentException("Error: la opción no es válida.");
		}
		return Accion.values()[ordinal];
	}

	@Override
	public String toString() {
		return String.format("%s. %s", ordinal(), texto);
	}
}
