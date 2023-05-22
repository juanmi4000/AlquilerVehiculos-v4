package org.iesalandalus.programacion.alquilervehiculos.vista.graficos.controladores;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ModificarCliente extends Controlador {

	@FXML
	private TextField tfDni;

	@FXML
	private TextField tfNombre;

	@FXML
	private TextField tfNombreActual;

	@FXML
	private TextField tfTelefono;

	@FXML
	private TextField tfTelefonoActual;

	private boolean cancelado;

	@FXML
	private void initialize() {
		tfNombreActual.setDisable(true);
		tfTelefonoActual.setDisable(true);
	}

	@FXML
	void aceptar(ActionEvent event) {
		cancelado = false;
	}

	@FXML
	void buscarCliente(ActionEvent event) {
		Cliente cliente = getCliente();
		tfNombreActual.setText(cliente.getNombre());
		tfTelefonoActual.setText(cliente.getTelefono());
	}

	@FXML
	public Cliente getCliente() {
		String dni = tfDni.getText();
		return cancelado ? null: Cliente.getClienteConDni(dni);

	}

	@FXML
	void cancelar(ActionEvent event) {
		cancelado = true;
	}

	@FXML
	public String getNombre() {
		return cancelado ? "" : tfNombre.getText();
	}

	@FXML
	public String getTelefono() {
		return cancelado ? "" : tfTelefono.getText();
	}

	@FXML
	public void limpiar() {
		tfDni.setText("");
		tfNombre.setText("");
		tfTelefono.setText("");
		cancelado = true;
	}

}
