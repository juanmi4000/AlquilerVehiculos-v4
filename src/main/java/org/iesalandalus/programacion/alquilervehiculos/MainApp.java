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
		Modelo modelo = new ModeloCascada(procesarArgumentosModelo(args));
		Vista vista = procesarArgumentosVista(args);

		Controlador controlador = new Controlador(modelo, vista);

		controlador.comenzar();

	}
	
	private static Vista procesarArgumentosVista(String[] args) {
		Vista vista = FactoriaVista.GRAFICOS.crear();
		for (String argumentos : args) {
			if (argumentos.equalsIgnoreCase("-vgraficos")) {
				vista = FactoriaVista.GRAFICOS.crear();
			} else if (argumentos.equalsIgnoreCase("-vtexto")) {
				vista = FactoriaVista.TEXTO.crear();
			}
		}
		
		return vista;
	}
	
	private static FactoriaFuenteDatos procesarArgumentosModelo(String[] args) {
		FactoriaFuenteDatos factoriaFuenteDatos = FactoriaFuenteDatos.MONGODB;
		for (String argumentos : args) {
			if (argumentos.equalsIgnoreCase("-fdficheros")) {
				factoriaFuenteDatos = FactoriaFuenteDatos.FICHEROS;
			} else if (argumentos.equalsIgnoreCase("-fdmariadb")) {
				factoriaFuenteDatos = FactoriaFuenteDatos.MARIADB;
			} else if (argumentos.equalsIgnoreCase("-fdmongodb")) {
				factoriaFuenteDatos = FactoriaFuenteDatos.MONGODB;
			}
		}
		return factoriaFuenteDatos;
	}
}