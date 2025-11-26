package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CuentaContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.DetallePartidaDiarioBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.PartidaAjusteBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.PartidaDiarioBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.PeriodoContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.PartidaAjusteDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.DetallePartidaDiario;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PartidaAjuste;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PartidaDiario;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PeriodoContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.EstadoAjuste;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.EstadoPartida;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.TipoAjuste;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.TipoPartida;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ContabilidadException;

/**
 * Service for managing adjustment entries (Partidas de Ajuste).
 * This is the main focus module - handles both manual and automatic adjustments.
 * 
 * Adjustment types:
 * - DEPRECIACION: Depreciation of fixed assets
 * - AMORTIZACION: Amortization of intangible assets
 * - PROVISION: Provisions for bad debts, etc.
 * - REGULARIZACION: Regularization entries
 * - RECLASIFICACION: Account reclassification
 * - CORRECCION: Error corrections
 */
@Stateless
public class PartidaAjusteService {

    @Inject
    PartidaAjusteBean partidaAjusteBean;
    
    @Inject
    PartidaDiarioBean partidaDiarioBean;
    
    @Inject
    DetallePartidaDiarioBean detallePartidaDiarioBean;
    
    @Inject
    PeriodoContableBean periodoContableBean;
    
    @Inject
    CuentaContableBean cuentaContableBean;

    /**
     * Create a manual adjustment entry.
     * @param dto adjustment entry data
     * @return created adjustment entry ID
     * @throws ContabilidadException if validation fails
     */
    public UUID crearPartidaAjuste(PartidaAjusteDTO dto) throws ContabilidadException {
        // Validate required fields
        if (dto.getMonto() == null || dto.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ContabilidadException("El monto del ajuste debe ser mayor a cero");
        }
        
        if (dto.getIdCuentaDebe() == null) {
            throw new ContabilidadException("Debe especificar la cuenta al debe");
        }
        
        if (dto.getIdCuentaHaber() == null) {
            throw new ContabilidadException("Debe especificar la cuenta al haber");
        }
        
        // Validate accounts exist
        CuentaContable cuentaDebe = cuentaContableBean.findById(dto.getIdCuentaDebe());
        if (cuentaDebe == null) {
            throw new ContabilidadException("Cuenta al debe no encontrada");
        }
        
        CuentaContable cuentaHaber = cuentaContableBean.findById(dto.getIdCuentaHaber());
        if (cuentaHaber == null) {
            throw new ContabilidadException("Cuenta al haber no encontrada");
        }
        
        // Validate period
        PeriodoContable periodo;
        if (dto.getIdPeriodoContable() != null) {
            periodo = periodoContableBean.findById(dto.getIdPeriodoContable());
        } else {
            periodo = periodoContableBean.findPeriodoActivo();
        }
        
        if (periodo == null) {
            throw new ContabilidadException("No hay un período contable activo");
        }
        
        if (periodo.getCerrado() != null && periodo.getCerrado()) {
            throw new ContabilidadException("El período contable está cerrado");
        }
        
        // Validate adjustment type
        String tipoAjuste = dto.getTipoAjuste();
        if (tipoAjuste == null || tipoAjuste.isEmpty()) {
            tipoAjuste = TipoAjuste.CORRECCION.name();
        }
        
        // Create adjustment entry
        UUID idAjuste = UUID.randomUUID();
        PartidaAjuste ajuste = new PartidaAjuste(idAjuste);
        ajuste.setNumeroAjuste(generateNumeroAjuste(periodo));
        ajuste.setFecha(dto.getFecha() != null ? dto.getFecha() : new Date());
        ajuste.setDescripcion(dto.getDescripcion());
        ajuste.setConcepto(dto.getConcepto());
        ajuste.setTipoAjuste(tipoAjuste);
        ajuste.setEstado(EstadoAjuste.PENDIENTE.name());
        ajuste.setAutomatico(dto.getAutomatico() != null ? dto.getAutomatico() : false);
        ajuste.setMonto(dto.getMonto());
        ajuste.setReferencia(dto.getReferencia());
        ajuste.setFechaCreacion(new Date());
        ajuste.setIdPeriodoContable(periodo);
        ajuste.setIdCuentaDebe(cuentaDebe);
        ajuste.setIdCuentaHaber(cuentaHaber);
        
        partidaAjusteBean.persistEntity(ajuste);
        
        return idAjuste;
    }

    /**
     * Apply an adjustment entry - generates the corresponding journal entry.
     * This is where the adjustment affects the accounting balances.
     * @param idAjuste adjustment entry ID
     * @return generated journal entry ID
     * @throws ContabilidadException if validation fails
     */
    public UUID aplicarAjuste(UUID idAjuste) throws ContabilidadException {
        PartidaAjuste ajuste = partidaAjusteBean.findById(idAjuste);
        
        if (ajuste == null) {
            throw new ContabilidadException("Partida de ajuste no encontrada");
        }
        
        if (!EstadoAjuste.PENDIENTE.name().equals(ajuste.getEstado())) {
            throw new ContabilidadException("Solo se pueden aplicar ajustes en estado PENDIENTE");
        }
        
        // Check period is still open
        PeriodoContable periodo = ajuste.getIdPeriodoContable();
        if (periodo.getCerrado() != null && periodo.getCerrado()) {
            throw new ContabilidadException("El período contable está cerrado");
        }
        
        // Generate journal entry for the adjustment
        UUID idPartida = generarPartidaDiario(ajuste);
        
        // Update adjustment status
        ajuste.setEstado(EstadoAjuste.APLICADO.name());
        ajuste.setFechaAplicacion(new Date());
        ajuste.setIdPartidaDiario(partidaDiarioBean.findById(idPartida));
        partidaAjusteBean.updateEntity(ajuste);
        
        return idPartida;
    }

    /**
     * Generate a journal entry from an adjustment entry.
     * The entry is created as CONTABILIZADA since it comes from an approved adjustment.
     */
    private UUID generarPartidaDiario(PartidaAjuste ajuste) {
        PeriodoContable periodo = ajuste.getIdPeriodoContable();
        
        // Get next entry number
        Integer maxNumero = partidaDiarioBean.findMaxNumeroByPeriodo(periodo);
        int numeroPartida = (maxNumero != null) ? maxNumero + 1 : 1;
        
        // Create journal entry
        UUID idPartida = UUID.randomUUID();
        PartidaDiario partida = new PartidaDiario(idPartida);
        partida.setNumeroPartida(numeroPartida);
        partida.setFecha(ajuste.getFecha());
        partida.setDescripcion("Ajuste: " + ajuste.getDescripcion());
        partida.setConcepto(ajuste.getConcepto());
        partida.setTotalDebe(ajuste.getMonto());
        partida.setTotalHaber(ajuste.getMonto());
        partida.setTipoPartida(TipoPartida.AJUSTE.name());
        partida.setEstado(EstadoPartida.CONTABILIZADA.name());
        partida.setReferencia("AJUSTE-" + ajuste.getNumeroAjuste());
        partida.setFechaCreacion(new Date());
        partida.setFechaContabilizacion(new Date());
        partida.setIdPeriodoContable(periodo);
        
        partidaDiarioBean.persistEntity(partida);
        
        // Create detail line for debit
        DetallePartidaDiario detalleDebe = new DetallePartidaDiario(UUID.randomUUID());
        detalleDebe.setNumeroLinea(1);
        detalleDebe.setDebe(ajuste.getMonto());
        detalleDebe.setHaber(null);
        detalleDebe.setDescripcion(ajuste.getDescripcion());
        detalleDebe.setIdPartidaDiario(partida);
        detalleDebe.setIdCuentaContable(ajuste.getIdCuentaDebe());
        detallePartidaDiarioBean.persistEntity(detalleDebe);
        
        // Create detail line for credit
        DetallePartidaDiario detalleHaber = new DetallePartidaDiario(UUID.randomUUID());
        detalleHaber.setNumeroLinea(2);
        detalleHaber.setDebe(null);
        detalleHaber.setHaber(ajuste.getMonto());
        detalleHaber.setDescripcion(ajuste.getDescripcion());
        detalleHaber.setIdPartidaDiario(partida);
        detalleHaber.setIdCuentaContable(ajuste.getIdCuentaHaber());
        detallePartidaDiarioBean.persistEntity(detalleHaber);
        
        return idPartida;
    }

    /**
     * Generate automatic depreciation adjustment for a period.
     * @param idPeriodo period ID
     * @param idCuentaActivo fixed asset account ID
     * @param idCuentaDepreciacion depreciation expense account ID
     * @param idCuentaAcumulada accumulated depreciation account ID
     * @param montoDepreciacion depreciation amount
     * @return list of created adjustment IDs
     * @throws ContabilidadException if validation fails
     */
    public UUID generarDepreciacionAutomatica(
            UUID idPeriodo,
            UUID idCuentaDepreciacion,
            UUID idCuentaAcumulada,
            BigDecimal montoDepreciacion,
            String descripcion) throws ContabilidadException {
        
        PartidaAjusteDTO dto = new PartidaAjusteDTO();
        dto.setFecha(new Date());
        dto.setDescripcion(descripcion != null ? descripcion : "Depreciación mensual automática");
        dto.setConcepto("Registro de depreciación del período");
        dto.setTipoAjuste(TipoAjuste.DEPRECIACION.name());
        dto.setAutomatico(true);
        dto.setMonto(montoDepreciacion);
        dto.setIdPeriodoContable(idPeriodo);
        dto.setIdCuentaDebe(idCuentaDepreciacion);  // Gasto depreciación (debe)
        dto.setIdCuentaHaber(idCuentaAcumulada);    // Depreciación acumulada (haber)
        
        return crearPartidaAjuste(dto);
    }

    /**
     * Generate automatic provision adjustment (e.g., bad debt provision).
     * @param idPeriodo period ID
     * @param idCuentaGasto expense account ID
     * @param idCuentaProvision provision account ID
     * @param monto provision amount
     * @param descripcion description
     * @return created adjustment ID
     * @throws ContabilidadException if validation fails
     */
    public UUID generarProvisionAutomatica(
            UUID idPeriodo,
            UUID idCuentaGasto,
            UUID idCuentaProvision,
            BigDecimal monto,
            String descripcion) throws ContabilidadException {
        
        PartidaAjusteDTO dto = new PartidaAjusteDTO();
        dto.setFecha(new Date());
        dto.setDescripcion(descripcion != null ? descripcion : "Provisión automática");
        dto.setConcepto("Registro de provisión del período");
        dto.setTipoAjuste(TipoAjuste.PROVISION.name());
        dto.setAutomatico(true);
        dto.setMonto(monto);
        dto.setIdPeriodoContable(idPeriodo);
        dto.setIdCuentaDebe(idCuentaGasto);      // Gasto provisión (debe)
        dto.setIdCuentaHaber(idCuentaProvision); // Provisión (haber)
        
        return crearPartidaAjuste(dto);
    }

    /**
     * Void an adjustment entry.
     * @param idAjuste adjustment entry ID
     * @throws ContabilidadException if validation fails
     */
    public void anularAjuste(UUID idAjuste) throws ContabilidadException {
        PartidaAjuste ajuste = partidaAjusteBean.findById(idAjuste);
        
        if (ajuste == null) {
            throw new ContabilidadException("Partida de ajuste no encontrada");
        }
        
        if (EstadoAjuste.ANULADO.name().equals(ajuste.getEstado())) {
            throw new ContabilidadException("El ajuste ya está anulado");
        }
        
        // If already applied, void the journal entry too
        if (EstadoAjuste.APLICADO.name().equals(ajuste.getEstado()) && ajuste.getIdPartidaDiario() != null) {
            PartidaDiario partida = ajuste.getIdPartidaDiario();
            partida.setEstado(EstadoPartida.ANULADA.name());
            partidaDiarioBean.updateEntity(partida);
        }
        
        ajuste.setEstado(EstadoAjuste.ANULADO.name());
        partidaAjusteBean.updateEntity(ajuste);
    }

    /**
     * Get all adjustment entries for a period.
     * @param idPeriodo period ID
     * @return list of adjustment entries
     */
    public List<PartidaAjuste> findByPeriodo(UUID idPeriodo) {
        PeriodoContable periodo = periodoContableBean.findById(idPeriodo);
        if (periodo == null) {
            return new ArrayList<>();
        }
        return partidaAjusteBean.findByPeriodo(periodo);
    }

    /**
     * Get all adjustment entries.
     * @return list of all adjustment entries
     */
    public List<PartidaAjuste> findAll() {
        return partidaAjusteBean.findAll();
    }

    /**
     * Get adjustment entry by ID.
     * @param idAjuste adjustment entry ID
     * @return adjustment entry or null
     */
    public PartidaAjuste findById(UUID idAjuste) {
        return partidaAjusteBean.findById(idAjuste);
    }

    /**
     * Get all pending adjustment entries.
     * @return list of pending adjustment entries
     */
    public List<PartidaAjuste> findPendientes() {
        return partidaAjusteBean.findPendientes();
    }

    /**
     * Get adjustment entries by type.
     * @param tipo adjustment type
     * @return list of adjustment entries
     */
    public List<PartidaAjuste> findByTipo(String tipo) {
        return partidaAjusteBean.findByTipo(tipo);
    }

    /**
     * Generate sequential adjustment number for the period using MAX query.
     */
    private Integer generateNumeroAjuste(PeriodoContable periodo) {
        List<PartidaAjuste> ajustes = partidaAjusteBean.findByPeriodo(periodo);
        if (ajustes == null || ajustes.isEmpty()) {
            return 1;
        }
        // Get max existing number to avoid duplicates
        int maxNumero = ajustes.stream()
            .map(PartidaAjuste::getNumeroAjuste)
            .filter(n -> n != null)
            .max(Integer::compareTo)
            .orElse(0);
        return maxNumero + 1;
    }
}
