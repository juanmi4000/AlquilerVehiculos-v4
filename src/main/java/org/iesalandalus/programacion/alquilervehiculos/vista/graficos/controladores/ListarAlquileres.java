package org.iesalandalus.programacion.alquilervehiculos.vista.graficos.controladores;

import java.awt.Desktop;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Autobus;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Furgoneta;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.VistaGraficos;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controlador;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controladores;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controles;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controles.FormateadorCeldaFecha;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Dialogos;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ListarAlquileres extends Controlador {

	private static final String GUIONES = "-----------";
	private static final String ERROR = "ERROR";
	private static final ObservableList<String> DEVOLVER = FXCollections.observableArrayList("Devolver por cliente",
			"Devolver por vehículo");

	@FXML
	private Button btBorrar;

	@FXML
	private Button btBuscar;

	@FXML
	private ChoiceBox<String> cbDevolver;

	@FXML
	private DatePicker dpFechaAlquiler;

	@FXML
	private DatePicker dpFechaDevolucion;

	@FXML
	private TableColumn<Alquiler, String> tcPrecio;

	@FXML
	private TableColumn<Alquiler, String> tcCliente;

	@FXML
	private TableColumn<Alquiler, LocalDate> tcFechaAlquiler;

	@FXML
	private TableColumn<Alquiler, LocalDate> tcFechaDevolucion;

	@FXML
	private TableColumn<Alquiler, String> tcVehiculo;

	@FXML
	private TextField tfCambiarCilindrada;

	@FXML
	private TextField tfCambiarDni;

	@FXML
	private TextField tfCambiarFecAlq;

	@FXML
	private TextField tfCambiarFecDev;

	@FXML
	private TextField tfCambiarMarca;

	@FXML
	private TextField tfCambiarMatricula;

	@FXML
	private TextField tfCambiarModelo;

	@FXML
	private TextField tfCambiarNombre;

	@FXML
	private TextField tfCambiarPlazas;

	@FXML
	private TextField tfCambiarPma;

	@FXML
	private TextField tfCambiarTelefono;

	@FXML
	private TextField tfCambiarTipo;

	@FXML
	private TextField tfDni;

	@FXML
	private TextField tfMatricula;

	@FXML
	private TextField tfPrecio;

	@FXML
	private TextField tfOpDni;

	@FXML
	private TextField tfOpMat;

	@FXML
	private TableView<Alquiler> tvListarAlquiler;

	@FXML
	private void initialize() {
		deshabilitar();
		cbDevolver.setItems(DEVOLVER);
		cbDevolver.valueProperty().addListener((ob, ol, ne) -> comprobarValor(ne));
		tcCliente.setCellValueFactory(fila -> new SimpleStringProperty(fila.getValue().getCliente().getDni()));
		tcVehiculo.setCellValueFactory(fila -> new SimpleStringProperty(fila.getValue().getVehiculo().getMatricula()));
		tcFechaAlquiler
				.setCellValueFactory(fila -> new SimpleObjectProperty<LocalDate>(fila.getValue().getFechaAlquiler()));
		tcFechaAlquiler.setCellFactory(columna -> new FormateadorCeldaFecha());
		tcFechaDevolucion
				.setCellValueFactory(fila -> new SimpleObjectProperty<LocalDate>(fila.getValue().getFechaDevolucion()));
		tcFechaDevolucion.setCellFactory(columna -> new FormateadorCeldaFecha());
		tcPrecio.setCellValueFactory(fila -> new SimpleStringProperty(cambiarPrecio(fila.getValue())));
		tvListarAlquiler.getSelectionModel().selectedItemProperty()
				.addListener((ob, oldValue, newValue) -> filaSeleccionada(newValue));
	}

	@FXML
	void acercaDe(ActionEvent event) {
		AcercaDe acercaDe = (AcercaDe) Controladores.get("vistas/AcercaDe.fxml", "Acerca de", getEscenario());
		acercaDe.getEscenario().showAndWait();
	}

	@FXML
	void borrarAlquiler(ActionEvent event) {

		try {
			Alquiler alquiler = getAlquiler(true);
			if (alquiler != null) {
				if (Dialogos.mostrarDialogoConfirmacion("BORRAR ALQUILER",
						"¿Estás seguro que desea eliminar el cliente?", getEscenario())) {
					VistaGraficos.getInstancia().getControlador().borrar(alquiler);
					tvListarAlquiler.getItems().remove(alquiler);
					Dialogos.mostrarDialogoAdvertencia("BORRAR ALQUILER", "El alquiler ha sido borrado correctamente.",
							getEscenario());
				} else {
					event.consume();
				}
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ERROR, e.getMessage(), getEscenario());
		}
		limpiarOperaciones();
	}

	@FXML
	void borrarAlquilerTablas(ActionEvent event) {
		Alquiler alquiler = tvListarAlquiler.getSelectionModel().getSelectedItem();
		try {
			if (Dialogos.mostrarDialogoConfirmacion("BORRAR ALQUILER", "¿Estás seguro que desea eliminar el cliente?",
					getEscenario())) {
				VistaGraficos.getInstancia().getControlador().borrar(alquiler);
				tvListarAlquiler.getItems().remove(tvListarAlquiler.getSelectionModel().getSelectedIndex());
				Dialogos.mostrarDialogoAdvertencia("BORRAR ALQUILER", "El alquiler se ha borrado correctamente.",
						getEscenario());
			} else {
				event.consume();
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ERROR, e.getMessage(), getEscenario());
		}
	}

	@FXML
	void buscarAlquiler(ActionEvent event) {
		try {
			Alquiler alquiler = getAlquiler(true);
			if (alquiler != null) {
				BuscarAlquiler buscarAlquiler = (BuscarAlquiler) Controladores.get("vistas/BuscarAlquiler.fxml",
						"ALQUILER BUSCADO", getEscenario());
				buscarAlquiler.limpiar();
				buscarAlquiler.cambiarValores(alquiler);
				buscarAlquiler.getEscenario().showAndWait();
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ERROR, e.getMessage(), getEscenario());
		}
		limpiarOperaciones();
	}

	@FXML
	void devolverAlquiler(ActionEvent event) {
		String dni = tfDni.getText();
		String matricula = tfMatricula.getText();
		LocalDate fechaDevolucion = dpFechaDevolucion.getValue();
		try {
			if (dni.isBlank() && matricula.isBlank()) {
				Dialogos.mostrarDialogoError(ERROR, "Tiene que rellenar un campo dni/matricula.", getEscenario());
			} else {
				if (Dialogos.mostrarDialogoConfirmacion("DEVOLVER ALQUILER",
						"¿Estás seguro que desea eliminar el cliente?", getEscenario())) {
					if (!dni.isBlank()) {
						Cliente cliente = VistaGraficos.getInstancia().getControlador()
								.buscar(Cliente.getClienteConDni(dni));
						VistaGraficos.getInstancia().getControlador().devolver(cliente, fechaDevolucion);
					} else {
						Vehiculo vehiculo = VistaGraficos.getInstancia().getControlador()
								.buscar(Vehiculo.getVehiculoConMatricula(matricula));
						VistaGraficos.getInstancia().getControlador().devolver(vehiculo, fechaDevolucion);
					}
				} else {
					event.consume();
				}
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ERROR, e.getMessage(), getEscenario());
		}
		limpiarDevolver();
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
	void leerAlquiler(ActionEvent event) {
		try {
			Alquiler alquiler = getAlquiler(false);
			if (alquiler != null) {
				VistaGraficos.getInstancia().getControlador().insertar(alquiler);
				tvListarAlquiler.getItems().add(alquiler);
				Dialogos.mostrarDialogoAdvertencia("INSERTAR ALQUILER", "El alquiler se ha insertado correctamente.",
						getEscenario());
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ERROR, e.getMessage(), getEscenario());
		}

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
	void listarVehiculos(ActionEvent event) {
		try {
			ListarVehiculos listarVehiculos = (ListarVehiculos) Controladores.get("vistas/ListarVehiculos.fxml",
					"LISTAR VEHICULOS", getEscenario());
			listarVehiculos.actualizar(VistaGraficos.getInstancia().getControlador().getVehiculos());
			listarVehiculos.getEscenario().showAndWait();
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ERROR, "La ventana ya está abierta.", getEscenario());
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
	void actualizar(List<Alquiler> alquileres) {
		Comparator<Cliente> ordenadoCliente = Comparator.comparing(Cliente::getNombre).thenComparing(Cliente::getDni);
		alquileres.sort(
				Comparator.comparing(Alquiler::getFechaAlquiler).thenComparing(Alquiler::getCliente, ordenadoCliente));
		tvListarAlquiler.setItems(FXCollections.observableArrayList(alquileres));
	}

	@FXML
	void deshabilitar() {
		Controles.deshabilitarCamposTexto(tfCambiarNombre, tfCambiarDni, tfCambiarTelefono,
				tfCambiarTipo, tfCambiarMarca, tfCambiarModelo);
		Controles.deshabilitarCamposTexto(tfCambiarMatricula, tfCambiarCilindrada, tfCambiarPma, tfCambiarPlazas,
				tfCambiarFecAlq, tfCambiarFecDev, tfPrecio);
		dpFechaDevolucion.setDisable(true);
	}

	@FXML
	private void comprobarValor(String cadena) {
		if (cadena.equals("Devolver por cliente")) {
			tfDni.setDisable(false);
			tfMatricula.setDisable(true);
			dpFechaDevolucion.setDisable(false);
		} else {
			tfDni.setDisable(true);
			tfMatricula.setDisable(false);
			dpFechaDevolucion.setDisable(false);
		}
	}

	@FXML
	private void filaSeleccionada(Alquiler alquiler) {
		Cliente cliente = VistaGraficos.getInstancia().getControlador().buscar(alquiler.getCliente());
		Vehiculo vehiculo = VistaGraficos.getInstancia().getControlador().buscar(alquiler.getVehiculo());
		LocalDate fechaAlquiler = alquiler.getFechaAlquiler();
		LocalDate fechaDevolucion = alquiler.getFechaDevolucion();
		tfCambiarNombre.setText(cliente.getNombre());
		tfCambiarDni.setText(cliente.getDni());
		tfCambiarTelefono.setText(cliente.getTelefono());
		tipoVehiculo(vehiculo);
		tfCambiarFecAlq.setText(String.format("%s", fechaAlquiler));
		if (fechaDevolucion == null) {
			tfCambiarFecDev.setText("Aún no está devuelto");
			tfPrecio.setText("---------------");
		} else {
			tfCambiarFecDev.setText(String.format("%s", fechaDevolucion));
			tfPrecio.setText(String.format("%s", alquiler.getPrecio()));
		}
	}

	@FXML
	private void tipoVehiculo(Vehiculo vehiculo) {
		tfCambiarMarca.setText(vehiculo.getMarca());
		tfCambiarModelo.setText(vehiculo.getModelo());
		tfCambiarMatricula.setText(vehiculo.getMatricula());
		if (vehiculo instanceof Turismo turismo) {
			tfCambiarTipo.setText("Turismo");
			tfCambiarCilindrada.setText(String.format("%s", turismo.getCilindrada()));
			tfCambiarPma.setText(GUIONES);
			tfCambiarPlazas.setText(GUIONES);
		} else if (vehiculo instanceof Furgoneta furgoneta) {
			tfCambiarTipo.setText("Furgoneta");
			tfCambiarCilindrada.setText(GUIONES);
			tfCambiarPma.setText(String.format("%s", furgoneta.getPma()));
			tfCambiarPlazas.setText(String.format("%s", furgoneta.getPlazas()));
		} else if (vehiculo instanceof Autobus autobus) {
			tfCambiarTipo.setText("Autobus");
			tfCambiarCilindrada.setText(GUIONES);
			tfCambiarPma.setText(GUIONES);
			tfCambiarPlazas.setText(String.format("%s", autobus.getPlazas()));

		}
	}

	@FXML
	Alquiler getAlquiler(boolean buscar) {
		Cliente cliente = VistaGraficos.getInstancia().getControlador()
				.buscar(Cliente.getClienteConDni(tfOpDni.getText()));
		Vehiculo vehiculo = VistaGraficos.getInstancia().getControlador()
				.buscar(Vehiculo.getVehiculoConMatricula(tfOpMat.getText()));
		LocalDate fechaAlquiler = dpFechaAlquiler.getValue();
		Alquiler alquiler = new Alquiler(cliente, vehiculo, fechaAlquiler);
		return buscar ? VistaGraficos.getInstancia().getControlador().buscar(alquiler) : alquiler;

	}
	
	@FXML
	void limpiar() {
		cbDevolver.getSelectionModel().select("Elige una opcion:");
		Controles.limpiarCamposTexto(tfDni, tfMatricula);
		dpFechaDevolucion.setValue(null);
		Controles.limpiarCamposTexto(tfOpDni, tfOpMat);
		dpFechaAlquiler.setValue(null);
		Controles.deshabilitarCamposTexto(tfDni, tfMatricula);
		dpFechaDevolucion.setDisable(true);
	}

	@FXML
	void limpiarDevolver() {
		Controles.limpiarCamposTexto(tfDni, tfMatricula);
		dpFechaDevolucion.setValue(null);
	}

	@FXML
	void limpiarOperaciones() {
		Controles.limpiarCamposTexto(tfOpDni, tfOpMat);
		dpFechaAlquiler.setValue(null);
	}

	@FXML
	private String cambiarPrecio(Alquiler alquiler) {
		String cadena = "";
		if (alquiler.getFechaDevolucion() != null) {
			cadena = String.format("%s", alquiler.getPrecio());
		}
		return cadena;
	}

}