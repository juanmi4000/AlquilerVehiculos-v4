package org.iesalandalus.programacion.alquilervehiculos.vista.graficos.controladores;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Autobus;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Furgoneta;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class LeerVehiculo extends Controlador {

	private static final String FURGONETA = "Furgoneta";
	private static final String TURISMO = "Turismo";
	private static final ObservableList<String> TIPOS_VEHICULOS = FXCollections.observableArrayList(TURISMO, FURGONETA,
			"Autobus");

	@FXML
	private ChoiceBox<String> cbTipo;

	@FXML
	private TextField tfPma;

	@FXML
	private TextField tfCilindrada;

	@FXML
	private TextField tfMarca;

	@FXML
	private TextField tfMatricula;

	@FXML
	private TextField tfModelo;

	@FXML
	private TextField tfPlazas;

	private boolean cancelado;

	@FXML
	void initialize() {
		cbTipo.setItems(TIPOS_VEHICULOS);
		tfCilindrada.setDisable(true);
		tfPlazas.setDisable(true);
		tfPma.setDisable(true);
		cbTipo.valueProperty().addListener((ob, ol, ne) -> comprobarValor(ne));
	}

	@FXML
	void cancelar(ActionEvent event) {
		cancelado = true;
		getEscenario().close();
	}

	@FXML
	void insertarAlquiler(ActionEvent event) {
		cancelado = false;
		getEscenario().close();
	}

	public Vehiculo getVehiculo() {
		String valor = cbTipo.valueProperty().getValue();
		Vehiculo vehiculo = null;
		String marca = tfMarca.getText();
		String modelo = tfModelo.getText();
		String matricula = tfMatricula.getText();
		if (valor.equals(TURISMO)) {
			String cilin = tfCilindrada.getText();
			if (!cilin.isBlank()) {
				int cilindrada = Integer.parseInt(cilin);
				vehiculo = new Turismo(marca, modelo, cilindrada, matricula);
			}
		} else if (valor.equals(FURGONETA)) {
			String plaz = tfPlazas.getText();
			String pm = tfPma.getText();
			if (!plaz.isBlank() && !pm.isBlank()) {
				int plazas = Integer.parseInt(plaz);
				int pma = Integer.parseInt(pm);
				vehiculo = new Furgoneta(marca, modelo, pma, plazas, matricula);
			}
		} else {
			String plaz = tfPlazas.getText();
			if (!plaz.isBlank()) {
				int plazas = Integer.parseInt(plaz);
				vehiculo = new Autobus(marca, modelo, plazas, matricula);
			}
		}
		return cancelado ? null : vehiculo;
	}

	private void comprobarValor(String cadena) {
		if (cadena.equals(TURISMO)) {
			tfCilindrada.setDisable(false);
			tfPlazas.setDisable(true);
			tfPma.setDisable(true);
		} else if (cadena.equals(FURGONETA)) {
			tfPlazas.setDisable(false);
			tfPma.setDisable(false);
			tfCilindrada.setDisable(true);
		} else {
			tfPlazas.setDisable(false);
			tfPma.setDisable(true);
			tfCilindrada.setDisable(true);
		}

	}

	void limpiar() {
		cbTipo.getSelectionModel().select("Elige un vehículo:");
		tfMarca.setText("");
		tfModelo.setText("");
		tfMatricula.setText("");
		tfCilindrada.setText("");
		tfPlazas.setText("");
		tfPma.setText("");
		tfPlazas.setDisable(true);
		cancelado = true;
	}

}