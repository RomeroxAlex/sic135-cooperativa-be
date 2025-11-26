# Módulo Contable - Documentación Técnica

## Descripción General

Este módulo implementa un sistema contable completo para una entidad bancaria cooperativa, siguiendo principios de coherencia bancaria, integridad contable, auditoría, control de períodos y consistencia en las transacciones.

## Estructura del Proyecto

```
src/main/java/ues/edu/sv/fmo/sic1352025/segundo/semestre/contabilidad/
├── boundary/rest/server/    # REST Resources (Endpoints)
├── control/                 # EJB Beans (Data Access Layer)
├── converter/              # Type converters
├── dto/                    # Data Transfer Objects
├── entity/                 # JPA Entities
├── enums/                  # Enumerations
├── exception/              # Custom exceptions
└── service/                # Business logic services
```

## Módulos Implementados

### 1. Catálogo de Cuentas (Account Catalog)
- **Endpoint:** `/v1/cuentas-contables`
- **Entidad:** `CuentaContable`
- Permite crear y gestionar el plan de cuentas contables.

### 2. Tipos de Cuenta Contable (Account Types)
- **Endpoint:** `/v1/tipo-cuentas-contables`
- **Entidad:** `TipoCuentaContable`
- Clasificación de cuentas (Activo, Pasivo, Patrimonio, Ingresos, Gastos).

### 3. Períodos Contables (Accounting Periods)
- **Endpoint:** `/v1/periodos-contables`
- **Entidad:** `PeriodoContable`
- Control de apertura y cierre de períodos contables.

### 4. Libro Diario (Journal Entries)
- **Endpoint:** `/v1/partidas-diario`
- **Entidad:** `PartidaDiario`, `DetallePartidaDiario`
- Registro de transacciones contables con validación debe=haber.

### 5. Partidas de Ajuste (Adjustment Entries) - **MÓDULO PRINCIPAL**
- **Endpoint:** `/v1/partidas-ajuste`
- **Entidad:** `PartidaAjuste`
- Ajustes manuales y automáticos:
  - DEPRECIACIÓN
  - AMORTIZACIÓN
  - PROVISIÓN
  - REGULARIZACIÓN
  - RECLASIFICACIÓN
  - CORRECCIÓN

### 6. Libro Mayor (General Ledger)
- **Endpoint:** `/v1/libro-mayor`
- **Entidad:** `LibroMayor`
- Mayorización de cuentas por período.

### 7. Balanza de Comprobación (Trial Balance)
- **Endpoint:** `/v1/balanza-comprobacion`
- **Entidad:** `BalanzaComprobacion`
- Generación y verificación de cuadratura contable.

### 8. Estados Financieros (Financial Statements)
- **Endpoint:** `/v1/estados-financieros`
- **Entidad:** `EstadoFinanciero`, `DetalleEstadoFinanciero`
- Balance General y Estado de Resultados.

### 9. Facturas Digitales (Digital Invoices)
- **Endpoint:** `/v1/facturas`
- **Entidad:** `Factura`, `DetalleFactura`
- Creación, emisión y anulación de facturas.

### 10. Reportes de Ventas (Daily Sales Reports)
- **Endpoint:** `/v1/facturas/reportes-diarios`
- **Entidad:** `ReporteVentas`
- Consolidación diaria de ventas.

## API Endpoints

### Períodos Contables
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/v1/periodos-contables` | Listar todos los períodos |
| GET | `/v1/periodos-contables/{id}` | Obtener período por ID |
| GET | `/v1/periodos-contables/activo` | Obtener período activo |
| POST | `/v1/periodos-contables` | Crear nuevo período |
| PUT | `/v1/periodos-contables/{id}/cerrar` | Cerrar período |
| PUT | `/v1/periodos-contables/{id}/activar/{activo}` | Activar/desactivar período |

### Partidas de Diario
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/v1/partidas-diario` | Listar todas las partidas |
| GET | `/v1/partidas-diario/{id}` | Obtener partida por ID |
| GET | `/v1/partidas-diario/periodo/{idPeriodo}` | Partidas por período |
| GET | `/v1/partidas-diario/{id}/detalles` | Detalles de una partida |
| POST | `/v1/partidas-diario` | Crear partida (valida debe=haber) |
| PUT | `/v1/partidas-diario/{id}/contabilizar` | Contabilizar partida |
| PUT | `/v1/partidas-diario/{id}/anular` | Anular partida |

### Partidas de Ajuste (MÓDULO PRINCIPAL)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/v1/partidas-ajuste` | Listar todos los ajustes |
| GET | `/v1/partidas-ajuste/{id}` | Obtener ajuste por ID |
| GET | `/v1/partidas-ajuste/periodo/{idPeriodo}` | Ajustes por período |
| GET | `/v1/partidas-ajuste/pendientes` | Ajustes pendientes |
| GET | `/v1/partidas-ajuste/tipo/{tipo}` | Ajustes por tipo |
| POST | `/v1/partidas-ajuste` | Crear ajuste manual |
| PUT | `/v1/partidas-ajuste/{id}/aplicar` | Aplicar ajuste (genera partida) |
| PUT | `/v1/partidas-ajuste/{id}/anular` | Anular ajuste |
| POST | `/v1/partidas-ajuste/depreciacion` | Generar depreciación automática |
| POST | `/v1/partidas-ajuste/provision` | Generar provisión automática |

### Libro Mayor
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/v1/libro-mayor/periodo/{idPeriodo}` | Mayorización por período |
| GET | `/v1/libro-mayor/periodo/{idPeriodo}/con-saldo` | Cuentas con movimiento |
| GET | `/v1/libro-mayor/cuenta/{idCuenta}` | Historial de una cuenta |
| GET | `/v1/libro-mayor/saldo/cuenta/{idCuenta}/periodo/{idPeriodo}` | Saldo de cuenta |
| POST | `/v1/libro-mayor/generar/{idPeriodo}` | Generar mayorización |

### Balanza de Comprobación
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/v1/balanza-comprobacion/periodo/{idPeriodo}` | Balanza por período |
| GET | `/v1/balanza-comprobacion/periodo/{idPeriodo}/totales` | Totales de balanza |
| GET | `/v1/balanza-comprobacion/periodo/{idPeriodo}/verificar` | Verificar cuadratura |
| POST | `/v1/balanza-comprobacion/generar/{idPeriodo}` | Generar balanza |

### Estados Financieros
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/v1/estados-financieros/periodo/{idPeriodo}` | Estados por período |
| GET | `/v1/estados-financieros/{id}` | Estado por ID |
| POST | `/v1/estados-financieros/balance-general/{idPeriodo}` | Generar Balance General |
| POST | `/v1/estados-financieros/estado-resultados/{idPeriodo}` | Generar Estado de Resultados |
| PUT | `/v1/estados-financieros/{id}/finalizar` | Finalizar estado |

### Facturas
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/v1/facturas` | Listar todas las facturas |
| GET | `/v1/facturas/{id}` | Obtener factura por ID |
| POST | `/v1/facturas` | Crear factura |
| PUT | `/v1/facturas/{id}/emitir` | Emitir factura |
| PUT | `/v1/facturas/{id}/anular` | Anular factura |
| POST | `/v1/facturas/reporte-diario` | Generar reporte diario |
| GET | `/v1/facturas/reporte-diario?fecha={fecha}` | Obtener reporte diario |
| GET | `/v1/facturas/reportes-diarios` | Listar todos los reportes |

## Validaciones Contables

### Cuadratura de Partidas
- Toda partida contable debe cumplir: `total_debe = total_haber`
- Validación automática al crear/contabilizar partidas
- Trigger de base de datos para doble validación

### Control de Períodos
- Solo se pueden registrar partidas en períodos activos y no cerrados
- Los períodos cerrados no permiten modificaciones

### Integridad de Ajustes
- Los ajustes pendientes deben aplicarse antes del cierre de período
- Al aplicar un ajuste, se genera automáticamente una partida de diario

## Ejemplos de Request/Response

### Crear Partida de Diario
```json
POST /v1/partidas-diario
{
  "fecha": "2025-01-15",
  "descripcion": "Venta de servicios financieros",
  "concepto": "Comisiones bancarias",
  "tipoPartida": "NORMAL",
  "detalles": [
    {
      "idCuentaContable": "uuid-cuenta-caja",
      "debe": 1000.00,
      "descripcion": "Ingreso por comisiones"
    },
    {
      "idCuentaContable": "uuid-cuenta-ingresos",
      "haber": 1000.00,
      "descripcion": "Comisiones por servicios"
    }
  ]
}
```

### Crear Partida de Ajuste
```json
POST /v1/partidas-ajuste
{
  "fecha": "2025-01-31",
  "descripcion": "Depreciación mensual equipo de cómputo",
  "concepto": "Ajuste por depreciación",
  "tipoAjuste": "DEPRECIACION",
  "automatico": false,
  "monto": 500.00,
  "idCuentaDebe": "uuid-gasto-depreciacion",
  "idCuentaHaber": "uuid-depreciacion-acumulada"
}
```

### Respuesta de Balanza de Comprobación (Totales)
```json
GET /v1/balanza-comprobacion/periodo/{id}/totales
{
  "saldoInicialDebe": 50000.00,
  "saldoInicialHaber": 50000.00,
  "movimientosDebe": 25000.00,
  "movimientosHaber": 25000.00,
  "saldoFinalDebe": 75000.00,
  "saldoFinalHaber": 75000.00
}
```

## Flujo Técnico del Módulo de Ajustes

```
┌─────────────────────────────────────────────────────────────────┐
│                  FLUJO DE PARTIDAS DE AJUSTE                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. CREAR AJUSTE (Manual o Automático)                          │
│     │                                                           │
│     ▼                                                           │
│  ┌─────────────────┐                                            │
│  │ Estado: PENDIENTE │                                          │
│  └────────┬────────┘                                            │
│           │                                                     │
│           ▼                                                     │
│  2. APLICAR AJUSTE                                              │
│     │                                                           │
│     ├─► Valida período activo                                   │
│     ├─► Valida cuentas existen                                  │
│     ├─► Genera Partida de Diario tipo AJUSTE                    │
│     │                                                           │
│     ▼                                                           │
│  ┌─────────────────┐     ┌─────────────────────────────────┐    │
│  │ Estado: APLICADO │ ──► │ Partida Diario CONTABILIZADA   │    │
│  └─────────────────┘     │  - debe = monto                 │    │
│                          │  - haber = monto                │    │
│                          │  - tipo = AJUSTE                │    │
│                          └─────────────────────────────────┘    │
│                                                                 │
│  3. MAYORIZACIÓN                                                │
│     │                                                           │
│     ▼                                                           │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │ Libro Mayor actualizado con movimientos de ajuste       │    │
│  │  - Suma débitos/créditos por cuenta                     │    │
│  │  - Calcula saldos finales                               │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                 │
│  4. BALANZA DE COMPROBACIÓN                                     │
│     │                                                           │
│     ▼                                                           │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │ Verifica: debe = haber en todas las columnas            │    │
│  │  - Saldos iniciales                                     │    │
│  │  - Movimientos del período                              │    │
│  │  - Saldos finales                                       │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                 │
│  5. ESTADOS FINANCIEROS                                         │
│     │                                                           │
│     ▼                                                           │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │ Balance General: Activo = Pasivo + Patrimonio           │    │
│  │ Estado Resultados: Ingresos - Gastos = Utilidad         │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## Checklist de Pruebas Manuales

### Pruebas del Libro Diario
- [ ] Crear partida balanceada (debe = haber)
- [ ] Intentar crear partida desbalanceada (debe fallar)
- [ ] Contabilizar partida en estado borrador
- [ ] Anular partida contabilizada
- [ ] Verificar que partidas anuladas no afectan saldos

### Pruebas de Partidas de Ajuste
- [ ] Crear ajuste manual de depreciación
- [ ] Crear ajuste manual de provisión
- [ ] Generar depreciación automática
- [ ] Aplicar ajuste y verificar partida generada
- [ ] Anular ajuste aplicado
- [ ] Verificar impacto en libro mayor

### Pruebas de Mayorización
- [ ] Generar mayorización de período
- [ ] Verificar saldos iniciales desde período anterior
- [ ] Verificar cálculo correcto de saldos finales
- [ ] Consultar historial de cuenta específica

### Pruebas de Balanza de Comprobación
- [ ] Generar balanza de período
- [ ] Verificar que debe = haber en todas las columnas
- [ ] Comparar totales con mayorización

### Pruebas de Estados Financieros
- [ ] Generar Balance General
- [ ] Verificar ecuación: Activos = Pasivos + Patrimonio
- [ ] Generar Estado de Resultados
- [ ] Verificar cálculo de utilidad neta
- [ ] Finalizar estado financiero

### Pruebas de Facturas
- [ ] Crear factura con múltiples líneas
- [ ] Verificar cálculo de IVA (13%)
- [ ] Emitir factura
- [ ] Anular factura
- [ ] Generar reporte diario de ventas

## Consideraciones de Seguridad

- Los endpoints requieren autenticación (a implementar según requerimientos)
- Las operaciones contables dejan registro de auditoría
- Los períodos cerrados son inmutables
- Las partidas contabilizadas no pueden modificarse

## Configuración

### Datasource
```xml
jdbc/contabilidad - PostgreSQL connection pool
```

### JPA
```xml
Persistence Unit: ContabilidadPU
Transaction Type: JTA
```
