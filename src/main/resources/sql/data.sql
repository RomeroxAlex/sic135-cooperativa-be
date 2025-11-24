INSERT INTO cuentas (codigo, nombre, tipo, nivel, descripcion, cuenta_padre_id) VALUES
('1', 'Activos', 'ACTIVO', 1, 'Recursos controlados por la empresa', NULL),
('101', 'Activo Corriente', 'ACTIVO', 2, 'Activos que se espera convertir en efectivo en menos de un año', 1),
('10101', 'Caja y Bancos', 'ACTIVO', 3, 'Dinero en efectivo y en cuentas bancarias', 2),
('2', 'Pasivos', 'PASIVO', 1, 'Obligaciones de la empresa', NULL),
('201', 'Pasivo Corriente', 'PASIVO', 2, 'Deudas y obligaciones a corto plazo', 4),
('3', 'Patrimonio', 'PATRIMONIO', 1, 'Capital de los accionistas', NULL),
('4', 'Ingresos', 'INGRESO', 1, 'Ingresos por ventas y otros', NULL),
('5', 'Gastos', 'GASTO', 1, 'Gastos operativos y administrativos', NULL);
