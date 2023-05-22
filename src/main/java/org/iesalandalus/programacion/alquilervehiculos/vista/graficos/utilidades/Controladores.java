package org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.recursos.LocalizadorRecursos;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controladores {

	private static final Map<String, Controlador> controladoresCreados = new HashMap<>();

	private Controladores() {
	}

	public static Controlador get(String vistaFxml, String titulo, Stage propietario) {
		return controladoresCreados.containsKey(vistaFxml) ? controladoresCreados.get(vistaFxml)
				: crear(vistaFxml, titulo, propietario);
	}

	private static Controlador crear(String vistaFxml, String titulo, Stage propietario) {
		Controlador controlador = null;
		try {
			FXMLLoader cargador = new FXMLLoader(LocalizadorRecursos.class.getResource(vistaFxml));
			Parent raiz = cargador.load();
			controlador = cargador.getController();
			Stage escenario = new Stage();
			controlador.setEscenario(escenario);
			escenario.initOwner(propietario);
			escenario.initModality(Modality.APPLICATION_MODAL);
			escenario.setTitle(titulo);
			escenario.setScene(new Scene(raiz));
			escenario.getIcons().add(new Image(LocalizadorRecursos.class.getResourceAsStream("imagenes/Coche.png")));
			escenario.getScene().getStylesheets().add(LocalizadorRecursos.class.getResource("estilos/escenarioPrincipal.css").toExternalForm());
			controladoresCreados.put(vistaFxml, controlador);
		} catch (IOException e) {
			System.out.println("Error al cargar: " + vistaFxml);
		}
		return controlador;
	}
}