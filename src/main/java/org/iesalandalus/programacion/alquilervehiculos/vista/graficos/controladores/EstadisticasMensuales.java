package org.iesalandalus.programacion.alquilervehiculos.vista.graficos.controladores;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Furgoneta;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.VistaGraficos;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class EstadisticasMensuales extends Controlador {

	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@FXML
	ObservableList<PieChart.Data> estadistica = FXCollections.observableArrayList();

	@FXML
	private Label lbFecha;

	@FXML
	private TextField tfFecha;

	@FXML
	private PieChart pcEstadisticasMensuales;

	@FXML
	private void initialize() {
		lbFecha.setText("");
	}

	@FXML
	void mostrarEstadisticas(ActionEvent event) {
		int contadorTurismo = 0;
		int contadorFurgoneta = 0;
		int contadorAutobus = 0;
		LocalDate fecha = LocalDate.parse("01/" + tfFecha.getText(), FORMATO_FECHA);
		for (Alquiler alquiler : VistaGraficos.getInstancia().getControlador().getAlquileres()) {
			if (alquiler.getFechaAlquiler().getMonthValue() == fecha.getMonthValue()
					&& alquiler.getFechaAlquiler().getYear() == fecha.getYear()) {
				if (alquiler.getVehiculo() instanceof Turismo) {
					contadorTurismo++;
				} else if (alquiler.getVehiculo() instanceof Furgoneta) {
					contadorFurgoneta++;
				} else {
					contadorAutobus++;
				}
			}
		}

		if (contadorAutobus == 0 && contadorFurgoneta == 0 && contadorTurismo == 0) {
			lbFecha.setText("En ese mes no hubo alquileres");
		} else {
			String mes = tfFecha.getText();
			lbFecha.setText("Las estadísticas del mes " + mes + " son:");
			PieChart.Data slTurismo = new PieChart.Data("Turismo", contadorTurismo);
			PieChart.Data slFurgoneta = new PieChart.Data("Furgoneta", contadorFurgoneta);
			PieChart.Data slAutobus = new PieChart.Data("Autobus", contadorAutobus);

			pcEstadisticasMensuales.getData().addAll(slTurismo, slFurgoneta, slAutobus);

			Tooltip tpTurismo = new Tooltip(String.format("Turismo: %s", contadorTurismo));
			Tooltip tpFurgoneta = new Tooltip(String.format("Furgoneta: %s", contadorFurgoneta));
			Tooltip tpAutobus = new Tooltip(String.format("Autobus: %s", contadorAutobus));

			Tooltip.install(slTurismo.getNode(), tpTurismo);
			Tooltip.install(slFurgoneta.getNode(), tpFurgoneta);
			Tooltip.install(slAutobus.getNode(), tpAutobus);
		}

		PieChart.Data slTurismo = new PieChart.Data("Turismo", contadorTurismo);
		PieChart.Data slFurgoneta = new PieChart.Data("Furgoneta", contadorFurgoneta);
		PieChart.Data slAutobus = new PieChart.Data("Autobus", contadorAutobus);

		pcEstadisticasMensuales.getData().addAll(slTurismo, slFurgoneta, slAutobus);

		Tooltip tpTurismo = new Tooltip(String.format("Turismo: %s", contadorTurismo));
		Tooltip tpFurgoneta = new Tooltip(String.format("Furgoneta: %s", contadorFurgoneta));
		Tooltip tpAutobus = new Tooltip(String.format("Autobus: %s", contadorAutobus));

		Tooltip.install(slTurismo.getNode(), tpTurismo);
		Tooltip.install(slFurgoneta.getNode(), tpFurgoneta);
		Tooltip.install(slAutobus.getNode(), tpAutobus);
	}

	@FXML
	void limpiar() {
		tfFecha.setText("");
	}
}
