package org.iesalandalus.programacion.alquilervehiculos.modelo.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FurgonetaTest {
	
	private static final String MENSAJE_ERROR_MARCA_NULA = "ERROR: La marca no puede ser nula.";
	private static final String MENSAJE_ERROR_FORMATO_MARCA_NO_VALIDA = "ERROR: La marca no tiene un formato válido.";
	private static final String MENSAJE_ERROR_MODELO_NULO = "ERROR: El modelo no puede ser nulo.";
	private static final String MENSAJE_ERROR_MODELO_BLANCO = "ERROR: El modelo no puede estar en blanco.";
	private static final String MENSAJE_ERROR_PMA_NO_VALIDO = "ERROR: El PMA no es correcto.";
	private static final String MENSAJE_ERROR_PLAZAS_NO_VALIDAS = "ERROR: Las plazas no son correctas.";
	private static final String MENSAJE_ERROR_MATRICULA_NULA = "ERROR: La matrícula no puede ser nula.";
	private static final String MENSAJE_ERROR_FORMATO_MATRICULA_NO_VALIDA = "ERROR: La matrícula no tiene un formato válido.";
	private static final String MENSAJE_ERROR_VEHICULO_NULO = "ERROR: No es posible copiar un vehículo nulo.";
	
	private static final String MARCA_VALIDA = "Mercedes-Benz";
	private static final String MODELO_VALIDO = "eSprinter";
	private static final int PMA_VALIDO = 7000;
	private static final int PLAZAS_VALIDAS = 2;
	private static final String MATRICULA_VALIDA = "1234BCD";
	
	private Furgoneta furgoneta;
	
	@BeforeEach
	void init() {
		furgoneta = new Furgoneta(MARCA_VALIDA, MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA);
	}

	@Test
	void constructorMarcaValidaModeloValidoPmaValidoPlazasValidasMatrivaValidaCreaFurgonetaCorrectamente() {
		assertEquals(MARCA_VALIDA, furgoneta.getMarca());
		assertEquals(MODELO_VALIDO, furgoneta.getModelo());
		assertEquals(PMA_VALIDO, furgoneta.getPma());
		assertEquals(PLAZAS_VALIDAS, furgoneta.getPlazas());
		assertEquals(MATRICULA_VALIDA, furgoneta.getMatricula());
		assertEquals(72, furgoneta.getFactorPrecio());
		furgoneta = new Furgoneta("Land Rover", MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA);
		furgoneta = new Furgoneta("KIA", MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA);
		furgoneta = new Furgoneta("Rolls-Royce", MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA);
		furgoneta = new Furgoneta("SsangYong", MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA);
	}
	
	@Test
	void constructorMarcaNoValidaModeloValidoPmaValidoPlazasValidasMatrivaValidaLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> new Furgoneta(null, MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_MARCA_NULA, npe.getMessage());
		IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> new Furgoneta("", MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_FORMATO_MARCA_NO_VALIDA, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Furgoneta("", MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_FORMATO_MARCA_NO_VALIDA, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Furgoneta("  ", MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_FORMATO_MARCA_NO_VALIDA, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Furgoneta("AA-BB", MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_FORMATO_MARCA_NO_VALIDA, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Furgoneta("aa", MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_FORMATO_MARCA_NO_VALIDA, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Furgoneta("aa bb", MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_FORMATO_MARCA_NO_VALIDA, iae.getMessage());
	}
	
	@Test
	void constructorMarcaValidaModeloNoValidoPmaValidoPlazasValidasMatrivaValidaLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> new Furgoneta(MARCA_VALIDA, null, PMA_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_MODELO_NULO, npe.getMessage());
		IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> new Furgoneta(MARCA_VALIDA, "", PMA_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_MODELO_BLANCO, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Furgoneta(MARCA_VALIDA, " ", PMA_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_MODELO_BLANCO, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Furgoneta(MARCA_VALIDA, "	", PMA_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_MODELO_BLANCO, iae.getMessage());
	}
	
	@Test
	void constructorMarcaValidaModeloValidoPmaNoValidoPlazasValidasMatrivaValidaLanzaExcepcion() {
		IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> new Furgoneta(MARCA_VALIDA, MODELO_VALIDO, 100, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_PMA_NO_VALIDO, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Furgoneta(MARCA_VALIDA, MODELO_VALIDO, 10001, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_PMA_NO_VALIDO, iae.getMessage());
	}
	
	@Test
	void constructorMarcaValidaModeloValidoPmaValidoPlazasNoValidasMatrivaValidaLanzaExcepcion() {
		IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> new Furgoneta(MARCA_VALIDA, MODELO_VALIDO, PMA_VALIDO, 1, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_PLAZAS_NO_VALIDAS, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Furgoneta(MARCA_VALIDA, MODELO_VALIDO, PMA_VALIDO, 10, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_PLAZAS_NO_VALIDAS, iae.getMessage());
	}
	
	@Test
	void constructorMarcaValidaModeloValidoPmaValidoPlazasValidasMatrivaNoValidaLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> new Furgoneta(MARCA_VALIDA, MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, null));
		assertEquals(MENSAJE_ERROR_MATRICULA_NULA, npe.getMessage());
		IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> new Furgoneta(MARCA_VALIDA, MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, "1234bcd"));
		assertEquals(MENSAJE_ERROR_FORMATO_MATRICULA_NO_VALIDA, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Furgoneta(MARCA_VALIDA, MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, "1234ABC"));
		assertEquals(MENSAJE_ERROR_FORMATO_MATRICULA_NO_VALIDA, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Furgoneta(MARCA_VALIDA, MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, "1234BC"));
		assertEquals(MENSAJE_ERROR_FORMATO_MATRICULA_NO_VALIDA, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Furgoneta(MARCA_VALIDA, MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, "234BCD"));
		assertEquals(MENSAJE_ERROR_FORMATO_MATRICULA_NO_VALIDA, iae.getMessage());
	}
	
	@Test
	void constructorFurgonetaValidaCopiaFurgonetaCorrectamente() {
		Furgoneta furgonetaCopia = new Furgoneta(furgoneta);
		assertEquals(furgoneta, furgonetaCopia);
		assertNotSame(furgoneta, furgonetaCopia);
		assertEquals(MARCA_VALIDA, furgonetaCopia.getMarca());
		assertEquals(MODELO_VALIDO, furgonetaCopia.getModelo());
		assertEquals(PMA_VALIDO, furgonetaCopia.getPma());
		assertEquals(PLAZAS_VALIDAS, furgonetaCopia.getPlazas());
		assertEquals(MATRICULA_VALIDA, furgonetaCopia.getMatricula());
	}
	
	@Test
	void constructoAFurgonetaNulaLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> new Furgoneta(null));
		assertEquals(MENSAJE_ERROR_VEHICULO_NULO, npe.getMessage());
	}
	
	
	@Test
	void equalsYHasCodeConsistentes() {
		Vehiculo furgoneaIgual = new Furgoneta(furgoneta);
		assertEquals(furgoneta, furgoneta);
		assertEquals(furgoneta, furgoneaIgual);
		assertEquals(furgoneaIgual, furgoneta);
		assertEquals(furgoneta.hashCode(), furgoneaIgual.hashCode());
		Vehiculo turismoDiferente = new Furgoneta(MARCA_VALIDA, MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, "1111BBB");
		assertNotEquals(furgoneta, "");
		assertNotEquals(furgoneta, turismoDiferente);
		assertNotEquals(furgoneta.hashCode(), turismoDiferente.hashCode());
		assertNotEquals(furgoneta, null);
	}
	
	@Test
	void toStringDevuelveLaCadenaEsperada() {
		assertEquals(String.format("%s %s (%d kg, %d plazas) - %s", MARCA_VALIDA, MODELO_VALIDO, PMA_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA), furgoneta.toString());
	}

}
