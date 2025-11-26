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
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.PartidaDiarioBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.PeriodoContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.PartidaDiarioDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.PartidaDiarioDTO.DetallePartidaDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.DetallePartidaDiario;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PartidaDiario;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PeriodoContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.EstadoPartida;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.TipoPartida;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ContabilidadException;

/**
 * Service for managing journal entries (Libro Diario).
 * Validates accounting rules like debe = haber.
 */
@Stateless
public class PartidaDiarioService {

    @Inject
    PartidaDiarioBean partidaDiarioBean;
    
    @Inject
    DetallePartidaDiarioBean detallePartidaDiarioBean;
    
    @Inject
    PeriodoContableBean periodoContableBean;
    
    @Inject
    CuentaContableBean cuentaContableBean;

    /**
     * Create a new journal entry from DTO.
     * Validates that debe = haber before saving.
     * @param dto journal entry data
     * @return created journal entry ID
     * @throws ContabilidadException if validation fails
     */
    public UUID crearPartidaDiario(PartidaDiarioDTO dto) throws ContabilidadException {
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
        
        // Calculate totals and validate
        BigDecimal totalDebe = BigDecimal.ZERO;
        BigDecimal totalHaber = BigDecimal.ZERO;
        
        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new ContabilidadException("La partida debe tener al menos un detalle");
        }
        
        for (DetallePartidaDTO detalle : dto.getDetalles()) {
            BigDecimal debe = detalle.getDebe() != null ? detalle.getDebe() : BigDecimal.ZERO;
            BigDecimal haber = detalle.getHaber() != null ? detalle.getHaber() : BigDecimal.ZERO;
            totalDebe = totalDebe.add(debe);
            totalHaber = totalHaber.add(haber);
        }
        
        // Validate accounting equation: debe = haber
        if (totalDebe.compareTo(totalHaber) != 0) {
            throw new ContabilidadException(
                String.format("La partida no está balanceada. Debe: %s, Haber: %s", 
                    totalDebe.toString(), totalHaber.toString()));
        }
        
        // Get next entry number
        Integer maxNumero = partidaDiarioBean.findMaxNumeroByPeriodo(periodo);
        int numeroPartida = (maxNumero != null) ? maxNumero + 1 : 1;
        
        // Create journal entry
        UUID idPartida = UUID.randomUUID();
        PartidaDiario partida = new PartidaDiario(idPartida);
        partida.setNumeroPartida(numeroPartida);
        partida.setFecha(dto.getFecha() != null ? dto.getFecha() : new Date());
        partida.setDescripcion(dto.getDescripcion());
        partida.setConcepto(dto.getConcepto());
        partida.setTotalDebe(totalDebe);
        partida.setTotalHaber(totalHaber);
        partida.setTipoPartida(dto.getTipoPartida() != null ? dto.getTipoPartida() : TipoPartida.NORMAL.name());
        partida.setEstado(EstadoPartida.BORRADOR.name());
        partida.setReferencia(dto.getReferencia());
        partida.setFechaCreacion(new Date());
        partida.setIdPeriodoContable(periodo);
        
        partidaDiarioBean.persistEntity(partida);
        
        // Create detail lines
        int numeroLinea = 1;
        for (DetallePartidaDTO detalleDto : dto.getDetalles()) {
            CuentaContable cuenta = cuentaContableBean.findById(detalleDto.getIdCuentaContable());
            if (cuenta == null) {
                throw new ContabilidadException("Cuenta contable no encontrada: " + detalleDto.getIdCuentaContable());
            }
            
            DetallePartidaDiario detalle = new DetallePartidaDiario(UUID.randomUUID());
            detalle.setNumeroLinea(numeroLinea++);
            detalle.setDebe(detalleDto.getDebe());
            detalle.setHaber(detalleDto.getHaber());
            detalle.setDescripcion(detalleDto.getDescripcion());
            detalle.setIdPartidaDiario(partida);
            detalle.setIdCuentaContable(cuenta);
            
            detallePartidaDiarioBean.persistEntity(detalle);
        }
        
        return idPartida;
    }

    /**
     * Post a journal entry (change status from BORRADOR to CONTABILIZADA).
     * @param idPartida journal entry ID
     * @throws ContabilidadException if validation fails
     */
    public void contabilizarPartida(UUID idPartida) throws ContabilidadException {
        PartidaDiario partida = partidaDiarioBean.findById(idPartida);
        
        if (partida == null) {
            throw new ContabilidadException("Partida no encontrada");
        }
        
        if (!EstadoPartida.BORRADOR.name().equals(partida.getEstado())) {
            throw new ContabilidadException("Solo se pueden contabilizar partidas en estado BORRADOR");
        }
        
        // Verify balance
        if (!partida.estaBalanceada()) {
            throw new ContabilidadException("La partida no está balanceada");
        }
        
        partida.setEstado(EstadoPartida.CONTABILIZADA.name());
        partida.setFechaContabilizacion(new Date());
        partidaDiarioBean.updateEntity(partida);
    }

    /**
     * Void a journal entry.
     * @param idPartida journal entry ID
     * @throws ContabilidadException if validation fails
     */
    public void anularPartida(UUID idPartida) throws ContabilidadException {
        PartidaDiario partida = partidaDiarioBean.findById(idPartida);
        
        if (partida == null) {
            throw new ContabilidadException("Partida no encontrada");
        }
        
        if (EstadoPartida.ANULADA.name().equals(partida.getEstado())) {
            throw new ContabilidadException("La partida ya está anulada");
        }
        
        partida.setEstado(EstadoPartida.ANULADA.name());
        partidaDiarioBean.updateEntity(partida);
    }

    /**
     * Get all journal entries for a period.
     * @param idPeriodo period ID
     * @return list of journal entries
     */
    public List<PartidaDiario> findByPeriodo(UUID idPeriodo) {
        PeriodoContable periodo = periodoContableBean.findById(idPeriodo);
        if (periodo == null) {
            return new ArrayList<>();
        }
        return partidaDiarioBean.findByPeriodo(periodo);
    }

    /**
     * Get all journal entries.
     * @return list of all journal entries
     */
    public List<PartidaDiario> findAll() {
        return partidaDiarioBean.findAll();
    }

    /**
     * Get journal entry by ID.
     * @param idPartida journal entry ID
     * @return journal entry or null
     */
    public PartidaDiario findById(UUID idPartida) {
        return partidaDiarioBean.findById(idPartida);
    }

    /**
     * Get detail lines for a journal entry.
     * @param idPartida journal entry ID
     * @return list of detail lines
     */
    public List<DetallePartidaDiario> findDetallesByPartida(UUID idPartida) {
        PartidaDiario partida = partidaDiarioBean.findById(idPartida);
        if (partida == null) {
            return new ArrayList<>();
        }
        return detallePartidaDiarioBean.findByPartida(partida);
    }
}
