package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IClientes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClientesTest {

	private static final String MENSAJE_ERROR_INSERTAR_CLIENTE_NULO = "ERROR: No se puede insertar un cliente nulo.";
	private static final String MENSAJE_ERROR_BORRAR_CLIENTE_NULO = "ERROR: No se puede borrar un cliente nulo.";
	private static final String MENSAJE_ERROR_BUSCAR_CLIENTE_NULO = "ERROR: No se puede buscar un cliente nulo.";
	private static final String MENSAJE_ERROR_MODIFICAR_CLIENTE_NULO = "ERROR: No se puede modificar un cliente nulo.";
	private static final String MENSAJE_ERROR_CLIENTE_MODIFICAR_NO_EXISTE = "ERROR: No existe ningún cliente con ese DNI.";
	private static final String MENSAJE_ERROR_CLIENTE_EXISTE = "ERROR: Ya existe un cliente con ese DNI.";
	private static final String MENSAJE_ERROR_CLIENTE_BORRAR_NO_EXISTE = "ERROR: No existe ningún cliente con ese DNI.";

	private static Cliente cliente1;
	private static Cliente cliente2;
	private IClientes clientes;
	
	@BeforeEach
	void init() {
		clientes = Clientes.getInstancia();
		cliente1 = mock();
		when(cliente1.getDni()).thenReturn("11223344B");
		cliente2 = mock();
		when(cliente2.getDni()).thenReturn("11111111H");
		for (Cliente cliente : clientes.get()) {
			assertDoesNotThrow(() -> clientes.borrar(cliente));
		}
	}

	@Test
	void constructorCreaClientesCorrectamente() {
		assertNotNull(clientes);
	}
	
	@Test
	void getDevuelveClientesCorrectamente() {
		assertDoesNotThrow(() -> clientes.insertar(cliente1));
		assertDoesNotThrow(() -> clientes.insertar(cliente2));
		List<Cliente> copiaClientes = clientes.get();
		assertEquals(2, copiaClientes.size());
		assertEquals(cliente1, copiaClientes.get(0));
		assertSame(cliente1, copiaClientes.get(0));
		assertEquals(cliente2, copiaClientes.get(1));
		assertSame(cliente2, copiaClientes.get(1));
	}
	
	@Test
	void insertarClienteValidoInsertaCorrectamente() {
		assertDoesNotThrow(() -> clientes.insertar(cliente1));
		assertEquals(cliente1, clientes.buscar(cliente1));
		assertSame(cliente1, clientes.buscar(cliente1));
	}
	
	@Test
	void insertarClienteNuloLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> clientes.insertar(null));
		assertEquals(MENSAJE_ERROR_INSERTAR_CLIENTE_NULO, npe.getMessage());
	}
	
	@Test
	void insertarClienteRepetidoLanzaExcepcion() {
		assertDoesNotThrow(() -> clientes.insertar(cliente1));
		OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> clientes.insertar(cliente1));
		assertEquals(MENSAJE_ERROR_CLIENTE_EXISTE, onse.getMessage());
	}
	
	@Test
	void borrarClienteExistenteBorraClienteCorrectamente() {
		assertDoesNotThrow(() -> clientes.insertar(cliente1));
		assertDoesNotThrow(() -> clientes.borrar(cliente1));
		assertNull(clientes.buscar(cliente1));
	}
	
	@Test
	void borrarClienteNoExistenteLanzaExcepcion() {
		assertDoesNotThrow(() -> clientes.insertar(cliente1));
		OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> clientes.borrar(cliente2));
		assertEquals(MENSAJE_ERROR_CLIENTE_BORRAR_NO_EXISTE, onse.getMessage());
	}

	@Test
	void borrarClienteNuloLanzaExcepcion() {
		assertDoesNotThrow(() -> clientes.insertar(cliente1));
		NullPointerException npe = assertThrows(NullPointerException.class, () -> clientes.borrar(null));
		assertEquals(MENSAJE_ERROR_BORRAR_CLIENTE_NULO, npe.getMessage());
	}
	
	@Test
	void busarClienteExistenteDevuelveClienteCorrectamente() {
		assertDoesNotThrow(() -> clientes.insertar(cliente1));
		assertEquals(cliente1, clientes.buscar(cliente1));
		assertSame(cliente1, clientes.buscar(cliente1));
	}
	
	@Test
	void busarClienteNoExistenteDevuelveClienteNulo() {
		assertNull(clientes.buscar(cliente1));
	}
	
	@Test
	void buscarClienteNuloLanzaExcepcion() {
		assertDoesNotThrow(() -> clientes.insertar(cliente1));
		NullPointerException npe = assertThrows(NullPointerException.class, () -> clientes.buscar(null));
		assertEquals(MENSAJE_ERROR_BUSCAR_CLIENTE_NULO, npe.getMessage());
	}
	
	@Test
	void modificarClienteExistenteNombreValidoTelefonoValidoModificaClienteCorrectamente() {
		assertDoesNotThrow(() -> clientes.insertar(cliente1));
		assertDoesNotThrow(() -> clientes.modificar(cliente1, "Patricio Estrella", "950123456"));
		verify(cliente1).setNombre("Patricio Estrella");
		verify(cliente1).setTelefono("950123456");
	}
	
	@Test
	void modificarClienteExistenteNombreNuloTelefonoValidoModificaClienteCorrectamente() {
		assertDoesNotThrow(() -> clientes.insertar(cliente1));
		assertDoesNotThrow(() -> clientes.modificar(cliente1, null, "950123456"));
		verify(cliente1, never()).setNombre(any());
		verify(cliente1).setTelefono("950123456");
		assertDoesNotThrow(() -> clientes.insertar(cliente2));
		assertDoesNotThrow(() -> clientes.modificar(cliente2, "", "950123456"));
		verify(cliente1, never()).setNombre(any());
		verify(cliente1).setTelefono("950123456");
	}
	
	@Test
	void modificarClienteExistenteNombreValidoTelefonoNuloModificaClienteCorrectamente() {
		assertDoesNotThrow(() -> clientes.insertar(cliente1));
		assertDoesNotThrow(() -> clientes.modificar(cliente1, "Patricio Estrella", null));
		verify(cliente1).setNombre("Patricio Estrella");
		verify(cliente1, never()).setTelefono(any());
		assertDoesNotThrow(() -> clientes.insertar(cliente2));
		assertDoesNotThrow(() -> clientes.modificar(cliente2, "Patricio Estrella", ""));
		verify(cliente1).setNombre("Patricio Estrella");
		verify(cliente1, never()).setTelefono(any());
	}
	
	@Test
	void modificarClienteExistenteNombreNuloTelefonoNuloNoModificaCliente() {
		assertDoesNotThrow(() -> clientes.insertar(cliente1));
		assertDoesNotThrow(() -> clientes.modificar(cliente1, null, null));
		verify(cliente1, never()).setNombre(any());
		verify(cliente1, never()).setTelefono(any());
	}
	
	@Test
	void modificarClienteNoExistenteTelefonoValidoLanzaExcepcion() {
		OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> clientes.modificar(cliente1, "Patricio Estrella", "950123456"));
		assertEquals(MENSAJE_ERROR_CLIENTE_MODIFICAR_NO_EXISTE, onse.getMessage());
	}
	
	@Test
	void modificarClienteNuloNombreValidoTelefonoValidoLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> clientes.modificar(null, "Patricio Estrella", "950123456"));
		assertEquals(MENSAJE_ERROR_MODIFICAR_CLIENTE_NULO, npe.getMessage());
	}

}