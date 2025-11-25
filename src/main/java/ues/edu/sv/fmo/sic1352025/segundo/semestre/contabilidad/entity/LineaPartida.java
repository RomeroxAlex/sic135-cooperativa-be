package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "linea_partida")
@NamedQueries({
    @NamedQuery(name = "LineaPartida.findByPartida", query = "SELECT l FROM LineaPartida l WHERE l.partidaDiario.id = :partidaId"),
    @NamedQuery(name = "LineaPartida.findByCuenta", query = "SELECT l FROM LineaPartida l WHERE l.cuenta.id = :cuentaId")
})
public class LineaPartida implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_linea_partida", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_partida_diario", nullable = false)
    private PartidaDiario partidaDiario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catalogo_cuenta", nullable = false)
    private CatalogoCuenta cuenta;

    @Column(name = "descripcion", length = 300)
    private String descripcion;

    @Column(name = "debe", precision = 18, scale = 2)
    private BigDecimal debe = BigDecimal.ZERO;

    @Column(name = "haber", precision = 18, scale = 2)
    private BigDecimal haber = BigDecimal.ZERO;

    @Column(name = "orden")
    private Integer orden;

    public LineaPartida() {
    }

    public LineaPartida(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PartidaDiario getPartidaDiario() {
        return partidaDiario;
    }

    public void setPartidaDiario(PartidaDiario partidaDiario) {
        this.partidaDiario = partidaDiario;
    }

    public CatalogoCuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(CatalogoCuenta cuenta) {
        this.cuenta = cuenta;
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

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LineaPartida that = (LineaPartida) obj;
        return id != null && id.equals(that.id);
    }

    @Override
    public String toString() {
        return "LineaPartida{id=" + id + ", debe=" + debe + ", haber=" + haber + "}";
    }
}
