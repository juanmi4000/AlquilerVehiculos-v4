package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mongodb;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Autobus;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Furgoneta;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IVehiculos;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.result.DeleteResult;

public class Vehiculos implements IVehiculos {

	private static final String TURISMO = "Turismo";
	private static final String AUTOBUS = "Autobus";
	private static final String PLAZAS = "plazas";
	private static final String PMA = "pma";
	private static final String CILINDRADA = "cilindrada";
	private static final String FURGONETA = "Furgoneta";
	private static final String TIPO = "tipo";
	private static final String MODELO = "modelo";
	private static final String MARCA = "marca";
	private static final String MATRICULA = "matricula";
	private static final String COLECCION = "vehiculos";
	// private static final String

	private MongoCollection<Document> coleccionVehiculos;
	private static final Vehiculos instancia = new Vehiculos();

	private Vehiculos() {
		// lo pongo privado para que no se puedan hacer instancias
	}

	static Vehiculos getInstancia() {
		return instancia;
	}

	@Override
	public void comenzar() {
		coleccionVehiculos = MongoDB.getBD().getCollection(COLECCION);

	}

	@Override
	public void terminar() {
		MongoDB.cerrarConexion();
	}
	
	Vehiculo getVehiculo(Document documento) {
		Vehiculo vehiculo = null;
		if (documento != null) {
			String marca = documento.getString(MARCA);
			String modelo = documento.getString(MODELO);
			String matricula = documento.getString(MATRICULA);
			String tipo = documento.getString(TIPO);
			if (tipo.equalsIgnoreCase(TURISMO)) {
				int cilindrada = documento.getInteger(CILINDRADA);
				vehiculo = new Turismo(marca, modelo, cilindrada, matricula);
			} else if (tipo.equalsIgnoreCase(FURGONETA)) {
				int plazas = documento.getInteger(PLAZAS);
				int pma = documento.getInteger(PMA);
				vehiculo = new Furgoneta(marca, modelo, pma, plazas, matricula);
			} else if (tipo.equalsIgnoreCase(AUTOBUS)) {
				int pma = documento.getInteger(PMA);
				vehiculo = new Autobus(marca, modelo, pma, matricula);
			} 
		}
		return vehiculo;
	}
	
	Document getDocumento(Vehiculo vehiculo) {
		Document documento = null;
		if (vehiculo != null) {
			String marca = vehiculo.getMarca();
			String modelo = vehiculo.getModelo();
			String matricula = vehiculo.getMatricula();
			if (vehiculo instanceof Turismo turismo) {
				int cilindrada = turismo.getCilindrada();
				documento = new Document().append(MARCA, marca).append(MODELO, modelo).append(CILINDRADA, cilindrada).append(MATRICULA, matricula).append(TIPO, TURISMO);
			}else if (vehiculo instanceof Furgoneta furgoneta) {
				int plazas = furgoneta.getPlazas();
				int pma = furgoneta.getPma();
				documento = new Document().append(MARCA, marca).append(MODELO, modelo).append(PMA, pma).append(PLAZAS, plazas).append(MATRICULA, matricula).append(TIPO, FURGONETA);
			} else if (vehiculo instanceof Autobus autobus) {
				int plazas = autobus.getPlazas();
				documento = new Document().append(MARCA, marca).append(MODELO, modelo).append(PLAZAS, plazas).append(MATRICULA, matricula).append(TIPO,	AUTOBUS);
			}
		}
		return documento;
	}
	
	private Bson getCriterioBusqueda(Vehiculo vehiculo) {
		return eq(MATRICULA, vehiculo.getMatricula());
	}

	@Override
	public List<Vehiculo> get() {
		List<Vehiculo> vehiculos = new ArrayList<>();
		for (Document documento : coleccionVehiculos.find()) {
			vehiculos.add(getVehiculo(documento));
		}
		return vehiculos;
	}

	@Override
	public void insertar(Vehiculo vehiculo) throws OperationNotSupportedException {
		if (vehiculo == null) {
			throw new NullPointerException("ERROR: No se puede insertar un vehículo nulo.");
		}
		FindOneAndReplaceOptions opciones = new FindOneAndReplaceOptions().upsert(true);
		Document resultado = coleccionVehiculos.findOneAndReplace(getCriterioBusqueda(vehiculo), getDocumento(vehiculo), opciones);
		if (resultado != null) {
			throw new OperationNotSupportedException("ERROR: Ya existe un vehículo con esa matrícula.");
		}
	}

	@Override
	public Vehiculo buscar(Vehiculo vehiculo) {
		if (vehiculo == null) {
			throw new NullPointerException("ERROR: No se puede buscar un vehículo nulo.");
		}
		return getVehiculo(coleccionVehiculos.find(getCriterioBusqueda(vehiculo)).first());
	}

	@Override
	public void borrar(Vehiculo vehiculo) throws OperationNotSupportedException {
		if (vehiculo == null) {
			throw new NullPointerException("ERROR: No se puede borrar un vehículo nulo.");
		}
		DeleteResult resultado = coleccionVehiculos.deleteOne(getCriterioBusqueda(vehiculo));
		if (resultado.getDeletedCount() == 0) {
			throw new OperationNotSupportedException("ERROR: No existe ningún vehículo con esa matrícula.");
		}
	}

}
