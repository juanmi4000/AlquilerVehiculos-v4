package org.iesalandalus.programacion.alquilervehiculos.vista.graficos.controladores;

import java.awt.Desktop;
import java.util.Comparator;
import java.util.List;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Autobus;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Furgoneta;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.VistaGraficos;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controlador;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controladores;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controles;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Dialogos;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ListarVehiculos extends Controlador {

	private static final String BORRAR_VEHICULO = "BORRAR VEHíCULO";

	private static final String AUTOBUS = "Autobus";

	private static final String FURGONETA = "Furgoneta";

	private static final String TURISMO = "Turismo";

	private static final String NO_ENCONTRADO = "-----------";

	private static final String ERROR = "ERROR";

	@FXML
	private Button btBorrar;

	@FXML
	private Button btBuscar;

	@FXML
	private Button btListVehi;

	@FXML
	private TableColumn<Vehiculo, String> tcCilindrada;

	@FXML
	private TableColumn<Vehiculo, String> tcMarca;

	@FXML
	private TableColumn<Vehiculo, String> tcMatricula;

	@FXML
	private TableColumn<Vehiculo, String> tcModelo;

	@FXML
	private TableColumn<Vehiculo, String> tcPlazas;

	@FXML
	private TableColumn<Vehiculo, String> tcPma;

	@FXML
	private TableColumn<Vehiculo, String> tcTipo;

	@FXML
	private TextField tfCambiarCilindrada;

	@FXML
	private TextField tfCambiarMarca;

	@FXML
	private TextField tfCambiarMatricula;

	@FXML
	private TextField tfCambiarModelo;

	@FXML
	private TextField tfCambiarPlazas;

	@FXML
	private TextField tfCambiarPma;

	@FXML
	private TextField tfCambiarTipo;

	@FXML
	private TextField tfCilindradaEncontrada;

	@FXML
	private TextField tfListarAlquileresV;

	@FXML
	private TextField tfMarcaEncontrada;

	@FXML
	private TextField tfMatricula;

	@FXML
	private TextField tfMatriculaEncontrada;

	@FXML
	private TextField tfModeloEncontrado;

	@FXML
	private TextField tfPlazasEncontradas;

	@FXML
	private TextField tfPmaEncontrada;

	@FXML
	private TextField tfTipoEncontrado;

	@FXML
	private TableView<Vehiculo> tvListaVehiculos;

	@FXML
	private void initialize() {
		deshabilitar();
		tcTipo.setCellValueFactory(fila -> new SimpleStringProperty(cambiarTipo(fila.getValue())));
		tcMarca.setCellValueFactory(fila -> new SimpleStringProperty(fila.getValue().getMarca()));
		tcModelo.setCellValueFactory(fila -> new SimpleStringProperty(fila.getValue().getModelo()));
		tcMatricula.setCellValueFactory(fila -> new SimpleStringProperty(fila.getValue().getMatricula()));
		tcCilindrada.setCellValueFactory(fila -> new SimpleStringProperty(cambiarCilindrada(fila.getValue())));
		tcPlazas.setCellValueFactory(fila -> new SimpleStringProperty(cambiarPlazas(fila.getValue())));
		tcPma.setCellValueFactory(fila -> new SimpleStringProperty(cambiarCPma(fila.getValue())));
		tvListaVehiculos.getSelectionModel().selectedItemProperty().addListener((ob, ol, ne) -> filaSeleccionada(ne));
	}

	@FXML
	void acercaDe(ActionEvent event) {
		AcercaDe acercaDe = (AcercaDe) Controladores.get("vistas/AcercaDe.fxml", "Acerca de", getEscenario());
		acercaDe.getEscenario().showAndWait();
	}

	@FXML
	void borrarVehiculo(ActionEvent event) {
		try {
			String matricula = tfMatricula.getText();
			if (!matricula.isBlank()) {
				if (Dialogos.mostrarDialogoConfirmacion(BORRAR_VEHICULO, "¿Estás seguro que desea eliminar el cliente?",
						getEscenario())) {
					Vehiculo vehiculo = VistaGraficos.getInstancia().getControlador()
							.buscar(Vehiculo.getVehiculoConMatricula(matricula));
					VistaGraficos.getInstancia().getControlador().borrar(vehiculo);
					tvListaVehiculos.getItems().remove(vehiculo);
					Dialogos.mostrarDialogoAdvertencia(BORRAR_VEHICULO, "El vehículo ha sido borrado correctamente.",
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
	void borrarVehiculoTabla(ActionEvent event) {
		Vehiculo vehiculo = tvListaVehiculos.getSelectionModel().getSelectedItem();
		try {
			if (Dialogos.mostrarDialogoConfirmacion(BORRAR_VEHICULO, "¿Estás seguro que desea eliminar el cliente?",
					getEscenario())) {
				VistaGraficos.getInstancia().getControlador().borrar(vehiculo);
				tvListaVehiculos.getItems().remove(vehiculo);
				Dialogos.mostrarDialogoAdvertencia(BORRAR_VEHICULO, "El vehículo ha sido borrado correctamente.",
						getEscenario());
			} else {
				event.consume();
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ERROR, e.getMessage(), getEscenario());
		}
	}

	@FXML
	void buscarVehiculo(ActionEvent event) {
		try {
			Vehiculo vehiculo = VistaGraficos.getInstancia().getControlador()
					.buscar(Vehiculo.getVehiculoConMatricula(tfMatricula.getText()));
			vehiculoEncontrado(vehiculo);
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ERROR, e.getMessage(), getEscenario());
		}
	}

	@FXML
	void comprobarAlquileresVehiculoCo(ActionEvent event) {
		if (tfListarAlquileresV.getText().equals("")) {
			Dialogos.mostrarDialogoError(ERROR,
					"Para listar los alquileres, el campo de la matricula no puede estar vacia.", getEscenario());
			btListVehi.setDisable(true);
		} else {
			btListVehi.setDisable(false);
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
	void gitHub(ActionEvent event) {
		String link = "https://github.com/juanmi4000/AlquilerVehiculos-v3.git";
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
	void insertarVehiculo(ActionEvent event) {
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
	void listarAlquileres(ActionEvent event) {
		try {
			ListarAlquileres listarAlquileres = (ListarAlquileres) Controladores.get("vistas/ListarAlquileres.fxml",
					"LISTAR ALQUILERES", getEscenario());
			listarAlquileres.actualizar(VistaGraficos.getInstancia().getControlador().getAlquileres());
			listarAlquileres.getEscenario().showAndWait();
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ERROR, "La ventana ya está abierta.", getEscenario());
		}

	}

	@FXML
	void listarAlquileresVehiculo(ActionEvent event) {
		ListarAlquileresClienVehi listarAlquileresVehiculo = (ListarAlquileresClienVehi) Controladores
				.get("vistas/ListarAlquileresClienVehi.fxml", "ALQUILERES VEHICULO", getEscenario());
		try {
			Vehiculo vehiculo = VistaGraficos.getInstancia().getControlador()
					.buscar(Vehiculo.getVehiculoConMatricula(tfListarAlquileresV.getText()));
			listarAlquileresVehiculo.actualizar(VistaGraficos.getInstancia().getControlador().getAlquileres(vehiculo));
			listarAlquileresVehiculo.getEscenario().showAndWait();
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ERROR, e.getMessage(), getEscenario());
		}
	}

	@FXML
	void listarClientes(ActionEvent event) {
		try {
			ListarClientes controladorListar = (ListarClientes) Controladores.get("vistas/ListarClientes.fxml",
					"Listar Clintes", getEscenario());
			controladorListar.limpiar();
			controladorListar.actualizar(VistaGraficos.getInstancia().getControlador().getClientes());
			controladorListar.getEscenario().showAndWait();
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ERROR, "La ventana ya está abierta.", getEscenario());
		}
	}

	@FXML
	void matriculaBusBor(ActionEvent event) {
		if (tfMatricula.getText().equals("")) {
			btBuscar.setDisable(true);
			btBorrar.setDisable(true);
		} else {
			btBuscar.setDisable(false);
			btBorrar.setDisable(false);
		}
	}

	@FXML
	void salir(ActionEvent event) {
		if (Dialogos.mostrarDialogoConfirmacion("SALIR", "¿Está seguro que desea salir?", getEscenario())) {
			getEscenario().close();
		} else {
			event.consume();
		}
	}

	@FXML
	void actualizar(List<Vehiculo> vehiculos) {
		vehiculos.sort(Comparator.comparing(Vehiculo::getMarca).thenComparing(Vehiculo::getModelo)
				.thenComparing(Vehiculo::getMatricula));
		tvListaVehiculos.setItems(FXCollections.observableArrayList(vehiculos));
	}

	@FXML
	void deshabilitar() {
		Controles.deshabilitarCamposTexto(tfCambiarTipo, tfCambiarMarca, tfCambiarModelo, tfCambiarMatricula,
				tfCambiarCilindrada, tfCambiarPlazas, tfCambiarPma);
		Controles.deshabilitarCamposTexto(tfTipoEncontrado, tfMarcaEncontrada, tfModeloEncontrado,
				tfMatriculaEncontrada, tfCilindradaEncontrada, tfPlazasEncontradas, tfPmaEncontrada);
		btBorrar.setDisable(true);
		btBuscar.setDisable(true);
		btListVehi.setDisable(true);
	}

	@FXML
	private void filaSeleccionada(Vehiculo vehiculo) {
		tfCambiarMarca.setText(vehiculo.getMarca());
		tfCambiarModelo.setText(vehiculo.getModelo());
		tfCambiarMatricula.setText(vehiculo.getMatricula());
		if (vehiculo instanceof Turismo turismo) {
			tfCambiarTipo.setText(TURISMO);
			tfCambiarCilindrada.setText(String.format("%s", turismo.getCilindrada()));
			tfCambiarPma.setText(NO_ENCONTRADO);
			tfCambiarPlazas.setText(NO_ENCONTRADO);
		} else if (vehiculo instanceof Furgoneta furgoneta) {
			tfCambiarTipo.setText(FURGONETA);
			tfCambiarCilindrada.setText(NO_ENCONTRADO);
			tfCambiarPma.setText(String.format("%s", furgoneta.getPma()));
			tfCambiarPlazas.setText(String.format("%s", furgoneta.getPlazas()));
		} else if (vehiculo instanceof Autobus autobus) {
			tfCambiarTipo.setText(AUTOBUS);
			tfCambiarCilindrada.setText(NO_ENCONTRADO);
			tfCambiarPma.setText(NO_ENCONTRADO);
			tfCambiarPlazas.setText(String.format("%s", autobus.getPlazas()));
		}
	}

	@FXML
	private void vehiculoEncontrado(Vehiculo vehiculo) {
		tfMarcaEncontrada.setText(vehiculo.getMarca());
		tfModeloEncontrado.setText(vehiculo.getModelo());
		tfMatriculaEncontrada.setText(vehiculo.getMatricula());
		if (vehiculo instanceof Turismo turismo) {
			tfTipoEncontrado.setText(TURISMO);
			tfCilindradaEncontrada.setText(String.format("%s", turismo.getCilindrada()));
			tfPmaEncontrada.setText(NO_ENCONTRADO);
			tfPlazasEncontradas.setText(NO_ENCONTRADO);
		} else if (vehiculo instanceof Furgoneta furgoneta) {
			tfTipoEncontrado.setText(FURGONETA);
			tfCilindradaEncontrada.setText(NO_ENCONTRADO);
			tfPmaEncontrada.setText(String.format("%s", furgoneta.getPma()));
			tfPlazasEncontradas.setText(String.format("%s", furgoneta.getPlazas()));
		} else if (vehiculo instanceof Autobus autobus) {
			tfTipoEncontrado.setText(AUTOBUS);
			tfCilindradaEncontrada.setText(NO_ENCONTRADO);
			tfPmaEncontrada.setText(NO_ENCONTRADO);
			tfPlazasEncontradas.setText(String.format("%s", autobus.getPlazas()));
		}
	}

	@FXML
	private String cambiarTipo(Vehiculo vehiculo) {
		String cadena = "";
		if (vehiculo instanceof Turismo) {
			cadena = TURISMO;
		} else if (vehiculo instanceof Furgoneta) {
			cadena = FURGONETA;
		} else {
			cadena = AUTOBUS;
		}
		return cadena;
	}

	@FXML
	private String cambiarCilindrada(Vehiculo vehiculo) {
		return vehiculo instanceof Turismo turismo ? String.format("%s", turismo.getCilindrada()) : "";
	}

	@FXML
	private String cambiarPlazas(Vehiculo vehiculo) {
		String cadena = "";
		if (vehiculo instanceof Furgoneta furgoneta) {
			cadena = String.format("%s", furgoneta.getPlazas());
		} else if (vehiculo instanceof Autobus autobus) {
			cadena = String.format("%s", autobus.getPlazas());
		}
		return cadena;
	}

	@FXML
	private String cambiarCPma(Vehiculo vehiculo) {
		return vehiculo instanceof Furgoneta furgoneta ? String.format("%s", furgoneta.getPma()) : "";
	}
	
	@FXML
	void limpiar() {
		tfMatricula.setText("");
	}
}