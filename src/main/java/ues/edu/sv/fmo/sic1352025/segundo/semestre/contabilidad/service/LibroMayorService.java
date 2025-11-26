package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CuentaContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.DetallePartidaDiarioBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.LibroMayorBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.PeriodoContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.LibroMayor;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PeriodoContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.NaturalezaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ContabilidadException;

/**
 * Service for managing the general ledger (Libro Mayor / Mayorización).
 * Calculates account balances based on posted journal entries.
 */
@Stateless
public class LibroMayorService {

    @Inject
    LibroMayorBean libroMayorBean;
    
    @Inject
    PeriodoContableBean periodoContableBean;
    
    @Inject
    CuentaContableBean cuentaContableBean;
    
    @Inject
    DetallePartidaDiarioBean detallePartidaDiarioBean;

    /**
     * Generate or update general ledger for a period.
     * This process calculates balances for all accounts based on posted journal entries.
     * @param idPeriodo period ID
     * @throws ContabilidadException if validation fails
     */
    public void generarMayorizacion(UUID idPeriodo) throws ContabilidadException {
        PeriodoContable periodo = periodoContableBean.findById(idPeriodo);
        if (periodo == null) {
            throw new ContabilidadException("Período contable no encontrado");
        }
        
        // Get all accounts
        List<CuentaContable> cuentas = cuentaContableBean.findAll();
        if (cuentas == null || cuentas.isEmpty()) {
            throw new ContabilidadException("No hay cuentas contables registradas");
        }
        
        // Find previous period to get initial balances
        PeriodoContable periodoAnterior = findPeriodoAnterior(periodo);
        
        for (CuentaContable cuenta : cuentas) {
            // Get or create ledger entry for this account and period
            LibroMayor libroMayor = libroMayorBean.findByCuentaYPeriodo(cuenta, periodo);
            
            boolean isNew = false;
            if (libroMayor == null) {
                libroMayor = new LibroMayor(UUID.randomUUID());
                libroMayor.setIdCuentaContable(cuenta);
                libroMayor.setIdPeriodoContable(periodo);
                isNew = true;
            }
            
            // Get initial balance from previous period
            BigDecimal saldoInicial = BigDecimal.ZERO;
            if (periodoAnterior != null) {
                LibroMayor mayorAnterior = libroMayorBean.findByCuentaYPeriodo(cuenta, periodoAnterior);
                if (mayorAnterior != null && mayorAnterior.getSaldoFinal() != null) {
                    saldoInicial = mayorAnterior.getSaldoFinal();
                }
            }
            
            // Sum movements from posted journal entries
            BigDecimal totalDebe = detallePartidaDiarioBean.sumDebeByPeriodoYCuenta(cuenta, periodo);
            BigDecimal totalHaber = detallePartidaDiarioBean.sumHaberByPeriodoYCuenta(cuenta, periodo);
            
            // Set values
            libroMayor.setSaldoInicial(saldoInicial);
            libroMayor.setTotalDebe(totalDebe);
            libroMayor.setTotalHaber(totalHaber);
            
            // Calculate final balance based on account nature
            boolean esDeudora = NaturalezaContable.DEUDORA.name().equals(cuenta.getNaturaleza());
            libroMayor.calcularSaldoFinal(esDeudora);
            
            if (isNew) {
                libroMayorBean.persistEntity(libroMayor);
            } else {
                libroMayorBean.updateEntity(libroMayor);
            }
        }
    }

    /**
     * Get general ledger entries for a period.
     * @param idPeriodo period ID
     * @return list of ledger entries
     */
    public List<LibroMayor> findByPeriodo(UUID idPeriodo) {
        PeriodoContable periodo = periodoContableBean.findById(idPeriodo);
        if (periodo == null) {
            return new ArrayList<>();
        }
        return libroMayorBean.findByPeriodo(periodo);
    }

    /**
     * Get ledger entries with non-zero balances for a period.
     * @param idPeriodo period ID
     * @return list of ledger entries with balances
     */
    public List<LibroMayor> findByPeriodoConSaldo(UUID idPeriodo) {
        PeriodoContable periodo = periodoContableBean.findById(idPeriodo);
        if (periodo == null) {
            return new ArrayList<>();
        }
        return libroMayorBean.findByPeriodoConSaldo(periodo);
    }

    /**
     * Get ledger entries for a specific account across all periods.
     * @param idCuenta account ID
     * @return list of ledger entries
     */
    public List<LibroMayor> findByCuenta(UUID idCuenta) {
        CuentaContable cuenta = cuentaContableBean.findById(idCuenta);
        if (cuenta == null) {
            return new ArrayList<>();
        }
        return libroMayorBean.findByCuenta(cuenta);
    }

    /**
     * Get current balance for an account in a specific period.
     * @param idCuenta account ID
     * @param idPeriodo period ID
     * @return balance or null if not found
     */
    public BigDecimal getSaldoCuenta(UUID idCuenta, UUID idPeriodo) {
        CuentaContable cuenta = cuentaContableBean.findById(idCuenta);
        PeriodoContable periodo = periodoContableBean.findById(idPeriodo);
        
        if (cuenta == null || periodo == null) {
            return null;
        }
        
        LibroMayor libroMayor = libroMayorBean.findByCuentaYPeriodo(cuenta, periodo);
        return libroMayor != null ? libroMayor.getSaldoFinal() : BigDecimal.ZERO;
    }

    /**
     * Find the previous accounting period.
     */
    private PeriodoContable findPeriodoAnterior(PeriodoContable periodoActual) {
        List<PeriodoContable> periodos = periodoContableBean.findAll();
        if (periodos == null || periodos.size() <= 1) {
            return null;
        }
        
        PeriodoContable anterior = null;
        for (PeriodoContable p : periodos) {
            if (p.getFechaFin() != null && periodoActual.getFechaInicio() != null) {
                if (p.getFechaFin().before(periodoActual.getFechaInicio())) {
                    if (anterior == null || p.getFechaFin().after(anterior.getFechaFin())) {
                        anterior = p;
                    }
                }
            }
        }
        return anterior;
    }
}
