package org.iesalandalus.programacion.alquilervehiculos.modelo.dominio;

public class Furgoneta extends Vehiculo {
	private static final int FACTOR_PMA = 100;
	private static final int FACTOR_PLAZAS = 1;
	private int pma;
	private int plazas;

	public Furgoneta(String marca, String modelo, int pma, int plazas, String matricula) {
		super(marca, modelo, matricula);
		setPma(pma);
		setPlazas(plazas);
	}

	public Furgoneta(Furgoneta furgoneta) {
		super(furgoneta);
		pma = furgoneta.getPma();
		plazas = furgoneta.getPlazas();
	}

	public int getPma() {
		return pma;
	}

	private void setPma(int pma) {
		if (pma < 101 || pma > 10000) {
			throw new IllegalArgumentException("ERROR: El PMA no es correcto.");
		}
		this.pma = pma;
	}

	public int getPlazas() {
		return plazas;
	}

	private void setPlazas(int plazas) {
		if (plazas < 2 || plazas > 9) {
			throw new IllegalArgumentException("ERROR: Las plazas no son correctas.");
		}
		this.plazas = plazas;
	}

	@Override
	public int getFactorPrecio() {
		return pma / FACTOR_PMA + plazas * FACTOR_PLAZAS;
	}

	@Override
	public String toString() {
		return String.format("%s %s (%d kg, %d plazas) - %s", getMarca(), getModelo(), getPma(), getPlazas(),
				getMatricula());
	}
}
