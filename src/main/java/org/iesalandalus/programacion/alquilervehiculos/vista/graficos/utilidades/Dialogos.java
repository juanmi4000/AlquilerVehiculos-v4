package org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Dialogos {

	private static final String ID_BT_ACEPTAR = "btAceptar";
	private static final String ID_BT_CANCELAR = "btCancelar";
	private static String css;

	private Dialogos() {
	}

	public static void setHojaEstilos(String url) {
		if (url == null) {
			throw new NullPointerException();
		}
		css = url;
	}

	private static void setHojaEstilos(Dialog<?> dialogo) {
		if (css != null) {
			dialogo.getDialogPane().getStylesheets().add(css);
		}
	}

	public static void mostrarDialogoError(String titulo, String contenido, Stage propietario) {
		Alert dialogo = new Alert(AlertType.ERROR);
		setHojaEstilos(dialogo);
		((Button) dialogo.getDialogPane().lookupButton(ButtonType.OK)).setId(ID_BT_ACEPTAR);
		dialogo.setTitle(titulo);
		dialogo.setHeaderText(null);
		dialogo.setContentText(contenido);
		dialogo.initModality(Modality.APPLICATION_MODAL);
		dialogo.initOwner(propietario);
		dialogo.showAndWait();
	}

	public static void mostrarDialogoInformacion(String titulo, String contenido, Stage propietario) {
		Alert dialogo = new Alert(AlertType.INFORMATION);
		setHojaEstilos(dialogo);
		((Button) dialogo.getDialogPane().lookupButton(ButtonType.OK)).setId(ID_BT_ACEPTAR);
		dialogo.setTitle(titulo);
		dialogo.setHeaderText(null);
		dialogo.setContentText(contenido);
		dialogo.initModality(Modality.APPLICATION_MODAL);
		dialogo.initOwner(propietario);
		dialogo.showAndWait();
	}

	public static void mostrarDialogoAdvertencia(String titulo, String contenido, Stage propietario) {
		Alert dialogo = new Alert(AlertType.WARNING);
		setHojaEstilos(dialogo);
		((Button) dialogo.getDialogPane().lookupButton(ButtonType.OK)).setId(ID_BT_ACEPTAR);
		dialogo.setTitle(titulo);
		dialogo.setHeaderText(null);
		dialogo.setContentText(contenido);
		dialogo.initModality(Modality.APPLICATION_MODAL);
		dialogo.initOwner(propietario);
		dialogo.showAndWait();
	}

	public static boolean mostrarDialogoConfirmacion(String titulo, String contenido, Stage propietario) {
		Alert dialogo = new Alert(AlertType.CONFIRMATION);
		setHojaEstilos(dialogo);
		((Button) dialogo.getDialogPane().lookupButton(ButtonType.OK)).setId(ID_BT_ACEPTAR);
		((Button) dialogo.getDialogPane().lookupButton(ButtonType.CANCEL)).setId(ID_BT_CANCELAR);
		dialogo.setTitle(titulo);
		dialogo.setHeaderText(null);
		dialogo.setContentText(contenido);
		dialogo.initModality(Modality.APPLICATION_MODAL);
		dialogo.initOwner(propietario);

		Optional<ButtonType> respuesta = dialogo.showAndWait();
		return (respuesta.isPresent() && respuesta.get() == ButtonType.OK);
	}

	public static String mostrarDialogoTexto(String titulo, String contenido, Stage propietario) {
		TextInputDialog dialogo = new TextInputDialog();
		dialogo.setGraphic(null);
		setHojaEstilos(dialogo);
		((Button) dialogo.getDialogPane().lookupButton(ButtonType.OK)).setId(ID_BT_ACEPTAR);
		((Button) dialogo.getDialogPane().lookupButton(ButtonType.CANCEL)).setId(ID_BT_CANCELAR);
		dialogo.setTitle(titulo);
		dialogo.setHeaderText(null);
		dialogo.setContentText(contenido);
		dialogo.initModality(Modality.APPLICATION_MODAL);
		dialogo.initOwner(propietario);

		Optional<String> respuesta = dialogo.showAndWait();
		return (respuesta.isPresent() ? respuesta.get() : null);
	}

}
