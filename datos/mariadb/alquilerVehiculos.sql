DROP SCHEMA IF EXISTS alquilerVehiculos;

CREATE SCHEMA IF NOT EXISTS alquilerVehiculos DEFAULT CHARACTER SET utf8 COLLATE utf8_spanish_ci;
USE alquilerVehiculos;

DROP TABLE IF EXISTS clientes;
CREATE TABLE IF NOT EXISTS clientes (
  nombre VARCHAR(45) NOT NULL,
  dni VARCHAR(9) NOT NULL,
  telefono VARCHAR(9) NULL,
  PRIMARY KEY (dni),
  UNIQUE INDEX `dni_idx` (dni ASC) VISIBLE);

DROP TABLE IF EXISTS vehiculos;
CREATE TABLE IF NOT EXISTS vehiculos (
  marca VARCHAR(45) NOT NULL,
  modelo VARCHAR(45) NOT NULL,
  matricula VARCHAR(7) NOT NULL,
  `tipo` ENUM("AUTOBUS", "FURGONETA", "TURISMO") NOT NULL,
  cilindrada INT NULL,
  plazas INT,
  pma INT,
  PRIMARY KEY (matricula),
  UNIQUE INDEX `matricula_idx` (matricula ASC) VISIBLE);

DROP TABLE IF EXISTS alquileres;
CREATE TABLE IF NOT EXISTS `alquileres` (
  `cliente` VARCHAR(9) NOT NULL,
  `vehiculo` VARCHAR(7) NOT NULL,
  `fechaAlquiler` DATE NOT NULL,
  `fechaDevolucion` DATE,
  INDEX `fk_clientes_idx` (`cliente` ASC) VISIBLE,
  INDEX `fk_vehiculos_idx` (`vehiculo` ASC) VISIBLE,
  INDEX `fechaAlquiler_idx` (`fechaAlquiler` ASC) VISIBLE,
  PRIMARY KEY (`cliente`, `vehiculo`, `fechaAlquiler`),
  CONSTRAINT `fk_clientes`
    FOREIGN KEY (`cliente`)
    REFERENCES `clientes` (`dni`),
  CONSTRAINT `fk_vehiculos`
    FOREIGN KEY (`vehiculo`)
    REFERENCES `vehiculos` (`matricula`));

