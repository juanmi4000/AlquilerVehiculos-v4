package org.iesalandalus.programacion.alquilervehiculos.vista.texto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Autobus;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Furgoneta;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.utilidades.Entrada;

public class Consola {

	private static final String PATRON_FECHA = "(dd/MM/yyyy): ";
	private static final String PATRON_MES = "(MM/yyyy): ";
	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private Consola() {

	}

	public static void mostrarCabecera(String mensaje) {
		StringBuilder cabecera = new StringBuilder();
		cabecera.append("-");
		System.out.println(mensaje);
		for (int i = 0; i < mensaje.length(); i++) {
			System.out.print(cabecera);
		}
		System.out.println();

	}

	public static void mostrarMenuAcciones() {
		mostrarCabecera("APLICACIÓN PARA ALQUILAR VEHÍCULOS");
		for (Accion opcion : Accion.values()) {
			System.out.println(opcion.toString());
		}
	}

	public static Accion elegirAccion() {
		Accion accionElegida = null;
		do {
			try {
				accionElegida = Accion.get(leerEntero("Elige una acción: "));
			} catch (Exception e) {
				System.out.println("ERROR: " + e.getMessage());
			}
		} while (accionElegida == null);
		return accionElegida;
	}

	private static String leerCadena(String mensaje) {
		System.out.print(mensaje);
		return Entrada.cadena();
	}

	private static int leerEntero(String mensaje) {
		System.out.print(mensaje);
		return Entrada.entero();
	}

	private static LocalDate leerFecha(String mensaje, String patron) {
		String fecha = leerCadena(mensaje + patron);
		LocalDate fechaConvertida = null;
		try {
			if (patron.equals(PATRON_FECHA)) {
				fechaConvertida = LocalDate.parse(fecha, FORMATO_FECHA);
			}
			if (patron.equals(PATRON_MES)) {
				fechaConvertida = LocalDate.parse("01/" + fecha, FORMATO_FECHA);
			}
		} catch (Exception e) {
			System.out.println("ERROR: no se ha conseguido poner una fecha.");
		}
		return fechaConvertida;
	}

	public static Cliente leerCliente() {
		return new Cliente(leerNombre(), leerCadena("Introduce el dni: "), leerTelefono());
	}

	public static Cliente leerClienteDni() {
		return Cliente.getClienteConDni(leerCadena("Introduce tu dni: "));
	}

	public static String leerNombre() {
		return leerCadena("Introduce el nombre:  ");
	}

	public static String leerTelefono() {
		return leerCadena("Introduce el teléfono: ");
	}

	public static Vehiculo leerVehiculo() {
		mostrarMenuTiposVehiculos();
		return leerVehiculo(elegirTipoVehiculo());
	}

	private static void mostrarMenuTiposVehiculos() {
		mostrarCabecera("OPCIONES DE VEHÍCULOS:");
		for (TipoVehiculo tipoVehiculo : TipoVehiculo.values()) {
			System.out.println(String.format("%s. %s", tipoVehiculo.ordinal(), tipoVehiculo.toString()));
		}
	}

	private static TipoVehiculo elegirTipoVehiculo() {
		TipoVehiculo tipoVehiculoElegido = null;
		do {
			try {
				tipoVehiculoElegido = TipoVehiculo.get(leerEntero("Elige un tipo de vehículo: "));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} while (tipoVehiculoElegido == null);
		Consola.mostrarCabecera("Has elegido " + tipoVehiculoElegido.toString());
		return tipoVehiculoElegido;
	}

	private static Vehiculo leerVehiculo(TipoVehiculo tipovehiculo) {
		Vehiculo vehiculoElegido = null;
		String marca = leerCadena("Introduce la marca: ");
		String modelo = leerCadena("Introduce el modelo: ");
		String matricula = leerCadena("Introduce la matrícula: ");
		switch (tipovehiculo) {
		case TURISMO:
			vehiculoElegido = new Turismo(marca, modelo, leerEntero("Introduce la cilindrada: "), matricula);
			break;
		case AUTOBUS:
			vehiculoElegido = new Autobus(marca, modelo, leerEntero("Introduce las plazas: "), matricula);
			break;

		case FURGONETA:
			vehiculoElegido = new Furgoneta(marca, modelo, leerEntero("Introduce el peso máximo autorizado (pma): "),
					leerEntero("Introduce las plazas: "), matricula);
			break;
		}
		return vehiculoElegido;
	}

	public static Vehiculo leerVehiculoMatricula() {
		return Vehiculo.getVehiculoConMatricula(leerCadena("Introduce la matricula: "));
	}

	public static Alquiler leerAlquiler() {
		return new Alquiler(leerClienteDni(), leerVehiculoMatricula(),
				leerFecha("Introduce la fecha de alquiler ", PATRON_FECHA));
	}

	public static LocalDate leerFechaDevolucion() {
		return leerFecha("Introduce la fecha de devolución ", PATRON_FECHA);
	}

	public static LocalDate leerMes() {
		return leerFecha("Introduce el mes ", PATRON_MES);
	}
}
