package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.BalanzaComprobacionBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CuentaContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.LibroMayorBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.PeriodoContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.BalanzaComprobacion;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.LibroMayor;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PeriodoContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.NaturalezaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ContabilidadException;

/**
 * Service for generating trial balances (Balanza de Comprobación).
 * Validates that total debits equal total credits.
 */
@Stateless
public class BalanzaComprobacionService {

    @Inject
    BalanzaComprobacionBean balanzaComprobacionBean;
    
    @Inject
    PeriodoContableBean periodoContableBean;
    
    @Inject
    CuentaContableBean cuentaContableBean;
    
    @Inject
    LibroMayorBean libroMayorBean;
    
    @Inject
    LibroMayorService libroMayorService;

    /**
     * Generate trial balance for a period.
     * First regenerates the general ledger, then creates trial balance entries.
     * @param idPeriodo period ID
     * @return validation result (true if balanced)
     * @throws ContabilidadException if validation fails
     */
    public boolean generarBalanzaComprobacion(UUID idPeriodo) throws ContabilidadException {
        PeriodoContable periodo = periodoContableBean.findById(idPeriodo);
        if (periodo == null) {
            throw new ContabilidadException("Período contable no encontrado");
        }
        
        // First, regenerate the general ledger
        libroMayorService.generarMayorizacion(idPeriodo);
        
        // Delete existing trial balance entries for this period
        balanzaComprobacionBean.deleteByPeriodo(periodo);
        
        // Get all accounts with movements
        List<CuentaContable> cuentas = cuentaContableBean.findAll();
        if (cuentas == null || cuentas.isEmpty()) {
            throw new ContabilidadException("No hay cuentas contables registradas");
        }
        
        Date fechaGeneracion = new Date();
        BigDecimal totalSaldoInicialDebe = BigDecimal.ZERO;
        BigDecimal totalSaldoInicialHaber = BigDecimal.ZERO;
        BigDecimal totalMovimientosDebe = BigDecimal.ZERO;
        BigDecimal totalMovimientosHaber = BigDecimal.ZERO;
        BigDecimal totalSaldoFinalDebe = BigDecimal.ZERO;
        BigDecimal totalSaldoFinalHaber = BigDecimal.ZERO;
        
        for (CuentaContable cuenta : cuentas) {
            LibroMayor libroMayor = libroMayorBean.findByCuentaYPeriodo(cuenta, periodo);
            
            // Skip accounts with no movements
            if (libroMayor == null) {
                continue;
            }
            
            boolean esDeudora = NaturalezaContable.DEUDORA.name().equals(cuenta.getNaturaleza());
            
            // Create trial balance entry
            BalanzaComprobacion balanza = new BalanzaComprobacion(UUID.randomUUID());
            balanza.setIdCuentaContable(cuenta);
            balanza.setIdPeriodoContable(periodo);
            balanza.setFechaGeneracion(fechaGeneracion);
            
            // Set initial balance in debit or credit column based on account nature
            BigDecimal saldoInicial = libroMayor.getSaldoInicial() != null ? libroMayor.getSaldoInicial() : BigDecimal.ZERO;
            if (esDeudora) {
                if (saldoInicial.compareTo(BigDecimal.ZERO) >= 0) {
                    balanza.setSaldoInicialDebe(saldoInicial);
                    balanza.setSaldoInicialHaber(BigDecimal.ZERO);
                } else {
                    balanza.setSaldoInicialDebe(BigDecimal.ZERO);
                    balanza.setSaldoInicialHaber(saldoInicial.abs());
                }
            } else {
                if (saldoInicial.compareTo(BigDecimal.ZERO) >= 0) {
                    balanza.setSaldoInicialDebe(BigDecimal.ZERO);
                    balanza.setSaldoInicialHaber(saldoInicial);
                } else {
                    balanza.setSaldoInicialDebe(saldoInicial.abs());
                    balanza.setSaldoInicialHaber(BigDecimal.ZERO);
                }
            }
            
            // Set movements
            balanza.setMovimientosDebe(libroMayor.getTotalDebe() != null ? libroMayor.getTotalDebe() : BigDecimal.ZERO);
            balanza.setMovimientosHaber(libroMayor.getTotalHaber() != null ? libroMayor.getTotalHaber() : BigDecimal.ZERO);
            
            // Set final balance in debit or credit column based on account nature
            BigDecimal saldoFinal = libroMayor.getSaldoFinal() != null ? libroMayor.getSaldoFinal() : BigDecimal.ZERO;
            if (esDeudora) {
                if (saldoFinal.compareTo(BigDecimal.ZERO) >= 0) {
                    balanza.setSaldoFinalDebe(saldoFinal);
                    balanza.setSaldoFinalHaber(BigDecimal.ZERO);
                } else {
                    balanza.setSaldoFinalDebe(BigDecimal.ZERO);
                    balanza.setSaldoFinalHaber(saldoFinal.abs());
                }
            } else {
                if (saldoFinal.compareTo(BigDecimal.ZERO) >= 0) {
                    balanza.setSaldoFinalDebe(BigDecimal.ZERO);
                    balanza.setSaldoFinalHaber(saldoFinal);
                } else {
                    balanza.setSaldoFinalDebe(saldoFinal.abs());
                    balanza.setSaldoFinalHaber(BigDecimal.ZERO);
                }
            }
            
            balanzaComprobacionBean.persistEntity(balanza);
            
            // Accumulate totals
            totalSaldoInicialDebe = totalSaldoInicialDebe.add(balanza.getSaldoInicialDebe());
            totalSaldoInicialHaber = totalSaldoInicialHaber.add(balanza.getSaldoInicialHaber());
            totalMovimientosDebe = totalMovimientosDebe.add(balanza.getMovimientosDebe());
            totalMovimientosHaber = totalMovimientosHaber.add(balanza.getMovimientosHaber());
            totalSaldoFinalDebe = totalSaldoFinalDebe.add(balanza.getSaldoFinalDebe());
            totalSaldoFinalHaber = totalSaldoFinalHaber.add(balanza.getSaldoFinalHaber());
        }
        
        // Validate balance - all columns should be equal
        boolean balanced = totalSaldoInicialDebe.compareTo(totalSaldoInicialHaber) == 0
            && totalMovimientosDebe.compareTo(totalMovimientosHaber) == 0
            && totalSaldoFinalDebe.compareTo(totalSaldoFinalHaber) == 0;
        
        return balanced;
    }

    /**
     * Get trial balance entries for a period.
     * @param idPeriodo period ID
     * @return list of trial balance entries
     */
    public List<BalanzaComprobacion> findByPeriodo(UUID idPeriodo) {
        PeriodoContable periodo = periodoContableBean.findById(idPeriodo);
        if (periodo == null) {
            return new ArrayList<>();
        }
        return balanzaComprobacionBean.findByPeriodo(periodo);
    }

    /**
     * Get totals for trial balance validation.
     * @param idPeriodo period ID
     * @return array with totals [saldoInicialDebe, saldoInicialHaber, movDebe, movHaber, saldoFinalDebe, saldoFinalHaber]
     */
    public BigDecimal[] getTotalesBalanza(UUID idPeriodo) {
        List<BalanzaComprobacion> balanzas = findByPeriodo(idPeriodo);
        
        BigDecimal[] totales = new BigDecimal[6];
        for (int i = 0; i < 6; i++) {
            totales[i] = BigDecimal.ZERO;
        }
        
        for (BalanzaComprobacion b : balanzas) {
            totales[0] = totales[0].add(b.getSaldoInicialDebe() != null ? b.getSaldoInicialDebe() : BigDecimal.ZERO);
            totales[1] = totales[1].add(b.getSaldoInicialHaber() != null ? b.getSaldoInicialHaber() : BigDecimal.ZERO);
            totales[2] = totales[2].add(b.getMovimientosDebe() != null ? b.getMovimientosDebe() : BigDecimal.ZERO);
            totales[3] = totales[3].add(b.getMovimientosHaber() != null ? b.getMovimientosHaber() : BigDecimal.ZERO);
            totales[4] = totales[4].add(b.getSaldoFinalDebe() != null ? b.getSaldoFinalDebe() : BigDecimal.ZERO);
            totales[5] = totales[5].add(b.getSaldoFinalHaber() != null ? b.getSaldoFinalHaber() : BigDecimal.ZERO);
        }
        
        return totales;
    }

    /**
     * Verify if trial balance is balanced.
     * @param idPeriodo period ID
     * @return true if balanced
     */
    public boolean verificarBalance(UUID idPeriodo) {
        BigDecimal[] totales = getTotalesBalanza(idPeriodo);
        return totales[0].compareTo(totales[1]) == 0
            && totales[2].compareTo(totales[3]) == 0
            && totales[4].compareTo(totales[5]) == 0;
    }
}
