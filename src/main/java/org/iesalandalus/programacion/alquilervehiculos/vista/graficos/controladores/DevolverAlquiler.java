package org.iesalandalus.programacion.alquilervehiculos.vista.graficos.controladores;

import java.time.LocalDate;

import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class DevolverAlquiler extends Controlador {

	private static final ObservableList<String> DEVOLVER = FXCollections.observableArrayList("Devolver por cliente",
			"Devolver por vehiculo");
	private boolean cancelado;

	@FXML
	private ChoiceBox<String> cbDevolver;

	@FXML
	private DatePicker dpFechaDevolucion;

	@FXML
	private TextField tfDni;

	@FXML
	private TextField tfMatricula;

	@FXML
	void initialize() {
		cbDevolver.getSelectionModel().select("Elige una opcion:");
		cbDevolver.setItems(DEVOLVER);
	}

	@FXML
	void cancelar(ActionEvent event) {
		cancelado = true;
		getEscenario().close();
	}

	@FXML
	void devolverAlquiler(ActionEvent event) {
		cancelado = false;
		getEscenario().close();
	}

	@FXML
	LocalDate getFechaDevolucion() {
		return cancelado ? null : dpFechaDevolucion.getValue();
	}

}
