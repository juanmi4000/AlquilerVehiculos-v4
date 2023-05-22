package org.iesalandalus.programacion.alquilervehiculos.vista.graficos;

import org.iesalandalus.programacion.alquilervehiculos.vista.Vista;

public class VistaGraficos extends Vista {
	private static VistaGraficos instancia;

	private VistaGraficos() {

	}

	public static VistaGraficos getInstancia() {
		if (instancia == null) {
			instancia = new VistaGraficos();
		}
		return instancia;
	}

	@Override
	public void comenzar() {
		LanzadorVentanaPrincipal.comenzar();
		getControlador().terminar();
	}

	@Override
	public void terminar() {
		getControlador().terminar();
		System.out.println("!!!!Hasta luego Lucas!!!!");
	}

}
