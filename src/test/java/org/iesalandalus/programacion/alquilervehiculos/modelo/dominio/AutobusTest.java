package org.iesalandalus.programacion.alquilervehiculos.modelo.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AutobusTest {
	
	private static final String MENSAJE_ERROR_MARCA_NULA = "ERROR: La marca no puede ser nula.";
	private static final String MENSAJE_ERROR_FORMATO_MARCA_NO_VALIDA = "ERROR: La marca no tiene un formato válido.";
	private static final String MENSAJE_ERROR_MODELO_NULO = "ERROR: El modelo no puede ser nulo.";
	private static final String MENSAJE_ERROR_MODELO_BLANCO = "ERROR: El modelo no puede estar en blanco.";
	private static final String MENSAJE_ERROR_PLAZAS_NO_VALIDAS = "ERROR: Las plazas no son correctas.";
	private static final String MENSAJE_ERROR_MATRICULA_NULA = "ERROR: La matrícula no puede ser nula.";
	private static final String MENSAJE_ERROR_FORMATO_MATRICULA_NO_VALIDA = "ERROR: La matrícula no tiene un formato válido.";
	private static final String MENSAJE_ERROR_VEHICULO_NULO = "ERROR: No es posible copiar un vehículo nulo.";
	
	private static final String MARCA_VALIDA = "Mercedes-Benz";
	private static final String MODELO_VALIDO = "Citaro K";
	private static final int PLAZAS_VALIDAS = 60;
	private static final String MATRICULA_VALIDA = "1234BCD";
	
	private Autobus autobus;
	
	@BeforeEach
	void init() {
		autobus = new Autobus(MARCA_VALIDA, MODELO_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA);
	}

	@Test
	void constructorMarcaValidaModeloValidoPlazasValidasMatrivaValidaCreaAutobusCorrectamente() {
		assertEquals(MARCA_VALIDA, autobus.getMarca());
		assertEquals(MODELO_VALIDO, autobus.getModelo());
		assertEquals(PLAZAS_VALIDAS, autobus.getPlazas());
		assertEquals(MATRICULA_VALIDA, autobus.getMatricula());
		assertEquals(120, autobus.getFactorPrecio());
		autobus = new Autobus("Land Rover", MODELO_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA);
		autobus = new Autobus("KIA", MODELO_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA);
		autobus = new Autobus("Rolls-Royce", MODELO_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA);
		autobus = new Autobus("SsangYong", MODELO_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA);
	}
	
	@Test
	void constructorMarcaNoValidaModeloValidoPlazasValidasMatrivaValidaLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> new Autobus(null, MODELO_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_MARCA_NULA, npe.getMessage());
		IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> new Autobus("", MODELO_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_FORMATO_MARCA_NO_VALIDA, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Autobus("", MODELO_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_FORMATO_MARCA_NO_VALIDA, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Autobus("  ", MODELO_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_FORMATO_MARCA_NO_VALIDA, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Autobus("AA-BB", MODELO_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_FORMATO_MARCA_NO_VALIDA, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Autobus("aa", MODELO_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_FORMATO_MARCA_NO_VALIDA, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Autobus("aa bb", MODELO_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_FORMATO_MARCA_NO_VALIDA, iae.getMessage());
	}
	
	@Test
	void constructorMarcaValidaModeloNoValidoPlazasValidasMatrivaValidaLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> new Autobus(MARCA_VALIDA, null, PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_MODELO_NULO, npe.getMessage());
		IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> new Autobus(MARCA_VALIDA, "", PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_MODELO_BLANCO, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Autobus(MARCA_VALIDA, " ", PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_MODELO_BLANCO, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Autobus(MARCA_VALIDA, "	", PLAZAS_VALIDAS, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_MODELO_BLANCO, iae.getMessage());
	}
	
	@Test
	void constructorMarcaValidaModeloValidoPlazasNoValidaaMatrivaValidaLanzaExcepcion() {
		IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> new Autobus(MARCA_VALIDA, MODELO_VALIDO, 6, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_PLAZAS_NO_VALIDAS, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Autobus(MARCA_VALIDA, MODELO_VALIDO, 101, MATRICULA_VALIDA));
		assertEquals(MENSAJE_ERROR_PLAZAS_NO_VALIDAS, iae.getMessage());
	}
	
	@Test
	void constructorMarcaValidaModeloValidoPlazasValidasMatrivaNoValidaLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> new Autobus(MARCA_VALIDA, MODELO_VALIDO, PLAZAS_VALIDAS, null));
		assertEquals(MENSAJE_ERROR_MATRICULA_NULA, npe.getMessage());
		IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> new Autobus(MARCA_VALIDA, MODELO_VALIDO, PLAZAS_VALIDAS, "1234bcd"));
		assertEquals(MENSAJE_ERROR_FORMATO_MATRICULA_NO_VALIDA, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Autobus(MARCA_VALIDA, MODELO_VALIDO, PLAZAS_VALIDAS, "1234ABC"));
		assertEquals(MENSAJE_ERROR_FORMATO_MATRICULA_NO_VALIDA, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Autobus(MARCA_VALIDA, MODELO_VALIDO, PLAZAS_VALIDAS, "1234BC"));
		assertEquals(MENSAJE_ERROR_FORMATO_MATRICULA_NO_VALIDA, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Autobus(MARCA_VALIDA, MODELO_VALIDO, PLAZAS_VALIDAS, "234BCD"));
		assertEquals(MENSAJE_ERROR_FORMATO_MATRICULA_NO_VALIDA, iae.getMessage());
	}
	
	@Test
	void constructorAutobusValidoCopiaAutobusCorrectamente() {
		Autobus autobusCopia = new Autobus(autobus);
		assertEquals(autobus, autobusCopia);
		assertNotSame(autobus, autobusCopia);
		assertEquals(MARCA_VALIDA, autobusCopia.getMarca());
		assertEquals(MODELO_VALIDO, autobusCopia.getModelo());
		assertEquals(PLAZAS_VALIDAS, autobusCopia.getPlazas());
		assertEquals(MATRICULA_VALIDA, autobusCopia.getMatricula());
	}
	
	@Test
	void constructorAutobusNuloLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> new Autobus(null));
		assertEquals(MENSAJE_ERROR_VEHICULO_NULO, npe.getMessage());
	}
	
	
	@Test
	void equalsYHasCodeConsistentes() {
		Vehiculo autobusIgual = new Autobus(autobus);
		assertEquals(autobus, autobus);
		assertEquals(autobus, autobusIgual);
		assertEquals(autobusIgual, autobus);
		assertEquals(autobus.hashCode(), autobusIgual.hashCode());
		Vehiculo autobusDiferente = new Autobus(MARCA_VALIDA, MODELO_VALIDO, PLAZAS_VALIDAS, "1111BBB");
		assertNotEquals(autobus, "");
		assertNotEquals(autobus, autobusDiferente);
		assertNotEquals(autobus.hashCode(), autobusDiferente.hashCode());
		assertNotEquals(autobus, null);
	}
	
	@Test
	void toStringDevuelveLaCadenaEsperada() {
		assertEquals(String.format("%s %s (%d plazas) - %s", MARCA_VALIDA, MODELO_VALIDO, PLAZAS_VALIDAS, MATRICULA_VALIDA), autobus.toString());
	}

}
