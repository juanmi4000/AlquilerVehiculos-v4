package org.iesalandalus.programacion.alquilervehiculos.vista.graficos.controladores;

import java.time.LocalDate;
import java.util.List;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controlador;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controles.FormateadorCeldaFecha;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Dialogos;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ListarAlquileresClienVehi extends Controlador {

	@FXML
	private TableColumn<Alquiler, String> tcCliente;

	@FXML
	private TableColumn<Alquiler, LocalDate> tcFechaAlquiler;

	@FXML
	private TableColumn<Alquiler, LocalDate> tcFechaDevolucion;

	@FXML
	private TableColumn<Alquiler, String> tcVehiculo;

	@FXML
	private TableColumn<Alquiler, String> tcPrecio;

	@FXML
	private TableView<Alquiler> tvListarAlquileresCliente;

	@FXML
	private void initialize() {
		tcCliente.setCellValueFactory(fila -> new SimpleStringProperty(fila.getValue().getCliente().getDni()));
		tcVehiculo.setCellValueFactory(fila -> new SimpleStringProperty(fila.getValue().getVehiculo().getMatricula()));
		tcFechaAlquiler
				.setCellValueFactory(fila -> new SimpleObjectProperty<LocalDate>(fila.getValue().getFechaAlquiler()));
		tcFechaAlquiler.setCellFactory(columna -> new FormateadorCeldaFecha());
		tcFechaDevolucion
				.setCellValueFactory(fila -> new SimpleObjectProperty<LocalDate>(fila.getValue().getFechaDevolucion()));
		tcFechaDevolucion.setCellFactory(columna -> new FormateadorCeldaFecha());
		tcPrecio.setCellValueFactory(fila -> new SimpleStringProperty(cambiarPrecio(fila.getValue())));
	}

	@FXML
	void actualizar(List<Alquiler> alquileres) {
		tvListarAlquileresCliente.setItems(FXCollections.observableArrayList(alquileres));
	}

	@FXML
	private String cambiarPrecio(Alquiler alquiler) {
		return alquiler.getFechaDevolucion() == null ? "" : String.format("%s", alquiler.getPrecio());
	}
	
	@FXML
    void salir(ActionEvent event) {
		if (Dialogos.mostrarDialogoConfirmacion("SALIR", "¿Está seguro que desea salir?", getEscenario())) {
			getEscenario().close();
		} else {
			event.consume();
		}
    }
}
