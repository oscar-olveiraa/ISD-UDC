-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------
-- TABLAS
DROP TABLE IF EXISTS Compra;
DROP TABLE IF EXISTS Partido;


------------TABLA Partido----------------
CREATE TABLE Partido (
    idPartido BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) COLLATE latin1_bin NOT NULL,
    fechaPartido DATETIME NOT NULL,
    precio FLOAT NOT NULL,
    maxEntradas INT NOT NULL,
    fechaAlta DATETIME NOT NULL,
    entradasVendidas INT NOT NULL,
    CONSTRAINT PartidoPk PRIMARY KEY (idPartido),
    CHECK (precio >= 0 AND precio <= 1000),
    CHECK (maxEntradas >= 1 AND maxEntradas <= 50000),
    CHECK (entradasVendidas >= 0 AND entradasVendidas <= 50000)
) ENGINE = InnoDB;

------------TABLA Compra-----------------
CREATE TABLE Compra (
    idCompra BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) COLLATE latin1_bin NOT NULL,
    tarjeta VARCHAR(16),
    cantidad INT NOT NULL,
    fechaCompra DATETIME NOT NULL,
    recogida BIT NOT NULL,
    idPartido BIGINT NOT NULL,
    CONSTRAINT CompraPk PRIMARY KEY (idCompra),
    CONSTRAINT IdPartidoFK FOREIGN KEY (idPartido)
    REFERENCES Partido(idPartido) ON DELETE CASCADE
) ENGINE = InnoDB;
