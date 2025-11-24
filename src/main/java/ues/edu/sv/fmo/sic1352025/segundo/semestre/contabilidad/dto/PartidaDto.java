package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PartidaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private Long numeroPartida;

    @NotNull(message = "La fecha es requerida")
    private LocalDate fecha;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Size(max = 100, message = "La referencia no puede exceder 100 caracteres")
    private String referencia;

    private String estado;

    private Boolean esAjuste;

    @Size(max = 500, message = "El motivo de ajuste no puede exceder 500 caracteres")
    private String motivoAjuste;

    @Valid
    @NotNull(message = "Las líneas son requeridas")
    @Size(min = 1, message = "Debe tener al menos una línea")
    private List<LineaDto> lineas = new ArrayList<>();

    private BigDecimal totalDebe;

    private BigDecimal totalHaber;

    private LocalDateTime fechaCreacion;

    public PartidaDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getNumeroPartida() {
        return numeroPartida;
    }

    public void setNumeroPartida(Long numeroPartida) {
        this.numeroPartida = numeroPartida;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getEsAjuste() {
        return esAjuste;
    }

    public void setEsAjuste(Boolean esAjuste) {
        this.esAjuste = esAjuste;
    }

    public String getMotivoAjuste() {
        return motivoAjuste;
    }

    public void setMotivoAjuste(String motivoAjuste) {
        this.motivoAjuste = motivoAjuste;
    }

    public List<LineaDto> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaDto> lineas) {
        this.lineas = lineas;
    }

    public BigDecimal getTotalDebe() {
        return totalDebe;
    }

    public void setTotalDebe(BigDecimal totalDebe) {
        this.totalDebe = totalDebe;
    }

    public BigDecimal getTotalHaber() {
        return totalHaber;
    }

    public void setTotalHaber(BigDecimal totalHaber) {
        this.totalHaber = totalHaber;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
