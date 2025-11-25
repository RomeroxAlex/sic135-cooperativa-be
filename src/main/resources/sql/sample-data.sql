-- Sample Data for SIC135 Contabilidad
-- PostgreSQL Sample Data

-- Insert sample catalog accounts (Chart of Accounts)
INSERT INTO catalogo_cuenta (id_catalogo_cuenta, codigo, nombre, tipo, nivel, descripcion, activo, fecha_creacion) VALUES
-- Activos
('a1000000-0000-0000-0000-000000000001', '1', 'ACTIVO', 'ACTIVO', 1, 'Activos totales de la empresa', true, NOW()),
('a1100000-0000-0000-0000-000000000001', '11', 'ACTIVO CORRIENTE', 'ACTIVO', 2, 'Activos corrientes', true, NOW()),
('a1110000-0000-0000-0000-000000000001', '1101', 'CAJA', 'ACTIVO', 3, 'Efectivo en caja', true, NOW()),
('a1110001-0000-0000-0000-000000000001', '110101', 'Caja General', 'ACTIVO', 4, 'Caja general de la empresa', true, NOW()),
('a1110002-0000-0000-0000-000000000001', '110102', 'Caja Chica', 'ACTIVO', 4, 'Fondo de caja chica', true, NOW()),
('a1120000-0000-0000-0000-000000000001', '1102', 'BANCOS', 'ACTIVO', 3, 'Cuentas bancarias', true, NOW()),
('a1120001-0000-0000-0000-000000000001', '110201', 'Banco Agrícola', 'ACTIVO', 4, 'Cuenta en Banco Agrícola', true, NOW()),
('a1130000-0000-0000-0000-000000000001', '1103', 'CUENTAS POR COBRAR', 'ACTIVO', 3, 'Cuentas por cobrar', true, NOW()),
('a1140000-0000-0000-0000-000000000001', '1104', 'INVENTARIOS', 'ACTIVO', 3, 'Inventarios de mercadería', true, NOW()),

-- Pasivos
('p2000000-0000-0000-0000-000000000001', '2', 'PASIVO', 'PASIVO', 1, 'Pasivos totales de la empresa', true, NOW()),
('p2100000-0000-0000-0000-000000000001', '21', 'PASIVO CORRIENTE', 'PASIVO', 2, 'Pasivos corrientes', true, NOW()),
('p2110000-0000-0000-0000-000000000001', '2101', 'CUENTAS POR PAGAR', 'PASIVO', 3, 'Cuentas por pagar a proveedores', true, NOW()),
('p2120000-0000-0000-0000-000000000001', '2102', 'IMPUESTOS POR PAGAR', 'PASIVO', 3, 'Impuestos pendientes', true, NOW()),

-- Patrimonio
('c3000000-0000-0000-0000-000000000001', '3', 'PATRIMONIO', 'PATRIMONIO', 1, 'Patrimonio de la empresa', true, NOW()),
('c3100000-0000-0000-0000-000000000001', '31', 'CAPITAL SOCIAL', 'PATRIMONIO', 2, 'Capital social', true, NOW()),
('c3200000-0000-0000-0000-000000000001', '32', 'RESULTADOS ACUMULADOS', 'PATRIMONIO', 2, 'Resultados de ejercicios anteriores', true, NOW()),

-- Ingresos
('i4000000-0000-0000-0000-000000000001', '4', 'INGRESOS', 'INGRESO', 1, 'Ingresos de operación', true, NOW()),
('i4100000-0000-0000-0000-000000000001', '41', 'INGRESOS POR VENTAS', 'INGRESO', 2, 'Ingresos por ventas', true, NOW()),
('i4110000-0000-0000-0000-000000000001', '4101', 'VENTAS DE MERCADERÍA', 'INGRESO', 3, 'Ventas de productos', true, NOW()),
('i4120000-0000-0000-0000-000000000001', '4102', 'VENTAS DE SERVICIOS', 'INGRESO', 3, 'Ingresos por servicios', true, NOW()),

-- Costos
('o5000000-0000-0000-0000-000000000001', '5', 'COSTOS', 'COSTO', 1, 'Costos de operación', true, NOW()),
('o5100000-0000-0000-0000-000000000001', '51', 'COSTO DE VENTAS', 'COSTO', 2, 'Costo de mercadería vendida', true, NOW()),

-- Gastos
('g6000000-0000-0000-0000-000000000001', '6', 'GASTOS', 'GASTO', 1, 'Gastos de operación', true, NOW()),
('g6100000-0000-0000-0000-000000000001', '61', 'GASTOS DE ADMINISTRACIÓN', 'GASTO', 2, 'Gastos administrativos', true, NOW()),
('g6110000-0000-0000-0000-000000000001', '6101', 'SUELDOS Y SALARIOS', 'GASTO', 3, 'Gastos de personal', true, NOW()),
('g6120000-0000-0000-0000-000000000001', '6102', 'SERVICIOS BÁSICOS', 'GASTO', 3, 'Agua, luz, teléfono', true, NOW()),
('g6200000-0000-0000-0000-000000000001', '62', 'GASTOS DE VENTA', 'GASTO', 2, 'Gastos relacionados a ventas', true, NOW());

-- Update parent references
UPDATE catalogo_cuenta SET id_cuenta_padre = 'a1000000-0000-0000-0000-000000000001' WHERE codigo = '11';
UPDATE catalogo_cuenta SET id_cuenta_padre = 'a1100000-0000-0000-0000-000000000001' WHERE codigo IN ('1101', '1102', '1103', '1104');
UPDATE catalogo_cuenta SET id_cuenta_padre = 'a1110000-0000-0000-0000-000000000001' WHERE codigo IN ('110101', '110102');
UPDATE catalogo_cuenta SET id_cuenta_padre = 'a1120000-0000-0000-0000-000000000001' WHERE codigo = '110201';
UPDATE catalogo_cuenta SET id_cuenta_padre = 'p2000000-0000-0000-0000-000000000001' WHERE codigo = '21';
UPDATE catalogo_cuenta SET id_cuenta_padre = 'p2100000-0000-0000-0000-000000000001' WHERE codigo IN ('2101', '2102');
UPDATE catalogo_cuenta SET id_cuenta_padre = 'c3000000-0000-0000-0000-000000000001' WHERE codigo IN ('31', '32');
UPDATE catalogo_cuenta SET id_cuenta_padre = 'i4000000-0000-0000-0000-000000000001' WHERE codigo = '41';
UPDATE catalogo_cuenta SET id_cuenta_padre = 'i4100000-0000-0000-0000-000000000001' WHERE codigo IN ('4101', '4102');
UPDATE catalogo_cuenta SET id_cuenta_padre = 'o5000000-0000-0000-0000-000000000001' WHERE codigo = '51';
UPDATE catalogo_cuenta SET id_cuenta_padre = 'g6000000-0000-0000-0000-000000000001' WHERE codigo IN ('61', '62');
UPDATE catalogo_cuenta SET id_cuenta_padre = 'g6100000-0000-0000-0000-000000000001' WHERE codigo IN ('6101', '6102');

-- Insert sample manual
INSERT INTO manual_cuenta (id_manual_cuenta, titulo, contenido, version, fecha_creacion, activo) VALUES
('m0000001-0000-0000-0000-000000000001', 'Manual de Contabilidad', 
'# Manual de Contabilidad

## 1. Introducción
Este manual establece las políticas y procedimientos contables de la cooperativa.

## 2. Catálogo de Cuentas
El catálogo de cuentas está estructurado de la siguiente manera:
- 1: Activos
- 2: Pasivos
- 3: Patrimonio
- 4: Ingresos
- 5: Costos
- 6: Gastos

## 3. Políticas Contables
- Los registros se realizan en base devengado
- Moneda funcional: Dólares americanos (USD)
- Ejercicio fiscal: 1 de enero al 31 de diciembre
', '1.0', NOW(), true);

-- Insert sample journal entry
INSERT INTO partida_diario (id_partida_diario, numero_partida, fecha, descripcion, referencia, estado, total_debe, total_haber, fecha_creacion) VALUES
('pd000001-0000-0000-0000-000000000001', 1, CURRENT_DATE, 'Partida de apertura inicial', 'AP-001', 'POSTEADA', 10000.00, 10000.00, NOW());

INSERT INTO linea_partida (id_linea_partida, id_partida_diario, id_catalogo_cuenta, descripcion, debe, haber, orden) VALUES
('lp000001-0000-0000-0000-000000000001', 'pd000001-0000-0000-0000-000000000001', 'a1110001-0000-0000-0000-000000000001', 'Saldo inicial caja', 5000.00, 0, 1),
('lp000002-0000-0000-0000-000000000001', 'pd000001-0000-0000-0000-000000000001', 'a1120001-0000-0000-0000-000000000001', 'Saldo inicial banco', 5000.00, 0, 2),
('lp000003-0000-0000-0000-000000000001', 'pd000001-0000-0000-0000-000000000001', 'c3100000-0000-0000-0000-000000000001', 'Capital social', 0, 10000.00, 3);

-- Insert sample invoice
INSERT INTO factura (id_factura, numero_factura, cliente_nombre, cliente_nit, fecha, subtotal, impuestos, total, estado, fecha_creacion) VALUES
('f0000001-0000-0000-0000-000000000001', 'FAC-202401-000001', 'Cliente Ejemplo S.A.', '0614-010101-101-1', CURRENT_DATE, 100.00, 13.00, 113.00, 'EMITIDA', NOW());

INSERT INTO item_factura (id_item_factura, id_factura, codigo_producto, descripcion, cantidad, precio_unitario, subtotal, orden) VALUES
('if000001-0000-0000-0000-000000000001', 'f0000001-0000-0000-0000-000000000001', 'PROD001', 'Producto de ejemplo', 2, 50.00, 100.00, 1);
