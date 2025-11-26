package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * Entity representing a detail line of a journal entry (Detalle de Partida de Diario).
 * Each line affects one account with either a debit or credit amount.
 */
@Entity
@Table(name = "detalle_partida_diario")
@NamedQueries({
    @NamedQuery(name = "DetallePartidaDiario.findAll", query = "SELECT d FROM DetallePartidaDiario d"),
    @NamedQuery(name = "DetallePartidaDiario.findByPartida", query = "SELECT d FROM DetallePartidaDiario d WHERE d.idPartidaDiario = :partida ORDER BY d.numeroLinea"),
    @NamedQuery(name = "DetallePartidaDiario.findByCuenta", query = "SELECT d FROM DetallePartidaDiario d WHERE d.idCuentaContable = :cuenta"),
    @NamedQuery(name = "DetallePartidaDiario.findByCuentaYPeriodo", query = "SELECT d FROM DetallePartidaDiario d WHERE d.idCuentaContable = :cuenta AND d.idPartidaDiario.idPeriodoContable = :periodo"),
    @NamedQuery(name = "DetallePartidaDiario.sumDebeByPeriodoYCuenta", query = "SELECT COALESCE(SUM(d.debe), 0) FROM DetallePartidaDiario d WHERE d.idCuentaContable = :cuenta AND d.idPartidaDiario.idPeriodoContable = :periodo AND d.idPartidaDiario.estado = 'CONTABILIZADA'"),
    @NamedQuery(name = "DetallePartidaDiario.sumHaberByPeriodoYCuenta", query = "SELECT COALESCE(SUM(d.haber), 0) FROM DetallePartidaDiario d WHERE d.idCuentaContable = :cuenta AND d.idPartidaDiario.idPeriodoContable = :periodo AND d.idPartidaDiario.estado = 'CONTABILIZADA'")
})
public class DetallePartidaDiario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id_detalle_partida")
    private UUID idDetallePartida;
    
    @Column(name = "numero_linea")
    private Integer numeroLinea;
    
    @Column(name = "debe")
    private BigDecimal debe;
    
    @Column(name = "haber")
    private BigDecimal haber;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @JoinColumn(name = "id_partida_diario", referencedColumnName = "id_partida_diario")
    @ManyToOne(fetch = FetchType.LAZY)
    private PartidaDiario idPartidaDiario;
    
    @JoinColumn(name = "id_cuenta_contable", referencedColumnName = "id_cuenta_contable")
    @ManyToOne(fetch = FetchType.LAZY)
    private CuentaContable idCuentaContable;

    public DetallePartidaDiario() {
    }

    public DetallePartidaDiario(UUID idDetallePartida) {
        this.idDetallePartida = idDetallePartida;
    }

    public UUID getIdDetallePartida() {
        return idDetallePartida;
    }

    public void setIdDetallePartida(UUID idDetallePartida) {
        this.idDetallePartida = idDetallePartida;
    }

    public Integer getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(Integer numeroLinea) {
        this.numeroLinea = numeroLinea;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @JsonbTransient
    public PartidaDiario getIdPartidaDiario() {
        return idPartidaDiario;
    }

    public void setIdPartidaDiario(PartidaDiario idPartidaDiario) {
        this.idPartidaDiario = idPartidaDiario;
    }

    public CuentaContable getIdCuentaContable() {
        return idCuentaContable;
    }

    public void setIdCuentaContable(CuentaContable idCuentaContable) {
        this.idCuentaContable = idCuentaContable;
    }
    
    /**
     * Gets the net movement (debe - haber) for this line
     * @return net movement amount
     */
    public BigDecimal getMovimientoNeto() {
        BigDecimal d = debe != null ? debe : BigDecimal.ZERO;
        BigDecimal h = haber != null ? haber : BigDecimal.ZERO;
        return d.subtract(h);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetallePartida != null ? idDetallePartida.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DetallePartidaDiario)) {
            return false;
        }
        DetallePartidaDiario other = (DetallePartidaDiario) object;
        return !((this.idDetallePartida == null && other.idDetallePartida != null) 
                || (this.idDetallePartida != null && !this.idDetallePartida.equals(other.idDetallePartida)));
    }

    @Override
    public String toString() {
        return "DetallePartidaDiario[ idDetallePartida=" + idDetallePartida + " ]";
    }
}
