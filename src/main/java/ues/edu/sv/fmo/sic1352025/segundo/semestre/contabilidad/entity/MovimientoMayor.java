package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "movimiento_mayor")
@NamedQueries({
    @NamedQuery(name = "MovimientoMayor.findByCuenta", query = "SELECT m FROM MovimientoMayor m WHERE m.cuenta.id = :cuentaId ORDER BY m.fecha ASC"),
    @NamedQuery(name = "MovimientoMayor.findByCuentaAndFecha", query = "SELECT m FROM MovimientoMayor m WHERE m.cuenta.id = :cuentaId AND m.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fecha ASC")
})
public class MovimientoMayor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_movimiento_mayor", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catalogo_cuenta", nullable = false)
    private CatalogoCuenta cuenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_partida_diario")
    private PartidaDiario partidaDiario;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "descripcion", length = 300)
    private String descripcion;

    @Column(name = "referencia", length = 100)
    private String referencia;

    @Column(name = "debe", precision = 18, scale = 2)
    private BigDecimal debe = BigDecimal.ZERO;

    @Column(name = "haber", precision = 18, scale = 2)
    private BigDecimal haber = BigDecimal.ZERO;

    @Column(name = "saldo", precision = 18, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    public MovimientoMayor() {
    }

    public MovimientoMayor(UUID id) {
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

    public CatalogoCuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(CatalogoCuenta cuenta) {
        this.cuenta = cuenta;
    }

    public PartidaDiario getPartidaDiario() {
        return partidaDiario;
    }

    public void setPartidaDiario(PartidaDiario partidaDiario) {
        this.partidaDiario = partidaDiario;
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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MovimientoMayor that = (MovimientoMayor) obj;
        return id != null && id.equals(that.id);
    }

    @Override
    public String toString() {
        return "MovimientoMayor{id=" + id + ", cuenta=" + (cuenta != null ? cuenta.getCodigo() : null) + ", saldo=" + saldo + "}";
    }
}
