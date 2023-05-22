package org.iesalandalus.programacion.alquilervehiculos.modelo.dominio;

public class Autobus extends Vehiculo {
	private static final int FACTOR_PLAZAS = 2;
	private int plazas;

	public Autobus(String marca, String modelo, int plazas, String matricula) {
		super(marca, modelo, matricula);
		setPlazas(plazas);
	}

	public Autobus(Autobus autobus) {
		super(autobus);
		plazas = autobus.getPlazas();
	}

	public int getPlazas() {
		return plazas;
	}

	private void setPlazas(int plazas) {
		if (plazas <= 9 || plazas >= 100) {
			throw new IllegalArgumentException("ERROR: Las plazas no son correctas.");
		}
		this.plazas = plazas;
	}

	@Override
	public int getFactorPrecio() {
		return plazas * FACTOR_PLAZAS;
	}

	@Override
	public String toString() {
		return String.format("%s %s (%d plazas) - %s", getMarca(), getModelo(), plazas, getMatricula());
	}

}
