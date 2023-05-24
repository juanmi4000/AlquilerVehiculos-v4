package org.iesalandalus.programacion.alquilervehiculos.vista.graficos.controladores;

import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NombrePdf extends Controlador{

    @FXML
    private TextField tfNombrePdf;
    
    private boolean cancelado;

    @FXML
    void cancelar(ActionEvent event) {
    	cancelado = true;
    	getEscenario().close();
    }

    @FXML
    void guardar(ActionEvent event) {
    	cancelado = false;
    	getEscenario().close();
    }
    
    @FXML
    void limpiar() {
    	tfNombrePdf.setText("");
    	cancelado = true;
    }
    
    @FXML
    String getNombrePdf() {
    	return cancelado ? "" : tfNombrePdf.getText();
    }
}