package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * DTO for creating/updating adjustment entries (Partida de Ajuste).
 */
public class PartidaAjusteDTO {
    
    private UUID idPartidaAjuste;
    private Date fecha;
    private String descripcion;
    private String concepto;
    private String tipoAjuste;
    private Boolean automatico;
    private BigDecimal monto;
    private String referencia;
    private UUID idPeriodoContable;
    private UUID idCuentaDebe;
    private UUID idCuentaHaber;
    
    public UUID getIdPartidaAjuste() {
        return idPartidaAjuste;
    }
    
    public void setIdPartidaAjuste(UUID idPartidaAjuste) {
        this.idPartidaAjuste = idPartidaAjuste;
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
    
    public String getTipoAjuste() {
        return tipoAjuste;
    }
    
    public void setTipoAjuste(String tipoAjuste) {
        this.tipoAjuste = tipoAjuste;
    }
    
    public Boolean getAutomatico() {
        return automatico;
    }
    
    public void setAutomatico(Boolean automatico) {
        this.automatico = automatico;
    }
    
    public BigDecimal getMonto() {
        return monto;
    }
    
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
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
    
    public UUID getIdCuentaDebe() {
        return idCuentaDebe;
    }
    
    public void setIdCuentaDebe(UUID idCuentaDebe) {
        this.idCuentaDebe = idCuentaDebe;
    }
    
    public UUID getIdCuentaHaber() {
        return idCuentaHaber;
    }
    
    public void setIdCuentaHaber(UUID idCuentaHaber) {
        this.idCuentaHaber = idCuentaHaber;
    }
}
