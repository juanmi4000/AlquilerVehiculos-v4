package org.iesalandalus.programacion.alquilervehiculos.vista.graficos.controladores;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LeerCliente extends Controlador {

	@FXML
	private TextField tfDni;
	@FXML
	private TextField tfNombre;
	@FXML
	private TextField tfTelefono;

	private boolean cancelado;

	@FXML
	void initialize() {
		tfNombre.textProperty().addListener((ob, ol, ne) -> comprobarNombre());
	}

	@FXML
	void aceptar(ActionEvent event) {
		cancelado = false;
		getEscenario().close();
	}

	@FXML
	void cancelar(ActionEvent event) {
		cancelado = true;
		getEscenario().close();
	}

	@FXML
	void comprobarNombre() {
		String nombre = tfNombre.getText();
		if (nombre.matches("[A-Z][a-z]+( [A-Z][a-z]+)*")) {
			tfNombre.setStyle("-fx-border-color: green");
		} else {
			tfNombre.setStyle("-fx-border-color: red");
		}
	}

	public Cliente getCliente() {
		String nombre = tfNombre.getText();
		String dni = tfDni.getText();
		String telefono = tfTelefono.getText();
		return cancelado ? null : new Cliente(nombre, dni, telefono);
	}

	void limpiar() {
		tfNombre.setText("");
		tfDni.setText("");
		tfTelefono.setText("");
		cancelado = true;
	}

}
