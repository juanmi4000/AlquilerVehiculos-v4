package org.iesalandalus.programacion.alquilervehiculos.vista;

import org.iesalandalus.programacion.alquilervehiculos.vista.graficos.VistaGraficos;
import org.iesalandalus.programacion.alquilervehiculos.vista.texto.VistaTexto;

public enum FactoriaVista {
	TEXTO {
		@Override
		public Vista crear() {
			return new VistaTexto();
		}
	},
	
	GRAFICOS {

		@Override
		public Vista crear() {
			return VistaGraficos.getInstancia();
		}
		
	}
	;

	public abstract Vista crear();
}
