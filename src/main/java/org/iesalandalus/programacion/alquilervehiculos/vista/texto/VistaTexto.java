package org.iesalandalus.programacion.alquilervehiculos.vista.texto;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Autobus;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Furgoneta;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.vista.Vista;

public class VistaTexto extends Vista {

	public VistaTexto() {
		super();
		Accion.setVista(this);
	}

	public void comenzar() throws OperationNotSupportedException {
		Accion accion;
		do {
			Consola.mostrarMenuAcciones();
			accion = Consola.elegirAccion();
			Consola.mostrarCabecera(accion.toString());
			accion.ejecutar();
		} while (accion != Accion.SALIR);
	}

	public void terminar() {
		getControlador().terminar();
		System.out.println("!!!!Hasta luego Lucas!!!!");
	}

	public void insertarCliente() {
		try {
			getControlador().insertar(Consola.leerCliente());
			System.out.println("El cliente se ha insertado correctamente.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void insertarVehiculo() {
		try {
			getControlador().insertar(Consola.leerVehiculo());
			System.out.println("El vehículo se ha insertado correctamente.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void insertarAlquiler() {
		try {
			getControlador().insertar(Consola.leerAlquiler());
			System.out.println("El alquiler se ha insertado correctamente.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void buscarCliente() {
		try {
			Cliente clienteEncontrado = getControlador().buscar(Consola.leerClienteDni());
			if (clienteEncontrado == null) {
				throw new OperationNotSupportedException("ERROR: no existe un cliente con ese dni.");
			} else {
				System.out.println(clienteEncontrado.toString());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

//	private void tipoVehiculoEncontrado (Vehiculo vehiculo) throws OperationNotSupportedException {
//		//en que me puedo basar para saber si se pone static o no/ lo mismo con final
//		 if (vehiculo instanceof Turismo) {
//			System.out.printf("Turismo --> %s%n", vehiculo.toString());
//		} else if (vehiculo instanceof Autobus) {
//			System.out.printf("Autobus --> %s%n", vehiculo.toString());
//		} else if (vehiculo instanceof Furgoneta) {
//			System.out.printf("Furgoneta --> %s%n", vehiculo.toString());
//		}
//	}

	public void buscarVehiculo() {
		try {
//			Vehiculo vehiculoEncontrado = getControlador().buscar(Consola.leerVehiculoMatricula());
//			if (vehiculoEncontrado == null) {
//				throw new OperationNotSupportedException("ERROR: no existe un vehiculo con esa matricula.");
//			} else {
//				tipoVehiculoEncontrado(vehiculoEncontrado);
//			}
			Vehiculo vehiculoEncontrado = getControlador().buscar(Consola.leerVehiculoMatricula());
			if (vehiculoEncontrado == null) {
				throw new OperationNotSupportedException("ERROR: no existe un vehiculo con esa matricula.");
			} else {
				if (vehiculoEncontrado instanceof Turismo) {
					System.out.printf("Turismo --> %s%n", vehiculoEncontrado.toString());
				} else if (vehiculoEncontrado instanceof Autobus) {
					System.out.printf("Autobus --> %s%n", vehiculoEncontrado.toString());
				} else if (vehiculoEncontrado instanceof Furgoneta) {
					System.out.printf("Furgoneta --> %s%n", vehiculoEncontrado.toString());
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void buscarAlquiler() {
		try {
			Alquiler alquilerEncontrado = getControlador().buscar(Consola.leerAlquiler());
			if (alquilerEncontrado == null) {
				throw new OperationNotSupportedException("Error: no existe un alquiler igual.");
			} else {
				System.out.println(alquilerEncontrado.toString());
			}
			System.out.println();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void modificarCliente() {
		try {
			getControlador().modificar(Consola.leerClienteDni(), Consola.leerNombre(), Consola.leerTelefono());
			System.out.println("El cliente se se ha modificado correctamente.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void devolverAlquilerCliente() {
		try {
			getControlador().devolver(Consola.leerClienteDni(), Consola.leerFechaDevolucion());
			System.out.println("Se ha devuelto correctamente el alquiler.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void devolverAlquilerVehiculo() {
		try {
			getControlador().devolver(Consola.leerVehiculoMatricula(), Consola.leerFechaDevolucion());
			System.out.println("Se ha devuelto correctamente el alquiler.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void borrarCliente() {
		try {
			getControlador().borrar(Consola.leerClienteDni());
			System.out.println("El cliente se ha borrado con éxito.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void borrarVehiculo() {
		try {
			getControlador().borrar(Consola.leerVehiculoMatricula());
			System.out.println("El vehículo se ha borrado con éxito.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void borrarAlquiler() {
		try {
			getControlador().borrar(Consola.leerAlquiler());
			System.out.println("El alquiler se ha borrado con éxito.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void listarClientes() {

		try {
			List<Cliente> listaClientesOrdenada = getControlador().getClientes();
			listaClientesOrdenada.sort(Comparator.comparing(Cliente::getNombre).thenComparing(Cliente::getDni));
			for (Cliente cliente : listaClientesOrdenada) {
				System.out.println(cliente.toString());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void listarVehiculos() {
		try {
			List<Vehiculo> ordenarVehiculos = getControlador().getVehiculos();
			ordenarVehiculos.sort(Comparator.comparing(Vehiculo::getMarca).thenComparing(Vehiculo::getModelo)
					.thenComparing(Vehiculo::getMatricula));
			for (Vehiculo vehiculo : ordenarVehiculos) {
				// tipoVehiculoEncontrado(vehiculo);
				if (vehiculo instanceof Turismo) {
					System.out.printf("Turismo --> %s%n", vehiculo.toString());
				} else if (vehiculo instanceof Autobus) {
					System.out.printf("Autobus --> %s%n", vehiculo.toString());
				} else if (vehiculo instanceof Furgoneta) {
					System.out.printf("Furgoneta --> %s%n", vehiculo.toString());
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void listarAlquileres() {
		try {
			Comparator<Cliente> ordenadoCliente = Comparator.comparing(Cliente::getNombre)
					.thenComparing(Cliente::getDni);
			List<Alquiler> ordenarAlquileres = getControlador().getAlquileres();
			ordenarAlquileres.sort(Comparator.comparing(Alquiler::getFechaAlquiler).thenComparing(Alquiler::getCliente,
					ordenadoCliente));
			for (Alquiler alquiler : ordenarAlquileres) {
				System.out.println(alquiler.toString());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void listarAlquileresClientes() {
		try {
			for (Alquiler alquilerCliente : getControlador().getAlquileres(Consola.leerClienteDni())) {
				System.out.println(alquilerCliente.toString());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void listarAlquileresVehiculos() {
		try {
			for (Alquiler alquilerVehiculo : getControlador().getAlquileres(Consola.leerVehiculoMatricula())) {
				System.out.println(alquilerVehiculo.toString());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void mostrarEstadisticasMensualesTipoVehiculo() {
		Map<TipoVehiculo, Integer> ordenar = iniciarEstadisticas();
		LocalDate mes = Consola.leerMes();
		for (Alquiler alquiler : getControlador().getAlquileres()) {
			if (alquiler.getFechaAlquiler().getMonthValue() == mes.getMonthValue()
					&& alquiler.getFechaAlquiler().getYear() == mes.getYear()) {
				ordenar.put(TipoVehiculo.get(alquiler.getVehiculo()),
						ordenar.getOrDefault(TipoVehiculo.get(alquiler.getVehiculo()), 0) + 1);
			}
		}
		System.out.println(ordenar);
	}

	private Map<TipoVehiculo, Integer> iniciarEstadisticas() {
		Map<TipoVehiculo, Integer> ordenar = new TreeMap<>();
		for (TipoVehiculo tipoVehiculo : TipoVehiculo.values()) {
			ordenar.put(tipoVehiculo, 0);
		}
		return ordenar;
	}
}
