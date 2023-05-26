package org.iesalandalus.programacion.alquilervehiculos.vista.graficos.controladores;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.VistaGraficos;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controlador;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controladores;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Controles.FormateadorCeldaFecha;
import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.utilidades.Dialogos;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class ListarAlquileresClienVehi extends Controlador{
	
	private static final String LISTAR_VEHICULO = "Listar alquiler de un vehículo";

	private static final String LISTAR_CLIENTE = "Listar alquileres de un cliente";

	private static final String CLIENTE = "Cliente";

	private static final String VEHÍCULO = "Vehículo";

	private static final String FECHA_ALQUILER = "Fecha Alquiler";

	private static final String FECHA_DEVOLUCIÓN = "Fecha Devolución";

	private static final String PRECIO = "Precio";
	
	private static final ObservableList<String> OPCIONES = FXCollections.observableArrayList(LISTAR_CLIENTE, LISTAR_VEHICULO);

    @FXML
    private Button btListar;

    @FXML
    private ChoiceBox<String> cbClienteVehiculo;

    @FXML
    private TableColumn<Alquiler, String> tcCliente;

    @FXML
    private TableColumn<Alquiler, LocalDate> tcFechaAlquiler;

    @FXML
    private TableColumn<Alquiler, LocalDate> tcFechaDevolucion;

    @FXML
    private TableColumn<Alquiler, String> tcPrecio;

    @FXML
    private TableColumn<Alquiler, String> tcVehiculo;

    @FXML
    private TextField tfMatricula;

    @FXML
    private TextField tfDni;

    @FXML
    private TableView<Alquiler> tvListarAlquileresCliente;
    
	@FXML
	private void initialize() {
		cbClienteVehiculo.getSelectionModel().select("Elige una opcion:");
		cbClienteVehiculo.setItems(OPCIONES);
		cbClienteVehiculo.valueProperty().addListener((ob, ol, ne) -> comprobarValor(ne));
		tfDni.setDisable(true);
		tfMatricula.setDisable(true);
		btListar.setDisable(true);
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
	void acercaDe(ActionEvent event) {
		AcercaDe acercaDe = (AcercaDe) Controladores.get("vistas/AcercaDe.fxml", "Acerca de", getEscenario());
		acercaDe.getEscenario().showAndWait();
	}

	@FXML
	void estadisticasAnuales(ActionEvent event) {
		EstadisticasAnuales estadisticasAnuales = (EstadisticasAnuales) Controladores
				.get("vistas/EstadisticasAnuales.fxml", "ESTADISTICAS ANUALES", getEscenario());
		estadisticasAnuales.limpiar();
		estadisticasAnuales.getEscenario().showAndWait();
	}

	@FXML
	void estadisticasMensuales(ActionEvent event) {
		EstadisticasMensuales estadisticasMensuales = (EstadisticasMensuales) Controladores
				.get("vistas/EstadisticasMensuales.fxml", "ESTADISTICAS MENSUALES", getEscenario());
		estadisticasMensuales.limpiar();
		estadisticasMensuales.getEscenario().showAndWait();
	}

	@FXML
	void generarPdf(ActionEvent event) {

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
				// le decimos el
				
				//creo una instancia de tipo font para añadirle una fuente
				Font fuenteNegrita = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
				PdfWriter.getInstance(documento, new FileOutputStream(ruta));
				// abrimos el documento
				documento.open();
				// Me crea un texto
				Paragraph texto = new Paragraph("Los alquileres para el cliente/vehículo", fuenteNegrita);
				// Me lo alinea al centro
				texto.setAlignment(Element.ALIGN_CENTER);
				// Lo añade
				documento.add(texto);

				Paragraph salto = new Paragraph("-------------------------------");
				salto.setAlignment(Element.ALIGN_CENTER);
				documento.add(salto);
				
				Paragraph salto1 = new Paragraph("  ");
				salto.setAlignment(Element.ALIGN_CENTER);
				documento.add(salto1);
				
				
				// LocalizadorRecursos.class.getClasses().getClass()
				// al genera la tabla le decimos la cantidad columnas que va a tener
				PdfPTable tabla = new PdfPTable(5);
				// De esta manera se crea una celda
				tabla.addCell(CLIENTE);
				tabla.addCell(VEHÍCULO);
				tabla.addCell(FECHA_ALQUILER);
				tabla.addCell(FECHA_DEVOLUCIÓN);
				tabla.addCell(PRECIO);
				
				String dni = tfDni.getText();
				String matricula = tfMatricula.getText();
				
				if (!dni.isBlank()) {
					Cliente cliente = VistaGraficos.getInstancia().getControlador().buscar(Cliente.getClienteConDni(dni));
					for (Alquiler alquiler : VistaGraficos.getInstancia().getControlador().getAlquileres(cliente)) {
						anadirDatos(tabla, alquiler);
					}
				} else {
					Vehiculo vehiculo = VistaGraficos.getInstancia().getControlador().buscar(Vehiculo.getVehiculoConMatricula(matricula));
					for (Alquiler alquiler : VistaGraficos.getInstancia().getControlador().getAlquileres(vehiculo)) {
						anadirDatos(tabla, alquiler);
					}
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
				
				Dialogos.mostrarDialogoAdvertencia("Guardardo", String.format("El PDF de ha guardado en: %s", ruta), getEscenario());
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("ERROR", "Ha ocurrido un error al crear el pdf.", getEscenario());
			System.exit(-1);
		}
	}

    @FXML
    void listar(ActionEvent event) {
		String dni = tfDni.getText();
		String matricula = tfMatricula.getText();
		try {
			if (!dni.isBlank()) {
				Cliente cliente = VistaGraficos.getInstancia().getControlador().buscar(Cliente.getClienteConDni(dni));
				for (Alquiler alquiler : VistaGraficos.getInstancia().getControlador().getAlquileres(cliente)) {
					tvListarAlquileresCliente.getItems().add(alquiler);
				}
			} else {
				Vehiculo vehiculo = VistaGraficos.getInstancia().getControlador().buscar(Vehiculo.getVehiculoConMatricula(matricula));
				for (Alquiler alquiler : VistaGraficos.getInstancia().getControlador().getAlquileres(vehiculo)) {
					tvListarAlquileresCliente.getItems().add(alquiler);
				}
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("ERROR", e.getMessage(), getEscenario());
		}
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
	void limpiar() {
		tfDni.setText("");
		tfMatricula.setText("");
	}
    
    private void comprobarValor(String ne) {
		if (ne.equals(LISTAR_CLIENTE)) {
			tfDni.setDisable(false);
			tfMatricula.setDisable(true);
		} else {
			tfMatricula.setDisable(false);
			tfDni.setDisable(true);
		}
		btListar.setDisable(false);
	}
    
    void anadirDatos(PdfPTable tabla, Alquiler alquiler) {
		tabla.addCell(alquiler.getCliente().getDni());
		tabla.addCell(alquiler.getVehiculo().getMatricula());
		tabla.addCell(String.format("%s", alquiler.getFechaAlquiler()));
		tabla.addCell(devolverFechaDevo(alquiler.getFechaDevolucion()));
		tabla.addCell(alquiler.getFechaDevolucion() == null ? "" : String.format("%s", alquiler.getPrecio()));
	}
    
    @FXML
	private String cambiarPrecio(Alquiler alquiler) {
		return alquiler.getFechaDevolucion() == null ? "" : String.format("%s", alquiler.getPrecio());
	}

	private String devolverFechaDevo(LocalDate fechaDevolucion) {
		return fechaDevolucion == null ? "Aún no está devuelto" : String.format("%s", fechaDevolucion);
	}

}