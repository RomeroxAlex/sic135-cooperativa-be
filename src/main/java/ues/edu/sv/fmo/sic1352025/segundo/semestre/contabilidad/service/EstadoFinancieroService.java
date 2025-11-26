package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CuentaContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.DetalleEstadoFinancieroBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.EstadoFinancieroBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.LibroMayorBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.PeriodoContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.DetalleEstadoFinanciero;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.EstadoFinanciero;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.LibroMayor;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PeriodoContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.TipoEstadoFinanciero;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ContabilidadException;

/**
 * Service for generating financial statements (Estados Financieros).
 * - Balance General (Balance Sheet)
 * - Estado de Resultados (Income Statement)
 */
@Stateless
public class EstadoFinancieroService {

    @Inject
    EstadoFinancieroBean estadoFinancieroBean;
    
    @Inject
    DetalleEstadoFinancieroBean detalleEstadoFinancieroBean;
    
    @Inject
    PeriodoContableBean periodoContableBean;
    
    @Inject
    CuentaContableBean cuentaContableBean;
    
    @Inject
    LibroMayorBean libroMayorBean;
    
    @Inject
    LibroMayorService libroMayorService;

    /**
     * Generate Balance Sheet (Balance General) for a period.
     * Shows: Assets = Liabilities + Equity
     * @param idPeriodo period ID
     * @return generated statement ID
     * @throws ContabilidadException if validation fails
     */
    public UUID generarBalanceGeneral(UUID idPeriodo) throws ContabilidadException {
        PeriodoContable periodo = periodoContableBean.findById(idPeriodo);
        if (periodo == null) {
            throw new ContabilidadException("Período contable no encontrado");
        }
        
        // Regenerate ledger first
        libroMayorService.generarMayorizacion(idPeriodo);
        
        // Create financial statement
        UUID idEstado = UUID.randomUUID();
        EstadoFinanciero estado = new EstadoFinanciero(idEstado);
        estado.setTipoEstado(TipoEstadoFinanciero.BALANCE_GENERAL.name());
        estado.setFechaGeneracion(new Date());
        estado.setFechaCorte(periodo.getFechaFin());
        estado.setDescripcion("Balance General al " + periodo.getFechaFin());
        estado.setEstado("BORRADOR");
        estado.setIdPeriodoContable(periodo);
        
        BigDecimal totalActivos = BigDecimal.ZERO;
        BigDecimal totalPasivos = BigDecimal.ZERO;
        BigDecimal totalPatrimonio = BigDecimal.ZERO;
        
        // Get all accounts
        List<CuentaContable> cuentas = cuentaContableBean.findAll();
        int orden = 1;
        
        for (CuentaContable cuenta : cuentas) {
            LibroMayor libroMayor = libroMayorBean.findByCuentaYPeriodo(cuenta, periodo);
            if (libroMayor == null || libroMayor.getSaldoFinal() == null) {
                continue;
            }
            
            BigDecimal saldo = libroMayor.getSaldoFinal();
            if (saldo.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            
            // Classify by account type (based on first digit of code or type)
            String tipoCuenta = cuenta.getIdTipoCuentaContable() != null 
                ? cuenta.getIdTipoCuentaContable().getNombre() 
                : "";
            
            String categoria = clasificarCuenta(tipoCuenta, cuenta.getCodigo().toString());
            
            // Create detail line
            DetalleEstadoFinanciero detalle = new DetalleEstadoFinanciero(UUID.randomUUID());
            detalle.setIdEstadoFinanciero(estado);
            detalle.setIdCuentaContable(cuenta);
            detalle.setCategoria(categoria);
            detalle.setDescripcion(cuenta.getNombre());
            detalle.setMonto(saldo.abs());
            detalle.setOrden(orden++);
            
            // Accumulate totals
            if (categoria.startsWith("ACTIVO")) {
                totalActivos = totalActivos.add(saldo.abs());
            } else if (categoria.startsWith("PASIVO")) {
                totalPasivos = totalPasivos.add(saldo.abs());
            } else if (categoria.startsWith("PATRIMONIO") || categoria.startsWith("CAPITAL")) {
                totalPatrimonio = totalPatrimonio.add(saldo.abs());
            }
            
            detalleEstadoFinancieroBean.persistEntity(detalle);
        }
        
        estado.setTotalActivos(totalActivos);
        estado.setTotalPasivos(totalPasivos);
        estado.setTotalPatrimonio(totalPatrimonio);
        
        estadoFinancieroBean.persistEntity(estado);
        
        return idEstado;
    }

    /**
     * Generate Income Statement (Estado de Resultados) for a period.
     * Shows: Revenue - Expenses = Net Income
     * @param idPeriodo period ID
     * @return generated statement ID
     * @throws ContabilidadException if validation fails
     */
    public UUID generarEstadoResultados(UUID idPeriodo) throws ContabilidadException {
        PeriodoContable periodo = periodoContableBean.findById(idPeriodo);
        if (periodo == null) {
            throw new ContabilidadException("Período contable no encontrado");
        }
        
        // Regenerate ledger first
        libroMayorService.generarMayorizacion(idPeriodo);
        
        // Create financial statement
        UUID idEstado = UUID.randomUUID();
        EstadoFinanciero estado = new EstadoFinanciero(idEstado);
        estado.setTipoEstado(TipoEstadoFinanciero.ESTADO_RESULTADOS.name());
        estado.setFechaGeneracion(new Date());
        estado.setFechaCorte(periodo.getFechaFin());
        estado.setDescripcion("Estado de Resultados del " + periodo.getFechaInicio() + " al " + periodo.getFechaFin());
        estado.setEstado("BORRADOR");
        estado.setIdPeriodoContable(periodo);
        
        BigDecimal totalIngresos = BigDecimal.ZERO;
        BigDecimal totalGastos = BigDecimal.ZERO;
        
        // Get all accounts
        List<CuentaContable> cuentas = cuentaContableBean.findAll();
        int orden = 1;
        
        for (CuentaContable cuenta : cuentas) {
            LibroMayor libroMayor = libroMayorBean.findByCuentaYPeriodo(cuenta, periodo);
            if (libroMayor == null || libroMayor.getSaldoFinal() == null) {
                continue;
            }
            
            BigDecimal saldo = libroMayor.getSaldoFinal();
            if (saldo.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            
            String tipoCuenta = cuenta.getIdTipoCuentaContable() != null 
                ? cuenta.getIdTipoCuentaContable().getNombre() 
                : "";
            
            String categoria = clasificarCuenta(tipoCuenta, cuenta.getCodigo().toString());
            
            // Only include income and expense accounts
            if (!categoria.startsWith("INGRESO") && !categoria.startsWith("GASTO") && !categoria.startsWith("COSTO")) {
                continue;
            }
            
            // Create detail line
            DetalleEstadoFinanciero detalle = new DetalleEstadoFinanciero(UUID.randomUUID());
            detalle.setIdEstadoFinanciero(estado);
            detalle.setIdCuentaContable(cuenta);
            detalle.setCategoria(categoria);
            detalle.setDescripcion(cuenta.getNombre());
            detalle.setMonto(saldo.abs());
            detalle.setOrden(orden++);
            
            // Accumulate totals
            if (categoria.startsWith("INGRESO")) {
                totalIngresos = totalIngresos.add(saldo.abs());
            } else {
                totalGastos = totalGastos.add(saldo.abs());
            }
            
            detalleEstadoFinancieroBean.persistEntity(detalle);
        }
        
        estado.setTotalIngresos(totalIngresos);
        estado.setTotalGastos(totalGastos);
        estado.setUtilidadNeta(totalIngresos.subtract(totalGastos));
        
        estadoFinancieroBean.persistEntity(estado);
        
        return idEstado;
    }

    /**
     * Classify account into balance sheet or income statement category.
     */
    private String clasificarCuenta(String tipoCuenta, String codigo) {
        String tipoUpper = tipoCuenta.toUpperCase();
        
        if (tipoUpper.contains("ACTIVO")) {
            if (tipoUpper.contains("CORRIENTE") || tipoUpper.contains("CIRCULANTE")) {
                return "ACTIVO_CORRIENTE";
            }
            return "ACTIVO_NO_CORRIENTE";
        }
        
        if (tipoUpper.contains("PASIVO")) {
            if (tipoUpper.contains("CORRIENTE") || tipoUpper.contains("CIRCULANTE")) {
                return "PASIVO_CORRIENTE";
            }
            return "PASIVO_NO_CORRIENTE";
        }
        
        if (tipoUpper.contains("PATRIMONIO") || tipoUpper.contains("CAPITAL")) {
            return "PATRIMONIO";
        }
        
        if (tipoUpper.contains("INGRESO") || tipoUpper.contains("VENTA")) {
            return "INGRESO";
        }
        
        if (tipoUpper.contains("GASTO") || tipoUpper.contains("COSTO")) {
            return "GASTO";
        }
        
        // Fallback based on account code pattern (standard chart of accounts)
        if (codigo != null && codigo.length() > 0) {
            char first = codigo.charAt(0);
            switch (first) {
                case '1': return "ACTIVO_CORRIENTE";
                case '2': return "PASIVO_CORRIENTE";
                case '3': return "PATRIMONIO";
                case '4': return "INGRESO";
                case '5': return "GASTO";
                case '6': return "COSTO";
                default: return "OTROS";
            }
        }
        
        return "OTROS";
    }

    /**
     * Get financial statements for a period.
     * @param idPeriodo period ID
     * @return list of financial statements
     */
    public List<EstadoFinanciero> findByPeriodo(UUID idPeriodo) {
        PeriodoContable periodo = periodoContableBean.findById(idPeriodo);
        if (periodo == null) {
            return new ArrayList<>();
        }
        return estadoFinancieroBean.findByPeriodo(periodo);
    }

    /**
     * Get financial statement by ID.
     * @param idEstado statement ID
     * @return financial statement or null
     */
    public EstadoFinanciero findById(UUID idEstado) {
        return estadoFinancieroBean.findById(idEstado);
    }

    /**
     * Finalize a financial statement (change status to DEFINITIVO).
     * @param idEstado statement ID
     * @throws ContabilidadException if validation fails
     */
    public void finalizarEstado(UUID idEstado) throws ContabilidadException {
        EstadoFinanciero estado = estadoFinancieroBean.findById(idEstado);
        if (estado == null) {
            throw new ContabilidadException("Estado financiero no encontrado");
        }
        
        // Validate balance sheet equation if applicable
        if (TipoEstadoFinanciero.BALANCE_GENERAL.name().equals(estado.getTipoEstado())) {
            if (!estado.balanceSheetEquationHolds()) {
                throw new ContabilidadException("El balance general no cuadra: Activos != Pasivos + Patrimonio");
            }
        }
        
        estado.setEstado("DEFINITIVO");
        estadoFinancieroBean.updateEntity(estado);
    }
}
