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
 * Entity representing a general ledger entry (Libro Mayor / Mayorización).
 * Each entry shows the accumulated balance for an account in a period.
 */
@Entity
@Table(name = "libro_mayor")
@NamedQueries({
    @NamedQuery(name = "LibroMayor.findAll", query = "SELECT l FROM LibroMayor l"),
    @NamedQuery(name = "LibroMayor.findByPeriodo", query = "SELECT l FROM LibroMayor l WHERE l.idPeriodoContable = :periodo"),
    @NamedQuery(name = "LibroMayor.findByCuenta", query = "SELECT l FROM LibroMayor l WHERE l.idCuentaContable = :cuenta ORDER BY l.idPeriodoContable.fechaInicio"),
    @NamedQuery(name = "LibroMayor.findByCuentaYPeriodo", query = "SELECT l FROM LibroMayor l WHERE l.idCuentaContable = :cuenta AND l.idPeriodoContable = :periodo"),
    @NamedQuery(name = "LibroMayor.findByPeriodoConSaldo", query = "SELECT l FROM LibroMayor l WHERE l.idPeriodoContable = :periodo AND (l.saldoFinal <> 0 OR l.totalDebe <> 0 OR l.totalHaber <> 0)")
})
public class LibroMayor implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id_libro_mayor")
    private UUID idLibroMayor;
    
    @Column(name = "saldo_inicial")
    private BigDecimal saldoInicial;
    
    @Column(name = "total_debe")
    private BigDecimal totalDebe;
    
    @Column(name = "total_haber")
    private BigDecimal totalHaber;
    
    @Column(name = "saldo_final")
    private BigDecimal saldoFinal;
    
    @JoinColumn(name = "id_cuenta_contable", referencedColumnName = "id_cuenta_contable")
    @ManyToOne(fetch = FetchType.LAZY)
    private CuentaContable idCuentaContable;
    
    @JoinColumn(name = "id_periodo_contable", referencedColumnName = "id_periodo_contable")
    @ManyToOne(fetch = FetchType.LAZY)
    private PeriodoContable idPeriodoContable;

    public LibroMayor() {
    }

    public LibroMayor(UUID idLibroMayor) {
        this.idLibroMayor = idLibroMayor;
    }

    public UUID getIdLibroMayor() {
        return idLibroMayor;
    }

    public void setIdLibroMayor(UUID idLibroMayor) {
        this.idLibroMayor = idLibroMayor;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
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

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
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
    
    /**
     * Calculates the final balance based on account nature and movements.
     * For debit accounts: saldoInicial + totalDebe - totalHaber
     * For credit accounts: saldoInicial - totalDebe + totalHaber
     * @param naturalezaDeudora true if the account has debit nature
     */
    public void calcularSaldoFinal(boolean naturalezaDeudora) {
        BigDecimal inicial = saldoInicial != null ? saldoInicial : BigDecimal.ZERO;
        BigDecimal debe = totalDebe != null ? totalDebe : BigDecimal.ZERO;
        BigDecimal haber = totalHaber != null ? totalHaber : BigDecimal.ZERO;
        
        if (naturalezaDeudora) {
            this.saldoFinal = inicial.add(debe).subtract(haber);
        } else {
            this.saldoFinal = inicial.subtract(debe).add(haber);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLibroMayor != null ? idLibroMayor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LibroMayor)) {
            return false;
        }
        LibroMayor other = (LibroMayor) object;
        return !((this.idLibroMayor == null && other.idLibroMayor != null) 
                || (this.idLibroMayor != null && !this.idLibroMayor.equals(other.idLibroMayor)));
    }

    @Override
    public String toString() {
        return "LibroMayor[ idLibroMayor=" + idLibroMayor + " ]";
    }
}
