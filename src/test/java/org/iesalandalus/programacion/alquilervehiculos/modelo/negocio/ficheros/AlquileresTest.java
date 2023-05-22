package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IAlquileres;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AlquileresTest {

	private static final String MENSAJE_ERROR_INSERTAR_ALQUILER_NULO = "ERROR: No se puede insertar un alquiler nulo.";
	private static final String MENSAJE_ERROR_INSERTAR_ALQUILER_CLIENTE_ALQUILER_ABIERTO = "ERROR: El cliente tiene otro alquiler sin devolver.";
	private static final String MENSAJE_ERROR_INSERTAR_ALQUILER_VEHICULO_ALQUILADO = "ERROR: El vehículo está actualmente alquilado.";
	private static final String MENSAJE_ERROR_INSERTAR_ALQUILER_CLIENTE_OTRO_POSTERIOR = "ERROR: El cliente tiene un alquiler posterior.";
	private static final String MENSAJE_ERROR_INSERTAR_ALQUILER_VEHICULO_OTRO_POSTERIOR = "ERROR: El vehículo tiene un alquiler posterior.";
	private static final String MENSAJE_ERROR_DEVOLVER_CLIENTE_NULO = "ERROR: No se puede devolver un alquiler de un cliente nulo.";
	private static final String MENSAJE_ERROR_DEVOLVER_CLIENTE_SIN_ALQUILER_ABIERTO = "ERROR: No existe ningún alquiler abierto para ese cliente.";
	private static final String MENSAJE_ERROR_DEVOLVER_VEHICULO_NULO = "ERROR: No se puede devolver un alquiler de un vehículo nulo.";
	private static final String MENSAJE_ERROR_DEVOLVER_VEHICULO_SIN_ALQUILER_ABIERTO = "ERROR: No existe ningún alquiler abierto para ese vehículo.";

	private static final String MENSAJE_ERROR_BORRAR_ALQUILER_NULO = "ERROR: No se puede borrar un alquiler nulo.";
	private static final String MENSAJE_ERROR_BUSCAR_ALQUILER_NULO = "ERROR: No se puede buscar un alquiler nulo.";
	private static final String MENSAJE_ERROR_ALQUILER_NO_EXISTE = "ERROR: No existe ningún alquiler igual.";

	private static Alquiler alquiler1;
	private static Alquiler alquiler2;
	private static Alquiler alquiler3;
	private static Alquiler alquiler4;
	private static Cliente cliente1;
	private static Cliente cliente2;
	private static Turismo turismo1;
	private static Turismo turismo2;
	private static LocalDate hoy;
	private static LocalDate ayer;
	private static LocalDate anteayer;
	private static LocalDate semanaPasada;
	private IAlquileres alquileres;

	@BeforeAll
	static void setup() {
		hoy = LocalDate.now();
		ayer = hoy.minusDays(1);
		anteayer = hoy.minusDays(2);
		semanaPasada = hoy.minusDays(7);
		cliente1 = mock();
		when(cliente1.getDni()).thenReturn("11223344B");
		cliente2 = mock();
		when(cliente2.getDni()).thenReturn("11111111H");
		turismo1 = mock();
		when(turismo1.getMatricula()).thenReturn("1234BCD");
		turismo2 = mock();
		when(turismo2.getMatricula()).thenReturn("1111BBB");
	}
	
	@BeforeEach
	void init() {
		alquileres = Alquileres.getInstancia();
		alquiler1 = mock();
		when(alquiler1.getCliente()).thenReturn(cliente1);
		when(alquiler1.getVehiculo()).thenReturn(turismo1);
		when(alquiler1.getFechaAlquiler()).thenReturn(semanaPasada);
		alquiler2 = mock();
		when(alquiler2.getCliente()).thenReturn(cliente1);
		when(alquiler2.getVehiculo()).thenReturn(turismo2);
		when(alquiler2.getFechaAlquiler()).thenReturn(ayer);
		alquiler3 = mock();
		when(alquiler3.getCliente()).thenReturn(cliente2);
		when(alquiler3.getVehiculo()).thenReturn(turismo2);
		when(alquiler3.getFechaAlquiler()).thenReturn(ayer);
		alquiler4 = mock();
		when(alquiler4.getCliente()).thenReturn(cliente2);
		when(alquiler4.getVehiculo()).thenReturn(turismo1);
		when(alquiler4.getFechaAlquiler()).thenReturn(ayer);
		for (Alquiler alquiler : alquileres.get()) {
			assertDoesNotThrow(() -> alquileres.borrar(alquiler));
		}
	}

	@Test
	void constructorCreaAlquilersCorrectamente() {
		assertNotNull(alquileres);
	}
	
	@Test
	void getDevuelveAlquileresCorrectamente() {
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		assertDoesNotThrow(() -> alquileres.devolver(cliente1, anteayer));
		when(alquiler1.getFechaDevolucion()).thenReturn(anteayer);
		assertDoesNotThrow(() -> alquileres.insertar(alquiler4));
		List<Alquiler> copiaAlquileres = alquileres.get();
		assertSame(alquiler1, copiaAlquileres.get(copiaAlquileres.indexOf(alquiler1)));
		assertSame(alquiler4, copiaAlquileres.get(copiaAlquileres.indexOf(alquiler4)));
	}
	
	@Test
	void getClienteValidoDevuelveAlquileresClienteCorrectamente() {
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		assertDoesNotThrow(() -> alquileres.devolver(cliente1, anteayer));
		when(alquiler1.getFechaDevolucion()).thenReturn(anteayer);
		assertDoesNotThrow(() -> alquileres.insertar(alquiler2));
		assertDoesNotThrow(() -> alquileres.insertar(alquiler4));
		List<Alquiler> alquileresCliente = alquileres.get(cliente1);
		assertEquals(2, alquileresCliente.size());
		assertEquals(alquiler1, alquileresCliente.get(0));
		assertSame(alquiler1, alquileresCliente.get(0));
		assertEquals(alquiler2, alquileresCliente.get(1));
		assertSame(alquiler2, alquileresCliente.get(1));
	}
	
	@Test
	void getTurismoValidoDevuelveAlquileresClienteCorrectamente() {
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		assertDoesNotThrow(() -> alquileres.devolver(cliente1, anteayer));
		when(alquiler1.getFechaDevolucion()).thenReturn(anteayer);
		assertDoesNotThrow(() -> alquileres.insertar(alquiler2));
		assertDoesNotThrow(() -> alquileres.insertar(alquiler4));
		List<Alquiler> alquileresTurismo = alquileres.get(turismo1);
		assertEquals(2, alquileresTurismo.size());
		assertEquals(alquiler1, alquileresTurismo.get(0));
		assertSame(alquiler1, alquileresTurismo.get(0));
		assertEquals(alquiler4, alquileresTurismo.get(1));
		assertSame(alquiler4,alquileresTurismo.get(1));
	}
	
	@Test
	void insertarAlquilerValidoInsertaCorrectamente() {
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		assertSame(alquiler1, alquileres.buscar(alquiler1));
	}
	
	@Test
	void insertarAlquilerNuloLanzaExcepcion() {
		NullPointerException npe = assertThrows(NullPointerException.class, () -> alquileres.insertar(null));
		assertEquals(MENSAJE_ERROR_INSERTAR_ALQUILER_NULO, npe.getMessage());
	}
	
	@Test
	void insertarAlquilerClienteAlquilerAbiertoLanzaExcepcion() {
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> alquileres.insertar(alquiler2));
		assertEquals(MENSAJE_ERROR_INSERTAR_ALQUILER_CLIENTE_ALQUILER_ABIERTO, onse.getMessage());
	}
	
	@Test
	void insertarAlquilerTurismoAlquilerAbiertoLanzaExcepcion() {
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> alquileres.insertar(alquiler4));
		assertEquals(MENSAJE_ERROR_INSERTAR_ALQUILER_VEHICULO_ALQUILADO, onse.getMessage());
	}
	
	@Test
	void insertarAlquilerClienteAlquilerAnteiorLanzaExcepcion() {
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		assertDoesNotThrow(() -> alquileres.devolver(cliente1, anteayer));
		when(alquiler1.getFechaDevolucion()).thenReturn(anteayer);
		when(alquiler1.getFechaAlquiler()).thenReturn(ayer);
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		when(alquiler1.getFechaDevolucion()).thenReturn(null);
		assertDoesNotThrow(() -> alquileres.devolver(cliente1, ayer));
		when(alquiler1.getFechaDevolucion()).thenReturn(ayer);
		OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> alquileres.insertar(alquiler2));
		assertEquals(MENSAJE_ERROR_INSERTAR_ALQUILER_CLIENTE_OTRO_POSTERIOR, onse.getMessage());
	}
	
	@Test
	void insertarAlquilerTurismoAlquilerAnteriorLanzaExcepcion() {
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		assertDoesNotThrow(() -> alquileres.devolver(cliente1, anteayer));
		when(alquiler1.getFechaDevolucion()).thenReturn(anteayer);
		when(alquiler1.getFechaAlquiler()).thenReturn(ayer);
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		when(alquiler1.getFechaDevolucion()).thenReturn(null);
		assertDoesNotThrow(() -> alquileres.devolver(cliente1, ayer));
		when(alquiler1.getFechaDevolucion()).thenReturn(ayer);
		OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> alquileres.insertar(alquiler4));
		assertEquals(MENSAJE_ERROR_INSERTAR_ALQUILER_VEHICULO_OTRO_POSTERIOR, onse.getMessage());
	}
	
	@Test
	void devolverClienteConAlquilerAbiertoDevuelveCorrectamente() {
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		assertDoesNotThrow(() -> alquileres.devolver(cliente1, ayer));
		when(alquiler1.getFechaDevolucion()).thenReturn(ayer);
		Alquiler alquiler = alquileres.buscar(alquiler1);
		assertEquals(ayer, alquiler.getFechaDevolucion());
	}
	
	@Test
	void devolverClienteNuloLanzaExcepcion() {
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		Cliente cliente = null;
		NullPointerException npe = assertThrows(NullPointerException.class, () -> alquileres.devolver(cliente, ayer));
		assertEquals(MENSAJE_ERROR_DEVOLVER_CLIENTE_NULO, npe.getMessage());
	}
	
	@Test
	void devolverClienteSinAlquilerAbiertoLanzaExcepcion() {
		OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> alquileres.devolver(cliente1, hoy));
		assertEquals(MENSAJE_ERROR_DEVOLVER_CLIENTE_SIN_ALQUILER_ABIERTO, onse.getMessage());
	}
	
	@Test
	void devolverVehiculoConAlquilerAbiertoDevuelveCorrectamente() {
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		assertDoesNotThrow(() -> alquileres.devolver(turismo1, ayer));
		when(alquiler1.getFechaDevolucion()).thenReturn(ayer);
		Alquiler alquiler = alquileres.buscar(alquiler1);
		assertEquals(ayer, alquiler.getFechaDevolucion());
	}
	
	@Test
	void devolverVehiculoNuloLanzaExcepcion() {
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		Vehiculo vehiculo = null;
		NullPointerException npe = assertThrows(NullPointerException.class, () -> alquileres.devolver(vehiculo, ayer));
		assertEquals(MENSAJE_ERROR_DEVOLVER_VEHICULO_NULO, npe.getMessage());
	}
	
	@Test
	void devolverVehiculoSinAlquilerAbiertoLanzaExcepcion() {
		OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> alquileres.devolver(turismo1, hoy));
		assertEquals(MENSAJE_ERROR_DEVOLVER_VEHICULO_SIN_ALQUILER_ABIERTO, onse.getMessage());
	}
	
	@Test
	void borrarAlquilerExistenteBorraAlquilerCorrectamente() {
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		assertDoesNotThrow(() -> alquileres.borrar(alquiler1));
		assertNull(alquileres.buscar(alquiler1));
	}
	
	@Test
	void borrarAlquilerNoExistenteLanzaExcepcion() {
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> alquileres.borrar(alquiler2));
		assertEquals(MENSAJE_ERROR_ALQUILER_NO_EXISTE, onse.getMessage());
	}

	@Test
	void borrarAlquilerNuloLanzaExcepcion() {
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		NullPointerException npe = assertThrows(NullPointerException.class, () -> alquileres.borrar(null));
		assertEquals(MENSAJE_ERROR_BORRAR_ALQUILER_NULO, npe.getMessage());
	}
	
	@Test
	void busarAlquilerExistenteDevuelveAlquilerCorrectamente() {
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		assertEquals(alquiler1, alquileres.buscar(alquiler1));
		assertSame(alquiler1, alquileres.buscar(alquiler1));
	}
	
	@Test
	void busarAlquilerNoExistenteDevuelveAlquilerNulo() {
		assertNull(alquileres.buscar(alquiler1));
	}
	
	@Test
	void buscarAlquilerNuloLanzaExcepcion() {
		assertDoesNotThrow(() -> alquileres.insertar(alquiler1));
		NullPointerException npe = assertThrows(NullPointerException.class, () -> alquileres.buscar(null));
		assertEquals(MENSAJE_ERROR_BUSCAR_ALQUILER_NULO, npe.getMessage());
	}

}