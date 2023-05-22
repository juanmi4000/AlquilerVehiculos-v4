package org.iesalandalus.programacion.alquilervehiculos.vista;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.controlador.Controlador;

public abstract class Vista {
	private Controlador controlador;

	public Controlador getControlador() {
		return controlador;
	}

	public void setControlador(Controlador controlador) {
		if (controlador != null) {
			this.controlador = controlador;
		}
	}

	public abstract void comenzar() throws OperationNotSupportedException;

	public abstract void terminar();
}
