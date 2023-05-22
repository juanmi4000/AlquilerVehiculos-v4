package org.iesalandalus.programacion.alquilervehiculos;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.controlador.Controlador;
import org.iesalandalus.programacion.alquilervehiculos.modelo.FactoriaFuenteDatos;
import org.iesalandalus.programacion.alquilervehiculos.modelo.Modelo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.ModeloCascada;
import org.iesalandalus.programacion.alquilervehiculos.vista.FactoriaVista;
import org.iesalandalus.programacion.alquilervehiculos.vista.Vista;

public class MainApp {

	public static void main(String[] args) throws OperationNotSupportedException {
		Modelo modelo = new ModeloCascada(FactoriaFuenteDatos.FICHEROS);
		Vista vista = procesarArgumentosVista(args);

		Controlador controlador = new Controlador(modelo, vista);

		controlador.comenzar();

	}
	
	private static Vista procesarArgumentosVista(String[] args) {
		
		Vista vista = FactoriaVista.GRAFICOS.crear();
		for (String argumentos : args) {
			if (argumentos.equals("-vgraficos")) {
				vista = FactoriaVista.GRAFICOS.crear();
			} else {
				vista = FactoriaVista.TEXTO.crear();
			}
		}
		
		return vista;
	}
}