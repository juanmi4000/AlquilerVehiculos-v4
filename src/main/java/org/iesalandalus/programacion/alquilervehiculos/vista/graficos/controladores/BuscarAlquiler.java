package org.iesalandalus.programacion.alquilervehiculos.vista.graficos.controladores;

import java.time.LocalDate;

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
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Dialogos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class BuscarAlquiler extends Controlador {

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
	private TextField tfPrecio;

	@FXML
	private Button btDevolver;

	@FXML
	private void initialize() {
		Controles.deshabilitarCamposTexto(tfCambiarNombre, tfCambiarDni, tfCambiarTelefono);
		Controles.deshabilitarCamposTexto(tfCambiarTipo, tfCambiarMarca, tfCambiarModelo, tfCambiarMatricula,
				tfCambiarCilindrada, tfCambiarPlazas, tfCambiarPma);
		Controles.deshabilitarCamposTexto(tfCambiarFecAlq, tfCambiarFecDev, tfPrecio);
	}

	@FXML
	void cancelar(ActionEvent event) {
		getEscenario().close();
	}

	@FXML
	void devolver(ActionEvent event) {
		try {
			DevolverAlquiler devolverAlquiler = (DevolverAlquiler) Controladores.get("vistas/DevolverAlquiler.fxml",
					"DEVOLVER ALQUILER", getEscenario());
			devolverAlquiler.getEscenario().showAndWait();
			LocalDate fechaDevolucion = devolverAlquiler.getFechaDevolucion();
			if (fechaDevolucion != null) {
				VistaGraficos.getInstancia().getControlador().devolver(getCliente(), fechaDevolucion);
				Dialogos.mostrarDialogoAdvertencia("DEVOLVER ALQUILER", "El alquiler ha sido devuelto correctamente.",
						getEscenario());
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("ERROR", e.getMessage(), getEscenario());
		}

	}

	@FXML
	void cambiarValores(Alquiler alquiler) {
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
			btDevolver.setDisable(false);
			tfCambiarFecDev.setText("Aún no está devuelto");
			tfPrecio.setText("---------------");
		} else {
			btDevolver.setDisable(false);
			tfCambiarFecDev.setText(String.format("%s", fechaDevolucion));
			tfPrecio.setText(String.format("%s", vehiculo.getFactorPrecio()));
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
			tfCambiarPma.setText("-----------");
			tfCambiarPlazas.setText("-----------");
		} else if (vehiculo instanceof Furgoneta furgoneta) {
			tfCambiarTipo.setText("Furgoneta");
			tfCambiarCilindrada.setText("-----------");
			tfCambiarPma.setText(String.format("%s", furgoneta.getPma()));
			tfCambiarPlazas.setText(String.format("%s", furgoneta.getPlazas()));
		} else if (vehiculo instanceof Autobus autobus) {
			tfCambiarTipo.setText("Autobus");
			tfCambiarCilindrada.setText("-----------");
			tfCambiarPma.setText("-----------");
			tfCambiarPlazas.setText(String.format("%s", autobus.getPlazas()));

		}
	}

	@FXML
	Cliente getCliente() {
		String dni = tfCambiarDni.getText();
		return VistaGraficos.getInstancia().getControlador().buscar(Cliente.getClienteConDni(dni));
	}

	@FXML
	void limpiar() {
		Controles.limpiarCamposTexto(tfCambiarNombre, tfCambiarDni, tfCambiarTelefono);
		Controles.limpiarCamposTexto(tfCambiarTipo, tfCambiarMarca, tfCambiarModelo, tfCambiarMatricula,
				tfCambiarCilindrada, tfCambiarPlazas, tfCambiarPma);
		Controles.limpiarCamposTexto(tfCambiarFecAlq, tfCambiarFecDev, tfPrecio);
	}

}
