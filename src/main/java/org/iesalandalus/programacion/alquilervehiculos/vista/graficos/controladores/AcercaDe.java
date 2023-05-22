package org.iesalandalus.programacion.alquilervehiculos.vista.graficos.controladores;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;

import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.recursos.LocalizadorRecursos;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controlador;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Dialogos;

import javafx.fxml.FXML;

public class AcercaDe extends Controlador {

	private static final String ERROR = "Ha ocurrido un error inesperado al abrir la página.";

	@FXML
	void abrirLink(ActionEvent event) {
		String link = "https://github.com/juanmi4000/AlquilerVehiculos-v3.git";
		try {
			Desktop deskpot = Desktop.getDesktop();
			deskpot.browse(java.net.URI.create(link));
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Error al abrir la URL", ERROR, getEscenario());
			e.printStackTrace();
		}
	}
	
	@FXML
    void abrirPdf(ActionEvent event) {
        try {
        	String rutaPDF = LocalizadorRecursos.class.getResource("archivos/bibliografia.pdf").toURI().getPath();
            File archivo = new File(rutaPDF);
            Desktop.getDesktop().open(archivo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}