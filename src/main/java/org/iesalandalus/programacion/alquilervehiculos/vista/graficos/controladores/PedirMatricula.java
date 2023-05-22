package org.iesalandalus.programacion.alquilervehiculos.vista.graficos.controladores;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.VistaGraficos;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PedirMatricula extends Controlador {

	private boolean cancelado;

	@FXML
	private TextField tfMatricula;

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
	public Vehiculo getVehiculo() {
		return cancelado ? null
				: VistaGraficos.getInstancia().getControlador()
						.buscar(Vehiculo.getVehiculoConMatricula(tfMatricula.getText()));
	}

	@FXML
	void limpiar() {
		tfMatricula.setText("");
		cancelado = true;
	}

}
