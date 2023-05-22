package org.iesalandalus.programacion.alquilervehiculos.vista.graficos;

import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controlador;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controladores;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Dialogos;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LanzadorVentanaPrincipal extends Application {

	private static final String TITULO = "Alquileres de Vehículos";

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			
			Controlador ventanaPrincipal = Controladores.get("vistas/VentanaPrincipal.fxml", TITULO, null);
			ventanaPrincipal.getEscenario().setOnCloseRequest(e -> confirmarSalida(ventanaPrincipal.getEscenario(), e));
			//Dialogos.setHojaEstilos(ventanaPrincipal).getEscenario().getScene().getRoot().getStylesheets().get(0);
			ventanaPrincipal.getEscenario().show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void comenzar() {
		launch(LanzadorVentanaPrincipal.class); // crea una instancia de la clase que le estamos pasando
	}

	private void confirmarSalida(Stage escenario, WindowEvent e) {
		if (Dialogos.mostrarDialogoConfirmacion("Salir", "¿Estás seguro de que quieres salir de la aplicación?",
				escenario)) {
			escenario.close();
		} else {
			e.consume();
		}
	}

}
