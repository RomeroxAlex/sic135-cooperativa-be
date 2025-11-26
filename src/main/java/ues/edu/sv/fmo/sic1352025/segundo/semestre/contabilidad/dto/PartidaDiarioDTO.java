package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * DTO for creating/updating journal entries (Partida de Diario).
 */
public class PartidaDiarioDTO {
    
    private UUID idPartidaDiario;
    private Date fecha;
    private String descripcion;
    private String concepto;
    private String tipoPartida;
    private String referencia;
    private UUID idPeriodoContable;
    private List<DetallePartidaDTO> detalles;
    
    public UUID getIdPartidaDiario() {
        return idPartidaDiario;
    }
    
    public void setIdPartidaDiario(UUID idPartidaDiario) {
        this.idPartidaDiario = idPartidaDiario;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getConcepto() {
        return concepto;
    }
    
    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }
    
    public String getTipoPartida() {
        return tipoPartida;
    }
    
    public void setTipoPartida(String tipoPartida) {
        this.tipoPartida = tipoPartida;
    }
    
    public String getReferencia() {
        return referencia;
    }
    
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
    
    public UUID getIdPeriodoContable() {
        return idPeriodoContable;
    }
    
    public void setIdPeriodoContable(UUID idPeriodoContable) {
        this.idPeriodoContable = idPeriodoContable;
    }
    
    public List<DetallePartidaDTO> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<DetallePartidaDTO> detalles) {
        this.detalles = detalles;
    }
    
    /**
     * Inner class for journal entry detail lines
     */
    public static class DetallePartidaDTO {
        private UUID idCuentaContable;
        private BigDecimal debe;
        private BigDecimal haber;
        private String descripcion;
        
        public UUID getIdCuentaContable() {
            return idCuentaContable;
        }
        
        public void setIdCuentaContable(UUID idCuentaContable) {
            this.idCuentaContable = idCuentaContable;
        }
        
        public BigDecimal getDebe() {
            return debe;
        }
        
        public void setDebe(BigDecimal debe) {
            this.debe = debe;
        }
        
        public BigDecimal getHaber() {
            return haber;
        }
        
        public void setHaber(BigDecimal haber) {
            this.haber = haber;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
        
        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
    }
}
