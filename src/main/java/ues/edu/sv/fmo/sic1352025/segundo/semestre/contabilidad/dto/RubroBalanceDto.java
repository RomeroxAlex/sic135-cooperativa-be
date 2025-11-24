package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class RubroBalanceDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID cuentaId;
    private String codigo;
    private String nombre;
    private BigDecimal saldo;

    public RubroBalanceDto() {
    }

    public RubroBalanceDto(UUID cuentaId, String codigo, String nombre, BigDecimal saldo) {
        this.cuentaId = cuentaId;
        this.codigo = codigo;
        this.nombre = nombre;
        this.saldo = saldo;
    }

    public UUID getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(UUID cuentaId) {
        this.cuentaId = cuentaId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
