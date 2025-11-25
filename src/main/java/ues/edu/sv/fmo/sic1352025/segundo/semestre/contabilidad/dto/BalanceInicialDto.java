package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class BalanceInicialDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotBlank(message = "El periodo es requerido")
    private String periodo;

    @NotNull(message = "La cuenta es requerida")
    private UUID cuentaId;

    private String cuentaCodigo;

    private String cuentaNombre;

    private BigDecimal saldoDebe;

    private BigDecimal saldoHaber;

    private LocalDate fechaRegistro;

    public BalanceInicialDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
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

    public BigDecimal getSaldoDebe() {
        return saldoDebe;
    }

    public void setSaldoDebe(BigDecimal saldoDebe) {
        this.saldoDebe = saldoDebe;
    }

    public BigDecimal getSaldoHaber() {
        return saldoHaber;
    }

    public void setSaldoHaber(BigDecimal saldoHaber) {
        this.saldoHaber = saldoHaber;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
