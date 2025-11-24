-- Schema SQL for SIC135 Contabilidad
-- PostgreSQL Database Schema

-- Catálogo de Cuentas
CREATE TABLE IF NOT EXISTS catalogo_cuenta (
    id_catalogo_cuenta UUID PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(200) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    nivel INTEGER,
    descripcion VARCHAR(500),
    id_cuenta_padre UUID REFERENCES catalogo_cuenta(id_catalogo_cuenta),
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP,
    fecha_modificacion TIMESTAMP,
    usuario_creacion VARCHAR(100),
    usuario_modificacion VARCHAR(100)
);

CREATE INDEX idx_catalogo_cuenta_codigo ON catalogo_cuenta(codigo);
CREATE INDEX idx_catalogo_cuenta_tipo ON catalogo_cuenta(tipo);

-- Manual de Cuentas
CREATE TABLE IF NOT EXISTS manual_cuenta (
    id_manual_cuenta UUID PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    contenido TEXT,
    version VARCHAR(20),
    fecha_creacion TIMESTAMP,
    fecha_modificacion TIMESTAMP,
    usuario_creacion VARCHAR(100),
    usuario_modificacion VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE
);

-- Partida de Diario
CREATE TABLE IF NOT EXISTS partida_diario (
    id_partida_diario UUID PRIMARY KEY,
    numero_partida BIGINT NOT NULL UNIQUE,
    fecha DATE NOT NULL,
    descripcion VARCHAR(500),
    referencia VARCHAR(100),
    estado VARCHAR(20) NOT NULL DEFAULT 'BORRADOR',
    es_ajuste BOOLEAN DEFAULT FALSE,
    motivo_ajuste VARCHAR(500),
    total_debe DECIMAL(18,2) DEFAULT 0,
    total_haber DECIMAL(18,2) DEFAULT 0,
    fecha_creacion TIMESTAMP,
    fecha_modificacion TIMESTAMP,
    usuario_creacion VARCHAR(100),
    usuario_modificacion VARCHAR(100)
);

CREATE INDEX idx_partida_diario_fecha ON partida_diario(fecha);
CREATE INDEX idx_partida_diario_estado ON partida_diario(estado);

-- Línea de Partida
CREATE TABLE IF NOT EXISTS linea_partida (
    id_linea_partida UUID PRIMARY KEY,
    id_partida_diario UUID NOT NULL REFERENCES partida_diario(id_partida_diario) ON DELETE CASCADE,
    id_catalogo_cuenta UUID NOT NULL REFERENCES catalogo_cuenta(id_catalogo_cuenta),
    descripcion VARCHAR(300),
    debe DECIMAL(18,2) DEFAULT 0,
    haber DECIMAL(18,2) DEFAULT 0,
    orden INTEGER
);

CREATE INDEX idx_linea_partida_partida ON linea_partida(id_partida_diario);
CREATE INDEX idx_linea_partida_cuenta ON linea_partida(id_catalogo_cuenta);

-- Movimiento Mayor
CREATE TABLE IF NOT EXISTS movimiento_mayor (
    id_movimiento_mayor UUID PRIMARY KEY,
    id_catalogo_cuenta UUID NOT NULL REFERENCES catalogo_cuenta(id_catalogo_cuenta),
    id_partida_diario UUID REFERENCES partida_diario(id_partida_diario),
    fecha DATE NOT NULL,
    descripcion VARCHAR(300),
    referencia VARCHAR(100),
    debe DECIMAL(18,2) DEFAULT 0,
    haber DECIMAL(18,2) DEFAULT 0,
    saldo DECIMAL(18,2) DEFAULT 0,
    fecha_creacion TIMESTAMP
);

CREATE INDEX idx_movimiento_mayor_cuenta ON movimiento_mayor(id_catalogo_cuenta);
CREATE INDEX idx_movimiento_mayor_fecha ON movimiento_mayor(fecha);

-- Balance Inicial
CREATE TABLE IF NOT EXISTS balance_inicial (
    id_balance_inicial UUID PRIMARY KEY,
    periodo VARCHAR(10) NOT NULL,
    id_catalogo_cuenta UUID NOT NULL REFERENCES catalogo_cuenta(id_catalogo_cuenta),
    saldo_debe DECIMAL(18,2) DEFAULT 0,
    saldo_haber DECIMAL(18,2) DEFAULT 0,
    fecha_registro DATE,
    fecha_creacion TIMESTAMP,
    usuario_creacion VARCHAR(100)
);

CREATE INDEX idx_balance_inicial_periodo ON balance_inicial(periodo);

-- Factura
CREATE TABLE IF NOT EXISTS factura (
    id_factura UUID PRIMARY KEY,
    numero_factura VARCHAR(50) NOT NULL UNIQUE,
    cliente_id UUID,
    cliente_nombre VARCHAR(200),
    cliente_nit VARCHAR(20),
    fecha DATE NOT NULL,
    subtotal DECIMAL(18,2) DEFAULT 0,
    impuestos DECIMAL(18,2) DEFAULT 0,
    total DECIMAL(18,2) DEFAULT 0,
    estado VARCHAR(20) NOT NULL DEFAULT 'BORRADOR',
    observaciones VARCHAR(500),
    fecha_creacion TIMESTAMP,
    fecha_modificacion TIMESTAMP,
    usuario_creacion VARCHAR(100),
    usuario_modificacion VARCHAR(100)
);

CREATE INDEX idx_factura_fecha ON factura(fecha);
CREATE INDEX idx_factura_estado ON factura(estado);
CREATE INDEX idx_factura_cliente ON factura(cliente_id);

-- Item Factura
CREATE TABLE IF NOT EXISTS item_factura (
    id_item_factura UUID PRIMARY KEY,
    id_factura UUID NOT NULL REFERENCES factura(id_factura) ON DELETE CASCADE,
    codigo_producto VARCHAR(50),
    descripcion VARCHAR(300) NOT NULL,
    cantidad DECIMAL(18,4) DEFAULT 1,
    precio_unitario DECIMAL(18,2) DEFAULT 0,
    subtotal DECIMAL(18,2) DEFAULT 0,
    orden INTEGER
);

CREATE INDEX idx_item_factura_factura ON item_factura(id_factura);

-- Auditoría
CREATE TABLE IF NOT EXISTS auditoria (
    id_auditoria UUID PRIMARY KEY,
    entidad VARCHAR(100) NOT NULL,
    entidad_id VARCHAR(100),
    accion VARCHAR(50) NOT NULL,
    usuario VARCHAR(100),
    fecha_accion TIMESTAMP NOT NULL,
    datos_anteriores TEXT,
    datos_nuevos TEXT,
    ip_address VARCHAR(50),
    detalle VARCHAR(500)
);

CREATE INDEX idx_auditoria_entidad ON auditoria(entidad);
CREATE INDEX idx_auditoria_usuario ON auditoria(usuario);
CREATE INDEX idx_auditoria_fecha ON auditoria(fecha_accion);
