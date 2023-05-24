package org.iesalandalus.programacion.alquilervehiculos.vista.graficos.controladores;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.VistaGraficos;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controlador;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controladores;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controles.FormateadorCeldaFecha;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Dialogos;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

public class ListarAlquileresClienVehi extends Controlador {

	private static final String CLIENTE = "Cliente";

	private static final String VEHÍCULO = "Vehículo";

	private static final String FECHA_ALQUILER = "Fecha Alquiler";

	private static final String FECHA_DEVOLUCIÓN = "Fecha Devolución";

	private static final String PRECIO = "Precio";

	@FXML
	private TableColumn<Alquiler, String> tcCliente;

	@FXML
	private TableColumn<Alquiler, LocalDate> tcFechaAlquiler;

	@FXML
	private TableColumn<Alquiler, LocalDate> tcFechaDevolucion;

	@FXML
	private TableColumn<Alquiler, String> tcVehiculo;

	@FXML
	private TableColumn<Alquiler, String> tcPrecio;

	@FXML
	private TableView<Alquiler> tvListarAlquileresCliente;

	@FXML
	private void initialize() {
		tcCliente.setCellValueFactory(fila -> new SimpleStringProperty(fila.getValue().getCliente().getDni()));
		tcVehiculo.setCellValueFactory(fila -> new SimpleStringProperty(fila.getValue().getVehiculo().getMatricula()));
		tcFechaAlquiler
				.setCellValueFactory(fila -> new SimpleObjectProperty<LocalDate>(fila.getValue().getFechaAlquiler()));
		tcFechaAlquiler.setCellFactory(columna -> new FormateadorCeldaFecha());
		tcFechaDevolucion
				.setCellValueFactory(fila -> new SimpleObjectProperty<LocalDate>(fila.getValue().getFechaDevolucion()));
		tcFechaDevolucion.setCellFactory(columna -> new FormateadorCeldaFecha());
		tcPrecio.setCellValueFactory(fila -> new SimpleStringProperty(cambiarPrecio(fila.getValue())));
	}

	@FXML
	void actualizar(List<Alquiler> alquileres) {
		tvListarAlquileresCliente.setItems(FXCollections.observableArrayList(alquileres));
	}

	@FXML
	private String cambiarPrecio(Alquiler alquiler) {
		return alquiler.getFechaDevolucion() == null ? "" : String.format("%s", alquiler.getPrecio());
	}

	@FXML
	void salir(ActionEvent event) {
		if (Dialogos.mostrarDialogoConfirmacion("SALIR", "¿Está seguro que desea salir?", getEscenario())) {
			getEscenario().close();
		} else {
			event.consume();
		}
	}

	@FXML
	void generarPdf(ActionEvent event) {
		NombrePdf nombrePdf = (NombrePdf) Controladores.get("vistas/NombrePdf.fxml", "Nombre del pdf", getEscenario());
		nombrePdf.limpiar();
		nombrePdf.getEscenario().showAndWait();

		String nombre = nombrePdf.getNombrePdf();

		Document documento = new Document();

		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Guardar Archivo");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));

			File archivo = fileChooser.showSaveDialog(this.getEscenario());
			if (archivo != null) {
				String ruta = archivo.getAbsolutePath();
				System.out.println("Ruta de archivo seleccionada: " + ruta);
				// Aquí puedes continuar con la generación del archivo PDF en la ruta
				// seleccionada
			}

			// le decimos el
			PdfWriter.getInstance(documento, new FileOutputStream(String.format("%s.pdf", nombre)));
			// abrimos el documento
			documento.open();
			documento.addHeader("Alquileres para un cliente", "Tabla para los alquiler de un cliente/vehículo");
			// Me crea un texto
			Paragraph texto = new Paragraph("Los alquileres para el cliente/vehiculo:");
			// Me lo alinea al centro
			texto.setAlignment(Element.ALIGN_CENTER);
			// Lo añade
			documento.add(texto);

			Paragraph salto = new Paragraph("");
			documento.add(salto);
			//LocalizadorRecursos.class.getClasses().getClass()
			// al genera la tabla le decimos la cantidad columnas que va a tener
			PdfPTable tabla = new PdfPTable(5);
			// De esta manera se crea una celda
			tabla.addCell(CLIENTE);
			tabla.addCell(VEHÍCULO);
			tabla.addCell(FECHA_ALQUILER);
			tabla.addCell(FECHA_DEVOLUCIÓN);
			tabla.addCell(PRECIO);

			for (Alquiler alquiler : VistaGraficos.getInstancia().getControlador().getAlquileres()) {
				tabla.addCell(alquiler.getCliente().getDni());
				tabla.addCell(alquiler.getVehiculo().getMatricula());
				tabla.addCell(String.format("%s", alquiler.getFechaAlquiler()));
				tabla.addCell(devolverFechaDevo(alquiler.getFechaDevolucion()));
				tabla.addCell(alquiler.getFechaDevolucion() == null ? "" : String.format("%s", alquiler.getPrecio()));
			}

			/*
			 * Esto es para cuando queremos que una fila pille más de una columna // Si
			 * desea crear una celda de mas de una columna // Cree un objecto Cell y cambie
			 * su propiedad span
			 * 
			 * PdfPCell celdaFinal = new PdfPCell(new Paragraph("Final de la tabla"));
			 * 
			 * // Indicamos cuantas columnas ocupa la celda celdaFinal.setColspan(3);
			 * table.addCell(celdaFinal);
			 */

			documento.add(tabla);

			documento.close();

		} catch (Exception e) {
			Dialogos.mostrarDialogoError("ERROR", "Ha ocurrido un error al crear el pdf.", getEscenario());
			System.exit(-1);
		}

	}

	private String devolverFechaDevo(LocalDate fechaDevolucion) {
		return fechaDevolucion == null ? "Aún no está devuelto" : String.format("%s", fechaDevolucion);
	}
}
