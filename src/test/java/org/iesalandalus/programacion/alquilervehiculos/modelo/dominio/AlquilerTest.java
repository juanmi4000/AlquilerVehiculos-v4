package org.iesalandalus.programacion.alquilervehiculos.modelo.dominio;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import javax.naming.OperationNotSupportedException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AlquilerTest {

	private static final String MENSAJE_ERROR_CLIENTE_NULO = "ERROR: El cliente no puede ser nulo.";
	private static final String MENSAJE_ERROR_TURISMO_NULO = "ERROR: El vehículo no puede ser nulo.";
	private static final String MENSAJE_ERROR_FECHA_ALQUILER_NULA = "ERROR: La fecha de alquiler no puede ser nula.";
	private static final String MENSAJE_ERROR_FECHA_ALQUILER_FUTURA = "ERROR: La fecha de alquiler no puede ser futura.";
	private static final String MENSAJE_ERROR_FECHA_DEVOLUCION_NULA = "ERROR: La fecha de devolución no puede ser nula.";
	private static final String MENSAJE_ERROR_FECHA_DEVOLUCION_FUTURA = "ERROR: La fecha de devolución no puede ser futura.";
	private static final String MENSAJE_ERROR_FECHA_DEVOLUCION_ANTERIOR_PRESTAMO = "ERROR: La fecha de devolución debe ser posterior a la fecha de alquiler.";
	private static final String MENSAJE_ERROR_FECHA_DEVOLUCION_YA_REGISTRADA = "ERROR: La devolución ya estaba registrada.";
	private static final String MENSAJE_ERROR_ALQUILER_NULO = "ERROR: No es posible copiar un alquiler nulo.";

	private static Cliente cliente;
	private static Turismo turismo;
	private static LocalDate hoy;
	private static LocalDate ayer;
	private static LocalDate manana;
	private static LocalDate semanaPasada;

	private Alquiler alquilerHoy;
	private Alquiler alquilerAyer;
	private Alquiler alquilerSemanaPasada;

	@BeforeAll
	static void setup() {
		hoy = LocalDate.now();
		ayer = hoy.minusDays(1);
		manana = hoy.plusDays(1);
		semanaPasada = hoy.minusDays(7);
	}

	@BeforeEach
	void init() {
		creaComportamientoTurismo();
		creaComportamientoCliente();
	}
	
	private void creaComportamientoTurismo() {
		turismo = mock();
		when(turismo.getMarca()).thenReturn("Seat");
		when(turismo.getModelo()).thenReturn("León");
		when(turismo.getMatricula()).thenReturn("1234BCD");
		when(turismo.getCilindrada()).thenReturn(90);
		when(turismo.getFactorPrecio()).thenReturn(9);
	}
	
	private void setComportamiento(Vehiculo turismo, String matricula) {
		when(turismo.getMatricula()).thenReturn(matricula);
	}
	
	private void creaComportamientoCliente() {
		cliente = mock();
		when(cliente.getNombre()).thenReturn("Bob Esponja");
		when(cliente.getDni()).thenReturn("11223344B");
		when(cliente.getTelefono()).thenReturn("950112233");
	}
	
	private void setComportamiento(Cliente cliente, String dni) {
		when(cliente.getDni()).thenReturn(dni);
	}
	
	@Test
	void constructorClienteValidoVehiculoValidoFechaAlquilerValidaCreaAlquilerCorrectamente() {
		alquilerHoy = new Alquiler(cliente, turismo, hoy);
		assertSame(cliente, alquilerHoy.getCliente());
		assertEquals(turismo, alquilerHoy.getVehiculo());
		assertSame(turismo, alquilerHoy.getVehiculo());
		assertEquals(hoy, alquilerHoy.getFechaAlquiler());
		assertNull(alquilerHoy.getFechaDevolucion());
		assertEquals(0, alquilerHoy.getPrecio());
		alquilerSemanaPasada = new Alquiler(cliente, turismo, semanaPasada);
		assertEquals(semanaPasada, alquilerSemanaPasada.getFechaAlquiler());
	}

	@Test
	void constructorClienteNoValidoVehiculoValidoFechaAlquilerValidaLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> new Alquiler(null, turismo, hoy));
		assertEquals(MENSAJE_ERROR_CLIENTE_NULO, npe.getMessage());
	}

	@Test
	void constructorClienteValidoVehiculoNoValidoFechaAlquilerValidaLanzaExcepcione() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> new Alquiler(cliente, null, hoy));
		assertEquals(MENSAJE_ERROR_TURISMO_NULO, npe.getMessage());
	}

	@Test
	void constructorClienteValidoVechiuloValidoFechaAlquilerNoValidaLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> new Alquiler(cliente, turismo, null));
		assertEquals(MENSAJE_ERROR_FECHA_ALQUILER_NULA, npe.getMessage());
		IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,	() -> new Alquiler(cliente, turismo, manana));
		assertEquals(MENSAJE_ERROR_FECHA_ALQUILER_FUTURA, iae.getMessage());
	}

	@Test
	void constructorAlquilerValidoCopiaAlquilerCorrectamente() {
		alquilerAyer = new Alquiler(cliente, turismo, ayer);
		assertDoesNotThrow(() -> alquilerAyer.devolver(hoy));
		Alquiler alquilerCopia = new Alquiler(alquilerAyer);
		assertNotSame(cliente, alquilerCopia.getCliente());
		assertNotSame(turismo, alquilerCopia.getVehiculo());
		assertEquals(ayer, alquilerCopia.getFechaAlquiler());
		assertEquals(hoy, alquilerCopia.getFechaDevolucion());
		Furgoneta furgoneta = mock();
		alquilerAyer = new Alquiler(cliente, furgoneta, ayer);
		assertDoesNotThrow(() -> alquilerAyer.devolver(hoy));
		alquilerCopia = new Alquiler(alquilerAyer);
		assertNotSame(cliente, alquilerCopia.getCliente());
		assertNotSame(furgoneta, alquilerCopia.getVehiculo());
		assertEquals(ayer, alquilerCopia.getFechaAlquiler());
		assertEquals(hoy, alquilerCopia.getFechaDevolucion());
		Autobus autobus = mock();
		alquilerAyer = new Alquiler(cliente, autobus, ayer);
		assertDoesNotThrow(() -> alquilerAyer.devolver(hoy));
		alquilerCopia = new Alquiler(alquilerAyer);
		assertNotSame(cliente, alquilerCopia.getCliente());
		assertNotSame(autobus, alquilerCopia.getVehiculo());
		assertEquals(ayer, alquilerCopia.getFechaAlquiler());
		assertEquals(hoy, alquilerCopia.getFechaDevolucion());
	}

	@Test
	void constructorAlquilerNoValidoLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> new Alquiler(null));
		assertEquals(MENSAJE_ERROR_ALQUILER_NULO, npe.getMessage());
	}

	@Test
	void devolverFechaValidaRealizaCorrectamenteDevolucion() {
		alquilerSemanaPasada = new Alquiler(cliente, turismo, semanaPasada);
		assertDoesNotThrow(() -> alquilerSemanaPasada.devolver(hoy));
		assertEquals(hoy, alquilerSemanaPasada.getFechaDevolucion());
	}

	@Test
	void devolverFechaValidaDevolucionYaRegistradaLanzaExcepcion() {
		alquilerSemanaPasada = new Alquiler(cliente, turismo, semanaPasada);
		assertDoesNotThrow(() -> alquilerSemanaPasada.devolver(hoy));
		OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> alquilerSemanaPasada.devolver(hoy));
		assertEquals(MENSAJE_ERROR_FECHA_DEVOLUCION_YA_REGISTRADA, onse.getMessage());
	}

	@Test
	void devolverFechaNoValidaLanzaExcepcion() {
		alquilerHoy = new Alquiler(cliente, turismo, hoy);
		NullPointerException npe = assertThrows(NullPointerException.class, () -> alquilerHoy.devolver(null));
		assertEquals(MENSAJE_ERROR_FECHA_DEVOLUCION_NULA, npe.getMessage());
		IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,	() -> alquilerHoy.devolver(manana));
		assertEquals(MENSAJE_ERROR_FECHA_DEVOLUCION_FUTURA, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> alquilerHoy.devolver(semanaPasada));
		assertEquals(MENSAJE_ERROR_FECHA_DEVOLUCION_ANTERIOR_PRESTAMO, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> alquilerHoy.devolver(hoy));
		assertEquals(MENSAJE_ERROR_FECHA_DEVOLUCION_ANTERIOR_PRESTAMO, iae.getMessage());
	}

	@Test
	void getPrecioDevuelvePrecioCorrecto() {
		alquilerAyer = new Alquiler(cliente, turismo, ayer);
		assertDoesNotThrow(() -> alquilerAyer.devolver(hoy));
		assertEquals(29, alquilerAyer.getPrecio());
		alquilerSemanaPasada = new Alquiler(cliente, turismo, semanaPasada);
		assertDoesNotThrow(() -> alquilerSemanaPasada.devolver(ayer));
		assertEquals(174, alquilerSemanaPasada.getPrecio());
	}

	@Test
	void equalsYHasCodeConsistentes() {
		alquilerAyer = new Alquiler(cliente, turismo, ayer);
		Alquiler alquilerCopia = new Alquiler(cliente, turismo, ayer);
		alquilerSemanaPasada = new Alquiler(cliente, turismo, semanaPasada);
		assertEquals(alquilerAyer, alquilerAyer);
		assertEquals(alquilerAyer, alquilerCopia);
		assertEquals(alquilerCopia, alquilerAyer);
		assertEquals(alquilerAyer.hashCode(), alquilerCopia.hashCode());
		assertNotEquals(alquilerAyer, "");
		assertNotEquals(alquilerAyer, alquilerSemanaPasada);
		assertNotEquals(alquilerAyer.hashCode(), alquilerSemanaPasada.hashCode());
		assertNotEquals(alquilerAyer, null);
		setComportamiento(cliente, "11111111H");
		creaComportamientoTurismo();
		Alquiler alquilerDiferente = new Alquiler(cliente, turismo, ayer);
		assertNotEquals(alquilerAyer, "");
		assertNotEquals(alquilerAyer, alquilerDiferente);
		assertNotEquals(alquilerAyer.hashCode(), alquilerDiferente.hashCode());
		assertNotEquals(alquilerAyer, null);
		setComportamiento(turismo, "2345BCD");
		setComportamiento(cliente, "11223344B");
		alquilerDiferente = new Alquiler(cliente, turismo, ayer);
		assertNotEquals(alquilerAyer, "");
		assertNotEquals(alquilerAyer, alquilerDiferente);
		assertNotEquals(alquilerAyer.hashCode(), alquilerDiferente.hashCode());
		assertNotEquals(alquilerAyer, null);
	}

	@Test
	void toStringDevuelveLaCadenaEsperada() {
		alquilerHoy = new Alquiler(cliente, turismo, ayer);
		String cadenaEsperada = String.format("%s <---> %s, %s - %s (%d€)", cliente, turismo,
				ayer.format(Alquiler.FORMATO_FECHA), "Aún no devuelto", 0);
		assertEquals(cadenaEsperada, alquilerHoy.toString());
		assertDoesNotThrow(() -> alquilerHoy.devolver(hoy));
		cadenaEsperada = String.format("%s <---> %s, %s - %s (%d€)", cliente, turismo,
				ayer.format(Alquiler.FORMATO_FECHA), hoy.format(Alquiler.FORMATO_FECHA), 29);
		assertEquals(cadenaEsperada, alquilerHoy.toString());
	}

}
