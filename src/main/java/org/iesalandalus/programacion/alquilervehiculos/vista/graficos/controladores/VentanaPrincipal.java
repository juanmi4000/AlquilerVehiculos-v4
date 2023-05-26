package org.iesalandalus.programacion.alquilervehiculos.vista.graficos.controladores;

import java.awt.Desktop;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.VistaGraficos;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controlador;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controladores;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Dialogos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class VentanaPrincipal extends Controlador {

	private static final String ERROR = "ERROR";

	@FXML
	private MenuItem confirmarSalida;

	@FXML
	void leerCliente(ActionEvent event) {
		LeerCliente leerCliente = (LeerCliente) Controladores.get("vistas/LeerCliente.fxml", "Leer cliente",
				getEscenario());
		leerCliente.limpiar();
		leerCliente.getEscenario().showAndWait();
		try {
			Cliente cliente = leerCliente.getCliente();
			if (cliente != null) {
				VistaGraficos.getInstancia().getControlador().insertar(cliente);
				Dialogos.mostrarDialogoAdvertencia("Insertar cliente", "El cliente se ha insertado correctamente",
						getEscenario());
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ERROR, e.getMessage(), getEscenario());
		}
	}

	@FXML
	void gitHub(ActionEvent event) {
		String link = "https://github.com/juanmi4000/AlquilerVehiculos-v4.git";
		try {
			Desktop deskpot = Desktop.getDesktop();
			deskpot.browse(java.net.URI.create(link));
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Error al abrir la URL", "Ha ocurrido un error inesperado al abrir la página.",
					getEscenario());
			e.printStackTrace();
		}
	}

	@FXML
	void listarClientes(ActionEvent event) {
		ListarClientes listarClientes = (ListarClientes) Controladores.get("vistas/ListarClientes.fxml",
				"Listar Clintes", getEscenario());
		listarClientes.limpiar();
		listarClientes.actualizar(VistaGraficos.getInstancia().getControlador().getClientes());
		listarClientes.getEscenario().showAndWait();
	}

	@FXML
	void borrarCliente(ActionEvent event) {
		PedirDni pedirDni = (PedirDni) Controladores.get("vistas/PedirDni.fxml", "BORRAR CLIENTE", getEscenario());
		pedirDni.limpiar();
		pedirDni.getEscenario().showAndWait();
		try {
			Cliente cliente = pedirDni.getCliente();
			if (cliente != null) {
				if (Dialogos.mostrarDialogoConfirmacion("BORRAR CLIENTE",
						"¿Estás seguro que desea eliminar el cliente?", getEscenario())) {
					VistaGraficos.getInstancia().getControlador().borrar(cliente);
					Dialogos.mostrarDialogoAdvertencia("BORRAR CLIENTE", "El cliente se ha borrado correctamente.",
							getEscenario());
				} else {
					event.consume();
				}
			}

		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ERROR, e.getMessage(), getEscenario());
		}

	}

	@FXML
	void leerAlquiler(ActionEvent event) {
		LeerAlquiler leerAlquiler = (LeerAlquiler) Controladores.get("vistas/LeerAlquiler.fxml", "INSERTAR ALQUILER",
				getEscenario());
		leerAlquiler.limpiar();
		leerAlquiler.getEscenario().showAndWait();
		try {
			Alquiler alquiler = leerAlquiler.getAlquiler();
			if (alquiler != null) {
				VistaGraficos.getInstancia().getControlador().insertar(alquiler);
				Dialogos.mostrarDialogoAdvertencia("INSERTAR ALQUILER", "El alquiler ha sido insertado correctamente.",
						getEscenario());
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ERROR, e.getMessage(), getEscenario());
		}
	}

	@FXML
	void estadisticasAnuales(ActionEvent event) {
		EstadisticasAnuales estadisticasAnuales = (EstadisticasAnuales) Controladores
				.get("vistas/EstadisticasAnuales.fxml", "ESTADISTICAS ANUALES", getEscenario());
		estadisticasAnuales.limpiar();
		estadisticasAnuales.getEscenario().showAndWait();
	}

	@FXML
	void estadisticasMensuales(ActionEvent event) {
		EstadisticasMensuales estadisticasMensuales = (EstadisticasMensuales) Controladores
				.get("vistas/EstadisticasMensuales.fxml", "ESTADISTICAS MENSUALES", getEscenario());
		estadisticasMensuales.limpiar();
		estadisticasMensuales.getEscenario().showAndWait();
	}

	@FXML
	void listarAlquileres(ActionEvent event) {
		ListarAlquileres listarAlquileres = (ListarAlquileres) Controladores.get("vistas/ListarAlquileres.fxml",
				"LISTAR ALQUILERES", getEscenario());
		listarAlquileres.limpiar();
		listarAlquileres.actualizar(VistaGraficos.getInstancia().getControlador().getAlquileres());
		listarAlquileres.getEscenario().showAndWait();
	}

	@FXML
	void borrarAlquiler(ActionEvent event) {
		LeerAlquiler leerAlquiler = (LeerAlquiler) Controladores.get("vistas/LeerAlquiler.fxml", "BORRAR ALQUILER",
				getEscenario());
		leerAlquiler.limpiar();
		leerAlquiler.getEscenario().showAndWait();
		try {
			Alquiler alquiler = leerAlquiler.getAlquiler();
			if (alquiler != null) {
				alquiler = VistaGraficos.getInstancia().getControlador().buscar(alquiler);
				if (Dialogos.mostrarDialogoConfirmacion("BORRAR ALQUILER",
						"¿Estás seguro que desea eliminar el cliente?", getEscenario())) {
					VistaGraficos.getInstancia().getControlador().borrar(alquiler);
					Dialogos.mostrarDialogoAdvertencia("BORRAR ALQUILER", "El alquiler ha sido borrado correctamente.",
							getEscenario());
				} else {
					event.consume();
				}
			}

		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ERROR, e.getMessage(), getEscenario());
		}
	}

	@FXML
	void leerVehiculo(ActionEvent event) {
		LeerVehiculo controladorLeerVehiculo = (LeerVehiculo) Controladores.get("vistas/LeerVehiculo.fxml",
				"Leer Vehiculo", getEscenario());
		controladorLeerVehiculo.limpiar();
		controladorLeerVehiculo.getEscenario().showAndWait();
		try {
			Vehiculo vehiculo = controladorLeerVehiculo.getVehiculo();
			if (vehiculo != null) {
				VistaGraficos.getInstancia().getControlador().insertar(vehiculo);
				Dialogos.mostrarDialogoAdvertencia("INSERTAR VEHICULO", "El vehiculo se ha insertado correctamente.",
						getEscenario());
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ERROR, e.getMessage(), getEscenario());
		}

	}

	@FXML
	void listarVehiculos(ActionEvent event) {
		ListarVehiculos listarVehiculos = (ListarVehiculos) Controladores.get("vistas/ListarVehiculos.fxml",
				"LISTAR VEHICULOS", getEscenario());
		listarVehiculos.limpiar();
		listarVehiculos.actualizar(VistaGraficos.getInstancia().getControlador().getVehiculos());
		listarVehiculos.getEscenario().showAndWait();
	}

	@FXML
	void borrarVehiculo(ActionEvent event) {
		PedirMatricula pedirMatricula = (PedirMatricula) Controladores.get("vistas/PedirMatricula.fxml",
				"BORRAR VEHICULO", getEscenario());
		pedirMatricula.limpiar();
		pedirMatricula.getEscenario().showAndWait();
		try {
			Vehiculo vehiculo = pedirMatricula.getVehiculo();
			if (vehiculo != null) {
				if (Dialogos.mostrarDialogoConfirmacion("BORRAR VEHÍCULO",
						"¿Estás seguro que desea eliminar el vehículo?", getEscenario())) {
					VistaGraficos.getInstancia().getControlador().borrar(vehiculo);
					Dialogos.mostrarDialogoAdvertencia("BORRAR VEHÍCULO", "El vehículo ha sido borrado correctamente.",
							getEscenario());
				} else {
					event.consume();
				}
			}

		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ERROR, e.getMessage(), getEscenario());
		}
	}

	@FXML
	void salir(ActionEvent event) {
		getEscenario().close();
	}

	@FXML
	void acercaDe(ActionEvent event) {
		AcercaDe acercaDe = (AcercaDe) Controladores.get("vistas/AcercaDe.fxml", "Acerca de", getEscenario());
		acercaDe.getEscenario().showAndWait();
	}
}