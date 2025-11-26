package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Entity representing a trial balance (Balanza de Comprobación).
 * Shows all accounts with their balances to verify accounting equation.
 */
@Entity
@Table(name = "balanza_comprobacion")
@NamedQueries({
    @NamedQuery(name = "BalanzaComprobacion.findAll", query = "SELECT b FROM BalanzaComprobacion b"),
    @NamedQuery(name = "BalanzaComprobacion.findByPeriodo", query = "SELECT b FROM BalanzaComprobacion b WHERE b.idPeriodoContable = :periodo ORDER BY b.idCuentaContable.codigo"),
    @NamedQuery(name = "BalanzaComprobacion.findByFecha", query = "SELECT b FROM BalanzaComprobacion b WHERE b.fechaGeneracion = :fecha"),
    @NamedQuery(name = "BalanzaComprobacion.findLatestByPeriodo", query = "SELECT b FROM BalanzaComprobacion b WHERE b.idPeriodoContable = :periodo ORDER BY b.fechaGeneracion DESC")
})
public class BalanzaComprobacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id_balanza")
    private UUID idBalanza;
    
    @Column(name = "saldo_inicial_debe")
    private BigDecimal saldoInicialDebe;
    
    @Column(name = "saldo_inicial_haber")
    private BigDecimal saldoInicialHaber;
    
    @Column(name = "movimientos_debe")
    private BigDecimal movimientosDebe;
    
    @Column(name = "movimientos_haber")
    private BigDecimal movimientosHaber;
    
    @Column(name = "saldo_final_debe")
    private BigDecimal saldoFinalDebe;
    
    @Column(name = "saldo_final_haber")
    private BigDecimal saldoFinalHaber;
    
    @Column(name = "fecha_generacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaGeneracion;
    
    @JoinColumn(name = "id_cuenta_contable", referencedColumnName = "id_cuenta_contable")
    @ManyToOne(fetch = FetchType.LAZY)
    private CuentaContable idCuentaContable;
    
    @JoinColumn(name = "id_periodo_contable", referencedColumnName = "id_periodo_contable")
    @ManyToOne(fetch = FetchType.LAZY)
    private PeriodoContable idPeriodoContable;

    public BalanzaComprobacion() {
    }

    public BalanzaComprobacion(UUID idBalanza) {
        this.idBalanza = idBalanza;
    }

    public UUID getIdBalanza() {
        return idBalanza;
    }

    public void setIdBalanza(UUID idBalanza) {
        this.idBalanza = idBalanza;
    }

    public BigDecimal getSaldoInicialDebe() {
        return saldoInicialDebe;
    }

    public void setSaldoInicialDebe(BigDecimal saldoInicialDebe) {
        this.saldoInicialDebe = saldoInicialDebe;
    }

    public BigDecimal getSaldoInicialHaber() {
        return saldoInicialHaber;
    }

    public void setSaldoInicialHaber(BigDecimal saldoInicialHaber) {
        this.saldoInicialHaber = saldoInicialHaber;
    }

    public BigDecimal getMovimientosDebe() {
        return movimientosDebe;
    }

    public void setMovimientosDebe(BigDecimal movimientosDebe) {
        this.movimientosDebe = movimientosDebe;
    }

    public BigDecimal getMovimientosHaber() {
        return movimientosHaber;
    }

    public void setMovimientosHaber(BigDecimal movimientosHaber) {
        this.movimientosHaber = movimientosHaber;
    }

    public BigDecimal getSaldoFinalDebe() {
        return saldoFinalDebe;
    }

    public void setSaldoFinalDebe(BigDecimal saldoFinalDebe) {
        this.saldoFinalDebe = saldoFinalDebe;
    }

    public BigDecimal getSaldoFinalHaber() {
        return saldoFinalHaber;
    }

    public void setSaldoFinalHaber(BigDecimal saldoFinalHaber) {
        this.saldoFinalHaber = saldoFinalHaber;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public CuentaContable getIdCuentaContable() {
        return idCuentaContable;
    }

    public void setIdCuentaContable(CuentaContable idCuentaContable) {
        this.idCuentaContable = idCuentaContable;
    }

    @JsonbTransient
    public PeriodoContable getIdPeriodoContable() {
        return idPeriodoContable;
    }

    public void setIdPeriodoContable(PeriodoContable idPeriodoContable) {
        this.idPeriodoContable = idPeriodoContable;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBalanza != null ? idBalanza.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BalanzaComprobacion)) {
            return false;
        }
        BalanzaComprobacion other = (BalanzaComprobacion) object;
        return !((this.idBalanza == null && other.idBalanza != null) 
                || (this.idBalanza != null && !this.idBalanza.equals(other.idBalanza)));
    }

    @Override
    public String toString() {
        return "BalanzaComprobacion[ idBalanza=" + idBalanza + " ]";
    }
}
