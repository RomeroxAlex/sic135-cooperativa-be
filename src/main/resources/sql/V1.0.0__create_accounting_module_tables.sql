-- Migration script for accounting module
-- Version: 1.0.0
-- Description: Create tables for complete accounting system

-- ============================================
-- ACCOUNTING PERIODS (Control de Períodos)
-- ============================================
CREATE TABLE IF NOT EXISTS periodo_contable (
    id_periodo_contable UUID PRIMARY KEY,
    nombre VARCHAR(100),
    descripcion TEXT,
    fecha_inicio DATE,
    fecha_fin DATE,
    activo BOOLEAN DEFAULT TRUE,
    cerrado BOOLEAN DEFAULT FALSE,
    fecha_cierre TIMESTAMP,
    anio INTEGER,
    mes INTEGER
);

CREATE INDEX idx_periodo_activo ON periodo_contable(activo);
CREATE INDEX idx_periodo_fechas ON periodo_contable(fecha_inicio, fecha_fin);

-- ============================================
-- JOURNAL ENTRIES (Libro Diario)
-- ============================================
CREATE TABLE IF NOT EXISTS partida_diario (
    id_partida_diario UUID PRIMARY KEY,
    numero_partida INTEGER,
    fecha DATE,
    descripcion TEXT,
    concepto TEXT,
    total_debe DECIMAL(18,2),
    total_haber DECIMAL(18,2),
    tipo_partida VARCHAR(50), -- NORMAL, AJUSTE, CIERRE, APERTURA
    estado VARCHAR(50), -- BORRADOR, CONTABILIZADA, ANULADA
    referencia VARCHAR(200),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_contabilizacion TIMESTAMP,
    id_periodo_contable UUID REFERENCES periodo_contable(id_periodo_contable)
);

CREATE INDEX idx_partida_periodo ON partida_diario(id_periodo_contable);
CREATE INDEX idx_partida_fecha ON partida_diario(fecha);
CREATE INDEX idx_partida_estado ON partida_diario(estado);
CREATE INDEX idx_partida_tipo ON partida_diario(tipo_partida);

-- ============================================
-- JOURNAL ENTRY DETAILS (Detalles de Partida)
-- ============================================
CREATE TABLE IF NOT EXISTS detalle_partida_diario (
    id_detalle_partida UUID PRIMARY KEY,
    numero_linea INTEGER,
    debe DECIMAL(18,2),
    haber DECIMAL(18,2),
    descripcion TEXT,
    id_partida_diario UUID REFERENCES partida_diario(id_partida_diario),
    id_cuenta_contable UUID REFERENCES cuenta_contable(id_cuenta_contable)
);

CREATE INDEX idx_detalle_partida ON detalle_partida_diario(id_partida_diario);
CREATE INDEX idx_detalle_cuenta ON detalle_partida_diario(id_cuenta_contable);

-- ============================================
-- ADJUSTMENT ENTRIES (Partidas de Ajuste)
-- ============================================
CREATE TABLE IF NOT EXISTS partida_ajuste (
    id_partida_ajuste UUID PRIMARY KEY,
    numero_ajuste INTEGER,
    fecha DATE,
    descripcion TEXT,
    concepto TEXT,
    tipo_ajuste VARCHAR(50), -- DEPRECIACION, AMORTIZACION, PROVISION, REGULARIZACION, RECLASIFICACION, CORRECCION
    estado VARCHAR(50), -- PENDIENTE, APLICADO, ANULADO
    automatico BOOLEAN DEFAULT FALSE,
    monto DECIMAL(18,2),
    referencia VARCHAR(200),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_aplicacion TIMESTAMP,
    id_periodo_contable UUID REFERENCES periodo_contable(id_periodo_contable),
    id_cuenta_debe UUID REFERENCES cuenta_contable(id_cuenta_contable),
    id_cuenta_haber UUID REFERENCES cuenta_contable(id_cuenta_contable),
    id_partida_diario UUID REFERENCES partida_diario(id_partida_diario)
);

CREATE INDEX idx_ajuste_periodo ON partida_ajuste(id_periodo_contable);
CREATE INDEX idx_ajuste_estado ON partida_ajuste(estado);
CREATE INDEX idx_ajuste_tipo ON partida_ajuste(tipo_ajuste);

-- ============================================
-- GENERAL LEDGER (Libro Mayor)
-- ============================================
CREATE TABLE IF NOT EXISTS libro_mayor (
    id_libro_mayor UUID PRIMARY KEY,
    saldo_inicial DECIMAL(18,2) DEFAULT 0,
    total_debe DECIMAL(18,2) DEFAULT 0,
    total_haber DECIMAL(18,2) DEFAULT 0,
    saldo_final DECIMAL(18,2) DEFAULT 0,
    id_cuenta_contable UUID REFERENCES cuenta_contable(id_cuenta_contable),
    id_periodo_contable UUID REFERENCES periodo_contable(id_periodo_contable),
    UNIQUE(id_cuenta_contable, id_periodo_contable)
);

CREATE INDEX idx_mayor_periodo ON libro_mayor(id_periodo_contable);
CREATE INDEX idx_mayor_cuenta ON libro_mayor(id_cuenta_contable);

-- ============================================
-- TRIAL BALANCE (Balanza de Comprobación)
-- ============================================
CREATE TABLE IF NOT EXISTS balanza_comprobacion (
    id_balanza UUID PRIMARY KEY,
    saldo_inicial_debe DECIMAL(18,2) DEFAULT 0,
    saldo_inicial_haber DECIMAL(18,2) DEFAULT 0,
    movimientos_debe DECIMAL(18,2) DEFAULT 0,
    movimientos_haber DECIMAL(18,2) DEFAULT 0,
    saldo_final_debe DECIMAL(18,2) DEFAULT 0,
    saldo_final_haber DECIMAL(18,2) DEFAULT 0,
    fecha_generacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_cuenta_contable UUID REFERENCES cuenta_contable(id_cuenta_contable),
    id_periodo_contable UUID REFERENCES periodo_contable(id_periodo_contable)
);

CREATE INDEX idx_balanza_periodo ON balanza_comprobacion(id_periodo_contable);

-- ============================================
-- FINANCIAL STATEMENTS (Estados Financieros)
-- ============================================
CREATE TABLE IF NOT EXISTS estado_financiero (
    id_estado_financiero UUID PRIMARY KEY,
    tipo_estado VARCHAR(50), -- BALANCE_GENERAL, ESTADO_RESULTADOS
    fecha_generacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_corte DATE,
    descripcion TEXT,
    total_activos DECIMAL(18,2) DEFAULT 0,
    total_pasivos DECIMAL(18,2) DEFAULT 0,
    total_patrimonio DECIMAL(18,2) DEFAULT 0,
    total_ingresos DECIMAL(18,2) DEFAULT 0,
    total_gastos DECIMAL(18,2) DEFAULT 0,
    utilidad_neta DECIMAL(18,2) DEFAULT 0,
    estado VARCHAR(50), -- BORRADOR, DEFINITIVO
    id_periodo_contable UUID REFERENCES periodo_contable(id_periodo_contable)
);

CREATE INDEX idx_estado_periodo ON estado_financiero(id_periodo_contable);
CREATE INDEX idx_estado_tipo ON estado_financiero(tipo_estado);

-- ============================================
-- FINANCIAL STATEMENT DETAILS
-- ============================================
CREATE TABLE IF NOT EXISTS detalle_estado_financiero (
    id_detalle_estado UUID PRIMARY KEY,
    categoria VARCHAR(100),
    subcategoria VARCHAR(100),
    descripcion TEXT,
    monto DECIMAL(18,2) DEFAULT 0,
    orden INTEGER,
    id_estado_financiero UUID REFERENCES estado_financiero(id_estado_financiero),
    id_cuenta_contable UUID REFERENCES cuenta_contable(id_cuenta_contable)
);

CREATE INDEX idx_detalle_estado ON detalle_estado_financiero(id_estado_financiero);

-- ============================================
-- INVOICES (Facturas)
-- ============================================
CREATE TABLE IF NOT EXISTS factura (
    id_factura UUID PRIMARY KEY,
    numero_factura VARCHAR(50) UNIQUE,
    serie VARCHAR(10),
    fecha DATE,
    fecha_emision TIMESTAMP,
    subtotal DECIMAL(18,2) DEFAULT 0,
    iva DECIMAL(18,2) DEFAULT 0,
    total DECIMAL(18,2) DEFAULT 0,
    estado VARCHAR(50), -- BORRADOR, EMITIDA, ANULADA
    tipo_factura VARCHAR(50), -- CONSUMIDOR_FINAL, CREDITO_FISCAL
    nombre_cliente VARCHAR(200),
    nit_cliente VARCHAR(50),
    direccion_cliente TEXT,
    observaciones TEXT,
    id_socio UUID REFERENCES socio(id_socio)
);

CREATE INDEX idx_factura_fecha ON factura(fecha);
CREATE INDEX idx_factura_estado ON factura(estado);
CREATE INDEX idx_factura_socio ON factura(id_socio);

-- ============================================
-- INVOICE DETAILS (Detalles de Factura)
-- ============================================
CREATE TABLE IF NOT EXISTS detalle_factura (
    id_detalle_factura UUID PRIMARY KEY,
    numero_linea INTEGER,
    descripcion TEXT,
    cantidad DECIMAL(18,4) DEFAULT 1,
    precio_unitario DECIMAL(18,2) DEFAULT 0,
    subtotal DECIMAL(18,2) DEFAULT 0,
    id_factura UUID REFERENCES factura(id_factura)
);

CREATE INDEX idx_detalle_factura ON detalle_factura(id_factura);

-- ============================================
-- DAILY SALES REPORT (Reporte Diario de Ventas)
-- ============================================
CREATE TABLE IF NOT EXISTS reporte_ventas (
    id_reporte_ventas UUID PRIMARY KEY,
    fecha DATE UNIQUE,
    numero_facturas INTEGER DEFAULT 0,
    total_ventas_gravadas DECIMAL(18,2) DEFAULT 0,
    total_ventas_exentas DECIMAL(18,2) DEFAULT 0,
    total_iva DECIMAL(18,2) DEFAULT 0,
    total_ventas DECIMAL(18,2) DEFAULT 0,
    primera_factura VARCHAR(50),
    ultima_factura VARCHAR(50),
    facturas_anuladas INTEGER DEFAULT 0,
    fecha_generacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_reporte_fecha ON reporte_ventas(fecha);

-- ============================================
-- INITIAL DATA - Account Types
-- ============================================
-- Insert standard account types if they don't exist
INSERT INTO tipo_cuenta_contable (id_tipo_cuenta_contable, nombre) 
SELECT gen_random_uuid(), 'ACTIVO CORRIENTE' WHERE NOT EXISTS (SELECT 1 FROM tipo_cuenta_contable WHERE nombre = 'ACTIVO CORRIENTE');

INSERT INTO tipo_cuenta_contable (id_tipo_cuenta_contable, nombre) 
SELECT gen_random_uuid(), 'ACTIVO NO CORRIENTE' WHERE NOT EXISTS (SELECT 1 FROM tipo_cuenta_contable WHERE nombre = 'ACTIVO NO CORRIENTE');

INSERT INTO tipo_cuenta_contable (id_tipo_cuenta_contable, nombre) 
SELECT gen_random_uuid(), 'PASIVO CORRIENTE' WHERE NOT EXISTS (SELECT 1 FROM tipo_cuenta_contable WHERE nombre = 'PASIVO CORRIENTE');

INSERT INTO tipo_cuenta_contable (id_tipo_cuenta_contable, nombre) 
SELECT gen_random_uuid(), 'PASIVO NO CORRIENTE' WHERE NOT EXISTS (SELECT 1 FROM tipo_cuenta_contable WHERE nombre = 'PASIVO NO CORRIENTE');

INSERT INTO tipo_cuenta_contable (id_tipo_cuenta_contable, nombre) 
SELECT gen_random_uuid(), 'PATRIMONIO' WHERE NOT EXISTS (SELECT 1 FROM tipo_cuenta_contable WHERE nombre = 'PATRIMONIO');

INSERT INTO tipo_cuenta_contable (id_tipo_cuenta_contable, nombre) 
SELECT gen_random_uuid(), 'INGRESO' WHERE NOT EXISTS (SELECT 1 FROM tipo_cuenta_contable WHERE nombre = 'INGRESO');

INSERT INTO tipo_cuenta_contable (id_tipo_cuenta_contable, nombre) 
SELECT gen_random_uuid(), 'GASTO' WHERE NOT EXISTS (SELECT 1 FROM tipo_cuenta_contable WHERE nombre = 'GASTO');

INSERT INTO tipo_cuenta_contable (id_tipo_cuenta_contable, nombre) 
SELECT gen_random_uuid(), 'COSTO' WHERE NOT EXISTS (SELECT 1 FROM tipo_cuenta_contable WHERE nombre = 'COSTO');

-- ============================================
-- VALIDATION TRIGGERS
-- ============================================
-- Trigger to validate journal entry balance (debe = haber)
CREATE OR REPLACE FUNCTION validate_partida_balance()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.total_debe IS NOT NULL AND NEW.total_haber IS NOT NULL THEN
        -- Use tolerance for rounding differences in accounting calculations
        IF ABS(NEW.total_debe - NEW.total_haber) > 0.01 THEN
            RAISE EXCEPTION 'La partida no está balanceada: Debe (%) != Haber (%)', NEW.total_debe, NEW.total_haber;
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_validate_partida_balance ON partida_diario;
CREATE TRIGGER trg_validate_partida_balance
    BEFORE INSERT OR UPDATE ON partida_diario
    FOR EACH ROW
    WHEN (NEW.estado = 'CONTABILIZADA')
    EXECUTE FUNCTION validate_partida_balance();

-- ============================================
-- AUDIT COLUMNS
-- ============================================
-- Add audit columns to main tables if needed
-- ALTER TABLE partida_diario ADD COLUMN IF NOT EXISTS created_by VARCHAR(100);
-- ALTER TABLE partida_diario ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
-- ALTER TABLE partida_ajuste ADD COLUMN IF NOT EXISTS created_by VARCHAR(100);
-- ALTER TABLE partida_ajuste ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
