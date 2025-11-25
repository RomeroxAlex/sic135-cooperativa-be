package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class LineaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotNull(message = "La cuenta es requerida")
    private UUID cuentaId;

    private String cuentaCodigo;

    private String cuentaNombre;

    @Size(max = 300, message = "La descripción no puede exceder 300 caracteres")
    private String descripcion;

    private BigDecimal debe;

    private BigDecimal haber;

    private Integer orden;

    public LineaDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(UUID cuentaId) {
        this.cuentaId = cuentaId;
    }

    public String getCuentaCodigo() {
        return cuentaCodigo;
    }

    public void setCuentaCodigo(String cuentaCodigo) {
        this.cuentaCodigo = cuentaCodigo;
    }

    public String getCuentaNombre() {
        return cuentaNombre;
    }

    public void setCuentaNombre(String cuentaNombre) {
        this.cuentaNombre = cuentaNombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }
}
