package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MayorResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID cuentaId;
    private String cuentaCodigo;
    private String cuentaNombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal saldoInicial;
    private BigDecimal saldoFinal;
    private List<MovimientoDto> movimientos = new ArrayList<>();

    public MayorResponseDto() {
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

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public List<MovimientoDto> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<MovimientoDto> movimientos) {
        this.movimientos = movimientos;
    }
}
