package org.iesalandalus.programacion.alquilervehiculos.modelo.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClienteTest {
	
	private static final String MENSAJE_ERROR_NOMBRE_NULO = "ERROR: El nombre no puede ser nulo.";
	private static final String MENSAJE_ERROR_FORMATO_NOMBRE_NO_VALIDO = "ERROR: El nombre no tiene un formato válido.";
	private static final String MENSAJE_ERROR_DNI_NULO = "ERROR: El DNI no puede ser nulo.";
	private static final String MENSAJE_ERROR_FORMATO_DNI_NO_VALIDO = "ERROR: El DNI no tiene un formato válido.";
	private static final String MENSAJE_ERROR_LETRA_DNI_NO_VALIDA = "ERROR: La letra del DNI no es correcta.";
	private static final String MENSAJE_ERROR_TELEFONO_NULO = "ERROR: El teléfono no puede ser nulo.";
	private static final String MENSAJE_ERROR_FORMATO_TELEFONO_NO_VALIDO = "ERROR: El teléfono no tiene un formato válido.";
	private static final String MENSAJE_ERROR_CLIENTE_NULO = "ERROR: No es posible copiar un cliente nulo.";

	private static final String NOMBRE_VALIDO = "Bob Esponja";
	private static final String DNI_VALIDO = "11223344B";
	private static final String TELEFONO_VALIDO = "950112233";
	
	private Cliente cliente;
	
	@BeforeEach
	void init() {
		cliente = new Cliente(NOMBRE_VALIDO, DNI_VALIDO, TELEFONO_VALIDO);
	}

	@Test
	void constructorNombreValidoDniValidoTelefonoValidoCreaClienteCorrectamente() {
		assertEquals(NOMBRE_VALIDO, cliente.getNombre());
		assertEquals(DNI_VALIDO, cliente.getDni());
		assertEquals(TELEFONO_VALIDO, cliente.getTelefono());
		cliente = new Cliente("Bob", DNI_VALIDO, TELEFONO_VALIDO);
	}
	
	@Test
	void constructorNombreNoValidoDniValidoTelefonoValidoLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> new Cliente(null, DNI_VALIDO, TELEFONO_VALIDO));
		assertEquals(MENSAJE_ERROR_NOMBRE_NULO, npe.getMessage());
		IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> new Cliente("", DNI_VALIDO, TELEFONO_VALIDO));
		assertEquals(MENSAJE_ERROR_FORMATO_NOMBRE_NO_VALIDO, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Cliente(" ", DNI_VALIDO, TELEFONO_VALIDO));
		assertEquals(MENSAJE_ERROR_FORMATO_NOMBRE_NO_VALIDO, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Cliente("bob esponja", DNI_VALIDO, TELEFONO_VALIDO));
		assertEquals(MENSAJE_ERROR_FORMATO_NOMBRE_NO_VALIDO, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Cliente("BOB ESPONJA", DNI_VALIDO, TELEFONO_VALIDO));
		assertEquals(MENSAJE_ERROR_FORMATO_NOMBRE_NO_VALIDO, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Cliente("Bob  Esponja", DNI_VALIDO, TELEFONO_VALIDO));
		assertEquals(MENSAJE_ERROR_FORMATO_NOMBRE_NO_VALIDO, iae.getMessage());
	}
	
	@Test
	void constructorNombreValidoDniNoValidoTelefonoValidoLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> new Cliente(NOMBRE_VALIDO, null, TELEFONO_VALIDO));
		assertEquals(MENSAJE_ERROR_DNI_NULO, npe.getMessage());
		IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> new Cliente(NOMBRE_VALIDO, "12345678", TELEFONO_VALIDO));
		assertEquals(MENSAJE_ERROR_FORMATO_DNI_NO_VALIDO, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Cliente(NOMBRE_VALIDO, "12345678X", TELEFONO_VALIDO));
		assertEquals(MENSAJE_ERROR_LETRA_DNI_NO_VALIDA, iae.getMessage());
	}
	
	@Test
	void constructorNombreValidoDniValidoTelefonoNoValidoLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> new Cliente(NOMBRE_VALIDO, DNI_VALIDO, null));
		assertEquals(MENSAJE_ERROR_TELEFONO_NULO, npe.getMessage());
		IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> new Cliente(NOMBRE_VALIDO, DNI_VALIDO, "12345678"));
		assertEquals(MENSAJE_ERROR_FORMATO_TELEFONO_NO_VALIDO, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> new Cliente(NOMBRE_VALIDO, DNI_VALIDO, "1234567890"));
		assertEquals(MENSAJE_ERROR_FORMATO_TELEFONO_NO_VALIDO, iae.getMessage());
	}
	
	@Test
	void constrctorClienteValidoCopiaClienteCorrectamente() {
		Cliente clienteCopia = new Cliente(cliente);
		assertEquals(cliente, clienteCopia);
		assertNotSame(cliente, clienteCopia);
		assertEquals(NOMBRE_VALIDO, clienteCopia.getNombre());
		assertEquals(DNI_VALIDO, clienteCopia.getDni());
		assertEquals(TELEFONO_VALIDO, clienteCopia.getTelefono());
	}

	@Test
	void constructorClienteNuloLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> new Cliente(null));
		assertEquals(MENSAJE_ERROR_CLIENTE_NULO, npe.getMessage());
	}
	
	@Test
	void getClienteConDniValidoDevuelveClienteConDichoDni() {
		Cliente cliente = Cliente.getClienteConDni(DNI_VALIDO);
		assertEquals(DNI_VALIDO, cliente.getDni());
	}
	
	@Test 
	void getClienteConDniNoValidoLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> Cliente.getClienteConDni(null));
		assertEquals(MENSAJE_ERROR_DNI_NULO, npe.getMessage());
		IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> Cliente.getClienteConDni("12345678"));
		assertEquals(MENSAJE_ERROR_FORMATO_DNI_NO_VALIDO, iae.getMessage());
		iae = assertThrows(IllegalArgumentException.class, () -> Cliente.getClienteConDni("12345678X"));
		assertEquals(MENSAJE_ERROR_LETRA_DNI_NO_VALIDA, iae.getMessage());
	}
	
	@Test
	void equalsYHasCodeConsistentes() {
		Cliente cliente = new Cliente(NOMBRE_VALIDO, DNI_VALIDO, TELEFONO_VALIDO);
		Cliente clienteIgual = Cliente.getClienteConDni(DNI_VALIDO);
		assertEquals(cliente, cliente);
		assertEquals(cliente, clienteIgual);
		assertEquals(clienteIgual, cliente);
		assertEquals(cliente.hashCode(), clienteIgual.hashCode());
		Cliente clienteDiferente = new Cliente(NOMBRE_VALIDO, "11111111H", TELEFONO_VALIDO);
		assertNotEquals(cliente, "");
		assertNotEquals(cliente, clienteDiferente);
		assertNotEquals(cliente.hashCode(), clienteDiferente.hashCode());
		assertNotEquals(cliente, null);
	}
	
	@Test
	void toStringDevuelveLaCadenaEsperada() {
		assertEquals(String.format("%s - %s (%s)", NOMBRE_VALIDO, DNI_VALIDO, TELEFONO_VALIDO), cliente.toString());
	}

}
