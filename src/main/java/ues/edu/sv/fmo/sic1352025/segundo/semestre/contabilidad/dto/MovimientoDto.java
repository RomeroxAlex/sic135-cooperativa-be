package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class MovimientoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private LocalDate fecha;
    private String descripcion;
    private String referencia;
    private Long numeroPartida;
    private BigDecimal debe;
    private BigDecimal haber;
    private BigDecimal saldo;

    public MovimientoDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Long getNumeroPartida() {
        return numeroPartida;
    }

    public void setNumeroPartida(Long numeroPartida) {
        this.numeroPartida = numeroPartida;
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

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
