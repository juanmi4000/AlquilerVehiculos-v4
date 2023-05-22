package org.iesalandalus.programacion.alquilervehiculos.vista.graficos.controladores;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.VistaGraficos;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PedirDni extends Controlador {

	private boolean cancelado;

	@FXML
	private TextField tfDni;

	@FXML
	void borrar(ActionEvent event) {
		cancelado = false;
		getEscenario().close();
	}

	@FXML
	void cancelar(ActionEvent event) {
		cancelado = true;
		getEscenario().close();
	}

	@FXML
	public Cliente getCliente() {
		return cancelado ? null
				: VistaGraficos.getInstancia().getControlador().buscar(Cliente.getClienteConDni(tfDni.getText()));
	}

	@FXML
	void limpiar() {
		tfDni.setText("");
		cancelado = true;
	}

}
