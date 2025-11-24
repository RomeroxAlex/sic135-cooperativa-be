CREATE TABLE cuentas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    nivel INT NOT NULL,
    descripcion VARCHAR(255),
    cuenta_padre_id BIGINT,
    CONSTRAINT uq_codigo UNIQUE (codigo),
    FOREIGN KEY (cuenta_padre_id) REFERENCES cuentas(id)
);
