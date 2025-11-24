package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "balance_inicial")
@NamedQueries({
    @NamedQuery(name = "BalanceInicial.findByPeriodo", query = "SELECT b FROM BalanceInicial b WHERE b.periodo = :periodo"),
    @NamedQuery(name = "BalanceInicial.findByCuenta", query = "SELECT b FROM BalanceInicial b WHERE b.cuenta.id = :cuentaId")
})
public class BalanceInicial implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_balance_inicial", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "periodo", nullable = false, length = 10)
    private String periodo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catalogo_cuenta", nullable = false)
    private CatalogoCuenta cuenta;

    @Column(name = "saldo_debe", precision = 18, scale = 2)
    private BigDecimal saldoDebe = BigDecimal.ZERO;

    @Column(name = "saldo_haber", precision = 18, scale = 2)
    private BigDecimal saldoHaber = BigDecimal.ZERO;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usuario_creacion", length = 100)
    private String usuarioCreacion;

    public BalanceInicial() {
    }

    public BalanceInicial(UUID id) {
        this.id = id;
    }

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
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

    public CatalogoCuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(CatalogoCuenta cuenta) {
        this.cuenta = cuenta;
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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BalanceInicial that = (BalanceInicial) obj;
        return id != null && id.equals(that.id);
    }

    @Override
    public String toString() {
        return "BalanceInicial{id=" + id + ", periodo='" + periodo + "'}";
    }
}
